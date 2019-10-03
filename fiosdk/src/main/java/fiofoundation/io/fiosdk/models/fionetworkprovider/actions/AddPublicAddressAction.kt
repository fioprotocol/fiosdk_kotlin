package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class AddPublicAddressAction(fioAddress: String,
                             tokenCode: String,
                               tokenPublicAddress: String,
                             maxFee: BigInteger,
                               walletFioAddress: String,
                               actorPublicKey: String) :
    IAction
{
    override var account = "fio.system"
    override var name = "addaddress"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            FIOAddressRequestData(
                fioAddress,
                tokenCode,
                tokenPublicAddress,
                maxFee,
                auth.actor,
                walletFioAddress
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class FIOAddressRequestData(@field:SerializedName("fio_address") var fioAddress:String,
                                @field:SerializedName("token_code") var tokenCode:String,
                                @field:SerializedName("public_address") var tokenPublicAddress:String,
                                @field:SerializedName("max_fee") var maxFee:BigInteger,
                                @field:SerializedName("actor") var actor:String,
                                @field:SerializedName("tpid") var walletFioAddress:String): FIOResponse()
}