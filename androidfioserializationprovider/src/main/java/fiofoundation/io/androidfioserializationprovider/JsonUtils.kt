package fiofoundation.io.androidfioserializationprovider

import org.json.JSONException
import org.json.JSONObject

object JsonUtils {

    fun jsonString(map: Map<String, Any>): String?
    {
        return try
        {
            val json = getJsonFromMap(map)
            json.toString()
        }
        catch (ex: JSONException) {
            null
        }

    }

    @Throws(JSONException::class)
    fun getJsonFromMap(map: Map<String, Any>): JSONObject
    {
        val jsonData = JSONObject()

        for (key in map.keys)
        {
            var value = map[key]
            if (value is Map<*, *>) {
                @Suppress("UNCHECKED_CAST")
                value = getJsonFromMap((value as Map<String, Any>?)!!)
            }

            jsonData.put(key, value)
        }

        return jsonData
    }
}