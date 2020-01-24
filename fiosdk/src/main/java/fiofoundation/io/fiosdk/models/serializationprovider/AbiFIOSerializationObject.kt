package fiofoundation.io.fiosdk.models.serializationprovider

class AbiFIOSerializationObject (val contract: String?, val name: String, val type: String?, val abi: String)
{
    //constructor(contract: String?, name: String, type: String?, abi: String, compress:Boolean):this(contract,name,type,abi)

    var hex: String = ""
    var json: String = ""
    var base64 = ""

}