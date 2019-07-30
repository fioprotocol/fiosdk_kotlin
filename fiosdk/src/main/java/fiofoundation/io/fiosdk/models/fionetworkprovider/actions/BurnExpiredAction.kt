package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization

class BurnExpiredAction(actorPublicKey: String) : IAction
{
    override var account = "fio.system"
    override var name = "burnexpired"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

    init {
        val auth = Authorization(actorPublicKey, "active")
        this.authorization.add(auth)
    }

}