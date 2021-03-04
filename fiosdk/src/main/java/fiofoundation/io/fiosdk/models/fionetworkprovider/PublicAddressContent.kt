package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PublicAddressContent(@field:SerializedName("public_address") var publicAddress:String = "",
                           @field:SerializedName("token_code") var tokenCode:String = "",
                           @field:SerializedName("chain_code") var chainCode:String = ""):Serializable
