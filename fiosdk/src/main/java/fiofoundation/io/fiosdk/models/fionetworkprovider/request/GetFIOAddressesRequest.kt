package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.SerializedName

class GetFIOAddressesRequest (
    @field:SerializedName("fio_public_key") var fioPublicKey: String,
    @field:SerializedName("limit") var limit:Int?=null,
    @field:SerializedName("offset") var offset:Int?=null): FIORequest()