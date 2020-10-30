package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.FIODomain

class GetFIODomainsResponse: FIOResponse()
{
    @field:SerializedName("fio_domains") var fioDomains: ArrayList<FIODomain> = arrayListOf()
    @field:SerializedName("more") var more: Int=0

}