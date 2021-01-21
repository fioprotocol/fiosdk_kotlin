package fiofoundation.io.fiosdk.models

import com.google.gson.annotations.SerializedName

class LockPeriod(
    @field:SerializedName("duration") var duration: Int,
    @field:SerializedName("percent") var percent: Double)