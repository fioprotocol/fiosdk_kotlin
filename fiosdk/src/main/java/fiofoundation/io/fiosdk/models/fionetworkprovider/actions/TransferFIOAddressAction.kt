package fiofoundation.io.fiosdk.models.fionetworkprovider.actions


import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import java.math.BigInteger

class TransferFIOAddressAction(fioAddress: String,
                               newOwnerFIOPublicKey: String,
                               maxFee: BigInteger,
                               technologyPartnerId: String,
                               actorPublicKey: String) : IAction
{
    override var account = "fio.address"
    override var name = "xferaddress"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            TransferFIOAddressRequestData(
                    fioAddress,
                    newOwnerFIOPublicKey,
                    maxFee,
                    auth.actor,
                    technologyPartnerId
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class TransferFIOAddressRequestData(
        @field:SerializedName("fio_address") var fio_address:String,
        @field:SerializedName("new_owner_fio_public_key") var new_owner_fio_public_key:String,
        @field:SerializedName("max_fee") var max_fee:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var technologyPartnerId:String): FIORequestData()
}
