package fiofoundation.io.fiosdk.models

import com.google.gson.GsonBuilder
import java.io.Serializable

class MultiLevelAddress: Serializable
{
    var address:String = ""
    var dt:String = ""
    var memo:String = ""
    var memo_id:String = ""
    var memo_text:String = ""
    var memo_hash:String = ""
    var memo_return:String = ""
    var payment_id:String = ""

    fun toJson(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this,this.javaClass)
    }
}