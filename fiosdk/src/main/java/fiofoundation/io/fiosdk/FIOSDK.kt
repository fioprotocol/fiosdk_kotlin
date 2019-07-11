package fiofoundation.io.fiosdk

import fiofoundation.io.fiosdk.errors.formatters.FIOFormatterError
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.implementations.ABIProvider
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.IAction
import fiofoundation.io.fiosdk.models.fionetworkprovider.RegisterFIOAddressAction
import fiofoundation.io.fiosdk.session.RegisterFIOAddressTransactionProcesser
import fiofoundation.io.fiosdk.session.TransactionProcessor
import fiofoundation.io.fiosdk.session.TransactionSession
import fiofoundation.io.fiosdk.utilities.PrivateKeyUtils

class FIOSDK(val privateKey: String, val publicKey: String,
             val serializationProvider: ISerializationProvider,
             val signatureProvider: ISignatureProvider) {

    val networkProvider:FIONetworkProvider
    val abiProvider:ABIProvider

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

    init {
        networkProvider = FIONetworkProvider("http://54.184.39.43:8889")
        abiProvider = ABIProvider(networkProvider,this.serializationProvider)
    }

    fun registerFioAddress(fioAddress:String,ownerPublicKey:String,
                           maxFee:Int,walletFioAddress:String)
    {
        var registerFioAddressAction = RegisterFIOAddressAction(fioAddress,ownerPublicKey,walletFioAddress,maxFee,this.publicKey)

        //var transactionSession = TransactionSession(this.serializationProvider,this.networkProvider,this.abiProvider,this.signatureProvider)
        var transactionProcessor = RegisterFIOAddressTransactionProcesser(this.serializationProvider,this.networkProvider,this.abiProvider,this.signatureProvider)

        var actionList = ArrayList<RegisterFIOAddressAction>()
        actionList.add(registerFioAddressAction)

        transactionProcessor.prepare(actionList as ArrayList<IAction>)

        transactionProcessor.sign()

        transactionProcessor.broadcast()


    }

}