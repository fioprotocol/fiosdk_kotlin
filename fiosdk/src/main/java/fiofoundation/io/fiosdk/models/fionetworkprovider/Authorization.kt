package fiofoundation.io.fiosdk.models.fionetworkprovider

import fiofoundation.io.fiosdk.utilities.Utils

class Authorization(actorPublicAddress: String, var permission: String)
{
    val actor: String

    init
    {
        actor = Utils.generateActor(actorPublicAddress)
    }
}