package fiofoundation.io.fiosdk.models.fionetworkprovider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import fiofoundation.io.fiosdk.hexStringToByteArray
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.toHexString
import fiofoundation.io.fiosdk.utilities.CompressionUtils
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import org.bouncycastle.util.encoders.Base64
import java.lang.Exception

class FundsRequestContent(
    @field:SerializedName("payee_public_address") var payeeTokenPublicAddress:String,
    @field:SerializedName("amount") var amount:String,
    @field:SerializedName("token_code") var tokenCode: String,
    @field:SerializedName("memo") var memo:String?=null,
    @field:SerializedName("hash") var hash:String?=null,
    @field:SerializedName("offline_url") var offlineUrl:String?=null)
{

    fun serialize(privateKey: String, publicKey: String, serializationProvider: ISerializationProvider): String
    {
        val serializedNewFundsContent = serializationProvider.serializeContent(this.toJson(),"new_funds_content")

        val secretKey = CryptoUtils.generateSharedSecret(privateKey,publicKey)

        val compressedContent = CompressionUtils.compress(serializedNewFundsContent)

        return CryptoUtils.encryptSharedMessage(compressedContent,secretKey,null)
    }

    fun toJson(): String {
        val gson = GsonBuilder().serializeNulls().create()
        return gson.toJson(this,this.javaClass)
    }

    companion object {
        fun deserialize(privateKey: String, publicKey: String, serializationProvider: ISerializationProvider,serializedFundsRequestContent:String):FundsRequestContent?
        {
            try {
                val secretKey = CryptoUtils.generateSharedSecret(privateKey,publicKey)

                val decryptedMessage = CryptoUtils.decryptSharedMessage(serializedFundsRequestContent,secretKey)

                val decompressedMessage = CompressionUtils.decompress(decryptedMessage)

                val deserializedMessage = serializationProvider.deserializeContent(decompressedMessage,"new_funds_content")

                return Gson().fromJson(deserializedMessage, FundsRequestContent::class.java)
            }
            catch(e: Exception)
            {
                return null
            }
        }
    }
}