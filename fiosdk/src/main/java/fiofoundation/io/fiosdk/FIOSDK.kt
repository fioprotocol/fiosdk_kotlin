package fiofoundation.io.fiosdk

import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.*
import fiofoundation.io.fiosdk.errors.formatters.FIOFormatterError
import fiofoundation.io.fiosdk.errors.serializationprovider.DeserializeTransactionError
import fiofoundation.io.fiosdk.errors.serializationprovider.SerializeTransactionError
import fiofoundation.io.fiosdk.errors.session.TransactionBroadCastError
import fiofoundation.io.fiosdk.errors.session.TransactionPrepareError
import fiofoundation.io.fiosdk.errors.session.TransactionSignError
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.implementations.ABIProvider
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.implementations.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIORequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordSendContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.*
import fiofoundation.io.fiosdk.session.processors.*
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import fiofoundation.io.fiosdk.utilities.PrivateKeyUtils

import java.math.BigInteger

/**
 * Kotlin SDK for FIO Foundation API
 *
 * @param privateKey the fio private key of the client sending requests to FIO API.
 * @param publicKey the fio public key of the client sending requests to FIO API.
 * @param serializationProvider the serialization provider used for abi serialization and deserialization.
 * @param signatureProvider the signature provider used to sign block chain transactions.
 * @param networkBaseUrl the url to the FIO API.
 * @param mockServerBaseUrl (optional) the url to the Mock Server.
 */
