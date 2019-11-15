package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.GsonBuilder
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization


open class Action(account: String, name: String, authorization: ArrayList<Authorization>,
                  data: String): IAction
{
    override var account = account
    override var name = name
    override var authorization = authorization
    override var data = data

    fun toJson(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this,this.javaClass)
    }
}