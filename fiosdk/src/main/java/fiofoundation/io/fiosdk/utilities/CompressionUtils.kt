package fiofoundation.io.fiosdk.utilities

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater


object CompressionUtils
{
    fun compress(data: UByteArray): UByteArray? {
        val deflater = Deflater()
        deflater.setInput(data.asByteArray())
        val outputStream = ByteArrayOutputStream(data.size)
        deflater.finish()
        val buffer = ByteArray(1024)
        while (!deflater.finished()) {
            val count: Int = deflater.deflate(buffer) // returns the generated code... index
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        val output: ByteArray = outputStream.toByteArray()
        deflater.end()
        return output.toUByteArray()
    }

    fun decompress(data: UByteArray): UByteArray? {
        val inflater = Inflater()
        inflater.setInput(data.asByteArray())
        val outputStream = ByteArrayOutputStream(data.size)
        val buffer = ByteArray(1024)
        while (!inflater.finished()) {
            val count: Int = inflater.inflate(buffer)
            outputStream.write(buffer, 0, count)
        }
        outputStream.close()
        val output: ByteArray = outputStream.toByteArray()
        inflater.end()
        return output.toUByteArray()
    }
}