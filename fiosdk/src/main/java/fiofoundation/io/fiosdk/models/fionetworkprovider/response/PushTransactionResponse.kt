package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName


class PushTransactionResponse(@SerializedName("transaction_id") val transactionId: String,
                              @SerializedName("processed") val processed: Map<*, *>?): FIOResponse()
{
    //TODO: Modify to match FIO Response - processed property
}