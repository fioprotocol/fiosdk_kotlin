package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.PublicAddressContent

class GetPublicAddressesResponse: FIOResponse()
{
    @field:SerializedName("public_addresses") var publicAddresses: ArrayList<PublicAddressContent> = arrayListOf()
    @field:SerializedName("more") var more: Boolean=false

}