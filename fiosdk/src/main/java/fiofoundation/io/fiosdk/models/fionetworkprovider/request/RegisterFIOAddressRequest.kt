package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Action
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.Transaction
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class RegisterFIOAddressRequest(@Expose(serialize = false, deserialize = false) var fioAddress: String,
                                @Expose(serialize = false, deserialize = false) var ownerPublicKey: String,
                                @Expose(serialize = false, deserialize = false) var walletFioAddress: String,
                                @Expose(serialize = false, deserialize = false) var maxFee: Int,
                                @Expose(serialize = false, deserialize = false) var actorPublicKey: String): Transaction("", BigInteger.ZERO,
    BigInteger.ZERO, BigInteger.ZERO,
    BigInteger.ZERO, BigInteger.ZERO,
    ArrayList(), ArrayList(), ArrayList())
{
    init
    {
        val authorization = Authorization(actorPublicKey,"active")
        var requestData = FIOAddressRequestData(fioAddress,ownerPublicKey,maxFee,walletFioAddress,authorization.actor)

        val authorizationList = listOf(authorization)
        var action = Action("fio.system","regaddress", authorizationList,requestData.toJson())

        this.actions.add(action)

    }

    class FIOAddressRequestData(@field:SerializedName("fio_address") var fioAddress:String,
                                @field:SerializedName("owner_fio_public_key") var ownerPublicKey:String,
                                @field:SerializedName("max_fee") var max_fee:Int,
                                @field:SerializedName("tpid") var walletFioAddress:String,
                                @field:SerializedName("actor") var actor:String): FIOResponse()

}