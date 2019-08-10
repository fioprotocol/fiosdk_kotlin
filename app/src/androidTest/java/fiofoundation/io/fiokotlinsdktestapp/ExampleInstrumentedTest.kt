package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.androidfiosoftkeysignatureprovider.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.models.fionetworkprovider.FundsRequestContent
import fiofoundation.io.fiosdk.utilities.CryptoUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.lang.Exception
import java.math.BigInteger

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("fiofoundation.io.fiokotlinsdktestapp", appContext.packageName)
    }

    @Test
    fun testRegisterFioAddress() {
        val private_key = "5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF"

        //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu  //5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF (pawel test key)

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val fio_address = "shawnm127:brd"
        val fio_public_key = "FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82"

        //"FIO8iB2mYT1zjMwyejw5UYaT5r4cq58sTuvGctoYwQ9rjFT5DGFDq" //FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o (registered via mock) //FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82 (pawel test key)

        val wallet_fio_address = "rewards:wallet"
        val max_fee = BigInteger("4000000000000000000")

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        fioSdk.registerFioAddress(fio_address,fio_public_key,max_fee,wallet_fio_address)
    }

    @Test
    fun testRegisterFioDomain() {
        val private_key = "5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF" //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val fio_domain = "yoroi"
        val fio_public_key = "FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82" //"FIO8iB2mYT1zjMwyejw5UYaT5r4cq58sTuvGctoYwQ9rjFT5DGFDq" //FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o
        val wallet_fio_address = "rewards:wallet"
        val max_fee = BigInteger("4000000000000000000")

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        fioSdk.registerFioDomain(fio_domain,fio_public_key,max_fee,wallet_fio_address)
    }

    @Test
    fun testTransferTokensToPublicKey() {
        val private_key = "5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF" //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val payee_public_key = "FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o"
        val fio_public_key = "FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82" //"FIO8iB2mYT1zjMwyejw5UYaT5r4cq58sTuvGctoYwQ9rjFT5DGFDq" //FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o
        val wallet_fio_address = "rewards:wallet"
        val max_fee = BigInteger("4000000000000000000")
        var amount = "10"

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        fioSdk.transferTokensToPublicKey(payee_public_key,amount,max_fee,wallet_fio_address)
    }

    @Test
    fun testGetFioBalance()
    {
        val private_key = "5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF" //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val fio_public_key = "FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82" //"FIO8iB2mYT1zjMwyejw5UYaT5r4cq58sTuvGctoYwQ9rjFT5DGFDq" //FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o

        try
        {
            var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
                serializationProvider,signatureProvider)

            Log.i("GET_FIO_BALANCE",fioSdk.getFioBalance().toString())
        }
        catch(e:Exception)
        {
            Log.e("GET_FIO_BALANCE",e.message)
        }

    }

    @Test
    fun testNewFundsRequest()
    {
        val private_key = "5JbcPK6qTpYxMXtfpGXagYbo3KFE3qqxv2tLXLMPR8dTWWeYCp9" //sm0alice

        val wallet_fio_address = "rewards:wallet"
        val max_fee = BigInteger("4000000000000000000")

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val fio_public_key = "FIO7c8SVyAyu6cACCaUjmPFEUyW9p2owWHeqq2WSEZ18FFTgErE1K" //sm0alice
        val payeeBTCAddress = "1AkZGXsnyDfp4faMmVfTWsN1nNRRvEZJk8"

        val newFundsContent = FundsRequestContent(payeeBTCAddress,"3.0","BTC")


        try
        {
            var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
                serializationProvider,signatureProvider)

            val response = fioSdk.requestNewFunds("sm0bob:brd",
                "sm0alice:brd",newFundsContent,max_fee,wallet_fio_address)

            Log.i("NewFundsRequest - processed:",response.processed.toString())

            Log.i("NewFundsRequest - responseKey:",response.processed!!["action_traces"].toString())
            Log.i("NewFundsRequest - json:",response.toJson())

            Log.i("NewFundsRequest",response.transactionId)
        }
        catch(e:Exception)
        {
            Log.e("ClaimBlockProducerRewards",e.message)
        }
    }

    @Test
    fun testPendingRequests()
    {
        val private_key = "5J9bWm2ThenDm3tjvmUgHtWCVMUdjRR1pxnRtnJjvKA4b2ut5WK" //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu
        val fio_public_key = "FIO7zsqi7QUAjTAdyynd6DVe8uv4K8gCTRHnAoMN9w9CA1xLCTDVv"

        val wallet_fio_address = "rewards:wallet"
        val max_fee = BigInteger("4000000000000000000")

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val payerFioAddress = "shawnmullen21:brd"

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        try
        {
            val pendingRequests = fioSdk.getPendingFioRequests("FIO87MK3VsNmCjSTtscRKBnEwzbNYsCnGaUWdFgGuCLCV3tVW4Wai")

            pendingRequests.toList()
        }
        catch(e:Exception)
        {
            System.out.println(e.message)
        }

    }

    @Test
    fun testSentRequests()
    {
        try
        {
            val private_key = "5JbcPK6qTpYxMXtfpGXagYbo3KFE3qqxv2tLXLMPR8dTWWeYCp9"  //sm0alice
            val fio_public_key = "FIO7c8SVyAyu6cACCaUjmPFEUyW9p2owWHeqq2WSEZ18FFTgErE1K"  //sm0alice

//            val wallet_fio_address = "rewards:wallet"
//            val max_fee = BigInteger("4000000000000000000")

            val serializationProvider = AbiFIOSerializationProvider()
            val signatureProvider = SoftKeySignatureProvider()
            signatureProvider.importKey(private_key)

            var fioSdk: FIOSDK = FIOSDK.getInstance(
                private_key, fio_public_key,
                serializationProvider, signatureProvider
            )


            val sentRequests = fioSdk.getSentFioRequests()

//            val sharedSecretKey = CryptoUtils.generateSharedSecret(private_key, sentRequests[0].payerFioPublicKey)
//
//            sentRequests[0].deserializeRequestContent(sharedSecretKey,serializationProvider)

            val s = ""
        }
        catch(e:Exception)
        {
            System.out.println(e.message)
        }

    }

    @Test
    fun testRejectFundsRequest()
    {
        val private_key = "5J9bWm2ThenDm3tjvmUgHtWCVMUdjRR1pxnRtnJjvKA4b2ut5WK" //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu
        val fio_public_key = "FIO7zsqi7QUAjTAdyynd6DVe8uv4K8gCTRHnAoMN9w9CA1xLCTDVv"

        val wallet_fio_address = "rewards:wallet"
        val max_fee = BigInteger("4000000000000000000")

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val payerFioAddress = "shawnmullen21:brd"

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        try
        {
            val response = fioSdk.rejectFundsRequest("8",max_fee,wallet_fio_address)

            println(response)
            println(response.toJson())

        }
        catch(e:Exception)
        {
            println(e.message)
        }
    }

}