class FIOSDK(private var privateKey: String, var publicKey: String,
             var serializationProvider: ISerializationProvider,
             var signatureProvider: ISignatureProvider, private val networkBaseUrl:String,private val mockServerBaseUrl:String="") {

    private val networkProvider:FIONetworkProvider = FIONetworkProvider(networkBaseUrl,mockServerBaseUrl)

    private val abiProvider:ABIProvider = ABIProvider(networkProvider,this.serializationProvider)

    companion object Static {
        private var fioSdk: FIOSDK? = null

        private const val ISLEGACY_KEY_FORMAT = true

        /**
         * Create a FIO private key.
         *
         * @param mnemonic mnemonic used to generate a random unique private key.
         */
        @Throws(FIOFormatterError::class)
        fun createPrivateKey(mnemonic: String): String {
            return FIOFormatter.convertPEMFormattedPrivateKeyToFIOFormat(
                PrivateKeyUtils.createPEMFormattedPrivateKey(mnemonic)
            )
        }

        /**
         * Create a FIO public key.
         *
         * @param fioPrivateKey FIO private key.
         */
        @Throws(FIOFormatterError::class)
        fun derivePublicKey(fioPrivateKey: String): String {
            return FIOFormatter.convertPEMFormattedPublicKeyToFIOFormat(
                PrivateKeyUtils.extractPEMFormattedPublicKey(
                    FIOFormatter.convertFIOPrivateKeyToPEMFormat(fioPrivateKey)
                ), ISLEGACY_KEY_FORMAT
            )
        }

        /**
         * Initialize a static instance of the FIO SDK.  If an instance already exists,
         * it will be returned.
         *
         * @param privateKey the fio private key of the client sending requests to FIO API.
         * @param publicKey the fio public key of the client sending requests to FIO API.
         * @param serializationProvider the serialization provider used for abi serialization and deserialization.
         * @param signatureProvider the signature provider used to sign block chain transactions.
         * @param networkBaseUrl the url to the FIO API.
         * @param mockServerBaseUrl (optional) the url to the Mock Server.
         */
        fun getInstance(privateKey: String,publicKey: String,
                        serializationProvider: ISerializationProvider,
                        signatureProvider: ISignatureProvider,networkBaseUrl:String
                        ,mockServerBaseUrl:String=""): FIOSDK
        {
            if(fioSdk == null)
             fioSdk = FIOSDK(privateKey,publicKey,serializationProvider, signatureProvider,networkBaseUrl,mockServerBaseUrl)

            return fioSdk!!
        }

        fun getInstance(privateKey: String,publicKey: String,
                        serializationProvider: ISerializationProvider,
                        networkBaseUrl:String,mockServerBaseUrl:String=""): FIOSDK
        {
            if(fioSdk == null)
            {
                val signatureProvider = SoftKeySignatureProvider()
                signatureProvider.importKey(privateKey)

                fioSdk = FIOSDK(
                    privateKey,
                    publicKey,
                    serializationProvider,
                    signatureProvider,
                    networkBaseUrl,
                    mockServerBaseUrl
                )
            }

            return fioSdk!!
        }

        /**
         * Return a previously initialized instance of the FIO SDK.
         */
        fun getInstance(): FIOSDK
        {
            if(this.fioSdk == null)
                throw FIOError("The instance has not been previously initialized.")

            return fioSdk!!
        }

        /**
         * Set the FIO SDK instance to null
         */
        fun destroyInstance()
        {
            fioSdk = null
        }
    }

    fun setPrivateKey(privateKey:String)
    {
        this.privateKey = privateKey

        //If the signature provider is the default provider, then import the private key
        if(this.signatureProvider is SoftKeySignatureProvider)
            (this.signatureProvider as SoftKeySignatureProvider).importKey(privateKey)
    }

    fun getPrivateKey():String
    {
        return this.privateKey
    }

    /**
     * @suppress
     */
    @Throws(FIOError::class)
    fun registerFioNameOnBehalfOfUser(fioName:String): RegisterFIONameForUserResponse
    {
        val request = RegisterFIONameForUserRequest(fioName, this.publicKey)

        return this.networkProvider.registerFioNameOnBehalfOfUser(request)
    }

    /**
     * @suppress
     */
    @Throws(FIOError::class)
    fun registerFioNameOnBehalfOfUser(fioName:String,ownerPublicKey:String): RegisterFIONameForUserResponse
    {
        try
        {
            val request = RegisterFIONameForUserRequest(fioName, ownerPublicKey)

            return this.networkProvider.registerFioNameOnBehalfOfUser(request)
        }
        catch(registerFIONameForUserError: RegisterFIONameForUserError)
        {
            throw FIOError(registerFIONameForUserError.message!!,registerFIONameForUserError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Registers a FIO Address on the FIO blockchain.
     *
     * @param fioAddress FIO Address to register.
     * @param ownerPublicKey Public key which will own the FIO Address after registration. Set to empty if same as sender.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by @ [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun registerFioAddress(fioAddress:String,ownerPublicKey:String, maxFee:BigInteger,
                           walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = RegisterFIOAddressTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            var registerFioAddressAction =
                RegisterFIOAddressAction(
                    fioAddress,
                    ownerPublicKey,
                    walletFioAddress,
                    maxFee,
                    this.publicKey
                )

            var actionList = ArrayList<RegisterFIOAddressAction>()
            actionList.add(registerFioAddressAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Registers a FIO Address on the FIO blockchain.  The owner will be the public key associated with the FIO SDK instance.
     *
     * @param fioAddress FIO Address to register.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by @ [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun registerFioAddress(fioAddress:String, maxFee:BigInteger, walletFioAddress:String): PushTransactionResponse
    {
        return registerFioAddress(fioAddress,"",maxFee,walletFioAddress)
    }

    /**
     * Registers a FIO Domain on the FIO blockchain.
     *
     * @param fioDomain FIO Domain to register.
     * @param ownerPublicKey Public key which will own the FIO Domain after registration. Set to empty if same as sender.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by @ [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun registerFioDomain(fioDomain:String,ownerPublicKey:String, maxFee:BigInteger,
                          walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = RegisterFIODomainTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            var registerFioDomainAction = RegisterFIODomainAction(
                fioDomain,
                ownerPublicKey,
                walletFioAddress,
                maxFee,
                this.publicKey
            )

            var actionList = ArrayList<RegisterFIODomainAction>()
            actionList.add(registerFioDomainAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Registers a FIO Domain on the FIO blockchain.
     *
     * @param fioDomain FIO Domain to register. The owner will be the public key associated with the FIO SDK instance.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by @ [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun registerFioDomain(fioDomain:String, maxFee:BigInteger,
                          walletFioAddress:String): PushTransactionResponse
    {
        return registerFioDomain(fioDomain,"",maxFee,walletFioAddress)
    }

    /**
     * Renew a FIO Domain on the FIO blockchain.
     *
     * @param fioDomain FIO Domain to renew.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by @ [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun renewFioDomain(fioDomain:String, maxFee:BigInteger,
                          walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = RegisterFIODomainTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            var renewFioDomainAction = RenewFIODomainAction(
                fioDomain,
                maxFee,
                walletFioAddress,
                this.publicKey
            )

            var actionList = ArrayList<RenewFIODomainAction>()
            actionList.add(renewFioDomainAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Renew a FIO Address on the FIO blockchain.
     *
     * @param fioDomain FIO Address to renew.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by @ [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun renewFioAddress(fioAddress:String, maxFee:BigInteger,
                           walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = RenewFIOAddressTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            var renewFioAddressAction =
                RenewFIOAddressAction(
                    fioAddress,
                    maxFee,
                    walletFioAddress,
                    this.publicKey
                )

            var actionList = ArrayList<RenewFIOAddressAction>()
            actionList.add(renewFioAddressAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     *
     * Transfers FIO tokens from public key associated with the FIO SDK instance to
     * the payeePublicKey.
     *
     * @param payeePublicKey FIO public Address of the one receiving the tokens.
     * @param amount Amount sent in SUFs.
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by /get_fee for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun transferTokensToPublicKey(payeePublicKey:String,amount:String, maxFee:BigInteger,
                                  walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = TransTokensPubKeyTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            var transferTokensToPublickey = TransferTokensPubKeyAction(
                payeePublicKey,
                amount,
                maxFee,
                walletFioAddress,
                this.publicKey
            )

            var actionList = ArrayList<TransferTokensPubKeyAction>()
            actionList.add(transferTokensToPublickey)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()

        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Retrieves balance of FIO tokens using the public key of the client
     * sending the request.
     */
    @Throws(FIOError::class)
    fun getFioBalance(): GetFIOBalanceResponse
    {
        return this.getFioBalance(this.publicKey)
    }

    /**
     * Retrieves balance of FIO tokens
     *
     * @param fioPublicKey FIO public key.
     */
    @Throws(FIOError::class)
    fun getFioBalance(fioPublicKey:String): GetFIOBalanceResponse
    {
        try
        {
            val request = GetFIOBalanceRequest(fioPublicKey)

            return this.networkProvider.getFIOBalance(request)
        }
        catch(fioBalanceError: GetFIOBalanceError)
        {
            throw FIOError(fioBalanceError.message!!,fioBalanceError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Create a new funds request on the FIO chain.
     *
     * @param payerFioAddress FIO Address of the payer. This address will receive the request and will initiate payment.
     * @param payeeFioAddress FIO Address of the payee. This address is sending the request and will receive payment.
     * @param fundsRequestContent [FundsRequestContent]
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    @ExperimentalUnsignedTypes
    fun requestNewFunds(payerFioAddress:String,payeeFioAddress:String,
                        fundsRequestContent: FundsRequestContent,maxFee:BigInteger,
                          walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = NewFundsRequestTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            val payerPublicKey = this.getPublicAddress(payerFioAddress).publicAddress

            val encryptedContent = serializeAndEncryptNewFundsContent(fundsRequestContent,payerPublicKey)

            var newFundsRequestAction = NewFundsRequestAction(
                payerFioAddress,
                payeeFioAddress,
                encryptedContent,
                maxFee,
                walletFioAddress,
                this.publicKey
            )


            var actionList = ArrayList<NewFundsRequestAction>()
            actionList.add(newFundsRequestAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Reject funds request.
     *
     * @param fioRequestId Existing funds request Id
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by [getFee] for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    fun rejectFundsRequest(fioRequestId: String, maxFee: BigInteger,
                        walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = RejectFundsRequestTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {

            var rejectFundsRequestAction = RejectFundsRequestAction(
                fioRequestId,
                maxFee,
                walletFioAddress,
                this.publicKey
            )

            var actionList = ArrayList<RejectFundsRequestAction>()
            actionList.add(rejectFundsRequestAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     *
     * This call is made to record information on the FIO blockchain about a transaction that occurred on other blockchain, i.e. 1 BTC was sent on Bitcoin Blockchain, and both
     * sender and receiver have FIO Addresses. OBT stands for Other Blockchain Transaction
     *
     * @param payerFioAddress FIO Address of the payer. This address initiated payment.
     * @param payeeFioAddress FIO Address of the payee. This address is receiving payment.
     * @param recordSendContent [RecordSendContent]
     * @param fioRequestId ID of funds request, if this Record Send transaction is in response to a previously received funds request.  Send empty if no FIO Request ID
     * @param maxFee Maximum amount of SUFs the user is willing to pay for fee. Should be preceded by /get_fee for correct value.
     * @param walletFioAddress FIO Address of the wallet which generates this transaction.
     */
    @Throws(FIOError::class)
    @ExperimentalUnsignedTypes
    fun recordSend(payerFioAddress:String,payeeFioAddress:String,
                        recordSendContent: RecordSendContent, fioRequestId: String,
                   maxFee:BigInteger, walletFioAddress:String): PushTransactionResponse
    {
        var transactionProcessor = RecordSendTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        try
        {
            if(recordSendContent.status == "")
                recordSendContent.status = "sent_to_blockchain"

            //TODO: If the requestId is not blank, need to verify that the payeePublicKey, token_code
            // and amount match the corresponding parameters of the funds request.

            val encryptedContent = serializeAndEncryptRecordSendContent(recordSendContent,this.publicKey)

            var recordSendAction = RecordSendAction(
                payerFioAddress,
                payeeFioAddress,
                encryptedContent,
                fioRequestId,
                maxFee,
                walletFioAddress,
                this.publicKey
            )


            var actionList = ArrayList<RecordSendAction>()
            actionList.add(recordSendAction)

            @Suppress("UNCHECKED_CAST")
            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()
        }
        catch(fioError:FIOError)
        {
            throw fioError
        }
        catch(prepError: TransactionPrepareError)
        {
            throw FIOError(prepError.message!!,prepError)
        }
        catch(signError: TransactionSignError)
        {
            throw FIOError(signError.message!!,signError)
        }
        catch(broadcastError: TransactionBroadCastError)
        {
            throw FIOError(broadcastError.message!!,broadcastError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Polls for any pending requests sent to the requestee.
     *
     * @param requesteeFioPublicKey FIO public key of the requestee.
     */
    @Throws(FIOError::class)
    fun getPendingFioRequests(requesteeFioPublicKey:String): List<FIORequestContent>
    {
        try
        {
            val request = GetPendingFIORequestsRequest(requesteeFioPublicKey)
            val response = this.networkProvider.getPendingFIORequests(request)

            for (item in response.requests)
            {
                try
                {
                    val sharedSecretKey = CryptoUtils.generateSharedSecret(this.privateKey, item.payeeFioPublicKey)
                    item.deserializeRequestContent(sharedSecretKey,this.serializationProvider)
                }
                catch(deserializationError: DeserializeTransactionError)
                {
                    //eat this error.  We do not want this error to stop the process.
                }
            }

            return response.requests
        }
        catch(getPendingFIORequestsError: GetPendingFIORequestsError)
        {
            throw FIOError(getPendingFIORequestsError.message!!,getPendingFIORequestsError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Polls for any pending requests sent to public key associated with the FIO SDK instance.
     */
    fun getPendingFioRequests(): List<FIORequestContent>
    {
        return this.getPendingFioRequests(this.publicKey)
    }

    /**
     * Polls for any requests sent by owner.
     *
     * @param senderFioPublicKey FIO public key of the owner who sent the request.
     */
    @Throws(FIOError::class)
    fun getSentFioRequests(senderFioPublicKey:String): List<FIORequestContent>
    {
        try
        {
            val request = GetSentFIORequestsRequest(senderFioPublicKey)
            val response = this.networkProvider.getSentFIORequests(request)

            for (item in response.requests)
            {
                try
                {
                    val sharedSecretKey = CryptoUtils.generateSharedSecret(this.privateKey, item.payerFioPublicKey)
                    item.deserializeRequestContent(sharedSecretKey,this.serializationProvider)
                }
                catch(deserializationError: DeserializeTransactionError)
                {
                    //eat this error.  We do not want this error to stop the process.
                }
            }

            return response.requests
        }
        catch(getSentFIORequestsError: GetSentFIORequestsError)
        {
            throw FIOError(getSentFIORequestsError.message!!,getSentFIORequestsError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Polls for any sent requests sent by public key associated with the FIO SDK instance.
     */
    @Throws(FIOError::class)
    fun getSentFioRequests(): List<FIORequestContent>
    {
        return this.getSentFioRequests(this.publicKey)
    }

    /**
     * Returns FIO Addresses and FIO Domains owned by this public key.
     *
     * @param fioPublicKey FIO public key of owner.
     */
    @Throws(FIOError::class)
    fun getFioNames(fioPublicKey:String): GetFIONamesResponse
    {
        try
        {
            val request = GetFIONamesRequest(fioPublicKey)

            return this.networkProvider.getFIONames(request)
        }
        catch(getFioNamesError: GetFIONamesError)
        {
            throw FIOError(getFioNamesError.message!!,getFioNamesError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Returns the FIO token public address for specified FIO Address.
     *
     * @param fioAddress FIO Address for which fio token public address is to be returned.
     */
    @Throws(FIOError::class)
    fun getPublicAddress(fioAddress:String): GetPublicAddressResponse
    {
        return getPublicAddress(fioAddress,"FIO")
    }

    /**
     * Returns a token public address for specified token code and FIO Address.
     *
     * @param fioAddress FIO Address for which the token public address is to be returned.
     * @param tokenCode Token code for which public address is to be returned.
     */
    @Throws(FIOError::class)
    fun getPublicAddress(fioAddress:String, tokenCode:String): GetPublicAddressResponse
    {
        try
        {
            val request = GetPublicAddressRequest(fioAddress,tokenCode)

            return this.networkProvider.getPublicAddress(request)
        }
        catch(getPublicAddressError: GetPublicAddressError)
        {
            throw FIOError(getPublicAddressError.message!!,getPublicAddressError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Checks if a FIO Address or FIO Domain is available for registration.
     *
     * @param fioName FIO Address or FIO Domain to check.
     */
    @Throws(FIOError::class)
    fun isFioAddressAvailable(fioName:String): FIONameAvailabilityCheckResponse
    {
        try
        {
            val request = FIONameAvailabilityCheckRequest(fioName)

            return this.networkProvider.isFIONameAvailable(request)
        }
        catch(fioNameAvailabilityCheckError: FIONameAvailabilityCheckError)
        {
            throw FIOError(fioNameAvailabilityCheckError.message!!,fioNameAvailabilityCheckError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Compute and return fee amount for specific call and specific user
     *
     * @param fioAddress FIO Address incurring the fee and owned by signer.
     * @param endPointName Name of API call end point, e.g. add_pub_address.
     */
    @Throws(FIOError::class)
    fun getFee(fioAddress:String,endPointName:String): GetFeeResponse
    {
        try
        {
            val request = GetFeeRequest(endPointName,fioAddress)

            return this.networkProvider.getFee(request)
        }
        catch(getFeeError: GetFeeError)
        {
            throw FIOError(getFeeError.message!!,getFeeError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Get FIO blockchain information
     */
    @Throws(FIOError::class)
    fun getInfo(): GetInfoResponse
    {
        try
        {
            return this.networkProvider.getInfo()
        }
        catch(getInfoError: GetInfoError)
        {
            throw FIOError(getInfoError.message!!,getInfoError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     * Get FIO block information
     *
     * @param blockIdentifier last_irreversible_block_num or last_irreversible_block_id from [getInfo].
     */
    @Throws(FIOError::class)
    fun getBlock(blockIdentifier:String): GetBlockResponse
    {
        try
        {
            val request = GetBlockRequest(blockIdentifier)

            return this.networkProvider.getBlock(request)
        }
        catch(getBlockError: GetBlockError)
        {
            throw FIOError(getBlockError.message!!,getBlockError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    /**
     *  Get ABI for specific account name.
     *  Each signed call uses one of 3 account names:
     *      fio.system,
     *      fio.reqobt,
     *      fio.token
     *
     * @param accountName Account name. Check request definition for specific call above.
     */
    @Throws(FIOError::class)
    fun getRawAbi(accountName:String): GetRawAbiResponse
    {
        try
        {
            val request = GetRawAbiRequest(accountName)

            return this.networkProvider.getRawAbi(request)
        }
        catch(getRawAbiError: GetRawAbiError)
        {
            throw FIOError(getRawAbiError.message!!,getRawAbiError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }
    }

    //Private Methods

    @Throws(FIOError::class)
    @ExperimentalUnsignedTypes
    private fun serializeAndEncryptNewFundsContent(fundsRequestContent: FundsRequestContent,payerPublickey: String): String
    {
        try
        {
            val serializedNewFundsContent = this.serializationProvider.serializeNewFundsContent(fundsRequestContent.toJson())

            val secretKey = CryptoUtils.generateSharedSecret(this.privateKey,payerPublickey)


            return CryptoUtils.encryptSharedMessage(serializedNewFundsContent,secretKey)
        }
        catch(serializeError: SerializeTransactionError)
        {
            throw FIOError(serializeError.message!!,serializeError)
        }

    }

    @Throws(FIOError::class)
    @ExperimentalUnsignedTypes
    private fun serializeAndEncryptRecordSendContent(recordSendContent: RecordSendContent,payerPublickey: String): String
    {
        try
        {
            val serializedNewFundsContent = this.serializationProvider.serializeRecordSendContent(recordSendContent.toJson())

            val secretKey = CryptoUtils.generateSharedSecret(this.privateKey,payerPublickey)

            return CryptoUtils.encryptSharedMessage(serializedNewFundsContent,secretKey)
        }
        catch(serializeError: SerializeTransactionError)
        {
            throw FIOError(serializeError.message!!,serializeError)
        }

    }
}