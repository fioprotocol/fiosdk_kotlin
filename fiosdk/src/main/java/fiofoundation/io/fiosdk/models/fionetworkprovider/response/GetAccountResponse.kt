package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.ChainPermission
import fiofoundation.io.fiosdk.models.fionetworkprovider.ObtDataRecord

class GetAccountResponse: FIOResponse()
{
    @field:SerializedName("account_name") val accountName: String = ""
    @field:SerializedName("created") val dateCreated: String = ""

    @field:SerializedName("permissions") val permissions: ArrayList<ChainPermission> = arrayListOf()

    fun getKeys(): ArrayList<String>
    {
        var rtn:ArrayList<String> = arrayListOf()

        this.permissions.forEach { cp ->
            cp.requiredAuth.keys.forEach{k->
                rtn.add(k.key)
            }
        }

        return rtn
    }

    fun getKeys(permissionName:String): ArrayList<String>
    {
        var rtn:ArrayList<String> = arrayListOf()

        val filteredPermissions = this.permissions.filter { it.permissionName == permissionName }
        filteredPermissions.forEach { fp ->
            fp.requiredAuth.keys.forEach{k->
                rtn.add(k.key)
            }
        }

        return rtn
    }
}