package fiofoundation.io.fiosdk

import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.formatters.FIOFormatterError
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.implementations.ABIProvider
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.IAction
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.RegisterFIOAddressAction
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.RegisterFIODomainAction
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.TransferTokensPubKeyAction
import fiofoundation.io.fiosdk.session.processors.RegisterFIOAddressTrxProcessor
import fiofoundation.io.fiosdk.session.processors.RegisterFIODomainTrxProcessor
import fiofoundation.io.fiosdk.session.processors.TransTokensPublicKeyTrxProcessor
import fiofoundation.io.fiosdk.utilities.PrivateKeyUtils
import java.lang.Exception

import java.math.BigInteger

class FIOSDK(val privateKey: String, val publicKey: String,
             val serializationProvider: ISerializationProvider,
             val signatureProvider: ISignatureProvider) {

    private val networkProvider:FIONetworkProvider = FIONetworkProvider("http://54.184.39.43:8889")
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
                           walletFioAddress:String)
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

            transactionProcessor.broadcast()

    }

    @Throws(FIOError::class)
    fun registerFioDomain(fioDomain:String,ownerPublicKey:String, maxFee:BigInteger,
                          walletFioAddress:String)
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

        transactionProcessor.broadcast()
    }

    @Throws(FIOError::class)
    fun transferTokensToPublicKey(payeePublicKey:String,amount:String, maxFee:BigInteger,
                                  walletFioAddress:String)
    {
        var transferTokensToPublickey = TransferTokensPubKeyAction(
            payeePublicKey,
            amount,
            maxFee,
            walletFioAddress,
            this.publicKey
        )

        var transactionProcessor = TransTokensPublicKeyTrxProcessor(
            this.serializationProvider,
            this.networkProvider,
            this.abiProvider,
            this.signatureProvider
        )

        var actionList = ArrayList<TransferTokensPubKeyAction>()
        actionList.add(transferTokensToPublickey)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        transactionProcessor.broadcast()
    }

}