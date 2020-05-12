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
import fiofoundation.io.fiosdk.models.TokenPublicAddress
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
class TestNetSdkTests {

    private val baseUrl = "https://testnet.fioprotocol.io:443/v1/"

    private var alicePrivateKey = "your private key"
    private var alicePublicKey = "your public key"
    private var bobPrivateKey = "your private key"
    private var bobPublicKey = "your public key"

    private val testPrivateKey = "5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu"
    private val testPublicKey = "FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o"
    private val testMnemonic = "valley alien library bread worry brother bundle hammer loyal barely dune brave"

    private var aliceFioAddress = "your registered_address1@fiotestnet"
    private var bobFioAddress = "your registered_address2@fiotestnet"

    private var fioTestNetDomain = "fiotestnet"
    private var defaultFee = BigInteger("400000000000")

    private val alicePublicTokenAddress = "1PzCN3cBkTL72GPeJmpcueU4wQi9guiLa6"
    private val alicePublicTokenCode = "BTC"
    private val alicePublicChainCode = "BTC"
    private val bobPublicTokenAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"
    private var otherBlockChainId = "123456789"

    private var aliceFioSdk = createSdkInstance(alicePrivateKey,alicePublicKey)
    private var bobFioSdk = createSdkInstance(bobPrivateKey,bobPublicKey)


