package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class ChainPermission {
    @field:SerializedName("perm_name") val permissionName: String = ""
    @field:SerializedName("parent") val parent: String = ""
    @field:SerializedName("required_auth") val requiredAuth: RequiredAuth

    class RequiredAuth
    {
        @field:SerializedName("keys") val keys: ArrayList<Key> = arrayListOf()
    }

    class Key
    {
        @field:SerializedName("key") val key: String = ""
    }

    init {
        requiredAuth = RequiredAuth()
    }

    fun toJson(): String {
        val gson = GsonBuilder().serializeNulls().create()
        return gson.toJson(this,this.javaClass)
    }
}