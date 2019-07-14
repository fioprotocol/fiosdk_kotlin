package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.androidfiosoftkeysignatureprovider.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.utilities.Utils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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
        val private_key = "5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF" //"5KHNgifC5hRJuq8pqYQ9pCxZbMNvHVW9bfvivY4UHyuxWcoa49T" //5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val fio_address = "shawnmullen123:brd"
        val fio_public_key = "FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82" //"FIO8iB2mYT1zjMwyejw5UYaT5r4cq58sTuvGctoYwQ9rjFT5DGFDq" //FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o
        val wallet_fio_address = "rewards:wallet"
        val max_fee = 4000000000000000000

        val actor = Utils.generateActor(fio_public_key)

        //val provider = FIONetworkProvider("http://34.208.190.214:8889")
        //val request = RegisterFIOAddressRequest(fio_address, "", wallet_fio_address, max_fee, actor)

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        val request = fioSdk.registerFioAddress(fio_address,fio_public_key,max_fee,wallet_fio_address)

//        Log.i("REGFIOADDRESS","Request Data: " + request.actions[0].data)
//
//        Log.i("REGFIOADDRESS","FIO Address: " + request.fioAddress)
//        Log.i("REGFIOADDRESS","FIO Private: " + request.ownerPublicKey)


    }
}
