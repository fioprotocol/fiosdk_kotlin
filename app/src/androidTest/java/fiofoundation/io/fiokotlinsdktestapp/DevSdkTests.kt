package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.enums.FioDomainVisiblity
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.implementations.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints
import org.bitcoinj.crypto.MnemonicCode
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.AssertionError
import java.lang.Exception
import java.math.BigInteger
import java.security.SecureRandom

@RunWith(AndroidJUnit4::class)
class DevSdkTests
{
    private val baseUrl = "http://dev3.fio.dev:8889/v1/"
    private val baseMockUrl = "http://mock.dapix.io/mockd/DEV3/"

    private var alicePrivateKey = ""
    private var alicePublicKey = ""
    private var bobPrivateKey = ""
    private var bobPublicKey = ""

    private var aliceFioAddress = ""
    private var bobFioAddress = ""

    private var fioTestDomain = "brd"
    private var defaultFee = BigInteger("300000000000")

    private val alicePublicTokenAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6"
    private val alicePublicTokenCode = "BTC"
    private val bobPublicTokenAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"
    private val bobPublicTokenCode = "BTC"
    private var otherBlockChainId = "123456789"

    private var aliceFioSdk:FIOSDK? = null
    private var bobFioSdk:FIOSDK? = null

    private var logTag = "FIOSDK-TEST"

