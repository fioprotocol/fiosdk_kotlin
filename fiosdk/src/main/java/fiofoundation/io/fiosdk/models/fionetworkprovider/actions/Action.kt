package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.GsonBuilder
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization


open class Action(account: String, name: String, data: String,
                  requestDataJson: String, actorPublicKey: String,
                  actorPermission:String="active"): IAction
{
    override var account = account
    override var name = name
    override var authorization = ArrayList<Authorization>()
    override var data = data


    init {
        val auth = Authorization(actorPublicKey, actorPermission)

        this.authorization.add(auth)

        this.data = requestDataJson
    }

    fun toJson(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this,this.javaClass)
    }
}