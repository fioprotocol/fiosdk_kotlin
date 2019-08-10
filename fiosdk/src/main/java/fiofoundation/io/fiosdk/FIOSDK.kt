package fiofoundation.io.fiosdk

import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetFIOBalanceError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetPendingFIORequestsError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetPublicAddressError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetSentFIORequestsError
import fiofoundation.io.fiosdk.errors.formatters.FIOFormatterError
import fiofoundation.io.fiosdk.errors.serializationprovider.SerializeTransactionError
import fiofoundation.io.fiosdk.errors.session.TransactionBroadCastError
import fiofoundation.io.fiosdk.errors.session.TransactionPrepareError
import fiofoundation.io.fiosdk.errors.session.TransactionSignError
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.implementations.ABIProvider
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import fiofoundation.io.fiosdk.models.Cryptography
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIORequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetFIOBalanceRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetPendingFIORequestsRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetPublicAddressRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetSentFIORequestsRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.PushTransactionResponse
import fiofoundation.io.fiosdk.session.processors.*
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import fiofoundation.io.fiosdk.utilities.HashUtils
import fiofoundation.io.fiosdk.utilities.PrivateKeyUtils

import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class FIOSDK(val privateKey: String, val publicKey: String,
             val serializationProvider: ISerializationProvider,
             val signatureProvider: ISignatureProvider) {

    private val networkProvider:FIONetworkProvider = FIONetworkProvider("http://54.184.39.43:8889/v1/")
    private val abiProvider:ABIProvider = ABIProvider(networkProvider,this.serializationProvider)

    companion object Static {
        private var fioSdk: FIOSDK? = null

        private const val ISLEGACY_KEY_FORMAT = true

        @Throws(FIOFormatterError::class)
        fun createPrivateKey(mnemonic: String): String {
            return FIOFormatter.convertPEMFormattedPrivateKeyToFIOFormat(
                PrivateKeyUtils.createPEMFormattedPrivateKey(mnemonic)
            )
        }

        @Throws(FIOFormatterError::class)
        fun derivePublicKey(fioPrivateKey: String): String {
            return FIOFormatter.convertPEMFormattedPublicKeyToFIOFormat(
                PrivateKeyUtils.extractPEMFormattedPublicKey(
                    FIOFormatter.convertFIOPrivateKeyToPEMFormat(fioPrivateKey)
                ), ISLEGACY_KEY_FORMAT
            )
        }

        fun getInstance(privateKey: String,publickey: String,
                        serializationProvider: ISerializationProvider,
                        signatureProvider: ISignatureProvider): FIOSDK
        {
            if(fioSdk == null)
             fioSdk = FIOSDK(privateKey,publickey,serializationProvider, signatureProvider)

            return fioSdk!!
        }


    }

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

    @Throws(FIOError::class)
    fun getFioBalance(): BigInteger
    {
        try
        {
            val request = GetFIOBalanceRequest(this.publicKey)
            val response = this.networkProvider.getFIOBalance(request)

            return response.balance
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

    @Throws(FIOError::class)
    fun getPublicKey(fioAddress:String): String
    {
        try
        {
            val request = GetPublicAddressRequest(fioAddress,"FIO")
            val response = this.networkProvider.getPublicAddress(request)

            return response.publicAddress
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


    @Throws(FIOError::class)
    fun requestNewFunds(payerfioAddress:String,payeefioAddress:String,
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
            val payerPublicKey = this.getPublicKey(payerfioAddress)

            val encryptedContent = serializeAndEncryptNewFundsContent(fundsRequestContent,payerPublicKey)

            var newFundsRequestAction = NewFundsRequestAction(
                payerfioAddress,
                payeefioAddress,
                encryptedContent,
                maxFee,
                walletFioAddress,
                this.publicKey
            )


            var actionList = ArrayList<NewFundsRequestAction>()
            actionList.add(newFundsRequestAction)

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

    @Throws(FIOError::class)
    fun getPendingFioRequests(requesteeFioPublicKey:String): List<FIORequestContent>
    {
        try
        {
            val request = GetPendingFIORequestsRequest(requesteeFioPublicKey)
            val response = this.networkProvider.getPendingFIORequests(request)

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

    fun getPendingFioRequests(): List<FIORequestContent>
    {
        return this.getPendingFioRequests(this.publicKey)
    }

    @Throws(FIOError::class)
    fun getSentFioRequests(): List<FIORequestContent>
    {
        return this.getSentFioRequests(this.publicKey)
    }

    @Throws(FIOError::class)
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
    private fun getSentFioRequests(requesteeFioPublicKey:String): List<FIORequestContent>
    {
        try
        {
            val request = GetSentFIORequestsRequest(requesteeFioPublicKey)
            val response = this.networkProvider.getSentFIORequests(request)

            for (item in response.requests)
            {
                val sharedSecretKey = CryptoUtils.generateSharedSecret(this.privateKey, item.payerFioPublicKey)
                item.deserializeRequestContent(sharedSecretKey,this.serializationProvider)
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

}