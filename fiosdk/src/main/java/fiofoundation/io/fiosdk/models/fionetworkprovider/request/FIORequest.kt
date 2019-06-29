package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.GsonBuilder

open class FIORequest {
    fun toJson(): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(this,this.javaClass)
    }
}