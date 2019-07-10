package fiofoundation.io.fiokotlinsdktestapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import fiofoundation.io.androidfioserializationprovider.AbiFIOSerializationProvider
import fiofoundation.io.androidfiosoftkeysignatureprovider.SoftKeySignatureProvider
import fiofoundation.io.fiosdk.FIOSDK
import fiofoundation.io.fiosdk.implementations.FIONetworkProvider
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.RegisterFIOAddressRequest
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
        val private_key = "5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu"

        val serializationProvider = AbiFIOSerializationProvider()
        val signatureProvider = SoftKeySignatureProvider()
        signatureProvider.importKey(private_key)

        val fio_address = "shawnmullen123.brd"
        val fio_public_key = "FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o"
        val wallet_fio_address = "rewards:wallet"
        val max_fee = 300000000
        val actor = Utils.generateActor(fio_public_key)

        val provider = FIONetworkProvider("http://54.184.39.43:8889")
        //val request = RegisterFIOAddressRequest(fio_address, "", wallet_fio_address, max_fee, actor)

        var fioSdk:FIOSDK = FIOSDK.getInstance(private_key,fio_public_key,
            serializationProvider,signatureProvider)

        val request = fioSdk.registerFioAddress(fio_address,"",max_fee,wallet_fio_address)

        Log.i("REGFIOADDRESS","Request Data: " + request.actions[0].data)

        Log.i("REGFIOADDRESS","FIO Address: " + request.fioAddress)
        Log.i("REGFIOADDRESS","FIO Private: " + request.ownerPublicKey)


    }
}
