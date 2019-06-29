package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.GsonBuilder

open class FIOResponse {
    fun toJson(): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(this,this.javaClass)
    }
}