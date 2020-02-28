package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.enums.FioDomainVisiblity
import fiofoundation.io.fiosdk.errors.FIOError
import fiofoundation.io.fiosdk.implementations.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.FIOApiEndPoints
import fiofoundation.io.fiosdk.models.fionetworkprovider.RecordObtDataContent
import fiofoundation.io.fiosdk.models.fionetworkprovider.actions.RegisterFIOAddressAction
import fiofoundation.io.fiosdk.toFIO
import fiofoundation.io.fiosdk.toSUF
import fiofoundation.io.fiosdk.utilities.SUFUtils
import fiofoundation.io.fiosdk.utilities.Utils
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
    private val baseUrl = "http://dev2.fio.dev:8889/v1/"
    private val baseMockUrl = "http://mock.dapix.io/mockd/DEV2/"

    private var alicePrivateKey = ""
    private var alicePublicKey = ""
    private var bobPrivateKey = ""
    private var bobPublicKey = ""
    private val testPrivateKey = "5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu"
    private val testPublicKey = "FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o"
    private val testMnemonic = "valley alien library bread worry brother bundle hammer loyal barely dune brave"

    private var aliceFioAddress = ""
    private var bobFioAddress = ""

    private var fioTestDomain = "dapixdev"
    private var defaultFee = BigInteger("500000000000")

    private val alicePublicTokenAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6"
    private val alicePublicTokenCode = "BTC"
    private val alicePublicChainCode = "BTC"
    private val bobPublicTokenAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"
    private var otherBlockChainId = "123456789"

    private var aliceFioSdk:FIOSDK? = null
    private var bobFioSdk:FIOSDK? = null
    private var whaleFioSdk:FIOSDK? = null

    private var logTag = "FIOSDK-TEST"

    private var useMockServerForAlice = false
    private var useMockServerForBob = false

    private var whalePrivateKey = "5KF2B21xT5pE5G3LNA6LKJc6AP2pAd2EnfpAUrJH12SFV8NtvCD"
    private var whalePublicKey = "FIO6zwqqzHQcqCc2MB4jpp1F73MXpisEQe2SDghQFSGQKoAPjvQ3H"
    private var useWhaleFunds = false

    @Test
    @ExperimentalUnsignedTypes
    fun testGenericActions()
    {
        this.setupTestVariables()

        println("testGenericActions: SUF Conversion Test")
        Log.i(this.logTag,"testGenericActions: SUF Conversion Test")

        val testFioAmount = 2.3
        val testSUFAmount = 2300000000.toBigInteger()

        Assert.assertTrue(
            "Amount of SUF does not match.",
            testSUFAmount == SUFUtils.amountToSUF(testFioAmount)
        )

        Assert.assertTrue(
            "Amount of SUF does not match.",
            testSUFAmount == testFioAmount.toSUF()
        )

        Assert.assertTrue(
            "Amount of FIO does not match.",
            testFioAmount == testSUFAmount.toFIO()
        )

        println("testGenericActions: Key Generation Test")
        Log.i(this.logTag,"testGenericActions: Key Generation Test")

        val genericPrivateTestKey = FIOSDK.createPrivateKey(testMnemonic)
        val genericPublicTestKey = FIOSDK.derivedPublicKey(genericPrivateTestKey)

        println("Private key test pass: ${genericPrivateTestKey == testPrivateKey}" )
        Log.i(this.logTag,"Private key test pass: ${genericPrivateTestKey == testPrivateKey}" )

        Assert.assertTrue(
            "Private key does not match test private key",
            genericPrivateTestKey == testPrivateKey
        )

        println("Public key test pass: ${genericPublicTestKey == testPublicKey}" )
        Log.i(this.logTag, "Public key test pass: ${genericPublicTestKey == testPublicKey}")

        Assert.assertTrue(
            "Public key does not match test public key",
            genericPublicTestKey == testPublicKey
        )

        println("testGenericActions: Begin Test for Generic Actions")
        Log.i(this.logTag,"testGenericActions: Begin Test for Generic Actions")

        var newFioDomain = this.generateTestingFioDomain()
        var newFioAddress = this.generateTestingFioAddress(newFioDomain)

        val registerAddressFee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress).fee
        val registerDomainFee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioDomain).fee

        println("testGenericActions: Test getFioBalance - Alice")
        Log.i(this.logTag,"testGenericActions: Test getFioBalance - Alice")

        try
        {
            val fioBalance = this.aliceFioSdk!!.getFioBalance().balance

            Assert.assertTrue(
                "Balance not Available for Alice.",
                fioBalance != null && fioBalance >= BigInteger.ZERO
            )

            Log.i(this.logTag, "Alice's balance: $fioBalance")
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
        Log.i(this.logTag,"testGenericActions: Test registerFioDomain")

        try
        {
            val response = this.aliceFioSdk!!.registerFioDomain(newFioDomain, registerDomainFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't register $newFioDomain for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )

            Log.i(this.logTag, "Registered Fio Domain: ${actionTraceResponse != null && actionTraceResponse.status == "OK"}")
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
        Log.i(this.logTag,"testGenericActions: Test setFioDomainVisibility to True")

        try
        {
            val response = this.aliceFioSdk!!.setFioDomainVisibility(newFioDomain,
                FioDomainVisiblity.PUBLIC,defaultFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Visibility NOT set for $newFioDomain",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )

            Log.i(this.logTag, "Set domain visibility: ${actionTraceResponse != null && actionTraceResponse.status == "OK"}")
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
        Log.i(this.logTag,"testGenericActions: Test registerFioAddress")

        try
        {
            val response = this.aliceFioSdk!!.registerFioAddress(newFioAddress,registerAddressFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't Register FioAddress $newFioAddress for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )

            Log.i(this.logTag, "Registered FioAddress: ${actionTraceResponse != null && actionTraceResponse.status == "OK"}")

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
        Log.i(this.logTag,"testGenericActions: Test renewFioAddress")

        try
        {
            val fee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RenewFioAddress).fee

            val response = this.aliceFioSdk!!.renewFioAddress(newFioAddress,fee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't Renew FioAddress $newFioAddress for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )

            Log.i(this.logTag, "Renewed FioAddress: ${actionTraceResponse != null && actionTraceResponse.status == "OK"}")
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
        Log.i(this.logTag,"testGenericActions: Test addPublicAddress")

        try
        {
            val addPublicAddressFee = this.aliceFioSdk!!.getFeeForAddPublicAddress(newFioAddress).fee

            val response = this.aliceFioSdk!!.addPublicAddress(newFioAddress,this.alicePublicTokenCode,
                this.alicePublicChainCode,this.alicePublicTokenAddress,addPublicAddressFee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't Add Public Address for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )

            Log.i(this.logTag, "Added public address: ${actionTraceResponse != null && actionTraceResponse.status == "OK"}")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Add Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Add Public Address for Alice Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        println("testGenericActions: Test getPublicAddress")
        Log.i(this.logTag,"testGenericActions: Test getPublicAddress")

        try
        {
            val response = this.aliceFioSdk!!.getPublicAddress(newFioAddress,this.alicePublicTokenCode,this.alicePublicChainCode)

            Assert.assertTrue(
                "Couldn't Find Public Address for Alice",
                !response.publicAddress.isNullOrEmpty()
            )

            Log.i(this.logTag, "Retrieved public address: ${!response.publicAddress.isNullOrEmpty()}")
        }
        catch (e: FIOError)
        {
            throw AssertionError("getPublicAddress Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("getPublicAddress Failed: " + generalException.message)
        }

        println("testGenericActions: Test isFioAddressAvailable True")
        Log.i(this.logTag,"testGenericActions: Test isFioAddressAvailable True")

        try
        {
            val testAddress = this.generateTestingFioAddress()
            val response = this.aliceFioSdk!!.isAvailable(testAddress)

            Assert.assertTrue(
                "FioAddress, $testAddress, is NOT Available",
                response != null && response.isAvailable
            )

            Log.i(this.logTag, "Is Fio Address Available: ${response != null && response.isAvailable}")
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
        Log.i(this.logTag,"testGenericActions: Test isFioAddressAvailable False")

        try
        {
            val response = this.aliceFioSdk!!.isAvailable(this.aliceFioAddress)

            Assert.assertTrue(
                "FioAddress, $aliceFioAddress, IS Available (not supposed to be)",
                response != null && !response.isAvailable
            )

            Log.i(this.logTag, "Is Fio Address Available: ${response != null && response.isAvailable}")
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
        Log.i(this.logTag,"testGenericActions: Test getFioNames")

        try
        {
            val response = this.aliceFioSdk!!.getFioNames()

            Assert.assertTrue(
                "Couldn't Get FioNames for Alice",
                response.fioAddresses!!.isNotEmpty()
            )

            Log.i(this.logTag, "Retrieved FioNames: ${response.fioAddresses!!.isNotEmpty()}")
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
        Log.i(this.logTag,"testGenericActions: Test getFee")

        try
        {
            val response = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress)

            Assert.assertTrue(
                "Couldn't Get Fee for " + FIOApiEndPoints.FeeEndPoint.RegisterFioAddress.endpoint,
                response.fee >= BigInteger.ZERO
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
        Log.i(this.logTag,"testGenericActions: End Test for Generic Actions")
    }

    @Test
    fun testFundsRequest()
    {
        this.setupTestVariables()

        println("testFundsRequest: Begin Test for NewFundsRequest")
        Log.i(this.logTag,"testFundsRequest: Begin Test for NewFundsRequest")

        println("testFundsRequest: Test requestNewFunds")

        Log.i(this.logTag,"testFundsRequest: Test requestNewFunds")

        try
        {
            val fee = this.aliceFioSdk!!.getFeeForNewFundsRequest(this.aliceFioAddress).fee

            val response = this.aliceFioSdk!!.requestFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,2.0,
                this.alicePublicTokenCode,this.alicePublicTokenCode,"", fee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Alice Couldn't Request Funds from Bob: " + response.toJson(),
                actionTraceResponse != null && actionTraceResponse.status == "requested"
            )

            Log.i(this.logTag, "Requested funds: ${actionTraceResponse != null && actionTraceResponse.status == "requested"}")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Alice's Funds Request Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Alice's Funds Request Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        println("testFundsRequest: Test getSentFioRequests")
        Log.i(this.logTag,"testFundsRequest: Test getSentFioRequests")

        try
        {
            var sentRequests = this.aliceFioSdk!!.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                Assert.assertTrue(
                    "Requests Sent by Alice are NOT Available",
                    sentRequests.isNotEmpty()
                )

                Log.i(this.logTag, "Retrieved Sent Fio Requests: ${sentRequests.isNotEmpty()}")

                for (req in sentRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        Assert.assertTrue(
                            "Funds Request Sent by Alice is NOT Valid",
                            req.deserializedContent != null
                        )
                    }
                }
            }

            sentRequests = this.aliceFioSdk!!.getSentFioRequests(2,0)
            if(sentRequests.isNotEmpty())
            {
                Assert.assertTrue(
                    "Requests Sent by Alice are NOT Available",
                    sentRequests.isNotEmpty()
                )
            }

        }
        catch (e: FIOError)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + generalException.message)
        }

        println("testFundsRequest: Test getPendingFioRequests")
        Log.i(this.logTag,"testFundsRequest: Test getPendingFioRequests")

        try {

            val pendingRequests = this.bobFioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                Assert.assertTrue(
                    "Bob does not have requests from Alice that are pending",
                    pendingRequests.isNotEmpty()
                )

                Log.i(this.logTag, "Retrieved Pending Fio Requests: ${pendingRequests.isNotEmpty()}")

                for (req in pendingRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        Assert.assertTrue("Pending Requests are Valid", req.deserializedContent != null)
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Pending Requests Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Pending Requests Failed: " + generalException.message)
        }

        println("testFundsRequest: Test recordObtData")
        Log.i(this.logTag,"testFundsRequest: Test recordObtData")

        try
        {
            val pendingRequests = this.bobFioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    if(firstPendingRequest.deserializedContent!=null)
                    {
                        var recordSendContent = RecordObtDataContent(this.bobPublicTokenAddress,
                            firstPendingRequest.deserializedContent!!.payeeTokenPublicAddress,
                            firstPendingRequest.deserializedContent!!.amount,
                            firstPendingRequest.deserializedContent!!.chainCode,firstPendingRequest.deserializedContent!!.tokenCode,
                            this.otherBlockChainId)

                        val response = this.bobFioSdk!!.recordObtData(firstPendingRequest.fioRequestId,firstPendingRequest.payerFioAddress
                            ,firstPendingRequest.payeeFioAddress,this.bobPublicTokenAddress,recordSendContent.payeeTokenPublicAddress,
                            recordSendContent.amount.toDouble(),recordSendContent.tokenCode,recordSendContent.chainCode,
                            recordSendContent.status,recordSendContent.obtId,this.defaultFee)

                        println("testFundsRequest: Test recordObtData No RecordId")
                        this.bobFioSdk!!.recordObtData(firstPendingRequest.payerFioAddress
                            ,firstPendingRequest.payeeFioAddress,this.bobPublicTokenAddress,recordSendContent.payeeTokenPublicAddress,
                            recordSendContent.amount.toDouble(),recordSendContent.tokenCode,recordSendContent.status,"987654321",
                            this.defaultFee)


                        val actionTraceResponse = response.getActionTraceResponse()

                        Assert.assertTrue(
                            "Couldn't Record Bob Sent Funds to Alice: " + response.toJson(),
                            actionTraceResponse != null && actionTraceResponse.status == "sent_to_blockchain"
                        )
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Record Send Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Record Send Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        println("testFundsRequest: Test getObtData")
        Log.i(this.logTag,"testFundsRequest: Test getObtData")

        try {

            val obtDataRecords = this.bobFioSdk!!.getObtData()

            if(obtDataRecords.isNotEmpty())
            {
                Assert.assertTrue(
                    "Bob does not have obt data recorded",
                    obtDataRecords.isNotEmpty()
                )

                for (req in obtDataRecords)
                {
                    if(req.deserializedContent!=null)
                    {
                        println("OBT Data: " + req.deserializedContent!!.toJson())

                        Assert.assertTrue(
                            "Obt Data NOT Valid",
                            req.deserializedContent != null
                        )
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Pending Requests Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Pending Requests Failed: " + generalException.message)
        }

        println("testFundsRequest: Test getObtDataByTokenCode")
        Log.i(this.logTag,"testFundsRequest: Test getObtDataByTokenCode")

        try {

            val obtDataRecords = this.bobFioSdk!!.getObtDataByTokenCode("BTC")

            if(obtDataRecords.isNotEmpty())
            {
                Assert.assertTrue(
                    "Bob does not have obt data recorded",
                    obtDataRecords.isNotEmpty()
                )

                for (req in obtDataRecords)
                {
                    if(req.deserializedContent!=null)
                    {
                        println("OBT Data: " + req.deserializedContent!!.toJson())

                        Assert.assertTrue(
                            "Obt Data NOT Valid",
                            req.deserializedContent != null
                        )
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Pending Requests Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Pending Requests Failed: " + generalException.message)
        }

        //Set up test for rejecting funds request
        println("testFundsRequest: Test requestNewFunds")
        Log.i(this.logTag,"testFundsRequest: Test requestNewFunds")

        try
        {
            val fee = this.aliceFioSdk!!.getFeeForNewFundsRequest(this.aliceFioAddress).fee

            val response = this.aliceFioSdk!!.requestFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,2.0,this.alicePublicTokenCode,
                fee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Alice Couldn't Request Funds from Bob: " + response.toJson(),
                actionTraceResponse != null && actionTraceResponse.status == "requested"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Alice's Funds Request Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Alice's Funds Request Failed: " + generalException.message)
        }

        Thread.sleep(4000)

        println("testFundsRequest: Test getSentFioRequests")
        Log.i(this.logTag,"testFundsRequest: Test getSentFioRequests")

        try
        {
            val sentRequests = this.aliceFioSdk!!.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                Assert.assertTrue(
                    "Requests Sent by Alice are NOT Available",
                    sentRequests.isNotEmpty()
                )

                for (req in sentRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        Assert.assertTrue(
                            "Funds Request Sent by Alice is NOT Valid",
                            req.deserializedContent != null
                        )
                    }
                }
            }

        }
        catch (e: FIOError)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Cannot Get List of Requests Sent by Alice: " + generalException.message)
        }

        println("testFundsRequest: Test getPendingFioRequests")
        Log.i(this.logTag,"testFundsRequest: Test getPendingFioRequests")

        try {

            val pendingRequests = this.bobFioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                Assert.assertTrue(
                    "Bob does not have requests from Alice that are pending",
                    pendingRequests.isNotEmpty()
                )

                for (req in pendingRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        Assert.assertTrue(
                            "Pending Requests are NOT Valid",
                            req.deserializedContent != null
                        )
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Pending Requests Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Pending Requests Failed: " + generalException.message)
        }

        println("testFundsRequest: Test rejectFundsRequest")
        Log.i(this.logTag,"testFundsRequest: Test rejectFundsRequest")

        try {
            val pendingRequests = this.bobFioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    if(firstPendingRequest.deserializedContent!=null)
                    {
                        val fee = this.bobFioSdk!!.getFeeForRejectFundsRequest(firstPendingRequest.payeeFioAddress).fee

                        val response = this.bobFioSdk!!.rejectFundsRequest(firstPendingRequest.fioRequestId, fee)

                        val actionTraceResponse = response.getActionTraceResponse()

                        if(actionTraceResponse!=null)
                        {
                            Assert.assertTrue(
                                "Bob Couldn't Reject Funds Request from Alice: " + response.toJson(),
                                actionTraceResponse.status == "request_rejected"
                            )
                        }
                    }
                }
            }
        }
        catch (e: FIOError)
        {
            throw AssertionError("Reject Funds Request Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Reject Funds Request Failed: " + generalException.message)
        }

        println("testFundsRequest: End Test for NewFundsRequest")
        Log.i(this.logTag,"testFundsRequest: End Test for NewFundsRequest")
    }

    @Test
    fun testTransferFioTokens()
    {
        this.setupTestVariables()

        println("testTransferFioTokens: Begin Test for TransferFioTokens")
        Log.i(this.logTag,"testTransferFioTokens: Begin Test for TransferFioTokens")


        val amountToTransfer = BigInteger("1000000000")   //Amount is in SUFs
        var bobBalanceBeforeTransfer = BigInteger.ZERO
        var bobBalanceAfterTransfer = BigInteger.ZERO

        println("testTransferFioTokens: Verify Bob's Current FIO Balance")
        Log.i(this.logTag,"testTransferFioTokens: Verify Bob's Current FIO Balance")

        try
        {
            bobBalanceBeforeTransfer = this.bobFioSdk!!.getFioBalance().balance
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + generalException.message)
        }

        println("testTransferFioTokens: Test transferTokens")
        Log.i(this.logTag,"testTransferFioTokens: Test transferTokens")

        try
        {
            val fee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.TransferTokens).fee
            val response = this.aliceFioSdk!!.transferTokens(this.bobPublicKey,amountToTransfer,fee)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Alice Failed to Transfer FIO to Bob",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("FIO Token Transfer Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("FIO Token Transfer Failed: " + generalException.message)
        }

        println("testTransferFioTokens: Verify Bob's New FIO Balance")
        Log.i(this.logTag,"testTransferFioTokens: Verify Bob's New FIO Balance")

        try
        {
            bobBalanceAfterTransfer = this.bobFioSdk!!.getFioBalance().balance

            Assert.assertTrue(
                "Alice Filed to Transfer FIO to Bob",
                (bobBalanceAfterTransfer - bobBalanceBeforeTransfer) == amountToTransfer
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("GetFioBalance for Bob Failed: " + generalException.message)
        }

        println("testTransferFioTokens: End Test for TransferFioTokens")
        Log.i(this.logTag,"testTransferFioTokens: End Test for TransferFioTokens")
    }

    @Test
    fun testGeneralPushTransaction()
    {
        this.setupTestVariables()

        println("testGenericActions: Test generic Push Transaction")
        Log.i(this.logTag,"testGenericActions: Test generic Push Transaction")

        try
        {
            var anotherfioAddress = this.generateTestingFioAddress()

            val fee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress).fee

            var addressRequestData = RegisterFIOAddressAction.FIOAddressRequestData(anotherfioAddress,this.alicePublicKey,fee,
                Utils.generateActor(this.alicePublicKey),"")

            var requestData = addressRequestData.toJson()

            val response = this.aliceFioSdk!!.pushTransaction("fio.address","regaddress",requestData)

            val actionTraceResponse = response.getActionTraceResponse()

            Assert.assertTrue(
                "Couldn't register $anotherfioAddress for Alice",
                actionTraceResponse != null && actionTraceResponse.status == "OK"
            )
        }
        catch (e: FIOError)
        {
            throw AssertionError("Generic Push Transaction for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Generic Push Transaction for Alice Failed: " + generalException.message)
        }
    }

    //Helper Methods
    private fun setupTestVariables()
    {
        this.generatePrivateAndPublicKeys()

        this.aliceFioSdk = createSdkInstance(this.alicePrivateKey,this.alicePublicKey)
        this.bobFioSdk = createSdkInstance(this.bobPrivateKey,this.bobPublicKey)

        val initialFioAddressForAlice = this.generateTestingFioAddress()

        if(this.useWhaleFunds)
        {
            this.whaleFioSdk = createSdkInstance(this.whalePrivateKey,this.whalePublicKey)
            this.whaleFioSdk!!.transferTokens(this.alicePublicKey,BigInteger("2500000000000"),this.defaultFee)
            this.whaleFioSdk!!.transferTokens(this.bobPublicKey,BigInteger("2500000000000"),this.defaultFee)

            Thread.sleep(6000)
        }

        if(useMockServerForAlice)
            this.registerFioNameForUser(this.aliceFioSdk!!,initialFioAddressForAlice)
        else
        {
            val fee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress).fee
            this.aliceFioSdk!!.registerFioAddress(initialFioAddressForAlice, fee)
        }

        val initialFioAddressForBob = this.generateTestingFioAddress()

        if(useMockServerForBob)
            this.registerFioNameForUser(this.bobFioSdk!!,initialFioAddressForBob)
        else
        {
            val fee = this.bobFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress).fee
            this.bobFioSdk!!.registerFioAddress(initialFioAddressForBob, fee)
        }

        this.aliceFioAddress = initialFioAddressForAlice
        this.bobFioAddress = initialFioAddressForBob

        println("Alice's Public Key: " + this.alicePublicKey)
        println("Bob's Public Key: " + this.bobPublicKey)

        Log.i(this.logTag, "Alice's Public Key: " + this.alicePublicKey)
        Log.i(this.logTag, "Bob's Public Key: " + this.bobPublicKey)

        Log.i(this.logTag, "Alice's Private Key: " + this.alicePrivateKey)
        Log.i(this.logTag, "Bob's Private Key: " + this.bobPrivateKey)

        if(useMockServerForAlice || useMockServerForBob) {
            this.requestFaucetFunds(25.0)
            Thread.sleep(4000)

            this.requestFaucetFunds(25.0)
            Thread.sleep(4000)

            this.requestFaucetFunds(25.0)

            Log.i(this.logTag, "Wait for balance to really be available.")
            Thread.sleep(60000)
        }


    }

    fun registerFioNameForUser(fioSdk:FIOSDK,fioAddress:String) {

        Log.i(this.logTag,"Start registerFioNameForUser")
        Log.i(this.logTag,"Register " + fioAddress)

        var response = fioSdk.registerFioNameOnBehalfOfUser(fioAddress)

        Log.i(this.logTag,"RegisterFioNameForUser: " + response.status)

        Assert.assertTrue(response.status == "OK")

        println("Registered Address: " + fioAddress)

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

        return "testing$now@$customDomain"
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

        if(this.alicePrivateKey == "") {
            alicePrivateKey = FIOSDK.createPrivateKey(mn)

            if(this.whalePrivateKey == "" || this.whalePublicKey == "")
                useMockServerForAlice = true
            else
                this.useWhaleFunds = true
        }

        alicePublicKey = FIOSDK.derivedPublicKey(alicePrivateKey)

        mn = getRandomSeedWords().joinToString(" ")

        if(this.bobPrivateKey == "") {
            bobPrivateKey = FIOSDK.createPrivateKey(mn)

            if(this.whalePrivateKey == "" || this.whalePublicKey == "")
                useMockServerForBob = true
            else
                this.useWhaleFunds = true
        }

        bobPublicKey = FIOSDK.derivedPublicKey(bobPrivateKey)
    }

    private fun getRandomSeedWords(): List<String> {
        val seedWords: List<String>

        val mnemonicCode = MnemonicCode()

        seedWords = mnemonicCode.toMnemonic(SecureRandom().generateSeed(16))

        return seedWords
    }

    private fun requestFaucetFunds(requestAmount:Double=1.0): Boolean
    {
        try
        {
            Log.i(this.logTag, "Start requestFaucetFunds")

            var response = this.aliceFioSdk!!.requestFunds("fio@faucet",
                this.aliceFioAddress,this.alicePublicKey,requestAmount,"FIO",
                this.defaultFee)

            var actionTraceResponse = response.getActionTraceResponse()
            if (actionTraceResponse != null && actionTraceResponse.status == "requested")
            {
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