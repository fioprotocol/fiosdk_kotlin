package fiofoundation.io.fiosdk.models.fionetworkprovider

import fiofoundation.io.fiosdk.utilities.Utils

class Authorization(actorPublicAddress: String, var permission: String)
{
    init
    {
        val actor: String = Utils.generateActor(actorPublicAddress)
    }
}