package fiofoundation.io.fiosdk.utilities

import com.google.gson.GsonBuilder
import com.google.gson.Gson
import java.io.*


class Utils {
    companion object Static {
        @Throws(IOException::class, ClassNotFoundException::class)
        fun <T : Serializable> clone(`object`: T): T {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(`object`)  // Could clone only the Transaction (i.e. this.transaction)
            val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            return objectInputStream.readObject() as T
        }

        fun getGson(datePattern: String): Gson {
            return GsonBuilder()
                .setDateFormat(datePattern)
                .disableHtmlEscaping()
                .create()
        }
    }
}