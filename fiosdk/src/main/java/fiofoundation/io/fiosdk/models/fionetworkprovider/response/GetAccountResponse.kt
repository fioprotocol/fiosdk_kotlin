package fiofoundation.io.fiosdk.models.fionetworkprovider.response

import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.models.fionetworkprovider.ObtDataRecord

class GetAccountResponse: FIOResponse()
{
    @field:SerializedName("account_name") val accountName: String = ""
    @field:SerializedName("created") val dateCreated: String = ""

    @field:SerializedName("obt_data_records") val records: ArrayList<ObtDataRecord>
    @field:SerializedName("more") var more: Int=0

    init {
        records = arrayListOf()
    }
}