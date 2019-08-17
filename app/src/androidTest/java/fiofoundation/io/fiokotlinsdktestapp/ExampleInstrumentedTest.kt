package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.androidfiosoftkeysignatureprovider.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.errors.fionetworkprovider.RegisterFIONameForUserError
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIORequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordSendContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.RegisterFIONameForUserRequest
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.PushTransactionResponse
import fiofoundation.io.fiosdk.utilities.CryptoUtils
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.IOException
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
class ExampleInstrumentedTest {

    private var alicePrivateKey = "5JbcPK6qTpYxMXtfpGXagYbo3KFE3qqxv2tLXLMPR8dTWWeYCp9"
    private var alicePublicKey = "FIO7c8SVyAyu6cACCaUjmPFEUyW9p2owWHeqq2WSEZ18FFTgErE1K"
    private var bobPrivateKey = "5JAExdhmQw8F1siD7uzLrhmzfjW97hubw7ZNxjAiAu6p7Xq9wqG"
    private var bobPublicKey = "FIO8LKt4DBzXKzDGjFcZo5x82Nv5ahmbZ8AUNXBv2vMfm6smiHst3"

    private var aliceFioAddress = "test-alice:brd"
    private var bobFioAddress = "test-bob:brd"

    private var testFioDomain = "sm"

    private var walletFioAddress = "rewards:wallet"
    private var testMaxFee = BigInteger("4000000000000000000")

    private var payeeBTCAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"  //bob
    private var payerBTCAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6" //alice
    private var otherBlockChainId = "123456789"
    private var newFundsRequestId = ""

    private var sharedSecretKey:ByteArray? = null

    private var baseUrl = "http://54.184.39.43:8889/v1/"
    private var baseMockUrl = "http://mock.dapix.io/mockd/DEV4/"
    private var fioSdk:FIOSDK? = null

    private var logTag = "FIOSDK-TEST"

    private val skipRegisterFioAddress = true
    private val skipRegisterFioDomain = true

    @Test
    fun setupTestVariable()
    {
        this.logTag = this.logTag + ": " + (0..10000).random().toString()

        Log.i(this.logTag,"Start setupTestVariable")

        this.generatePrivateAndPublicKeys()

        aliceFioAddress = "test-alice" + (0..10000).random().toString() + ":brd"
        bobFioAddress = "test-bob" + (0..10000).random().toString() + ":brd"

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

        var response = this.fioSdk!!.registerFioNameOnBehalfOfUser(this.aliceFioAddress)

        Log.i(this.logTag,"Register FioName For Alice: " + response.status)

        assertTrue(response.status == "OK")

        this.switchUser("bob")

        response = this.fioSdk!!.registerFioNameOnBehalfOfUser(this.bobFioAddress)

        Log.i(this.logTag,"Register FioName For Bob: " + response.status)

        assertTrue(response.status == "OK")

        this.switchUser("alice")

        Log.i(this.logTag,"Finish registerFioNameForUser")
    }

    @Test
    fun registerFioDomain()
    {

        if(skipRegisterFioDomain == false)
        {
            try {
                this.registerFioNameForUser()

                Log.i(this.logTag, "Start registerFioDomain")

                val fioDomainToRegister = testFioDomain + (0..8).random().toString()

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
                    Log.i(this.logTag, "Register Fio Domain, " + fioDomainToRegister + ", for Alice: failed")

            }
            catch (e: FIOError) {
                Log.e(this.logTag, e.toJson())

                throw AssertionError("Register Fio Domain Failed: " + e.toJson())
            }
            catch(generalException:Exception)
            {
                throw AssertionError("Register Fio Domain Failed: " + generalException.message)
            }

            Log.i(this.logTag, "Finish registerFioDomain")
        }
        else
            Log.i(this.logTag, "Skipped registerFioDomain")

    }

    @Test
    fun registerFioAddress() {

        if(skipRegisterFioAddress == false) {
            try {
                this.registerFioNameForUser()

                Log.i(this.logTag, "Start registerFioAddress")

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
        else
            Log.i(this.logTag, "Skipped registerFioAddress")
    }

    @Test
    fun getFioBalance()
    {
        try
        {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start getFioBalance")

            val balance = this.fioSdk!!.getFioBalance()

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
        try
        {
            this.registerFioNameForUser()

            Log.i(this.logTag, "Start newFundsRequest")

            val newFundsContent = FundsRequestContent(payeeBTCAddress,"4.2","BTC")

            val response = this.fioSdk!!.requestNewFunds(this.bobFioAddress,
                this.aliceFioAddress,newFundsContent,this.testMaxFee,this.walletFioAddress)

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

                        val response = this.fioSdk!!.recordSend(this.bobFioAddress,this.aliceFioAddress,
                            recordSendContent,firstPendingRequest.fioRequestId,
                            testMaxFee,walletFioAddress)

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

    //Private Methods

    private fun generatePrivateAndPublicKeys() {
        //String mn = "valley alien library bread worry brother bundle hammer loyal barely dune brave";//"ability sport fly alarm pool spin cupboard quarter laptop write comic torch";

        //Alice First
        var mn = getRandomSeedWords().joinToString(" ")

        alicePrivateKey = FIOSDK.createPrivateKey(mn)
        alicePublicKey = FIOSDK.derivePublicKey(alicePrivateKey)

        //Bob Next

        mn = getRandomSeedWords().joinToString(" ")

        bobPrivateKey = FIOSDK.createPrivateKey(mn)
        bobPublicKey = FIOSDK.derivePublicKey(bobPrivateKey)
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

            val serializationProvider = AbiFIOSerializationProvider()
            val signatureProvider = SoftKeySignatureProvider()
            signatureProvider.importKey(this.alicePrivateKey)

            this.fioSdk = FIOSDK.getInstance(this.alicePrivateKey,this.alicePublicKey,
                serializationProvider,signatureProvider,baseUrl,baseMockUrl)

            Log.i(this.logTag,"Alice Private Key: " + this.alicePrivateKey)
            Log.i(this.logTag,"Alice Public Key: " + this.alicePublicKey)
        }
        else if(userToSwitchTo == "bob")
        {
            FIOSDK.destroyInstance()

            val serializationProvider = AbiFIOSerializationProvider()
            val signatureProvider = SoftKeySignatureProvider()
            signatureProvider.importKey(this.bobPrivateKey)

            this.fioSdk = FIOSDK.getInstance(this.bobPrivateKey,this.bobPublicKey,
                serializationProvider,signatureProvider,baseUrl,baseMockUrl)

            Log.i(this.logTag,"Bob Private Key: " + this.bobPrivateKey)
            Log.i(this.logTag,"Bob Public Key: " + this.bobPublicKey)
        }
    }

}