    @Test
    @ExperimentalUnsignedTypes
    fun testGenericActions()
    {
        this.setupTestVariables()

        println("testGenericActions: Begin Test for Generic Actions")

        val newFioDomain = this.generateTestingFioDomain()
        val newFioAddress = this.generateTestingFioAddress(newFioDomain)

        println("testGenericActions: Test getFioBalance - Alice")
        try
        {
            val fioBalance = this.aliceFioSdk!!.getFioBalance().balance

            Assert.assertTrue(
                "Balance not Available for Alice.",
                fioBalance != null && fioBalance >= BigInteger.ZERO
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Alice Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test registerFioDomain")
        try
        {
            val response = this.aliceFioSdk!!.registerFioDomain(newFioDomain, defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't register $newFioDomain for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register Fio Domain for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register Fio Domain for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test setFioDomainVisibility to True")
        try
        {
            val response = this.aliceFioSdk!!.setFioDomainVisibility(newFioDomain,
                FioDomainVisiblity.PUBLIC,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Visibility NOT set for $newFioDomain",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Setting Fio Domain Visibility for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Setting Fio Domain Visibility for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test registerFioAddress")
        try
        {
            val response = this.aliceFioSdk!!.registerFioAddress(newFioAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't Register FioAddress $newFioAddress for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test renewFioAddress")
        try
        {
            val response = this.aliceFioSdk!!.renewFioAddress(newFioAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't Renew FioAddress $newFioAddress for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Renew FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Renew FioAddress for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test addPublicAddress")
        try
        {
            val response = this.aliceFioSdk!!.addPublicAddress(newFioAddress,this.alicePublicTokenCode,
                this.alicePublicTokenAddress,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't Add Public Address for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Add Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Add Public Address for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test isFioAddressAvailable True")
        try
        {
            val testAddress = this.generateTestingFioAddress()
            val response = this.aliceFioSdk!!.isAvailable(testAddress)

            Assert.assertTrue(
                "FioAddress, $testAddress, is NOT Available",
                response != null && response.isAvailable
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test isFioAddressAvailable False")
        try
        {
            val response = this.aliceFioSdk!!.isAvailable(this.aliceFioAddress)

            Assert.assertTrue(
                "FioAddress, $aliceFioAddress, IS Available (not supposed to be)",
                response != null && !response.isAvailable
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("isFioAddressAvailable for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test getFioNames")
        try
        {
            val response = this.aliceFioSdk!!.getFioNames()

            Assert.assertTrue(
                "Couldn't Get FioNames for Alice",
                response.fioAddresses!!.isNotEmpty()
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Get FioNames for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Get FioNames for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test getFee")
        try
        {
            val response = this.aliceFioSdk!!.getFee(FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress)

            Assert.assertTrue(
                "Couldn't Get Fee for " + FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress.endpoint,
                response.fee >= 0
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Get Fee Call Failed for Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Get Fee Call Failed for Alice: " + generalException.message)
        }

        println("testGenericActions: End Test for Generic Actions")
    }


    //Helper Methods
    private fun setupTestVariables()
    {
        this.generatePrivateAndPublicKeys()

        this.aliceFioSdk = createSdkInstance(this.alicePrivateKey,this.alicePublicKey)
        this.bobFioSdk = createSdkInstance(this.bobPrivateKey,this.bobPublicKey)

        val initialFioAddressForAlice = this.generateTestingFioAddress()
        this.registerFioNameForUser(this.aliceFioSdk!!,initialFioAddressForAlice)

        val initialFioAddressForBob = this.generateTestingFioAddress()
        this.registerFioNameForUser(this.bobFioSdk!!,initialFioAddressForBob)

        this.aliceFioAddress = initialFioAddressForAlice
        this.bobFioAddress = initialFioAddressForBob

        this.requestFaucetFunds("25")
        this.requestFaucetFunds("25")

        Log.i(this.logTag, "Wait for balance to really be available.")
        Thread.sleep(60000)
    }

    fun registerFioNameForUser(fioSdk:FIOSDK,fioAddress:String) {

        Log.i(this.logTag,"Start registerFioNameForUser")
        Log.i(this.logTag,"Register " + fioAddress)

        var response = fioSdk.registerFioNameOnBehalfOfUser(fioAddress)

        Log.i(this.logTag,"RegisterFioNameForUser: " + response.status)

        Assert.assertTrue(response.status == "OK")

        Log.i(this.logTag,"Finish registerFioNameForUser")
    }

    private fun generateTestingFioDomain():String
    {
        val now = System.currentTimeMillis().toString()

        return "testing-domain-$now"
    }

    private fun generateTestingFioAddress(customDomain:String = fioTestDomain):String
    {
        val now = System.currentTimeMillis().toString()

        return "testing$now:$customDomain"
    }

    private fun createSdkInstance(privateKey: String, publicKey: String):FIOSDK
    {
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(privateKey)

        val serializer = AbiFIOSerializationProvider()

        val fioSdk = FIOSDK.getInstance(privateKey,publicKey, serializer, this.baseUrl)

        fioSdk.mockServerBaseUrl = this.baseMockUrl

        return fioSdk
    }

    private fun generatePrivateAndPublicKeys() {

        var mn = getRandomSeedWords().joinToString(" ")

        alicePrivateKey = FIOSDK.createPrivateKey(mn)
        alicePublicKey = FIOSDK.derivedPublicKey(alicePrivateKey)

        mn = getRandomSeedWords().joinToString(" ")

        bobPrivateKey = FIOSDK.createPrivateKey(mn)
        bobPublicKey = FIOSDK.derivedPublicKey(bobPrivateKey)
    }

    private fun getRandomSeedWords(): List<String> {
        val seedWords: List<String>

        val mnemonicCode = MnemonicCode()

        seedWords = mnemonicCode.toMnemonic(SecureRandom().generateSeed(16))

        return seedWords
    }

    private fun requestFaucetFunds(requestAmount:String="1"): Boolean
    {
        try
        {
            Log.i(this.logTag, "Start requestFaucetFunds")

            var response = this.aliceFioSdk!!.requestNewFunds("faucet:fio",
                this.aliceFioAddress,this.alicePublicKey,requestAmount,"FIO",
                this.defaultFee,"")

            var actionTraceResponse = response.getActionTraceResponse()
            if (actionTraceResponse != null && actionTraceResponse.status == "requested") {
                Log.i(this.logTag,
                    "New Funds Requested by Alice: " + (actionTraceResponse.status == "requested").toString()
                )

                var now = System.currentTimeMillis()

                var check_for_10_minutes = now + (1000 * 60 * 10)

                do {
                    if(now.rem(10000) == 0L)
                    {
                        val balance = this.aliceFioSdk!!.getFioBalance().balance
                        if(balance> BigInteger.ZERO)
                            break

                        Log.i(this.logTag,"Waiting on balance...")
                    }

                    now = System.currentTimeMillis()

                }while(now<check_for_10_minutes)

                if(now>check_for_10_minutes)
                {
                    Log.i(this.logTag, "New Funds Requested by Alice: failed")
                    throw FIOError("New Funds Requested by Alice: failed")
                }

                Log.i(this.logTag, "Finish requestFaucetFunds")

                return true
            }
            else
                Log.i(this.logTag, "New Funds Requested by Alice: failed")

            response = this.bobFioSdk!!.requestNewFunds("faucet:fio",
                this.bobFioAddress,this.bobPublicKey,requestAmount,"FIO",
                this.defaultFee,"")

            actionTraceResponse = response.getActionTraceResponse()
            if (actionTraceResponse != null && actionTraceResponse.status == "requested") {
                Log.i(this.logTag,
                    "New Funds Requested by Alice: " + (actionTraceResponse.status == "requested").toString()
                )

                var now = System.currentTimeMillis()

                var check_for_10_minutes = now + (1000 * 60 * 10)

                do {
                    if(now.rem(10000) == 0L)
                    {
                        val balance = this.aliceFioSdk!!.getFioBalance().balance
                        if(balance> BigInteger.ZERO)
                            break

                        Log.i(this.logTag,"Waiting on balance...")
                    }

                    now = System.currentTimeMillis()

                }while(now<check_for_10_minutes)

                if(now>check_for_10_minutes)
                {
                    Log.i(this.logTag, "New Funds Requested by Alice: failed")
                    throw FIOError("New Funds Requested by Alice: failed")
                }

                Log.i(this.logTag, "Finish requestFaucetFunds")

                return true
            }
            else
                Log.i(this.logTag, "New Funds Requested by Bob: failed")

            Log.i(this.logTag, "Finish requestFaucetFunds")

            return false
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("New Funds Request Failed: " + e.toJson())
        }
        catch(generalException: Exception)
        {
            throw AssertionError("New Funds Request Failed: " + generalException.message)
        }
    }
}