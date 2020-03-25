package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.enums.FioDomainVisiblity
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordObtDataContent
import fiofoundation.io.androidfioserializationprovider.*
import fiofoundation.io.fiosdk.implementations.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.RegisterFIOAddressAction
import fiofoundation.io.fiosdk.toFIO
import fiofoundation.io.fiosdk.toSUF
import fiofoundation.io.fiosdk.utilities.SUFUtils
import fiofoundation.io.fiosdk.utilities.Utils
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.lang.AssertionError
import java.lang.Exception
import java.math.BigInteger

@RunWith(AndroidJUnit4::class)
class EncryptDecryptTests
{

    private val alicePrivateKey = "5J35xdLtcPvDASxpyWhNrR2MfjSZ1xjViH5cvv15VVjqyNhiPfa"
    private val alicePublicKey = "FIO6NxZ7FLjjJuHGByJtNJQ1uN1P5X9JJnUmFW3q6Q7LE7YJD4GZs"
    private val bobPrivateKey = "5J37cXw5xRJgE869B5LxC3FQ8ZJECiYnsjuontcHz5cJsz5jhb7"
    private val bobPublicKey = "FIO4zUFC29aq8uA4CnfNSyRZCnBPya2uQk42jwevc3UZ2jCRtepVZ"

    private val nonPartyPrivateKey = "5HujRtqceTPo4awwHAEdHRTWdMTgA6s39dJjwWcjhNdSjVWUqMk"
    private val nonPartyPublicKey = "FIO5mh1UqE5v9TKdYm2Ro6JXCXpSxj1Sm4vKUeydaLd7Cu5aqiSSp"

