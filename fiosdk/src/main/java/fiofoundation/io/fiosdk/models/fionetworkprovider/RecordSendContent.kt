package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class RecordSendContent(
    @field:SerializedName("payer_public_address") var payerTokenPublicKey:String,
    @field:SerializedName("payee_public_address") var payeeTokenPublicKey:String,
    @field:SerializedName("amount") var amount:String,
    @field:SerializedName("token_code") var tokenCode: String,
    @field:SerializedName("obt_id") var obtId:String,
    @field:SerializedName("status") var status:String="sent_to_blockchain",
    @field:SerializedName("memo") var memo:String?=null,
    @field:SerializedName("hash") var hash:String?=null,
    @field:SerializedName("offline_url") var offlineUrl:String?=null)
{

    fun toJson(): String {
        val gson = GsonBuilder().serializeNulls().create()
        return gson.toJson(this,this.javaClass)
    }
}