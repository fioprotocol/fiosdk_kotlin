package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.SentFIORequestContent

class GetSentFIORequestsResponse: FIOResponse()
{
    @field:SerializedName("requests") var requests: ArrayList<SentFIORequestContent> = arrayListOf()
    @field:SerializedName("more") var more: Int=0

}