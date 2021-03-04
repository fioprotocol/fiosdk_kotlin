package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.SerializedName

class GetPublicAddressesRequest (
    @field:SerializedName("fio_address") var fioAaddress: String,
    @field:SerializedName("limit") var limit:Int?=null,
    @field:SerializedName("offset") var offset:Int?=null):FIORequest()