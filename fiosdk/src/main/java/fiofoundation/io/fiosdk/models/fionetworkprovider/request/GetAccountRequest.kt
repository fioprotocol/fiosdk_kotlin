package fiofoundation.io.fiosdk.models.fionetworkprovider.request

import com.google.gson.annotations.SerializedName

class GetAccountRequest (@field:SerializedName("account_name") var accountName: String): FIORequest()