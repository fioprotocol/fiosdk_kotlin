package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class RegisterFIOAddressAction(fioAddress: String,
                               ownerPublicKey: String,
                               walletFioAddress: String,
                               maxFee: BigInteger,
                               actorPublicKey: String) :
    IAction
{
    override var account = "fio.address"
    override var name = "regaddress"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            FIOAddressRequestData(
                fioAddress,
                ownerPublicKey,
                maxFee,
                auth.actor,
                walletFioAddress
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class FIOAddressRequestData(@field:SerializedName("fio_address") var fioAddress:String,
                                @field:SerializedName("owner_fio_public_key") var ownerPublicKey:String,
                                @field:SerializedName("max_fee") var max_fee:BigInteger,
                                @field:SerializedName("actor") var actor:String,
                                @field:SerializedName("tpid") var walletFioAddress:String): FIOResponse()
}