package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse

class ClaimBpRewardsAction(blockProducerFioAddress:String,actorPublicKey: String) : IAction
{
    override var account = "fio.treasury"
    override var name = "bpclaim"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData = ClaimBpRewardsData(
            blockProducerFioAddress,
            auth.actor)

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class ClaimBpRewardsData(
        @field:SerializedName("fio_address") var blockProducerFioAddress:String,
        @field:SerializedName("actor") var actor:String): FIOResponse()
}