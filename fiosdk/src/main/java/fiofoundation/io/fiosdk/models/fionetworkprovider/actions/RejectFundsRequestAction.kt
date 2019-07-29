package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class RejectFundsRequestAction(fioRequestId: String,
                            maxFee: BigInteger,
                            actorPublicKey: String,
                            walletFioAddress: String) : IAction
{
    override var account = "fio.reqobt"
    override var name = "rejectfndreq"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            RejectFundsRequestData(
                fioRequestId,
                maxFee,
                auth.actor,
                walletFioAddress
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class RejectFundsRequestData(@field:SerializedName("fio_request_id") var fioRequestId:String,
                              @field:SerializedName("max_fee") var max_fee:BigInteger,
                              @field:SerializedName("actor") var actor:String,
                              @field:SerializedName("tpid") var walletFioAddress:String): FIOResponse()
}