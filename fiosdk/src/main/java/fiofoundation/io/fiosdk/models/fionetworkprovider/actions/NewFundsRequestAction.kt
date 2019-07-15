package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

//This is not fully implemented.
class NewFundsRequestAction(fioAddress: String,
                               ownerPublicKey: String,
                               walletFioAddress: String,
                               maxFee: BigInteger,
                               actorPublicKey: String) :
    IAction
{
    override var account = "fio.reqobt"
    override var name = "newfundsreq"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            NewFundsRequestData(
                fioAddress,
                ownerPublicKey,
                maxFee,
                auth.actor
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class NewFundsRequestData(@field:SerializedName("payer_fio_address") var payerFioAddress:String,
                                @field:SerializedName("payee_fio_address") var payeeFioAddress:String,
                                @field:SerializedName("max_fee") var max_fee:BigInteger,
                                @field:SerializedName("actor") var actor:String): FIOResponse()
}