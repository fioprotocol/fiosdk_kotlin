package fiofoundation.io.fiosdk.models.fionetworkprovider.actions


import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import java.math.BigInteger

class AddBundledAction(fioAddress: String,bundleSets: BigInteger, maxFee: BigInteger,
                              technologyPartnerId: String,
                              actorPublicKey: String) : IAction
{
    override var account = "fio.address"
    override var name = "addbundles"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            AddBundledTransactionsRequestData(
                fioAddress.toLowerCase(),
                bundleSets,
                maxFee,
                auth.actor,
                technologyPartnerId
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class AddBundledTransactionsRequestData(
        @field:SerializedName("fio_address") var fioAddress:String,
        @field:SerializedName("bundle_sets") var bundleSets:BigInteger,
        @field:SerializedName("max_fee") var max_fee:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var technologyPartnerId:String): FIORequestData()
}