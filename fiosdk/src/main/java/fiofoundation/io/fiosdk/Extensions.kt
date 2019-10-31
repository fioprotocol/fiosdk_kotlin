package fiofoundation.io.fiosdk

fun ByteArray.toHexString():String
{
    val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }

    return result.toString()
}

fun String.hexStringToByteArray(returnUnsignedIntegers:Boolean=false) : ByteArray
{
    val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    val result = ByteArray(length / 2)

    for (i in 0 until length step 2) {
        val firstIndex = HEX_CHARS.indexOf(this[i])
        val secondIndex = HEX_CHARS.indexOf(this[i + 1])

        val octet = firstIndex.shl(4).or(secondIndex)
        result.set(i.shr(1), octet.toByte())
    }

    if(returnUnsignedIntegers)
        return result.filter { byte->byte.compareTo(-1)!=0 }.toByteArray()
    else
        return result
}

fun String.isFioAddress(): Boolean
{
    if(this.isNotEmpty())
    {
        if(this.length in 3..64)
        {
            val fioRegEx = Regex("^(?:(?=.{3,64}\$)[a-zA-Z0-9]{1}(?:(?!-{2,}))[a-zA-Z0-9-]*(?:(?<!-)):[a-zA-Z0-9]{1}(?:(?!-{2,}))[a-zA-Z0-9-]*(?:(?<!-))\$)",RegexOption.IGNORE_CASE)
            if(fioRegEx.matchEntire(this)!=null)
                return true
        }
    }

    return false
}

fun String.isFioDomain(): Boolean
{
    if(this.isNotEmpty())
    {
        if(this.length in 1..62)
        {
            val fioRegEx = Regex("^[a-zA-Z0-9\\\\-]+\$")
            if(fioRegEx.matchEntire(this)!=null)
                return true
        }
    }

    return false
}

fun String.isTokenCode(): Boolean
{
    if(this.isNotEmpty())
    {
        if(this.length in 1..10)
        {
            val fioRegEx = Regex("^[a-zA-Z0-9]+\$")
            if(fioRegEx.matchEntire(this)!=null)
                return true
        }
    }

    return false
}

fun String.isFioPublicKey(): Boolean
{
    if(this.isNotEmpty())
    {
        val fioRegEx = Regex("^FIO.+\$")
        if(fioRegEx.matchEntire(this)!=null)
            return true
    }

    return false
}

fun String.isNativeBlockChainPublicAddress(): Boolean
{
    if(this.isNotEmpty())
    {
        if(this.length in 1..128) {
            return true
        }
    }

    return false
}
