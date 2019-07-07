package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.SerializedName

class PushTransactionRequest(
    var signatures: List<String>,
    var compression: Int,
    @field:SerializedName("packed_context_free_data") var packagedContextFreeData: String?,
    @field:SerializedName("packed_trx") var packTrx: String)