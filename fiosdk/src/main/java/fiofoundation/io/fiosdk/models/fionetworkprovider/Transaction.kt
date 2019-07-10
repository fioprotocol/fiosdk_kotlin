package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigInteger

open class Transaction(
    var expiration: String,
    @SerializedName("ref_block_num") var refBlockNum: BigInteger?,
    @SerializedName("ref_block_prefix") var refBlockPrefix: BigInteger?,
    @SerializedName("max_net_usage_words") var maxNetUsageWords: BigInteger?,
    @SerializedName("max_cpu_usage_ms") var maxCpuUsageMs: BigInteger?,
    @SerializedName("delay_sec") var delaySec: BigInteger?,
    @SerializedName("context_free_actions") var contextFreeActions: ArrayList<Action>?,
    var actions: ArrayList<Action>,
    @SerializedName("transaction_extensions") var transactionExtensions: ArrayList<String>?):Serializable