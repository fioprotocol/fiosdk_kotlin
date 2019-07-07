package fiofoundation.io.androidfiosoftkeysignatureprovider;

import fiofoundation.io.androidfiosoftkeysignatureprovider.errors.ImportKeyError;
import fiofoundation.io.fiosdk.errors.signatureprovider.GetAvailableKeysError;
import fiofoundation.io.fiosdk.errors.signatureprovider.SignTransactionError;
import fiofoundation.io.fiosdk.models.signatureprovider.FIOTransactionSignatureRequest;
import fiofoundation.io.fiosdk.models.signatureprovider.FIOTransactionSignatureResponse;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void importKeyR1Test() {
        String privateKeyEOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyEOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        SoftKeySignatureProvider provider = new SoftKeySignatureProvider();

        try {
            provider.importKey(privateKeyEOS);
            List<String> keys = provider.getAvailableKeys();
            assertEquals(1, keys.size());
            assertEquals(publicKeyEOS, keys.get(0));
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!!");
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void importKeyK1Test() {
        String privateKeyEOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String publicKeyEOSLegacy = "FIO8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMiGxPbF";
        String publicKeyEOS = "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB";
        SoftKeySignatureProvider provider = new SoftKeySignatureProvider();

        try {
            provider.importKey(privateKeyEOS);
            List<String> keys = provider.getAvailableKeys();
            assertEquals(2, keys.size());
            assertTrue(keys.contains(publicKeyEOS));
            assertTrue(keys.contains(publicKeyEOSLegacy));
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!!");
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void getAvailableKeyTest() {
        String privateKeyK1EOS = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";
        String publicKeyK1EOS = "PUB_K1_8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMn1uFhB";
        String publicKeyK1EOSLegacy = "FIO8CbY5PhQZGF2gzPKRBaNG4YzB4AwpmfnDcVZMSPZTqQMiGxPbF";

        SoftKeySignatureProvider provider = new SoftKeySignatureProvider();

        try {
            provider.importKey(privateKeyK1EOS);
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not throw error!!!");
        }

        try {
            List<String> keys = provider.getAvailableKeys();
            assertEquals(3, keys.size());
            assertTrue(keys.contains(publicKeyK1EOS));
            assertTrue(keys.contains(publicKeyK1EOSLegacy));
            assertTrue(keys.contains(publicKeyR1EOS));
        } catch (GetAvailableKeysError getAvailableKeysError) {
            getAvailableKeysError.printStackTrace();
            fail("Should not throw error!!!");
        }
    }

    @Test
    public void signTransactionTest() {
        String privateKeyR1EOS = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String publicKeyR1EOS = "PUB_R1_71AYFp3Aasa2od6bwmXEQ13MMfqv4wuJwCRx1Z1dbRifrQEqZt";

        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        List<String> publicKeys = Collections.singletonList(publicKeyR1EOS);
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        FIOTransactionSignatureRequest request = new FIOTransactionSignatureRequest(serializedTransaction, publicKeys, chainId, null, false);
        SoftKeySignatureProvider provider = new SoftKeySignatureProvider();

        try {
            provider.importKey(privateKeyR1EOS);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            fail("Should not fail here!!!");
        }

        try {
            FIOTransactionSignatureResponse response = provider.signTransaction(request);
            assertNotNull(response);
            assertEquals(serializedTransaction, response.getSerializedTransaction());
            assertEquals(1, request.getSigningPublicKeys().size());
            assertTrue(response.getSignatures().get(0).contains("SIG_R1_"));

            System.out.println(response.getSignatures().get(0));
        } catch (SignTransactionError signTransactionError) {
            signTransactionError.printStackTrace();
            fail("Should not fail here!!!");
        }
    }
}