package fiofoundation.io.fiokotlinsdktestapp

import android.content.Context
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.*


object Utils {
    /**
     * Loads values from non-tracked assets/local.properties
     *
     * Some unit tests interact with "real" FIO accounts via a server. The following are values
     * taken from revision 202dd1d225e12fdf0c8d31f2a40d17dc7666e80a and as of writing this do not
     * work but serve as reference to what you have to generate to run these tests. Store the chosen
     * values in app/src/androidTest/assets/local.properties in the following form:
baseUrl=http://dev2.fio.dev:8889/v1/
baseMockUrl=http://mock.dapix.io/mockd/DEV2/
whalePrivateKey=5KF2B21xT5pE5G3LNA6LKJc6AP2pAd2EnfpAUrJH12SFV8NtvCD
whalePublicKey=FIO6zwqqzHQcqCc2MB4jpp1F73MXpisEQe2SDghQFSGQKoAPjvQ3H

alicePrivateKey=5Je5uqXQ86rymhCPhjwpAUTR2dngJnmwKQduAKrKqmRtBTYG4sE
alicePublicKey=FIO6J1uka8y14eoZZ9hWtDGVMRUjXQXqDpqbBMzs6yRMyazx9eAku
bobPrivateKey=5Hpw3ccf8igGgtuWRoaEwGU7DhCbdzmvdEK83NrcCUCvjY4EhNz
bobPublicKey=FIO8kYn3qRD8UBJLsBwT3XBqgTwk6sdg9DT6YSJ4iaQ5Eyy6rwaKH
aliceFioAddress=alicetest61@fiotestnet
bobFioAddress=bobtest61@fiotestnet
     */
    fun getLocalProperty(key: String, context: Context): String {
        try {
            val properties = Properties()
            val inputStream = context.assets.open("local.properties")
            properties.load(inputStream)
            return properties.getProperty(key)
        } catch (e: FileNotFoundException) {
            throw Exception("For this unit test you need to define some values in app/src/androidTest/assets/local.properties!")
        } catch (e: IllegalStateException) {
            throw Exception("Did you define a value for $key in your assets/local.properties?")
        }
    }
}