    @Test
    @ExperimentalUnsignedTypes
    fun testEncryptDecryptRequestFunds()
    {

        val payeeTokenPublicAddress = bobPublicKey
        val amount = 1.57
        val chainCode = "FIO"
        val tokenCode = "FIO"
        val memo = "testing encryption does it work?"
        val hash = ""
        val offlineUrl = ""

        println("Encrypt Request Funds")
        try
        {

            var fundsRequestContent = FundsRequestContent(payeeTokenPublicAddress = payeeTokenPublicAddress, amount=amount.toString(), chainCode = chainCode, tokenCode = tokenCode, memo=memo, hash=hash, offlineUrl = offlineUrl)

            val serializer = AbiFIOSerializationProvider()
            val encryptedContent = fundsRequestContent.serialize(bobPrivateKey,alicePublicKey,serializer)

            println("*" + encryptedContent + "*")

            Assert.assertTrue(
                "Couldn't encrypt",
                encryptedContent != null
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Encrypt Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Encrypt Failed: " + generalException.message)
        }
    }

    @Test
    fun testEncryptDecryptRequestFundsTypeScriptSDK()
    {
        val payeeTokenPublicAddress = bobPublicKey
        val amount = 1.57
        val chainCode = "FIO"
        val tokenCode = "FIO"
        val memo = "testing encryption does it work?"
        val hash = ""
        val offlineUrl = ""

        println("Decrypted Request Funds - TypeScript SDK")
        try
        {

            var encryptedRequestContent = "UKPHFo0xs3GwGAXMc44QqkDtj7dcbGHjU3cJmN1qiYWADvzyd9pen2WwKn0VZtk0ZTGFXpap7Id8nZxlCMK7TjkabO85XNbhausE4ZZzx3hm25bqV2GDRHpRomsRDGAzLbFEumsm+4UNBtnOqUK3Kuo91vKjlLIV3NoF83qOSbhL8QDqV2N/yJxSu4PsiDeqhhSypZx8McaubVoUueioWA=="

            val serializer = AbiFIOSerializationProvider()
            val decryptedContent = FundsRequestContent.deserialize(alicePrivateKey,bobPublicKey,serializer,encryptedRequestContent)

            println("*" + decryptedContent + "*")
            println("*" + decryptedContent?.payeeTokenPublicAddress + "*")

            Assert.assertTrue(
                "Couldn't decrypt",
                decryptedContent != null && decryptedContent.payeeTokenPublicAddress == payeeTokenPublicAddress
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Decrypt Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Decrypt Failed: " + generalException.message)
        }
    }

    @Test
    fun testEncryptDecryptRequestFundsiOSSDK()
    {
        val payeeTokenPublicAddress = bobPublicKey
        val amount = 1.57
        val chainCode = "FIO"
        val tokenCode = "FIO"
        val memo = "testing encryption does it work?"
        val hash = ""
        val offlineUrl = ""

        println("Decrypted Request Funds - iOS SDK")
        try
        {

            var encryptedRequestContent = "iNz623p8SjbFG3rNbxLeVzQhs7n4aB8UGHvkF08HhBXD3X9g6bVFJl93j/OqYdkiycxShF64uc9OHFc/qbOeeS8+WVL2YRpd9JaRqdTUE9XKFPZ6lETQ7MTbGT+qppMoJ0tWCP6mWL4M9V1xu6lE3lJkuRS4kXnwtOUJOcBDG7ddFyHaV1LnLY/jnOJHJhm8"

            val serializer = AbiFIOSerializationProvider()
            val decryptedContent = FundsRequestContent.deserialize(bobPrivateKey,alicePublicKey,serializer,encryptedRequestContent)

            println("*" + decryptedContent + "*")
            println("*" + decryptedContent?.payeeTokenPublicAddress + "*")

            Assert.assertTrue(
                "Couldn't decrypt",
                decryptedContent != null && decryptedContent.payeeTokenPublicAddress == payeeTokenPublicAddress
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Decrypt Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Decrypt Failed: " + generalException.message)
        }
    }

    @Test
    fun testEncryptDecryptRecordObtData()
    {
        val payerTokenPublicAddress = alicePublicKey
        val payeeTokenPublicAddress = bobPublicKey
        val amount = 1.57
        val chainCode = "FIO"
        val tokenCode = "FIO"
        val memo = "testing Kotlin SDK encryption does it work?"
        val hash = ""
        val offlineUrl = ""
        val obtId = ""
        val status = ""

        println("Encrypt Request Funds")
        try
        {

            val recordObtContent = RecordObtDataContent(payerTokenPublicAddress=payeeTokenPublicAddress,payeeTokenPublicAddress = payeeTokenPublicAddress, amount=amount.toString(), chainCode = chainCode, tokenCode = tokenCode, obtId = obtId ,status=status,memo=memo, hash=hash, offlineUrl = offlineUrl)

            val serializer = AbiFIOSerializationProvider()
            val encryptedContent = recordObtContent.serialize(bobPrivateKey,alicePublicKey,serializer)

            println("*" + encryptedContent + "*")

            Assert.assertTrue(
                "Couldn't encrypt",
                encryptedContent != null
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Encrypt Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Encrypt Failed: " + generalException.message)
        }
    }


    @Test
    fun testEncryptDecryptRecordObtDataTypeScriptSDK()
    {
        val payeeTokenPublicAddress = bobPublicKey
        val amount = 1.57
        val chainCode = "FIO"
        val tokenCode = "FIO"
        val memo = "testing encryption does it work?"
        val hash = ""
        val offlineUrl = ""

        println("Decrypted RecordObtData - TypeScript SDK")
        try
        {

            var encryptedRequestContent = "iAHI/QVUuH1RNh1aSb8iMd6f0KQWIO6QtkJs1krSeXaKLmaYKBQnItzjGsDdQr2dSP9T+gYZDAX94rwp478tCyh6AoHc/jFe0wEUKsZhQSORb5n49cSph6oDvsWLzwoN2fNMGONUnCejTY9vxOheooWJhOaeIRMl8Fyqt49ltqTLsnPiJDfBFGAPe2h2HadXGbO0jawIJnboRyxw3UdZ45bKAmdyRjcnu8HtnxfMTVyirclWaFjomPDrR4eVHYqHcg/gxANmHJ/lf1xYHoasw9LCFmaoOveIjZVtSY4Fxb37gUQJUUPAwaXFnpz0ZAgk"

            val serializer = AbiFIOSerializationProvider()
            val decryptedContent = RecordObtDataContent.deserialize(alicePrivateKey,bobPublicKey,serializer,encryptedRequestContent)

            println("*" + decryptedContent + "*")
            println("*" + decryptedContent?.payeeTokenPublicAddress + "*")

            Assert.assertTrue(
                "Couldn't decrypt",
                decryptedContent != null && decryptedContent.payeeTokenPublicAddress == payeeTokenPublicAddress
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Decrypt Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Decrypt Failed: " + generalException.message)
        }
    }

    @Test
    fun testEncryptDecryptRecordObtDataiOSSDK()
    {
        val payeeTokenPublicAddress = bobPublicKey
        val amount = 1.57
        val chainCode = "FIO"
        val tokenCode = "FIO"
        val memo = "testing encryption does it work?"
        val hash = ""
        val offlineUrl = ""

        println("Decrypted RecordObtData - iOS SDK")
        try
        {

            var encryptedRequestContent = "XJqqkHspW0zp+dHKj9TZMn5mZzdMQrdIAXNOlKPekeEpbjyeh92hO+lB9gA6wnNuq8YNLcGA1s0NPGzb+DlHzXT2tCulgk5fiQy6+8AbThPzB0N6xICmVV3Ontib8FVlTrVrqg053PK9JeHUsg0Sb+vG/dz9+ovcSDHaByxybRNhZOVBe8jlg91eakaU1H8XKDxYOtI3+jYESK02g2Rw5Ya9ec+/PnEBQ6DjkHruKDorEF1D+nDT/0CK46VsfdYzYK8IV0T9Nal4H6Bf4wrMlQ=="

            val serializer = AbiFIOSerializationProvider()
            val decryptedContent = RecordObtDataContent.deserialize(bobPrivateKey,alicePublicKey,serializer,encryptedRequestContent)

            println("*" + decryptedContent + "*")
            println("*" + decryptedContent?.payeeTokenPublicAddress + "*")

            Assert.assertTrue(
                "Couldn't decrypt",
                decryptedContent != null && decryptedContent.payeeTokenPublicAddress == payeeTokenPublicAddress
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Decrypt Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Decrypt Failed: " + generalException.message)
        }
    }
}

