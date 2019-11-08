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
import org.bitcoinj.crypto.MnemonicCode

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.lang.AssertionError
import java.lang.Exception
import java.math.BigInteger
import java.security.SecureRandom

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedSdkTests {

    private var alicePrivateKey = "5JbcPK6qTpYxMXtfpGXagYbo3KFE3qqxv2tLXLMPR8dTWWeYCp9"
    private var alicePublicKey = "FIO7c8SVyAyu6cACCaUjmPFEUyW9p2owWHeqq2WSEZ18FFTgErE1K"
    private var bobPrivateKey = "5JAExdhmQw8F1siD7uzLrhmzfjW97hubw7ZNxjAiAu6p7Xq9wqG"
    private var bobPublicKey = "FIO8LKt4DBzXKzDGjFcZo5x82Nv5ahmbZ8AUNXBv2vMfm6smiHst3"

    private var aliceFioAddress = ""
    private var bobFioAddress = ""

    private var testFioDomain = "brd"
    private var testRenewDomain = ""

    private var walletFioAddress = "rewards:wallet"
    private var testMaxFee = BigInteger("4000000000000000000")

    private var payeeBTCAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"  //bob
    private var payerBTCAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6" //alice
    private var publicBTCAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6" //alice
    private var otherBlockChainId = "123456789"
    private var endPointNameForGetFee = FIOApiEndPoints.EndPointsWithFees.RegisterFioAddress
    private var chainId = "cf057bbfb72640471fd910bcb67639c22df9f92470936cddc1ade0e2f2e7dc4f"

    private var newFundsRequestId = BigInteger.ZERO

    private var sharedSecretKey:ByteArray? = null

    private var baseUrl = "http://dev3.fio.dev:8889/v1/"
    private var baseMockUrl = "http://mock.dapix.io/mockd/DEV3/"
    private var fioSdk:FIOSDK? = null

    private var logTag = "FIOSDK-TEST"

    private val skipSetFioDomainVisibility = true

    @Test
    fun setupTestVariable()
    {
        this.logTag = this.logTag + ": " + (0..100000).random().toString()

        Log.i(this.logTag,"Start setupTestVariable")

        this.generatePrivateAndPublicKeys()

        aliceFioAddress = System.currentTimeMillis().toString() + (0..100000).random().toString() + "a:" + this.testFioDomain//":brd"
        bobFioAddress =  System.currentTimeMillis().toString() + (0..100000).random().toString() + "b:" + this.testFioDomain//":brd"

        Log.i(this.logTag,"Alice FIO Address: " + this.aliceFioAddress)
        Log.i(this.logTag,"Bob FIO Address: " + this.bobFioAddress)

        assertTrue(this.aliceFioAddress.isNotEmpty())
        assertTrue(this.bobFioAddress.isNotEmpty())

        Log.i(this.logTag,"Finish setupTestVariable")
    }

    @Test
    fun initializeFIOSDK()
    {
        this.setupTestVariable()

        Log.i(this.logTag,"Start initializeFIOSDK")

        this.switchUser("alice")

        assertTrue("FIOSDK Initialize",this.fioSdk!=null)

        Log.i(this.logTag,"Finish initializeFIOSDK")
    }

    @Test
    fun registerFioNameForUser() {

        this.initializeFIOSDK()

        Log.i(this.logTag,"Start registerFioNameForUser")
        Log.i(this.logTag,"Register " + this.aliceFioAddress)

        var response = this.fioSdk!!.registerFioNameOnBehalfOfUser(this.aliceFioAddress)

        Log.i(this.logTag,"Register FioName For Alice: " + response.status)

        assertTrue(response.status == "OK")

        this.switchUser("bob")

        Log.i(this.logTag,"Register " + this.bobFioAddress)
        response = this.fioSdk!!.registerFioNameOnBehalfOfUser(this.bobFioAddress)

        Log.i(this.logTag,"Register FioName For Bob: " + response.status)

        assertTrue(response.status == "OK")

        this.switchUser("alice")

        Log.i(this.logTag,"Finish registerFioNameForUser")
    }

    @Test
    fun registerFioDomain() {

        try {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start registerFioDomain")

            var funds_available  = this.requestFaucetFunds("25.0")

            if(funds_available)
            {
                val fioDomainToRegister = testFioDomain + (0..10000).random().toString()

                val response = this.fioSdk!!.registerFioDomain(
                    fioDomainToRegister, this.alicePublicKey,
                    testMaxFee, walletFioAddress
                )

                val actionTraceResponse = response.getActionTraceResponse()
                if (actionTraceResponse != null) {
                    Log.i(
                        this.logTag,
                        "Register Fio Domain, " + fioDomainToRegister + ", for Alice: " + actionTraceResponse.status
                    )

                    assertTrue(actionTraceResponse.status == "OK")
                } else
                    Log.i(this.logTag,
                        "Register Fio Domain, " + fioDomainToRegister + ", for Alice: failed")

            }
            else
                Log.i(this.logTag, "Finish registerFioDomain - No Funds Available.  Method not tested.")

        } catch (e: FIOError) {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Register Fio Domain Failed: " + e.toJson())
        } catch (generalException: Exception) {
            throw AssertionError("Register Fio Domain Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish registerFioDomain")

    }

    @Test
    fun renewFioDomain() {

        try {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start renewFioDomain")

            var funds_available  = this.requestFaucetFunds("50.0")

            if(funds_available)
            {
                val fioDomainToRegister = testFioDomain + (0..10000).random().toString()

                var response = this.fioSdk!!.registerFioDomain(
                    fioDomainToRegister, this.alicePublicKey,
                    testMaxFee, walletFioAddress
                )

                var actionTraceResponse = response.getActionTraceResponse()
                if (actionTraceResponse != null) {

                    response = this.fioSdk!!.registerFioDomain(fioDomainToRegister,testMaxFee)

                    actionTraceResponse = response.getActionTraceResponse()
                    if (actionTraceResponse != null) {
                        Log.i(this.logTag,
                            "Renew Fio Domain, " + fioDomainToRegister + ", for Alice: " + actionTraceResponse.status)

                        assertTrue(actionTraceResponse.status == "OK")
                    }
                    else
                    {
                        Log.i(this.logTag,
                            "Renew Fio Domain, " + fioDomainToRegister + ", for Alice: failed")

                        throw FIOError("Renew Fio Domain, " + fioDomainToRegister + ", for Alice: failed")
                    }

                } else
                    Log.i(this.logTag,
                        "Renew Fio Domain, " + fioDomainToRegister + ", for Alice: failed")

            }
            else
                Log.i(this.logTag, "Finish renewFioDomain - No Funds Available.  Method not tested.")

        } catch (e: FIOError) {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Renew Fio Domain Failed: " + e.toJson())
        } catch (generalException: Exception) {
            throw AssertionError("Renew Fio Domain Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish renewFioDomain")

    }


    @Test
    fun registerFioAddress() {

            try {
                this.registerFioNameForUser()

                Log.i(this.logTag, "Start registerFioAddress")

                var funds_available  = this.requestFaucetFunds("3.0")

                if(funds_available)
                {
                    val fioAddressToRegister = "test-shawn" + (0..10000).random().toString() + ":brd"

                    val response = this.fioSdk!!.registerFioAddress(
                        fioAddressToRegister, this.alicePublicKey,
                        testMaxFee, walletFioAddress
                    )

                    val actionTraceResponse = response.getActionTraceResponse()
                    if (actionTraceResponse != null) {
                        Log.i(
                            this.logTag,
                            "Register Fio Address for Alice: " + (actionTraceResponse.status == "OK").toString()
                        )

                        assertTrue(actionTraceResponse.status == "OK")
                    } else
                        Log.i(this.logTag, "Register Fio Address for Alice: failed")
                }
                else
                    Log.i(this.logTag, "Finish registerFioAddress - No Funds Available.  Method not tested.")

            }
            catch (e: FIOError)
            {
                Log.e(this.logTag, e.toJson())

                throw AssertionError("Register Fio Address for Alice Failed: " + e.toJson())
            }
            catch(generalException:Exception)
            {
                throw AssertionError("Register Fio Address for Alice: " + generalException.message)
            }

            Log.i(this.logTag, "Finish registerFioAddress")
    }

    @Test
    fun renewFioAddress()
    {

        this.registerFioNameForUser()

        var funds_available  = this.requestFaucetFunds("3.0")

        if(funds_available)
        {
            try {

                Log.i(this.logTag, "Start renewFioAddress")

                val response = this.fioSdk!!.renewFioAddress(
                    this.aliceFioAddress, this.testMaxFee)

                val actionTraceResponse = response.getActionTraceResponse()
                if (actionTraceResponse != null) {
                    Log.i(
                        this.logTag,
                        "Renew FioAddress, " + this.aliceFioAddress + ", for Alice: " + actionTraceResponse.status
                    )

                    assertTrue(actionTraceResponse.status == "OK")
                }
                else
                    Log.i(this.logTag, "Renew FioAddress, " + this.aliceFioAddress + ", for Alice: failed")

            }
            catch (e: FIOError) {
                Log.e(this.logTag, e.toJson())

                throw AssertionError("Renew FioAddress Failed: " + e.toJson())
            }
            catch(generalException:Exception)
            {
                throw AssertionError("Renew FioAddress Failed: " + generalException.message)
            }

            Log.i(this.logTag, "Finish renewFioAddress")
        }
        else
            Log.i(this.logTag, "Finish renewFioAddress - No Funds Available.  Method not tested.")

    }

    @Test
    fun getFioBalance()
    {
        try
        {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start getFioBalance")

            val balance = this.fioSdk!!.getFioBalance().balance

            Log.i(this.logTag, "GetFioBalance: " + balance.toString())

            assertTrue(true)
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("GetFioBalance Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance Failed: " + generalException.message)
        }


        Log.i(this.logTag, "Finish getFioBalance")
    }

    @Test
    fun newFundsRequest()
    {
        this.requestFunds(false,"4.1","btc")
    }

    @Test
    fun sentRequests()
    {
        try
        {
            this.newFundsRequest()

            Log.i(this.logTag, "Start sentRequests")

            this.sharedSecretKey = CryptoUtils.generateSharedSecret(this.alicePrivateKey,this.bobPublicKey)

            val sentRequests = this.fioSdk!!.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                for (req in sentRequests)
                {
                    req.deserializeRequestContent(this.sharedSecretKey!!,this.fioSdk!!.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        Log.i(this.logTag, "Request Content: " + req.requestContent!!.toJson())

                        assertTrue(req.requestContent != null)
                    }
                }

                assertTrue(sentRequests.isNotEmpty())
            }

        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Sent Funds Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("Sent Funds Request Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish sentRequests")

    }

    @Test
    fun multipleSentRequests()
    {
        try
        {
            this.newFundsRequest()

            for (i in 1..3)
            {
                this.requestFunds(true,"3.".plus(i))
            }

            Log.i(this.logTag, "Start multipleSentRequests")

            this.sharedSecretKey = CryptoUtils.generateSharedSecret(this.alicePrivateKey,this.bobPublicKey)

            val sentRequests = this.fioSdk!!.getSentFioRequests(3,1)

            if(sentRequests.isNotEmpty())
            {
                Log.i(this.logTag, "multipleSentRequests available: " + (sentRequests.count() == 3))

                assertTrue("multipleSentRequests available!",sentRequests.count() == 3)

                for (req in sentRequests)
                {
                    req.deserializeRequestContent(this.sharedSecretKey!!,this.fioSdk!!.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        Log.i(this.logTag, "Request Content: " + req.requestContent!!.toJson())

                        assertTrue(req.requestContent != null)
                    }
                }

                assertTrue(sentRequests.isNotEmpty())
            }

        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("multipleSentRequests Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("multipleSentRequests Request Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish multipleSentRequests")

    }

    @Test
    fun pendingRequests()
    {
        try
        {
            this.newFundsRequest()

            Log.i(this.logTag, "Start pendingRequests")

            this.switchUser("bob")

            this.sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,this.alicePublicKey)

            val pendingRequests = this.fioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                for (req in pendingRequests)
                {
                    req.deserializeRequestContent(this.sharedSecretKey!!,this.fioSdk!!.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        Log.i(this.logTag, "Request Content: " + req.requestContent!!.toJson())

                        assertTrue(req.requestContent != null)
                    }
                }

                assertTrue(pendingRequests.isNotEmpty())
            }

            this.switchUser("alice")
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Pending Funds Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("Pending Funds Request Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish pendingRequests")
    }

    @Test
    fun multiplePendingRequests()
    {
        try
        {
            this.newFundsRequest()

            for (i in 1..3)
            {
                this.requestFunds(true,"3.".plus(i))
            }

            Log.i(this.logTag, "Start multiplePendingRequests")

            this.switchUser("bob")

            this.sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,this.alicePublicKey)

            val pendingRequests = this.fioSdk!!.getPendingFioRequests(3,1)

            if(pendingRequests.isNotEmpty())
            {
                Log.i(this.logTag, "Funds available: " + (pendingRequests.count() == 3))

                assertTrue("Funds available!",pendingRequests.count() == 3)

                for (req in pendingRequests)
                {
                    req.deserializeRequestContent(this.sharedSecretKey!!,this.fioSdk!!.serializationProvider)

                    if(req.requestContent!=null)
                    {
                        Log.i(this.logTag, "Request Content: " + req.requestContent!!.toJson())

                        assertTrue(req.requestContent != null)
                    }
                }

                assertTrue(pendingRequests.isNotEmpty())
            }

            this.switchUser("alice")
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("multiplePendingRequests Funds Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("multiplePendingRequests Funds Request Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish multiplePendingRequests")
    }


    @Test
    fun rejectFundsRequest()
    {
        try
        {
            this.newFundsRequest()

            Log.i(this.logTag, "Start rejectFundsRequest")

            this.switchUser("bob")

            //Find a pending request to reject.  There should be at least one since the "newFundsRequest" call succeeded
            this.sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,this.alicePublicKey)

            val pendingRequests = this.fioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    firstPendingRequest.deserializeRequestContent(this.sharedSecretKey!!,this.fioSdk!!.serializationProvider)

                    if(firstPendingRequest.requestContent!=null)
                    {
                        val response = this.fioSdk!!.rejectFundsRequest(firstPendingRequest.fioRequestId,
                            testMaxFee,
                            walletFioAddress)

                        val actionTraceResponse = response.getActionTraceResponse()

                        if(actionTraceResponse!=null)
                        {
                            Log.i(this.logTag,
                                "Reject Funds Requested by Alice: " + (actionTraceResponse.status == "request_rejected").toString()
                            )
                            assertTrue(actionTraceResponse.status == "request_rejected")
                        }
                    }
                }
                else
                    throw AssertionError("Reject Funds Request Failed: Didn't find a pending request for Bob.")
            }
            else
                throw AssertionError("Reject Funds Request Failed: Didn't find a pending request.")
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Reject Funds Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("Reject Funds Request Failed: " + generalException.message)
        }

        this.switchUser("alice")

        Log.i(this.logTag, "Finish rejectFundsRequest")
    }

    @Test
    fun recordSend()
    {
        try
        {
            this.newFundsRequest()

            Log.i(this.logTag, "Start recordSend")

            this.switchUser("bob")

            //Find a pending request to record.  There should be at least one since the "newFundsRequest" call succeeded
            this.sharedSecretKey = CryptoUtils.generateSharedSecret(this.bobPrivateKey,this.alicePublicKey)

            val pendingRequests = this.fioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    firstPendingRequest.deserializeRequestContent(this.sharedSecretKey!!,this.fioSdk!!.serializationProvider)

                    if(firstPendingRequest.requestContent!=null)
                    {
                        var recordSendContent = RecordSendContent(payerBTCAddress,
                            firstPendingRequest.requestContent!!.payeeTokenPublicAddress,
                            firstPendingRequest.requestContent!!.amount,
                            firstPendingRequest.requestContent!!.tokenCode,this.otherBlockChainId)

                        val response = this.fioSdk!!.recordSend(firstPendingRequest.fioRequestId,firstPendingRequest.payerFioAddress
                            ,firstPendingRequest.payeeFioAddress,payerBTCAddress,recordSendContent.payeeTokenPublicAddress,recordSendContent.amount.toDouble()
                            ,recordSendContent.tokenCode,"",recordSendContent.obtId,testMaxFee,"")

                        val actionTraceResponse = response.getActionTraceResponse()

                        if(actionTraceResponse!=null)
                        {
                            Log.i(this.logTag,
                                "Record Send by Bob: " + (actionTraceResponse.status == "sent_to_blockchain").toString()
                            )

                            assertTrue(actionTraceResponse.status == "sent_to_blockchain")
                        }
                    }
                }
                else
                    throw AssertionError("Record Send Failed: Didn't find a pending request for Alice.")
            }
            else
                throw AssertionError("Record Send Failed: Didn't find a pending request.")
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Record Send Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("Record Send Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish recordSend")

        this.switchUser("alice")
    }

    @Test
    fun listFioNames()
    {
        try
        {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start listFioNames")

            val response = this.fioSdk!!.getFioNames(this.alicePublicKey)

            assertTrue(response.fioAddresses!!.isNotEmpty())

            Log.i(this.logTag, "Found Fio Names for Alice: " + response.fioAddresses!!.isNotEmpty().toString())
        }
        catch (e: GetFIONamesError)
        {
            Log.e(this.logTag, e.toJson())
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("List Fio Names Failed: " + e.toJson())
        }
        catch (ex: Exception)
        {
            throw AssertionError("List Fio Names Failed: " + ex.message)
        }

        Log.i(this.logTag, "Finish listFioNames")
    }

    @Test
    fun isFioNameAvailable()
    {
        try
        {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start isFioAddressAvailable")

            val response = this.fioSdk!!.isAvailable(this.aliceFioAddress)

            Log.i(this.logTag, "Is Fio Address, " + this.aliceFioAddress + ", Available: " + response.isAvailable.toString())

            assertTrue(true)
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("isFioAddressAvailable Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("isFioAddressAvailable Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish isFioAddressAvailable")
    }

    @Test
    fun getFee()
    {
        try
        {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start getFee")

            val response = this.fioSdk!!.getFee(endPointNameForGetFee)

            Log.i(this.logTag, endPointNameForGetFee.endpoint + " Fee: " + response.fee)

            assertTrue(true)
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("getFee Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("getFee Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish getFee")
    }

    @Test
    fun addPublicAddress() {

        try {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start addPublicAddress")

            val response = this.fioSdk!!.addPublicAddress(this.aliceFioAddress, "BTC",
                this.publicBTCAddress, testMaxFee, walletFioAddress)

            val actionTraceResponse = response.getActionTraceResponse()

            if (actionTraceResponse != null)
            {
                Log.i(
                    this.logTag,
                    "Add Public Address for Alice: " + (actionTraceResponse.status == "OK").toString()
                )

                assertTrue(actionTraceResponse.status == "OK")
            }
            else
                Log.i(this.logTag, "Add Public Address for Alice: failed")

        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("Add Public Address for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception) {
            throw AssertionError("Add Public Address for Alice: " + generalException.message)
        }

        Log.i(this.logTag, "Finish addPublicAddress")

    }

    @Test
    fun getPublicAddress()
    {
        try
        {
            this.addPublicAddress()

            Log.i(this.logTag, "Start getPublicAddress")

            val response = this.fioSdk!!.getPublicAddress(this.aliceFioAddress,"BTC")

            Log.i(this.logTag, "Alice BTC Public Address: " + response.publicAddress)

            assertTrue(true)
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("getFee Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("getFee Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish getPublicAddress")
    }

    @Test
    fun setFioDomainVisibility() {

        if(skipSetFioDomainVisibility == false)
        {
            try {
                this.registerFioNameForUser()

                Log.i(this.logTag, "Start setFioDomainVisibility")

                val response = this.fioSdk!!.setFioDomainVisibility(this.aliceFioAddress, FioDomainVisiblity.PUBLIC,
                    testMaxFee, walletFioAddress)

                val actionTraceResponse = response.getActionTraceResponse()

                if (actionTraceResponse != null)
                {
                    Log.i(
                        this.logTag,
                        "Set Alice's Domain public: " + (actionTraceResponse.status == "OK").toString()
                    )

                    assertTrue(actionTraceResponse.status == "OK")
                }
                else
                    Log.i(this.logTag, "Set Alice's Domain Public: failed")

            }
            catch (e: FIOError)
            {
                Log.e(this.logTag, e.toJson())

                throw AssertionError("Set Alice's Domain Public Failed: " + e.toJson())
            }
            catch (generalException: Exception) {
                throw AssertionError("Set Alice's Domain Public: " + generalException.message)
            }

            Log.i(this.logTag, "Finish setFioDomainVisibility")
        }


    }


    //Private Methods

    private fun generatePrivateAndPublicKeys() {
        //String mn = "valley alien library bread worry brother bundle hammer loyal barely dune brave";//"ability sport fly alarm pool spin cupboard quarter laptop write comic torch";

        //Alice First
        var mn = getRandomSeedWords().joinToString(" ")

        alicePrivateKey = FIOSDK.createPrivateKey(mn)
        alicePublicKey = FIOSDK.derivedPublicKey(alicePrivateKey)

        //Bob Next

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

    private fun switchUser(userToSwitchTo: String)
    {
        if(userToSwitchTo == "alice")
        {
            FIOSDK.destroyInstance()

            val serializer = AbiFIOSerializationProvider()

            this.fioSdk = FIOSDK.getInstance(this.alicePrivateKey,this.alicePublicKey,
                serializer, baseUrl)

            this.fioSdk!!.mockServerBaseUrl = this.baseMockUrl

            Log.i(this.logTag,"Alice Private Key: " + this.alicePrivateKey)
            Log.i(this.logTag,"Alice Public Key: " + this.alicePublicKey)
        }
        else if(userToSwitchTo == "bob")
        {
            FIOSDK.destroyInstance()

            val serializer = AbiFIOSerializationProvider()

            this.fioSdk = FIOSDK.getInstance(this.bobPrivateKey,this.bobPublicKey,
                serializer,baseUrl)

            this.fioSdk!!.mockServerBaseUrl = this.baseMockUrl

            Log.i(this.logTag,"Bob Private Key: " + this.bobPrivateKey)
            Log.i(this.logTag,"Bob Public Key: " + this.bobPublicKey)
        }
    }

    private fun requestFunds(skipUserRegistration:Boolean = false,requestAmount:String="4.1",tokenCode:String="BTC")
    {
        try
        {
            if(!skipUserRegistration)
                this.registerFioNameForUser()

            Log.i(this.logTag, "Start newFundsRequest")

            val newFundsContent = FundsRequestContent(payeeBTCAddress,requestAmount,tokenCode)

            val response = this.fioSdk!!.requestNewFunds(this.bobFioAddress,
                this.aliceFioAddress,payeeBTCAddress,requestAmount,tokenCode,
                this.testMaxFee,this.walletFioAddress)

            val actionTraceResponse = response.getActionTraceResponse()
            if (actionTraceResponse != null) {
                Log.i(this.logTag,
                    "New Funds Requested by Alice: " + (actionTraceResponse.status == "requested").toString()
                )

                this.newFundsRequestId = actionTraceResponse.fioRequestId

                assertTrue(actionTraceResponse.status == "requested")
            }
            else
                Log.i(this.logTag, "New Funds Requested by Alice: failed")

        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("New Funds Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("New Funds Request Failed: " + generalException.message)
        }

        Log.i(this.logTag, "Finish newFundsRequest")
    }

    private fun requestFaucetFunds(requestAmount:String="1"): Boolean
    {
        try
        {
            Log.i(this.logTag, "Start requestFaucetFunds")

            val response = this.fioSdk!!.requestNewFunds("faucet:fio",
                this.aliceFioAddress,this.alicePublicKey,requestAmount,"FIO",
                this.testMaxFee,this.walletFioAddress)

            val actionTraceResponse = response.getActionTraceResponse()
            if (actionTraceResponse != null && actionTraceResponse.status == "requested") {
                Log.i(this.logTag,
                    "New Funds Requested by Alice: " + (actionTraceResponse.status == "requested").toString()
                )

                var now = System.currentTimeMillis()

                var check_for_10_minutes = now + (1000 * 60 * 10)

                do {
                    if(now.rem(10000) == 0L)
                    {
                        val balance = this.fioSdk!!.getFioBalance().balance
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

            Log.i(this.logTag, "Finish requestFaucetFunds")

            return false
        }
        catch (e: FIOError)
        {
            Log.e(this.logTag, e.toJson())

            throw AssertionError("New Funds Request Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("New Funds Request Failed: " + generalException.message)
        }
    }
}
