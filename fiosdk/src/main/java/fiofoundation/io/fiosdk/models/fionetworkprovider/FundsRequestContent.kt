package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class FundsRequestContent(
    @field:SerializedName("payee_public_address") var payeeTokenPublicAddress:String,
    @field:SerializedName("amount") var amount:String,
    @field:SerializedName("token_code") var tokenCode: String,
    @field:SerializedName("memo") var memo:String?=null,
    @field:SerializedName("hash") var hash:String?=null,
    @field:SerializedName("offline_url") var offlineUrl:String?=null)
{
    fun toJson(): String {
        val gson = GsonBuilder().serializeNulls() .create()
        return gson.toJson(this,this.javaClass)
    }
}