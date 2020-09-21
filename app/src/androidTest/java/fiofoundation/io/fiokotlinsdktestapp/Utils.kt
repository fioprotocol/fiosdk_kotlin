package fiofoundation.io.fiokotlinsdktestapp

import android.content.Context
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.*


object Utils {
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
