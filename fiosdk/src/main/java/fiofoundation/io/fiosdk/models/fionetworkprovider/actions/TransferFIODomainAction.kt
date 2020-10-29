package fiofoundation.io.fiosdk.models.fionetworkprovider.actions


import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import java.math.BigInteger

class TransferFIODomainAction(fioDomain: String,newOwnerFioPublicKey: String, maxFee: BigInteger,
                                 technologyPartnerId: String,
                                 actorPublicKey: String) : IAction
{
    override var account = "fio.address"
    override var name = "xferdomain"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            TransferFIODomainRequestData(
                fioDomain.toLowerCase(),
                newOwnerFioPublicKey,
                maxFee,
                auth.actor,
                technologyPartnerId
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class TransferFIODomainRequestData(
        @field:SerializedName("fio_domain") var fioDomain:String,
        @field:SerializedName("new_owner_fio_public_key") var newOwnerFioPublicKey:String,
        @field:SerializedName("max_fee") var max_fee:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var technologyPartnerId:String): FIORequestData()
}
