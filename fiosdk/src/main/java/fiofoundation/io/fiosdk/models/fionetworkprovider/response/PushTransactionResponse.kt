package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName

//TODO: Modify to match FIO Response - processed property
class PushTransactionResponse(@SerializedName("transaction_id") val transactionId: String,
                              @SerializedName("processed") val processed: Map<*, *>?): FIOResponse()