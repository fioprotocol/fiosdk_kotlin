package fiofoundation.io.fiosdk.session

import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import fiofoundation.io.fiosdk.interfaces.IABIProvider
import fiofoundation.io.fiosdk.interfaces.IFIONetworkProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.Transaction
import fiofoundation.io.fiosdk.models.fionetworkprovider.TransactionConfig
import fiofoundation.io.fiosdk.errors.ErrorConstants
import fiofoundation.io.fiosdk.errors.session.TransactionProcessorConstructorInputError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetBlockError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetInfoError
import fiofoundation.io.fiosdk.errors.session.TransactionPrepareRpcError
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetBlockRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.GetBlockResponse
import fiofoundation.io.fiosdk.errors.session.TransactionPrepareError
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.GetInfoResponse
import fiofoundation.io.fiosdk.errors.session.TransactionPrepareInputError
import fiofoundation.io.fiosdk.formatters.DateFormatter
import fiofoundation.io.fiosdk.models.fionetworkprovider.Action
import java.math.BigInteger
import java.text.ParseException
import fiofoundation.io.fiosdk.utilities.Utils
import java.io.IOException
import fiofoundation.io.fiosdk.errors.session.TransactionSignError
import fiofoundation.io.fiosdk.errors.signatureprovider.SignatureProviderError
import fiofoundation.io.fiosdk.errors.session.TransactionGetSignatureError
import fiofoundation.io.fiosdk.errors.session.TransactionCreateSignatureRequestError
import fiofoundation.io.fiosdk.models.signatureprovider.FIOTransactionSignatureRequest
import fiofoundation.io.fiosdk.models.signatureprovider.FIOTransactionSignatureResponse
import fiofoundation.io.fiosdk.errors.session.TransactionGetSignatureDeserializationError
import fiofoundation.io.fiosdk.errors.serializationprovider.DeserializeTransactionError
import fiofoundation.io.fiosdk.errors.session.TransactionGetSignatureNotAllowModifyTransactionError
import fiofoundation.io.fiosdk.errors.session.TransactionGetSignatureSigningError

import fiofoundation.io.fiosdk.errors.fionetworkprovider.PushTransactionError
import fiofoundation.io.fiosdk.errors.session.TransactionPushTransactionError
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.PushTransactionRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.PushTransactionResponse
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetRequiredKeysError
import fiofoundation.io.fiosdk.errors.session.TransactionCreateSignatureRequestRpcError
import fiofoundation.io.fiosdk.errors.session.TransactionCreateSignatureRequestRequiredKeysEmptyError
import fiofoundation.io.fiosdk.errors.session.TransactionCreateSignatureRequestEmptyAvailableKeyError
import fiofoundation.io.fiosdk.errors.session.TransactionCreateSignatureRequestKeyError
import fiofoundation.io.fiosdk.errors.signatureprovider.GetAvailableKeysError
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetRequiredKeysRequest


