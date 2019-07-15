package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.androidfiosoftkeysignatureprovider.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.FIOSDK

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
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
}
