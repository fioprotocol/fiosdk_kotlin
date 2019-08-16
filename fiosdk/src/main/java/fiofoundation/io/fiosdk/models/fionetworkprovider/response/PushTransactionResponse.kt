package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import java.io.Serializable
import java.math.BigInteger

class PushTransactionResponse(@SerializedName("transaction_id") val transactionId: String,
                              @SerializedName("processed") val processed: Map<*, *>?): FIOResponse()
{
    fun getActionTraceResponse() : ActionTraceResponse?
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
                    val actionTraceResponse = actionTraceReceipt["response"].toString()

                    return Gson().fromJson(actionTraceResponse,ActionTraceResponse::class.java)
                }
            }

        }

        return null
    }

    class ActionTraceResponse: Serializable
    {
        @SerializedName("status")
        val status: String =""

        @SerializedName("expiration")
        val expiration: String =""

        @SerializedName("fee_collected")
        val feeCollected: BigInteger = BigInteger("0")

    }
}