package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.enums.FioDomainVisiblity
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.GetFIONamesError
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordSendContent
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import fiofoundation.io.androidfioserializationprovider.*
import fiofoundation.io.fiosdk.implementations.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.interfaces.ISerializationProvider
import fiofoundation.io.fiosdk.interfaces.ISignatureProvider
import org.bitcoinj.crypto.MnemonicCode

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.lang.AssertionError
import java.lang.Exception
import java.math.BigInteger
import java.security.SecureRandom

@RunWith(AndroidJUnit4::class)
class TestNetSdkTests {

    private val baseUrl = "https://testnet.fioprotocol.io:443/v1/"

    private var alicePrivateKey = ""
    private var alicePublicKey = ""
    private var bobPrivateKey = ""
    private var bobPublicKey = ""

    private var aliceFioAddress = ""
    private var bobFioAddress = ""

    private var fioTestNetDomain = "fiotestnet"
    private var defaultFee = BigInteger("30000000000")

    private val testPublicTokenAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6"
    private val testPublicTokenCode = "BTC"

    private var aliceFioSdk = createSdkInstance(alicePrivateKey,alicePublicKey)
    private var bobFioSdk = createSdkInstance(bobPrivateKey,bobPublicKey)


    @Test
    @ExperimentalUnsignedTypes
    fun testGenericActions()
    {
        val newFioDomain = this.generateTestingFioDomain()
        val newFioAddress = this.generateTestingFioAddress(newFioDomain)

        println("Test getFioBalance - Alice")
        try
        {
            val fioBalance = this.aliceFioSdk.getFioBalance().balance

            assertTrue("Balance available for Alice.",fioBalance!=null && fioBalance>=BigInteger.ZERO)
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Alice Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Alice Failed: " + generalException.message)
        }

        println("Test registerFioDomain")
        try
        {
            val response = this.aliceFioSdk.registerFioDomain(newFioDomain, defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice registered $newFioDomain for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register Fio Domain for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register Fio Domain for Alice Failed: " + generalException.message)
        }

        println("Test setFioDomainVisibility to True")
        try
        {
            val response = this.aliceFioSdk.setFioDomainVisibility(newFioDomain,FioDomainVisiblity.PUBLIC,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Visibility set for $newFioDomain",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Setting Fio Domain Visibility for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Setting Fio Domain Visibility for Alice Failed: " + generalException.message)
        }

        println("Test registerFioAddress")
        try
        {
            val response = this.aliceFioSdk.registerFioAddress(newFioAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Register FioAddress $newFioAddress for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + generalException.message)
        }

        println("Test renewFioAddress")
        try
        {
            val response = this.aliceFioSdk.renewFioAddress(newFioAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Renew FioAddress $newFioAddress for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Renew FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Renew FioAddress for Alice Failed: " + generalException.message)
        }

        println("Test addPublicAddress")
        try
        {
            val response = this.aliceFioSdk.addPublicAddress(newFioAddress,this.testPublicTokenCode,
                testPublicTokenAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Add Public Address for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Add Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Add Public Address for Alice Failed: " + generalException.message)
        }

        println("Test isFioAddressAvailable True")
        try
        {
            val testAddress = this.generateTestingFioAddress()
            val response = this.aliceFioSdk.isAvailable(testAddress)

            assertTrue("FioAddress, $testAddress, is Available",response!=null && response.isAvailable)
        }
        catch (e: FIOError)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + generalException.message)
        }

        println("Test isFioAddressAvailable False")
        try
        {
            val response = this.aliceFioSdk.isAvailable(this.aliceFioAddress)

            assertTrue("FioAddress, $aliceFioAddress, is not Available",response!=null && !response.isAvailable)
        }
        catch (e: FIOError)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + generalException.message)
        }

        println("Test getFioNames")
        try
        {
            val response = this.aliceFioSdk.getFioNames()

            assertTrue("Get FioNames for Alice",response.fioAddresses!!.isNotEmpty())
        }
        catch (e: FIOError)
        {
            throw AssertionError("Get FioNames for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Get FioNames for Alice Failed: " + generalException.message)
        }

        println("Test getFee")
        try
        {
            val response = this.aliceFioSdk.getFee(FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress)

            assertTrue("Get Fee for " + FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress.endpoint,response.fee>=0)
        }
        catch (e: FIOError)
        {
            throw AssertionError("Get Fee Call Failed for Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Get Fee Call Failed for Alice: " + generalException.message)
        }

    }

    //Helper Methods
    private fun generateTestingFioDomain():String
    {
        val now = System.currentTimeMillis().toString()

        return "testing-domain-$now"
    }

    private fun generateTestingFioAddress(customDomain:String = fioTestNetDomain):String
    {
        val now = System.currentTimeMillis().toString()

        return "testing$now:$customDomain"
    }

    private fun createSdkInstance(privateKey: String, publicKey: String):FIOSDK
    {
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(privateKey)

        val serializer = AbiFIOSerializationProvider()

        return FIOSDK(privateKey,publicKey,serializer,signatureProvider,this.baseUrl)
    }

}