package fiofoundation.io.fiosdk.models

import fiofoundation.io.fiosdk.formatters.ByteFormatter
import fiofoundation.io.fiosdk.utilities.HashUtils
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.SecureRandom

import kotlin.experimental.and

class Cryptography(val key:String,val iv:String?)
{
    private val hexArray = "0123456789ABCDEF".toCharArray()

    private val secureRandom = SecureRandom()

    //for testing only
    var _iv:ByteArray? = null

    companion object Static {
        val Algorithm = "AES"

        fun createHmac(data: ByteArray, key: ByteArray): ByteArray {
            val keySpec = SecretKeySpec(key, "HmacSHA256")
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(keySpec)

            val hmac = mac.doFinal(data)
            return hmac
        }
    }

    @Throws(Exception::class)
    fun encrypt(plainText: String): ByteArray
    {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8).copyOf(16), "AES")

        _iv = generateIv()
        val ivSpec = IvParameterSpec(_iv)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        //val ciphertext = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))

        return cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
    }

    @Throws(Exception::class)
    fun decrypt(ecnryptedText: ByteArray): ByteArray
    {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8).copyOf(16), "AES")
        val ivSpec = IvParameterSpec(_iv)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        val plaintext = cipher.doFinal(ecnryptedText)
        return plaintext
    }


    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = (bytes[j] and 0xFF.toByte()).toInt()
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    fun generateIv(): ByteArray {
        val result = ByteArray(128 / 8)
        secureRandom.nextBytes(result)
        return result
    }
}