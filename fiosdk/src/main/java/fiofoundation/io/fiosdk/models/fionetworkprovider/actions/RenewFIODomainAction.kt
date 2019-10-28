package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIOResponse
import java.math.BigInteger

class RenewFIODomainAction(fioDomain: String, maxFee: BigInteger, walletFioAddress: String,
                           actorPublicKey: String) :
    IAction
{
    override var account = "fio.address"
    override var name = "renewdomain"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            RenewFIODomainRequestData(
                fioDomain,
                maxFee,
                auth.actor,
                walletFioAddress
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class RenewFIODomainRequestData(@field:SerializedName("fio_domain") var fioDomain:String,
                               @field:SerializedName("max_fee") var max_fee:BigInteger,
                               @field:SerializedName("actor") var actor:String,
                               @field:SerializedName("tpid") var walletFioAddress:String): FIOResponse()
}