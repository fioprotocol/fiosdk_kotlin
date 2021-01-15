package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.Action
import java.io.Serializable
import java.math.BigInteger

open class Transaction(
    @SerializedName("expiration") var expiration: String,
    @SerializedName("ref_block_num") var refBlockNum: BigInteger?,
    @SerializedName("ref_block_prefix") var refBlockPrefix: BigInteger?,
    @SerializedName("max_net_usage_words") var maxNetUsageWords: BigInteger?,
    @SerializedName("max_cpu_usage_ms") var maxCpuUsageMs: BigInteger?,
    @SerializedName("delay_sec") var delaySec: BigInteger?,
    @SerializedName("context_free_actions") var contextFreeActions: ArrayList<Action>?,
    @SerializedName("actions") var actions: ArrayList<Action>,
    @SerializedName("transaction_extensions") var transactionExtensions: ArrayList<String>?):Serializable


public class TxItem( @SerializedName("status") var status: String,
              @SerializedName("cpu_usage_us") var cpuUsageUs: BigInteger?,
              @SerializedName("net_usage_words") var netUsageWords: BigInteger?,
              @SerializedName("trx") var trx: Trx?):Serializable

public class Trx (@SerializedName("id") var id: String,
           @SerializedName("signatures") var signatures: ArrayList<String>?,
           @SerializedName("compression") var compression: String,
           @SerializedName("packed_context_free_data") var packedContextFreeData: String,
           @SerializedName("context_free_data") var contextFreeData: ArrayList<Action>?,
           @SerializedName("packed_trx") var packedTrx: String,
           @SerializedName("transaction") var transaction: Transaction):Serializable

