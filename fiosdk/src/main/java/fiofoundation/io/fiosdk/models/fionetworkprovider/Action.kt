package fiofoundation.io.fiosdk.models.fionetworkprovider


open class Action(account: String,name: String,
             authorization: ArrayList<Authorization>,data: String): IAction
{
    override var account = account
    override var name = name
    override var authorization = authorization
    override var data = data
}