package fiofoundation.io.fiosdk.utilities

import fiofoundation.io.fiosdk.enums.AlgorithmEmployed
import fiofoundation.io.fiosdk.formatters.FIOFormatter
import fiofoundation.io.fiosdk.toHexString
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECPrivateKeyParameters
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import org.bouncycastle.jce.ECNamedCurveTable

import java.math.BigInteger

object CryptoUtils
{
    fun generateSharedSecret(yourPrivateKey: String, othersFIOPublicKey: String): ByteArray
    {
        val publicKeyNoPrefix = othersFIOPublicKey.trimStart(FIOFormatter.PATTERN_STRING_FIO_PREFIX_EOS.toCharArray()::contains)

        val publicKeyBytes = FIOFormatter.decodePublicKey(publicKeyNoPrefix, "FIO")
        val privateKeyBytes = FIOFormatter.decodePrivateKey(yourPrivateKey, AlgorithmEmployed.SECP256K1)

        val spec = ECNamedCurveTable.getParameterSpec("secp256k1")
        val domain = ECDomainParameters(spec.curve, spec.g, spec.n, spec.h)
        val privateKey = ECPrivateKeyParameters(BigInteger(1, privateKeyBytes), domain)
        val publicKey = ECPublicKeyParameters(spec.curve.decodePoint(publicKeyBytes), domain)

        val agreement = ECDHBasicAgreement()

        agreement.init(privateKey)

        return HashUtils.sha512(agreement.calculateAgreement(publicKey).toByteArray().toHexString()).toByteArray()
    }
}