class TransactionProcessor(private val serializationProvider: ISerializationProvider,
                           private val fioNetworkProvider: IFIONetworkProvider,
                           val abiProvider: IABIProvider,
                           private val signatureProvider: ISignatureProvider)
{

    @Throws(TransactionProcessorConstructorInputError::class)
    constructor(serializationProvider: ISerializationProvider,
                fioNetworkProvider: IFIONetworkProvider,
                abiProvider: IABIProvider,
                signatureProvider: ISignatureProvider,transaction: Transaction)
            :this(serializationProvider,fioNetworkProvider,abiProvider,signatureProvider){
        if (transaction.actions.isEmpty()) {
            throw TransactionProcessorConstructorInputError(
                    ErrorConstants.TRANSACTION_PROCESSOR_ACTIONS_EMPTY_ERROR_MSG)
        }

        this.transaction = transaction
    }

    var transaction: Transaction? = null

    var originalTransaction: Transaction? = null

    var signatures = ArrayList<String>()

    var serializedTransaction: String? = null

    var availableKeys: List<String>? = null

    var requiredKeys: List<String>? = null

    var transactionConfig = TransactionConfig()

    var chainId: String? = null

    var isTransactionModificationAllowed: Boolean = false

    private fun finishPreparing(preparingTransaction: Transaction)
    {
        transaction = preparingTransaction

        if (!this.serializedTransaction.isNullOrEmpty()) {
            this.serializedTransaction = ""
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun getDeepClone(): Transaction?
    {
        return if (this.transaction != null) Utils.clone(this.transaction!!) else null

    }

    @Throws(TransactionGetSignatureError::class)
    private fun getSignature(fioTransactionSignatureRequest: FIOTransactionSignatureRequest): FIOTransactionSignatureResponse
    {
        val fioTransactionSignatureResponse: FIOTransactionSignatureResponse

        try
        {
            fioTransactionSignatureResponse = this.signatureProvider
                .signTransaction(fioTransactionSignatureRequest)

            if (fioTransactionSignatureResponse.error != null) {
                throw fioTransactionSignatureResponse.error
            }

        }
        catch (signatureProviderError: SignatureProviderError)
        {
            throw TransactionGetSignatureSigningError(
                ErrorConstants.TRANSACTION_PROCESSOR_SIGN_TRANSACTION_ERROR,
                signatureProviderError)
        }

        if (fioTransactionSignatureResponse.serializedTransaction.isNullOrEmpty()) {
            throw TransactionGetSignatureSigningError(
                ErrorConstants.TRANSACTION_PROCESSOR_SIGN_TRANSACTION_TRANS_EMPTY_ERROR
            )
        }

        if (fioTransactionSignatureResponse.signatures.isEmpty()) {
            throw TransactionGetSignatureSigningError(
                ErrorConstants.TRANSACTION_PROCESSOR_SIGN_TRANSACTION_SIGN_EMPTY_ERROR)
        }

        this.originalTransaction = this.transaction

        if (this.serializedTransaction != null && !this.serializedTransaction
                .equals(fioTransactionSignatureResponse.serializedTransaction))
        {
            // Throw error if an unmodifiable transaction is modified
            if (!this.isTransactionModificationAllowed)
            {
                throw TransactionGetSignatureNotAllowModifyTransactionError(
                    ErrorConstants.TRANSACTION_IS_NOT_ALLOWED_TOBE_MODIFIED)
            }

            val transactionJSON: String?
            try
            {
                transactionJSON = this.serializationProvider
                    .deserializeTransaction(fioTransactionSignatureResponse.serializedTransaction)

                if (transactionJSON == null || transactionJSON.isEmpty())
                {
                    throw DeserializeTransactionError(
                        ErrorConstants.TRANSACTION_PROCESSOR_GET_SIGN_DESERIALIZE_TRANS_EMPTY_ERROR)
                }
            }
            catch (deserializeTransactionError: DeserializeTransactionError)
            {
                throw TransactionGetSignatureDeserializationError(
                    ErrorConstants.TRANSACTION_PROCESSOR_GET_SIGN_DESERIALIZE_TRANS_ERROR,
                    deserializeTransactionError)
            }

            this.transaction =
                Utils.getGson(DateFormatter.BACKEND_DATE_PATTERN).fromJson(transactionJSON, Transaction::class.java)
        }

        this.signatures = ArrayList()
        this.signatures.addAll(fioTransactionSignatureResponse.signatures)
        this.serializedTransaction = fioTransactionSignatureResponse.serializedTransaction

        return fioTransactionSignatureResponse
    }

    @Throws(TransactionPushTransactionError::class)
    private fun pushTransaction(pushTransactionRequest: PushTransactionRequest): PushTransactionResponse {
        try {
            return this.fioNetworkProvider.pushTransaction(pushTransactionRequest)
        } catch (pushTransactionRpcError: PushTransactionError) {
            throw TransactionPushTransactionError(
                ErrorConstants.TRANSACTION_PROCESSOR_RPC_PUSH_TRANSACTION,
                pushTransactionRpcError
            )
        }

    }

    @Throws(TransactionCreateSignatureRequestError::class)
    private fun createSignatureRequest(): FIOTransactionSignatureRequest {
        if (this.transaction == null)
        {
            throw TransactionCreateSignatureRequestError(
                ErrorConstants.TRANSACTION_PROCESSOR_TRANSACTION_HAS_TO_BE_INITIALIZED)
        }

        if (this.transaction?.actions!!.isEmpty())
        {
            throw TransactionCreateSignatureRequestError(
                ErrorConstants.TRANSACTION_PROCESSOR_ACTIONS_EMPTY_ERROR_MSG)
        }

        // Cache the serialized version of transaction in the TransactionProcessor
        this.serializedTransaction = this.serializedTransaction

        val fioTransactionSignatureRequest = FIOTransactionSignatureRequest(
            this.serializedTransaction, null,
            this.chainId, null, this.isTransactionModificationAllowed)

        if (this.requiredKeys != null && this.requiredKeys?.isNotEmpty()!!)
        {
            fioTransactionSignatureRequest.signingPublicKeys = this.requiredKeys
            return fioTransactionSignatureRequest
        }

        if (this.availableKeys == null || this.availableKeys?.isEmpty()!!)
        {
            try
            {
                this.availableKeys = this.signatureProvider.getAvailableKeys()

            }
            catch (getAvailableKeysError: GetAvailableKeysError)
            {
                throw TransactionCreateSignatureRequestKeyError(
                    ErrorConstants.TRANSACTION_PROCESSOR_GET_AVAILABLE_KEY_ERROR,
                    getAvailableKeysError)
            }

            if (this.availableKeys?.isEmpty()!!)
            {
                throw TransactionCreateSignatureRequestEmptyAvailableKeyError(
                    ErrorConstants.TRANSACTION_PROCESSOR_GET_AVAILABLE_KEY_EMPTY)
            }
        }

        try
        {
            val getRequiredKeysResponse = this.fioNetworkProvider.getRequiredKeys(
                    GetRequiredKeysRequest(this.availableKeys, this.transaction))

            if (getRequiredKeysResponse.requiredKeys == null || getRequiredKeysResponse.requiredKeys.isEmpty()) {
                throw TransactionCreateSignatureRequestRequiredKeysEmptyError(
                    ErrorConstants.GET_REQUIRED_KEY_RPC_EMPTY_RESULT)
            }

            val backendRequiredKeys = getRequiredKeysResponse.requiredKeys

            if (!this.availableKeys?.containsAll(backendRequiredKeys)!!) {
                throw TransactionCreateSignatureRequestRequiredKeysEmptyError(
                    ErrorConstants.TRANSACTION_PROCESSOR_REQUIRED_KEY_NOT_SUBSET)
            }

            this.requiredKeys = backendRequiredKeys
        }
        catch (getRequiredKeysRpcError: GetRequiredKeysError)
        {
            throw TransactionCreateSignatureRequestRpcError(
                ErrorConstants.TRANSACTION_PROCESSOR_RPC_GET_REQUIRED_KEYS,
                getRequiredKeysRpcError)
        }

        fioTransactionSignatureRequest.signingPublicKeys = this.requiredKeys

        return fioTransactionSignatureRequest
    }

    //public methods

    @Throws(TransactionPrepareError::class)
    fun prepare(actions: List<Action>, contextFreeActions: List<Action>)
    {
        if (actions.isEmpty()) {
            throw TransactionPrepareInputError(ErrorConstants.TRANSACTION_PROCESSOR_ACTIONS_EMPTY_ERROR_MSG)
        }

        val preparingTransaction = Transaction(
            "", BigInteger.ZERO, BigInteger.ZERO,
            BigInteger.ZERO, BigInteger.ZERO,
            BigInteger.ZERO, contextFreeActions, actions, ArrayList())

        val getInfoResponse: GetInfoResponse

        try
        {
            getInfoResponse = this.fioNetworkProvider.getInfo()
        }
        catch (getInfoRpcError: GetInfoError) {
            throw TransactionPrepareRpcError(
                ErrorConstants.TRANSACTION_PROCESSOR_RPC_GET_INFO,
                getInfoRpcError
            )
        }


        if (this.chainId.isNullOrEmpty())
        {
            this.chainId = getInfoResponse.chainId ?: throw TransactionPrepareError(ErrorConstants.TRANSACTION_PROCESSOR_PREPARE_CHAINID_RPC_EMPTY)

            if (getInfoResponse.chainId.isNullOrEmpty()) {
                throw TransactionPrepareError(ErrorConstants.TRANSACTION_PROCESSOR_PREPARE_CHAINID_RPC_EMPTY)
            }

            this.chainId = getInfoResponse.chainId
        }
        else if (!getInfoResponse.chainId.isNullOrEmpty() && getInfoResponse.chainId != chainId)
        {
            // Throw error if both are not empty but one does not match with another
            throw TransactionPrepareError(
                String.format(ErrorConstants.TRANSACTION_PROCESSOR_PREPARE_CHAINID_NOT_MATCH,
                    this.chainId,
                    getInfoResponse.chainId)
            )
        }

        if (preparingTransaction.expiration.isEmpty())
        {
            val strHeadBlockTime = getInfoResponse.headBlockTime

            val headBlockTime: Long

            try
            {
                headBlockTime = DateFormatter.convertBackendTimeToMilli(strHeadBlockTime)
            }
            catch (e: ParseException)
            {
                throw TransactionPrepareError(
                    ErrorConstants.TRANSACTION_PROCESSOR_HEAD_BLOCK_TIME_PARSE_ERROR, e)
            }

            val expiresSeconds = this.transactionConfig.expiresSeconds

            val expirationTimeInMilliseconds = headBlockTime + expiresSeconds * 1000

            preparingTransaction.expiration = DateFormatter.convertMilliSecondToBackendTimeString(expirationTimeInMilliseconds)
        }

        val headBlockNum: BigInteger

        val blockBehindConfig = this.transactionConfig.blocksBehind

        if (getInfoResponse.headBlockNumber?.compareTo(BigInteger.valueOf(blockBehindConfig.toLong()))!! > 0) {
            headBlockNum = getInfoResponse.headBlockNumber.subtract(BigInteger.valueOf(blockBehindConfig.toLong()))
        }
        else
        {
            headBlockNum = BigInteger.valueOf(blockBehindConfig.toLong())
        }

        val getBlockResponse: GetBlockResponse
        try
        {
            getBlockResponse = this.fioNetworkProvider
                .getBlock(GetBlockRequest(headBlockNum.toString()))
        }
        catch (getBlockRpcError: GetBlockError)
        {
            throw TransactionPrepareRpcError(
                ErrorConstants.TRANSACTION_PROCESSOR_PREPARE_RPC_GET_BLOCK, getBlockRpcError)
        }

        // Restrict the refBlockNum to 32 bit unsigned value
        val refBlockNum = getBlockResponse.blockNumber?.and(BigInteger.valueOf(0xffff))
        val refBlockPrefix = getBlockResponse.refBlockPrefix

        preparingTransaction.refBlockNum = refBlockNum
        preparingTransaction.refBlockPrefix = refBlockPrefix

        this.finishPreparing(preparingTransaction)
    }

    @Throws(TransactionPrepareError::class)
    fun prepare(actions: List<Action>) {
        this.prepare(actions, ArrayList())
    }

    @Throws(TransactionSignError::class)
    fun sign(): Boolean
    {
        val fioTransactionSignatureRequest: FIOTransactionSignatureRequest
        try
        {
            fioTransactionSignatureRequest = this.createSignatureRequest()
        }
        catch (transactionCreateSignatureRequestError: TransactionCreateSignatureRequestError)
        {
            throw TransactionSignError(
                ErrorConstants.TRANSACTION_PROCESSOR_SIGN_CREATE_SIGN_REQUEST_ERROR,
                transactionCreateSignatureRequestError)
        }

        val fioTransactionSignatureResponse: FIOTransactionSignatureResponse
        try
        {
            fioTransactionSignatureResponse = this.getSignature(fioTransactionSignatureRequest)
            if (fioTransactionSignatureResponse.error != null) {
                throw fioTransactionSignatureResponse.error
            }
        }
        catch (transactionGetSignatureError: TransactionGetSignatureError) {
            throw TransactionSignError(transactionGetSignatureError)
        }
        catch (signatureProviderError: SignatureProviderError) {
            throw TransactionSignError(
                ErrorConstants.TRANSACTION_PROCESSOR_SIGN_SIGNATURE_RESPONSE_ERROR,
                signatureProviderError)
        }

        return true
    }
}