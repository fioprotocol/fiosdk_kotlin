package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.SerializedName

class GetSentFIORequestsRequest (
    @field:SerializedName("fio_public_key") var requesteeFioPublicKey: String): FIORequest()