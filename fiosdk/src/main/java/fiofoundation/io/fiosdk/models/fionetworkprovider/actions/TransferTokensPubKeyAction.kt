package fiofoundation.io.fiosdk.models.fionetworkprovider.actions


import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.Validator
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class TransferTokensPubKeyAction(payeePublicKey: String,
                                 amount: String,
                                 maxFee: BigInteger,
                              walletFioAddress: String,
                              actorPublicKey: String) : IAction
{
    override var account = "fio.token"
    override var name = "trnsfiopubky"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            TransferTokensPubKeyRequestData(
                payeePublicKey,
                amount,
                maxFee,
                auth.actor,
                walletFioAddress
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class TransferTokensPubKeyRequestData(
        @field:SerializedName("payee_public_key") var payeePublicKey:String,
        @field:SerializedName("amount") var amount:String,
        @field:SerializedName("max_fee") var max_fee:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var walletFioAddress:String): FIORequestData()
}
