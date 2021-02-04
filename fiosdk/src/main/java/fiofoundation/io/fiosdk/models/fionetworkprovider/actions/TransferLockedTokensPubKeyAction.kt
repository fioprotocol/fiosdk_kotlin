package fiofoundation.io.fiosdk.models.fionetworkprovider.actions


import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.LockPeriod
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIORequestData
import java.math.BigInteger

class TransferLockedTokensPubKeyAction(payeePublicKey: String,
                                       canVote: Boolean,
                                       periods: List<LockPeriod>,
                                       amount: BigInteger,
                                       maxFee: BigInteger,
                                       technologyPartnerId: String,
                                       actorPublicKey: String) : IAction
{
    override var account = "fio.token"
    override var name = "trnsloctoks"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init
    {
        val auth = Authorization(actorPublicKey, "active")
        var requestData =
            TransferLockedTokensPubKeyRequestData(
                payeePublicKey,
                canVote,
                periods,
                amount,
                maxFee,
                auth.actor,
                technologyPartnerId
            )

        this.authorization.add(auth)
        this.data = requestData.toJson()
    }

    class TransferLockedTokensPubKeyRequestData(
        @field:SerializedName("payee_public_key") var payeePublicKey:String,
        @field:SerializedName("can_vote") var can_vote:Boolean,
        @field:SerializedName("periods") var periods:List<LockPeriod>,
        @field:SerializedName("amount") var amount:BigInteger,
        @field:SerializedName("max_fee") var max_fee:BigInteger,
        @field:SerializedName("actor") var actor:String,
        @field:SerializedName("tpid") var technologyPartnerId:String): FIORequestData()
}
