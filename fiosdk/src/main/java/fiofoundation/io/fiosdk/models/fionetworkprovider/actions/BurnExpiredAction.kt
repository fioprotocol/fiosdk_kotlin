package fiofoundation.io.fiosdk.models.fionetworkprovider.actions

import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization

class BurnExpiredAction : IAction
{
    override var account = "fio.system"
    override var name = "burnexpired"
    override var authorization = ArrayList<Authorization>()
    override var data = ""

}