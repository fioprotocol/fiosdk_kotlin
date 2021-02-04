package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.LockPeriod
import java.math.BigInteger

/*
{
    "lock_amount": 1000000000,
    "remaining_lock_amount": 1000000000,
    "time_stamp": 1611956409,
    "payouts_performed": 0,
    "can_vote": 0,
    "unlock_periods": [
        {
            "duration": 20,
            "percent": "100.00000000000000000"
        }
    ]
}
 */

class GetLocksResponse: FIOResponse() {

    @SerializedName("lock_amount")
    val lockAmount: BigInteger? = null
    @SerializedName("remaining_lock_amount")
    val remainingLockAmount: BigInteger? = null
    @SerializedName("time_stamp")
    val timeStamp: BigInteger? = null
    @SerializedName("payouts_performed")
    val payoutsPerformed: BigInteger? = null
    @SerializedName("can_vote")
    val canVote: Int? = null
    @SerializedName("unlock_periods")
    val transactions: List<LockPeriod>? = null

}