package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.SerializedName

class GetFIODomainsRequest (
    @field:SerializedName("fio_public_key") var fioioPublicKey: String,
    @field:SerializedName("limit") var limit:Int?=null,
    @field:SerializedName("offset") var offset:Int?=null): FIORequest()