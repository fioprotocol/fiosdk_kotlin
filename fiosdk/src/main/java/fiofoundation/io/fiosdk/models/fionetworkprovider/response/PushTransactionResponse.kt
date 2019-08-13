package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

class PushTransactionResponse(@SerializedName("transaction_id") val transactionId: String,
                              @SerializedName("processed") val processed: Map<*, *>?): FIOResponse()
{
    fun getActionTraceResponse() : String
    {
        if(this.processed!!.containsKey("action_traces"))
        {
            val actionTracesData = this.processed["action_traces"] as ArrayList<*>

            if(actionTracesData.size>0)
            {
                var actionTraceData:LinkedTreeMap<String,Any> = actionTracesData[0] as LinkedTreeMap<String,Any>
                var actionTraceReceipt = actionTraceData["receipt"] as LinkedTreeMap<String,Any>

                if(actionTraceReceipt["response"]!=null)
                {
                    return actionTraceReceipt["response"].toString()
                }
            }

        }

        return ""
    }
}