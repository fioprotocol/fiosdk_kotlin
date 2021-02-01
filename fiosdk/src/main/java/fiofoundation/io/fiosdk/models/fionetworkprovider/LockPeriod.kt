package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

open class LockPeriod(
    @SerializedName("duration") var duration: BigInteger,
    @SerializedName("percent") var percent: Double?):Serializable

