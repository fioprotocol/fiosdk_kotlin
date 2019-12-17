package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import java.lang.Exception
import java.math.BigInteger

class ObtDataRecord
{
    @field:SerializedName("fio_request_id") var fioRequestId: BigInteger = BigInteger.ZERO
    @field:SerializedName("payer_fio_address") var payerFioAddress:String = ""
    @field:SerializedName("payee_fio_address") var payeeFioAddress:String = ""
    @field:SerializedName("payer_fio_public_key") var payerFioPublicKey:String = ""
    @field:SerializedName("payee_fio_public_key") var payeeFioPublicKey:String = ""
    @field:SerializedName("content") private var content:String = ""
    @field:SerializedName("status") var status:String=""
    @field:SerializedName("time_stamp") var timeStamp:String = ""

    var obtDataContent : RecordObtDataContent? = null

    fun deserializeObtDataContent(sharedSecretKey: ByteArray, serializationProvider: ISerializationProvider):RecordObtDataContent?
    {
        try {
            val decryptedMessage = CryptoUtils.decryptSharedMessage(this.content,sharedSecretKey)
            val deserializedMessage = serializationProvider.deserializeRecordObtDataContent(decryptedMessage)

            this.obtDataContent = Gson().fromJson(deserializedMessage, RecordObtDataContent::class.java)

            return this.obtDataContent
        }
        catch(e: Exception)
        {
            this.obtDataContent = null
            return this.obtDataContent
        }

    }

    fun toJson(): String {
        val gson = GsonBuilder().create()
        return gson.toJson(this,this.javaClass)
    }
}