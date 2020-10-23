package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import java.math.BigInteger

class CancelFundsRequestAction(
    fioRequestId: BigInteger,
    maxFee: BigInteger,
    technologyPartnerId: String,
    actorPublicKey: String) : IAction
{
    override var account = "fio.reqobt"
    override var name = "cancelfndreq"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            CancelFundsRequestData(
                if(fioRequestId <= BigInteger.ZERO) "" else fioRequestId.toString(),
                maxFee,
                auth.actor,
                technologyPartnerId
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class CancelFundsRequestData(@field:SerializedName("fio_request_id") var fioRequestId:String,
                              @field:SerializedName("max_fee") var max_fee:BigInteger,
                              @field:SerializedName("actor") var actor:String,
                              @field:SerializedName("tpid") var technologyPartnerId:String): FIORequestData()
}