    @Test
    @ExperimentalUnsignedTypes
    fun testGenericActions()
    {
        println("testGenericActions: SUF Conversion Test")

        val testFioAmount = 2.3
        val testSUFAmount = 2300000000.toBigInteger()

        Assert.assertTrue("Amount of SUF does not match.", testSUFAmount == SUFUtils.amountToSUF(testFioAmount))
        Assert.assertTrue("Amount of SUF does not match.", testSUFAmount == testFioAmount.toSUF())
        Assert.assertTrue("Amount of FIO does not match.", testFioAmount == testSUFAmount.toFIO())

        println("testGenericActions: Key Generation Test")
        val genericPrivateTestKey = FIOSDK.createPrivateKey(testMnemonic)
        val genericPublicTestKey = FIOSDK.derivedPublicKey(genericPrivateTestKey)

        println("Private key test pass: ${genericPrivateTestKey == testPrivateKey}" )
        Assert.assertTrue(
            "Private key does not match test private key",
            genericPrivateTestKey == testPrivateKey
        )

        println("Public key test pass: ${genericPublicTestKey == testPublicKey}" )
        Assert.assertTrue(
            "Public key does not match test public key",
            genericPublicTestKey == testPublicKey
        )

        println("testGenericActions: Begin Test for Generic Actions")

        val newFioDomain = this.generateTestingFioDomain()
        val newFioAddress = this.generateTestingFioAddress(newFioDomain)

        val registerAddressFee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress).fee
        val registerDomainFee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioDomain).fee


        println("testGenericActions: Test getFioBalance - Alice")
        try
        {
            val fioBalance = this.aliceFioSdk.getFioBalance().balance

            assertTrue("Balance not Available for Alice.",fioBalance!=null && fioBalance>=BigInteger.ZERO)
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
            val response = this.aliceFioSdk.registerFioDomain(newFioDomain, registerDomainFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't register $newFioDomain for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
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
            val fee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.SetDomainVisibility).fee

            val response = this.aliceFioSdk.setFioDomainVisibility(newFioDomain,FioDomainVisiblity.PUBLIC,fee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Visibility NOT set for $newFioDomain",actionTraceResponse!=null && actionTraceResponse.status == "OK")
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
            val response = this.aliceFioSdk.registerFioAddress(newFioAddress,registerAddressFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Register FioAddress $newFioAddress for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Register FioAddress for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test generic Push Transaction")
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

        println("testGenericActions: Test renewFioAddress")
        try
        {
            val fee = this.aliceFioSdk!!.getFee(FIOApiEndPoints.FeeEndPoint.RenewFioAddress).fee

            val response = this.aliceFioSdk.renewFioAddress(newFioAddress,fee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Renew FioAddress $newFioAddress for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
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
            val addPublicAddressFee = this.aliceFioSdk!!.getFeeForAddPublicAddress(newFioAddress).fee

            val response = this.aliceFioSdk.addPublicAddress(newFioAddress,this.alicePublicTokenCode,
                this.alicePublicTokenCode,this.alicePublicTokenAddress,addPublicAddressFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Add Public Address for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Add Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Add Public Address for Alice Failed: " + generalException.message)
        }

        println("testGenericActions: Test getPublicAddress")
        try
        {
            val response = this.aliceFioSdk!!.getPublicAddress(newFioAddress,this.alicePublicChainCode,this.alicePublicTokenCode)

            assertTrue(
                "Couldn't Find Public Address for Alice",
                !response.publicAddress.isNullOrEmpty()
            )

        }
        catch (e: FIOError)
        {
            throw AssertionError("getPublicAddress Failed: " + e.toJson())
        }
        catch(generalException:Exception)
        {
            throw AssertionError("getPublicAddress Failed: " + generalException.message)
        }

        println("testGenericActions: Test addPublicAddress to alice for removal")
        try
        {
            val addPublicAddressFee = this.aliceFioSdk!!.getFeeForAddPublicAddress(newFioAddress).fee

            val response = this.aliceFioSdk.addPublicAddress(this.aliceFioAddress,this.alicePublicTokenCode,
                this.alicePublicTokenCode,this.alicePublicTokenAddress,addPublicAddressFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't Add Public Address for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("Add Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Add Public Address for Alice Failed: " + generalException.message)
        }


        println("testGenericActions: Test removePublicAddresses")
        try
        {
            val removePublicAddressesFee = this.aliceFioSdk!!.getFeeForRemovePublicAddresses(newFioAddress).fee

            val response = this.aliceFioSdk.removePublicAddresses(this.aliceFioAddress,
                listOf(TokenPublicAddress(this.alicePublicTokenAddress,this.alicePublicTokenCode,this.alicePublicTokenCode)),
                removePublicAddressesFee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Couldn't remove Public Address for Alice",actionTraceResponse!=null && actionTraceResponse.status == "OK")
        }
        catch (e: FIOError)
        {
            throw AssertionError("remove Public Address  for Alice Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("remove Public Address for Alice Failed: " + generalException.message)
        }


        println("testGenericActions: Test isFioAddressAvailable True")
        try
        {
            val testAddress = this.generateTestingFioAddress()
            val response = this.aliceFioSdk.isAvailable(testAddress)

            assertTrue("FioAddress, $testAddress, is NOT Available",response!=null && response.isAvailable)
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
            val response = this.aliceFioSdk.isAvailable(this.aliceFioAddress)

            assertTrue("FioAddress, $aliceFioAddress, IS Available (not supposed to be)",response!=null && !response.isAvailable)
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
            val response = this.aliceFioSdk.getFioNames()

            assertTrue("Couldn't Get FioNames for Alice",response.fioAddresses!!.isNotEmpty())
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
            val response = this.aliceFioSdk.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress)

            assertTrue("Couldn't Get Fee for " + FIOApiEndPoints.FeeEndPoint.RegisterFioAddress.endpoint,response.fee>=BigInteger.ZERO)
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

    @Test
    fun testFundsRequest()
    {
        println("testFundsRequest: Begin Test for NewFundsRequest")

        println("testFundsRequest: Test requestNewFunds")
        try
        {
            val fee = this.aliceFioSdk.getFeeForNewFundsRequest(this.aliceFioAddress).fee

            val response = this.aliceFioSdk.requestFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,2.0,this.alicePublicTokenCode,
                this.alicePublicTokenCode,fee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Couldn't Request Funds from Bob: " + response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "requested")
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
        try
        {
            val sentRequests = this.aliceFioSdk.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                assertTrue("Requests Sent by Alice are NOT Available",sentRequests.isNotEmpty())

                for (req in sentRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        assertTrue("Funds Request Sent by Alice is NOT Valid",req.deserializedContent != null)
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
        try {
            val pendingRequests = this.bobFioSdk.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                assertTrue("Bob does not have requests from Alice that are pending",pendingRequests.isNotEmpty())

                for (req in pendingRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        assertTrue("Pending Requests are Valid",req.deserializedContent != null)
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

        // this approach to testing, or getting the request id 1 will not work on test net, this test needs work
        // it works well when run on a local 3 node test net, but will not work when run on testnet.
        println("testFundsRequest: Test recordObtData")
        try
        {
            val pendingRequests = this.bobFioSdk!!.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{( it.fioRequestId == BigInteger("1")
                )}

                if(firstPendingRequest!=null)
                {
                    if(firstPendingRequest.deserializedContent!=null)
                    {
                        var recordSendContent = RecordObtDataContent(this.bobPublicTokenAddress,
                            firstPendingRequest.deserializedContent!!.payeeTokenPublicAddress,
                            firstPendingRequest.deserializedContent!!.amount,
                            firstPendingRequest.deserializedContent!!.chainCode,
                            firstPendingRequest.deserializedContent!!.tokenCode,this.otherBlockChainId)

                        val fee = this.bobFioSdk.getFeeForRecordObtData(firstPendingRequest.payerFioAddress).fee

                        val response = this.bobFioSdk.recordObtData(firstPendingRequest.fioRequestId,firstPendingRequest.payerFioAddress
                            ,firstPendingRequest.payeeFioAddress,this.bobPublicTokenAddress,recordSendContent.payeeTokenPublicAddress,
                            recordSendContent.amount.toDouble(),recordSendContent.tokenCode,recordSendContent.chainCode,
                            recordSendContent.status, recordSendContent.obtId,fee)

                        println("testFundsRequest: Test recordObtData No RecordId")

                        this.bobFioSdk!!.recordObtData(firstPendingRequest.payerFioAddress
                            ,firstPendingRequest.payeeFioAddress,this.bobPublicTokenAddress,recordSendContent.payeeTokenPublicAddress,
                            recordSendContent.amount.toDouble(),recordSendContent.tokenCode,
                            recordSendContent.status,"987654321", fee)

                        val actionTraceResponse = response.getActionTraceResponse()

                        assertTrue("Couldn't Record Bob Sent Funds to Alice: "+ response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "sent_to_blockchain")
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
        try {

            val obtDataRecords = this.bobFioSdk!!.getObtData()

            if(obtDataRecords.isNotEmpty())
            {
                assertTrue(
                    "Bob does not have obt data recorded",
                    obtDataRecords.isNotEmpty()
                )

                for (req in obtDataRecords)
                {
                    if(req.deserializedContent!=null)
                    {
                        println("OBT Data: " + req.deserializedContent!!.toJson())

                        assertTrue(
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
            throw AssertionError("Test getObtDataByTokenCode Failed: " + e.toJson())
        }
        catch (generalException: Exception)
        {
            throw AssertionError("Test getObtDataByTokenCode Failed: " + generalException.message)
        }

        //Set up test for rejecting funds request
        println("testFundsRequest: Test requestNewFunds")
        try
        {
            val fee = this.aliceFioSdk.getFeeForNewFundsRequest(this.aliceFioAddress).fee

            val response = this.aliceFioSdk.requestFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,2.0,this.alicePublicTokenCode, fee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Couldn't Request Funds from Bob: "+ response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "requested")
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

        //Set up 2nd test for recordobt request
        println("testFundsRequest: Test requestNewFunds")
        try
        {
            val fee = this.aliceFioSdk.getFeeForNewFundsRequest(this.aliceFioAddress).fee

            val response = this.aliceFioSdk.requestFunds(this.bobFioAddress,
                this.aliceFioAddress,this.alicePublicTokenAddress,3.0,this.alicePublicTokenCode, fee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Couldn't Request Funds from Bob: "+ response.toJson(),actionTraceResponse!=null && actionTraceResponse.status == "requested")
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
        try
        {
            val sentRequests = this.aliceFioSdk.getSentFioRequests()

            if(sentRequests.isNotEmpty())
            {
                assertTrue("Requests Sent by Alice are NOT Available",sentRequests.isNotEmpty())

                for (req in sentRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        assertTrue("Funds Request Sent by Alice is NOT Valid",req.deserializedContent != null)
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
        try {

            val pendingRequests = this.bobFioSdk.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                assertTrue("Bob does not have requests from Alice that are pending",pendingRequests.isNotEmpty())

                for (req in pendingRequests)
                {
                    if(req.deserializedContent!=null)
                    {
                        assertTrue("Pending Requests are NOT Valid",req.deserializedContent != null)
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
        try {
            val pendingRequests = this.bobFioSdk.getPendingFioRequests()

            if(pendingRequests.isNotEmpty())
            {
                val firstPendingRequest = pendingRequests.firstOrNull{it.payerFioAddress == this.bobFioAddress}

                if(firstPendingRequest!=null)
                {
                    if(firstPendingRequest.deserializedContent!=null)
                    {
                        val fee = this.bobFioSdk.getFeeForRejectFundsRequest(firstPendingRequest.payeeFioAddress).fee
                        val response = this.bobFioSdk.rejectFundsRequest(firstPendingRequest.fioRequestId,
                            fee)

                        val actionTraceResponse = response.getActionTraceResponse()

                        if(actionTraceResponse!=null)
                        {
                            assertTrue("Bob Couldn't Reject Funds Request from Alice: "+ response.toJson(),actionTraceResponse.status == "request_rejected")
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

    }

    @Test
    fun testTransferFioTokens()
    {
        println("testTransferFioTokens: Begin Test for TransferFioTokens")

        val amountToTransfer = 1.0.toSUF()  //Amount is in SUFs
        var bobBalanceBeforeTransfer = BigInteger.ZERO
        var bobBalanceAfterTransfer = BigInteger.ZERO

        println("testTransferFioTokens: Verify Bob's Current FIO Balance")
        try
        {
            bobBalanceBeforeTransfer = this.bobFioSdk.getFioBalance().balance
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
        try
        {
            val fee = this.bobFioSdk.getFee(FIOApiEndPoints.FeeEndPoint.TransferTokens).fee
            val response = this.aliceFioSdk.transferTokens(this.bobPublicKey,amountToTransfer,fee)

            val actionTraceResponse = response.getActionTraceResponse()

            assertTrue("Alice Failed to Transfer FIO to Bob",actionTraceResponse!=null && actionTraceResponse.status == "OK")
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
        try
        {
            bobBalanceAfterTransfer = this.bobFioSdk.getFioBalance().balance

            assertTrue("Alice Filed to Transfer FIO to Bob",(bobBalanceAfterTransfer - bobBalanceBeforeTransfer) == amountToTransfer)
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
    }

    @Test
    fun testGeneralPushTransaction()
    {
        println("testGenericActions: Test generic Push Transaction")
        try
        {
            var anotherfioAddress = this.generateTestingFioAddress()

            val fee = this.bobFioSdk.getFee(FIOApiEndPoints.FeeEndPoint.RegisterFioAddress).fee

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
    private fun generateTestingFioDomain():String
    {
        val now = System.currentTimeMillis().toString()

        return "testing-domain-$now"
    }

    private fun generateTestingFioAddress(customDomain:String = fioTestNetDomain):String
    {
        val now = System.currentTimeMillis().toString()

        return "testing$now@$customDomain"
    }

    private fun createSdkInstance(privateKey: String, publicKey: String):FIOSDK
    {
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(privateKey)

        val serializer = AbiFIOSerializationProvider()

        return FIOSDK(privateKey,publicKey,"",serializer,signatureProvider,this.baseUrl)
    }

}