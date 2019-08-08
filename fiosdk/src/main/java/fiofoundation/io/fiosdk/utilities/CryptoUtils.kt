package fiofoundation.io.fiosdk.utilities

import fiofoundation.io.fiosdk.enums.AlgorithmEmployed
import fiofoundation.io.fiosdk.errors.Base58ManipulationError
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.formatters.FIOFormatterError
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.hexStringToByteArray
import fiofoundation.io.fiosdk.models.Cryptography
import fiofoundation.io.fiosdk.toHexString
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import org.bouncycastle.jce.ECNamedCurveTable
import java.lang.Exception

import java.math.BigInteger

object CryptoUtils
{
    @Throws(FIOError::class)
    fun generateSharedSecret(yourPrivateKey: String, othersFIOPublicKey: String): ByteArray
    {
        try
        {
            val publicKeyNoPrefix = othersFIOPublicKey.trimStart(FIOFormatter.PATTERN_STRING_FIO_PREFIX_EOS.toCharArray()::contains)

            var publicKeyBytes = FIOFormatter.decompressPublicKey(FIOFormatter.decodePublicKey(publicKeyNoPrefix, "FIO"),AlgorithmEmployed.SECP256K1)

            val privateKeyBytes = FIOFormatter.decodePrivateKey(yourPrivateKey, AlgorithmEmployed.SECP256K1)

            val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
            val domain = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
            val privateKey = ECPrivateKeyParameters(BigInteger(1, privateKeyBytes), domain)
            val publicKey = ECPublicKeyParameters(spec.curve.decodePoint(publicKeyBytes), domain)

            val agreement = ECDHBasicAgreement()

            agreement.init(privateKey)

            var secretBytes = agreement.calculateAgreement(publicKey).toByteArray()
            secretBytes = if(secretBytes.size>=33) secretBytes.copyOfRange(1,33) else secretBytes

            return HashUtils.sha512(secretBytes)
        }
        catch(formatError: FIOFormatterError)
        {
            throw FIOError(formatError.message!!,formatError)
        }
        catch(decodeError: Base58ManipulationError)
        {
            throw FIOError(decodeError.message!!,decodeError)
        }
        catch(e:Exception)
        {
            throw FIOError(e.message!!,e)
        }

    }

    @Throws(FIOError::class)
    fun encryptSharedMessage(messageAsHexString: String, sharedKey: ByteArray, iv: ByteArray?=null): String
    {
        try
        {
            val hashedSecretKey = HashUtils.sha512(sharedKey)

            val encryptionKey = hashedSecretKey.copyOf(32)
            val hmacKey = hashedSecretKey.copyOfRange(32,hashedSecretKey.size)
            val encryptor = Cryptography(encryptionKey,iv)
            val encryptedMessage = encryptor.encrypt(messageAsHexString.hexStringToByteArray().asUByteArray())
            val hmacContent = ByteArray(encryptor.iv!!.size + encryptedMessage.size)

            encryptor.iv!!.copyInto(hmacContent)
            encryptedMessage.copyInto(hmacContent,encryptor.iv!!.size)

            val hmacData = Cryptography.createHmac(hmacContent,hmacKey)

            val returnArray = ByteArray(hmacContent.size + hmacData.size)

            hmacContent.copyInto(returnArray)
            hmacData.copyInto(returnArray,hmacContent.size)

            return returnArray.toHexString()
        }
        catch (e:Exception)
        {
            throw FIOError(e.message!!,e)
        }

    }
}