package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import com.google.gson.*
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization
import java.io.Serializable
import java.lang.reflect.Type

interface IAction: Serializable
{
    var account: String
    var name: String
    var authorization: ArrayList<Authorization>
    var data: String
}

