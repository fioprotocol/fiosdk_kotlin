package fiofoundation.io.fiosdk.models.fionetworkprovider.actions


import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import java.math.BigInteger

class BurnFIOAddressAction(fioAddress: String,
                           maxFee: BigInteger,
                           technologyPartnerId: String,
                           actorPublicKey: String) : IAction
{
    override var account = "fio.address"
    override var name = "burnaddress"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            BurnFIOAddressRequestData(
                fioAddress,
                maxFee,
                auth.actor,
                technologyPartnerId
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class BurnFIOAddressRequestData(
        @field:SerializedName("fio_address") var fio_address:String,
        @field:SerializedName("max_fee") var max_fee:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var technologyPartnerId:String): FIORequestData()
}
