package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse

class PayTpIdRewardsAction(actorPublicKey: String) : IAction
{
    override var account = "fio.treasury"
    override var name = "tpidclaim"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData = PayTpIdRewardsData(auth.actor)

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class PayTpIdRewardsData(@field:SerializedName("actor") var actor:String): FIOResponse()
}