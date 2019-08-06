package fiofoundation.io.fiosdk

import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetFIOBalanceError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetPublicAddressError
import fiofoundation.io.fiosdk.errors.formatters.FIOFormatterError
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.implementations.ABIProvider
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import fiofoundation.io.fiosdk.models.Cryptography
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetFIOBalanceRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.GetPublicAddressRequest
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
            var registerFioAddressAction =
                RegisterFIOAddressAction(
                    fioAddress,
                    ownerPublicKey,
                    walletFioAddress,
                    maxFee,
                    this.publicKey
                )

            var transactionProcessor = RegisterFIOAddressTrxProcessor(
                this.serializationProvider,
                this.networkProvider,
                this.abiProvider,
                this.signatureProvider
            )

            var actionList = ArrayList<RegisterFIOAddressAction>()
            actionList.add(registerFioAddressAction)

            transactionProcessor.prepare(actionList as ArrayList<IAction>)

            transactionProcessor.sign()

            return transactionProcessor.broadcast()

    }

    @Throws(FIOError::class)
    fun registerFioDomain(fioDomain:String,ownerPublicKey:String, maxFee:BigInteger,
                          walletFioAddress:String): PushTransactionResponse
    {
        var registerFioDomainAction = RegisterFIODomainAction(
            fioDomain,
            ownerPublicKey,
            walletFioAddress,
            maxFee,
            this.publicKey
        )

        var transactionProcessor = RegisterFIODomainTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        var actionList = ArrayList<RegisterFIODomainAction>()
        actionList.add(registerFioDomainAction)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        return transactionProcessor.broadcast()
    }

    @Throws(FIOError::class)
    fun transferTokensToPublicKey(payeePublicKey:String,amount:String, maxFee:BigInteger,
                                  walletFioAddress:String): PushTransactionResponse
    {
        var transferTokensToPublickey = TransferTokensPubKeyAction(
            payeePublicKey,
            amount,
            maxFee,
            walletFioAddress,
            this.publicKey
        )

        var transactionProcessor = TransTokensPubKeyTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        var actionList = ArrayList<TransferTokensPubKeyAction>()
        actionList.add(transferTokensToPublickey)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        return transactionProcessor.broadcast()
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
    fun payTpIdRewards(): PushTransactionResponse
    {
        var payTpIdReward = PayTpIdRewardsAction(this.publicKey)

        var transactionProcessor = PayTpIdRewardsTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        var actionList = ArrayList<PayTpIdRewardsAction>()
        actionList.add(payTpIdReward)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        return transactionProcessor.broadcast()
    }

    @Throws(FIOError::class)
    fun burnExpiredFioAddressesAndDomains(): PushTransactionResponse
    {
        var burnExpired = BurnExpiredAction(this.publicKey)

        var transactionProcessor = BurnExpiredTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        var actionList = ArrayList<BurnExpiredAction>()
        actionList.add(burnExpired)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        return transactionProcessor.broadcast()
    }

    @Throws(FIOError::class)
    fun claimBlockProducerRewards(blockProducerFioAddress: String): PushTransactionResponse
    {
        var claimBpRewards = ClaimBpRewardsAction(blockProducerFioAddress,this.publicKey)

        var transactionProcessor = ClaimBpRewardsTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        var actionList = ArrayList<ClaimBpRewardsAction>()
        actionList.add(claimBpRewards)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        return transactionProcessor.broadcast()
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

    private fun serializeAndEncryptNewFundsContent(fundsRequestContent: FundsRequestContent,payerPublickey: String): String
    {
        val serializedNewFundsContent = this.serializationProvider.serializeNewFundsContent(fundsRequestContent.toJson())

        val secretKey = CryptoUtils.generateSharedSecret(this.privateKey,payerPublickey)

        val hashedSecretKey = HashUtils.sha512(secretKey)

        val encryptionKey = hashedSecretKey.copyOf(32)
        val hmacKey = hashedSecretKey.copyOfRange(32,hashedSecretKey.size)
        val encryptor = Cryptography(encryptionKey,null)
        val encryptedMessage = encryptor.encrypt(serializedNewFundsContent.hexStringToByteArray().asUByteArray())
        val hmacContent = ByteArray(encryptor.iv!!.size + encryptedMessage.size)

        encryptor.iv!!.copyInto(hmacContent)
        encryptedMessage.copyInto(hmacContent,encryptor.iv!!.size)

        val hmacData = Cryptography.createHmac(hmacContent,hmacKey)

        val returnArray = ByteArray(hmacContent.size + hmacData.size)

        hmacContent.copyInto(returnArray)
        hmacData.copyInto(returnArray,hmacContent.size)

        return returnArray.toHexString()
    }

}