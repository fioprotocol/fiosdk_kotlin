package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

open class FIORequestContent {

    @field:SerializedName("fio_request_id") var fioRequestId:String = ""
    @field:SerializedName("payer_fio_address") var payerFioAddress:String = ""
    @field:SerializedName("payee_fio_address") var payeeFioAddress:String = ""
    @field:SerializedName("payer_fio_public_key") var payerFioPublicKey:String = ""
    @field:SerializedName("payee_fio_public_key") var payeeFioPublicKey:String = ""
    @field:SerializedName("content") var content:String = ""
    @field:SerializedName("time_stamp") var time_stamp:Int = 0


    fun toJson(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this,this.javaClass)
    }
}