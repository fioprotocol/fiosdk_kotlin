package fiofoundation.io.fiosdk.utilities

import fiofoundation.io.fiosdk.models.Constants
import java.math.BigInteger

object SUFUtils {

    fun amountToSUF(amount:Double):BigInteger
    {
        return (Constants.multiplier.toDouble() * amount).toBigDecimal().toBigInteger()
    }

    fun fromSUFtoAmount(suf:BigInteger):Double
    {
        return suf.toBigDecimal().toDouble() / Constants.multiplier.toDouble()
    }

    fun fromSUFtoAmount(suf:String):Double
    {
        try {
            return suf.toBigDecimal().toDouble() / Constants.multiplier.toDouble()
        }
        catch(e:Error)
        {
            return 0.0
        }
    }
}