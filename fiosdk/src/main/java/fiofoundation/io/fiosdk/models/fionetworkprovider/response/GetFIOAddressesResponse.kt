package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.FIOAddress

class GetFIOAddressesResponse: FIOResponse()
{
    @field:SerializedName("fio_addresses") var fioAddresses: ArrayList<FIOAddress> = arrayListOf()
    @field:SerializedName("more") var more: Int=0

}