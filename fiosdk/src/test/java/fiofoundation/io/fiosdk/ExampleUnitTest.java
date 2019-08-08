package fiofoundation.io.fiosdk;

import fiofoundation.io.fiosdk.errors.fionetworkprovider.*;

import fiofoundation.io.fiosdk.models.Cryptography;
import fiofoundation.io.fiosdk.models.fionetworkprovider.Authorization;
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.FIONameAvailabilityCheckRequest;
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.FIONameAvailabilityCheckResponse;
import fiofoundation.io.fiosdk.utilities.CryptoUtils;
import fiofoundation.io.fiosdk.utilities.HashUtils;
import fiofoundation.io.fiosdk.utilities.Utils;
import fiofoundation.io.fiosdk.ExtensionsKt;

import org.junit.Test;

import fiofoundation.io.fiosdk.implementations.FIONetworkProvider;
import fiofoundation.io.fiosdk.models.fionetworkprovider.response.*;
import fiofoundation.io.fiosdk.models.fionetworkprovider.request.*;

import fiofoundation.io.fiosdk.models.signatureprovider.*;

import fiofoundation.io.fiosdk.enums.*;
import fiofoundation.io.fiosdk.errors.*;
import fiofoundation.io.fiosdk.formatters.*;
import fiofoundation.io.fiosdk.errors.formatters.*;

import fiofoundation.io.fiosdk.models.PEMProcessor;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    String baseUrl = "http://54.184.39.43:8889/v1/";
    String baseMockUrl = "http://mock.dapix.io/mockd/DEV4/";

    @Test
    public void testFIONames() {
        try
        {//shawnmullen123.brd
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetFIONamesRequest request = new GetFIONamesRequest("FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82");
            GetFIONamesResponse response = provider.getFIONames(request);

            System.out.println(response.getFioDomains());
            System.out.println(response.getFioAddresses());
            System.out.println(response.toJson());

            System.out.println(response.getFioAddresses().get(0).getFioAddress());
        }
        catch (GetFIONamesError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    @Test
    public void testGetPubAddressLookUp() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetPublicAddressRequest request = new GetPublicAddressRequest("shawnmullen123:brd","FIO");
            GetPublicAddressResponse response = provider.getPublicAddress(request);

            System.out.println(response.getPublicAddress());
            System.out.println(response.toJson());
        }
        catch (GetPublicAddressError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testIsFIONameAvailable() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            FIONameAvailabilityCheckRequest request = new FIONameAvailabilityCheckRequest("pawel78261.woohoo713841");
            FIONameAvailabilityCheckResponse response = provider.isFIONameAvailable(request);

            System.out.println(response.isAvailable());

            System.out.println(response.toJson());
        }
        catch (FIONameAvailabilityCheckError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetFIOBalance() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetFIOBalanceRequest request = new GetFIOBalanceRequest("FIO5oBUYbtGTxMS66pPkjC2p8pbA3zCtc8XD4dq9fMut867GRdh82");
            GetFIOBalanceResponse response = provider.getFIOBalance(request);

            System.out.println(response.getBalance());
            System.out.println(response.toJson());
        }
        catch (GetFIOBalanceError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetFee() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetFeeRequest request = new GetFeeRequest("add_pub_address","pawel78261.woohoo713841");

            System.out.println(request.toJson());

            GetFeeResponse response = provider.getFee(request);

            System.out.println(response.getFee());
            System.out.println(response.toJson());
        }
        catch (GetFeeError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetInfo() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetInfoResponse response = provider.getInfo();

            System.out.println(response.toJson());
        }
        catch (GetInfoError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetBlock() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetBlockRequest request = new GetBlockRequest("1381533");
            GetBlockResponse response = provider.getBlock(request);

            //System.out.println(response.get);
            System.out.println(response.toJson());
        }
        catch (GetBlockError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetRawAbi() {
        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetRawAbiRequest request = new GetRawAbiRequest("fio.token");
            GetRawAbiResponse response = provider.getRawAbi(request);

            //System.out.println(response.get);
            System.out.println(response.toJson());
        }
        catch (GetRawAbiError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testAbi(){
        BinaryAbi bAbi = new BinaryAbi("fio.system","asdfkhasd2lrkjhas;dlfkj3");

        System.out.println(bAbi.getAccountName());
        System.out.println(bAbi.getAbi());

        System.out.println(AlgorithmEmployed.SECP256K1.name().toLowerCase());


    }

    @Test
    public void validatePEMCreationOfSecp256r1PrivateKey()
    {
        String eosFormattedPrivateKey = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String pemFormattedPrivateKey = "-----BEGIN EC PRIVATE KEY-----\n"
                + "MDECAQEEIFjJPuD5efj0AdOolGUxlte5szjCItDfSLDtWjJio4AroAoGCCqGSM49AwEH\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            assertEquals(pemFormattedPrivateKey,
                    FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(eosFormattedPrivateKey));
        } catch (FIOFormatterError e) {
            System.out.println("I failed!");
            fail("Not expecting an EOSFormatterError to be thrown!");
        }
    }

    @Test
    public void validateFIOCreationOfSecp256r1PrivateKey() {
        String eosFormattedPrivateKey = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String pemFormattedPrivateKey = "-----BEGIN EC PRIVATE KEY-----\n"
                + "MDECAQEEIFjJPuD5efj0AdOolGUxlte5szjCItDfSLDtWjJio4AroAoGCCqGSM49AwEH\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            assertEquals(eosFormattedPrivateKey,
                    FIOFormatter.Static.convertPEMFormattedPrivateKeyToFIOFormat(pemFormattedPrivateKey));
        } catch (FIOFormatterError e) {
            fail("Not expecting an EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validateFIOtoPEMtoEOSCreationOfSecp256r1PrivateKey() {
        String eosFormattedPrivateKey = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String pemFormattedPrivateKey = "-----BEGIN EC PRIVATE KEY-----\n"
                + "MDECAQEEIFjJPuD5efj0AdOolGUxlte5szjCItDfSLDtWjJio4AroAoGCCqGSM49AwEH\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            String eosToPem = FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(eosFormattedPrivateKey);
            assertEquals(pemFormattedPrivateKey,eosToPem);
            assertEquals(eosFormattedPrivateKey,FIOFormatter.Static.convertPEMFormattedPrivateKeyToFIOFormat(eosToPem));
        } catch (FIOFormatterError e) {
            System.out.println(e.getMessage());
            fail("Not expecting an EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validateExceptionWhenPEMFormatOfSecp256r1PrivateKeyIsInvalidWrongHeader() {
        String eosFormattedPrivateKey = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String pemFormattedPrivateKey = "-----BEGIN PUBLIC KEY-----\n"
                + "MDECAQEEIFjJPuD5efj0AdOolGUxlte5szjCItDfSLDtWjJio4AroAoGCCqGSM49AwEH\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            assertEquals(eosFormattedPrivateKey,
                    FIOFormatter.Static.convertPEMFormattedPrivateKeyToFIOFormat(pemFormattedPrivateKey));
            fail("Expected EOSFormatterError to be thrown!");
        } catch (FIOFormatterError e) {
            System.out.println(e.getMessage());
            assert(e instanceof FIOFormatterError);
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail("Expected EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validateExceptionWhenPEMFormatOfSecp256r1PrivateKeyIsInvalidNoHeader() {
        String eosFormattedPrivateKey = "PVT_R1_g6vV9tiGqN3LkhD53pVUbxDn76PuVeR6XfmJzrnLR3PbGWLys";
        String pemFormattedPrivateKey = "MDECAQEEIFjJPuD5efj0AdOolGUxlte5szjCItDfSLDtWjJio4AroAoGCCqGSM49AwEH\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            assertEquals(eosFormattedPrivateKey,
                    FIOFormatter.Static.convertPEMFormattedPrivateKeyToFIOFormat(pemFormattedPrivateKey));
            fail("Expected EOSFormatterError to be thrown!");
        } catch (FIOFormatterError e) {
            System.out.println(e.getMessage());
            assert(e instanceof FIOFormatterError);
        }catch (Exception e){
            System.out.println(e.getMessage());
            fail("Expected EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validatePEMCreationOfSecp256k1PrivateKey() {
        String eosFormattedPrivateKey = "5JKVeYzRs42DpnHU1rUeJHPZyXb1pCdhyayx7FD2qKHV63F71zU";
        String pemFormattedPrivateKey = "-----BEGIN EC PRIVATE KEY-----\n"
                + "MC4CAQEEIEJSCKmyR0kmxy2pgkEwkqrodn2jG9mhXRhhxgsneuBsoAcGBSuBBAAK\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            assertEquals(pemFormattedPrivateKey,
                    FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(eosFormattedPrivateKey));
        } catch (FIOFormatterError e) {
            System.out.println(e.getMessage());
            fail("Not expecting an EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validatePEMCreationOfSecp256r1PublicKey() {
        String eosFormattedPublicKey = "PUB_R1_5AvUuRssyb7Z2HgNHVofX5heUV5dk8Gni1BGNMzMRCGbhdhBbu";
        String pemFormattedPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MDkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDIgACJVBOXmBTBSUedKnkv11sD8ZBHVmJN3aCJEk+5aArDhY=\n" +
                "-----END PUBLIC KEY-----";

        try {
            assertEquals(pemFormattedPublicKey,
                    FIOFormatter.Static.convertFIOPublicKeyToPEMFormat(eosFormattedPublicKey));
        } catch (FIOFormatterError e) {
            fail("Not expecting an EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validatePrepareSerializedTransactionForSigning() {
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        String serializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        String expectedSignableTransaction = chainId + serializedTransaction + Hex.toHexString(new byte[32]);

        try {
            String signableTransaction = FIOFormatter.Static.prepareSerializedTransactionForSigning(serializedTransaction, chainId);
            assertEquals(expectedSignableTransaction, signableTransaction);
        } catch (FIOFormatterError eosFormatterError) {
            System.out.println(eosFormatterError.getMessage());
            eosFormatterError.printStackTrace();
            fail("Should not throw exception here");
        }
    }

    @Test
    public void validateExtractSerializedTransactionFromSignable() {
        String chainId = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17";
        String expectedSerializedTransaction = "8BC2A35CF56E6CC25F7F000000000100A6823403EA3055000000572D3CCDCD01000000000000C03400000000A8ED32322A000000000000C034000000000000A682A08601000000000004454F530000000009536F6D657468696E6700";
        String signableTransaction = chainId + expectedSerializedTransaction + Hex.toHexString(new byte[32]);

        try {
            String serializedTransaction = FIOFormatter.Static.extractSerializedTransactionFromSignable(signableTransaction);
            assertEquals(expectedSerializedTransaction, serializedTransaction);
        } catch (FIOFormatterError eosFormatterError) {
            eosFormatterError.printStackTrace();
            fail("Should not throw exception here");
        }
    }

    @Test
    public void validatePrepareSerializedTransactionForSigningthenThrowErrorLengthInput() {
        String chainId = "687fa513e18843ad3e820744f4ffcf9";
        String serializedTransaction = "8";

        try {
            FIOFormatter.Static.prepareSerializedTransactionForSigning(serializedTransaction, chainId);
            fail("Expected EOSFormatterError to be thrown!");
        } catch (FIOFormatterError eosFormatterError) {
            System.out.println(eosFormatterError.getMessage());
            System.out.println(String.format(ErrorConstants.INVALID_INPUT_SIGNABLE_TRANS_LENGTH_EXTRACT_SERIALIZIED_TRANS_FROM_SIGNABLE, 129));
            assertEquals(String.format(ErrorConstants.INVALID_INPUT_SIGNABLE_TRANS_LENGTH_EXTRACT_SERIALIZIED_TRANS_FROM_SIGNABLE, 129), eosFormatterError.getMessage());
        }
    }

    @Test
    public void validateEOSSignatureCreationWithSECP256R1GeneratedPublicKey() {
        String publicKey = "PUB_R1_6Aze12hAmj1qWeXpdxsbMMP29NZ7EJhnuNJmDoBgx9xjmyZ8n8";
        String signableTransaction = "687fa513e18843ad3e820744f4ffcf93b1354036d80737db8dc444fe4b15ad17528cab5c770a54cebec1000000000100a6823403ea3055000000572d3ccdcd01000000000000c03400000000a8ed323236000000000000c034000000000000a682102700000000000004454f530000000015426f6e757320666f7220676f6f64206a6f62212121000000000000000000000000000000000000000000000000000000000000000000";
        String derEncodedSignature = "304502202b180ef7236a62ff1e3fd741c3d5ba00cf3d3114a7e038a0730a2f45d1551219022100da335c840a4f42c051c12fed3d7d012bb083c7150c0eb691cac9ad9e898a75f3";
        String eosFormattedSignature = "SIG_R1_KaPKLBn1FnnYDf4E5zmnj7qQWWcN5REJFnadzLUyDp7TEVMAmD1CT15SyGmwdreoYTWSbJzWXayPdsHwLySWviiJoA7W4p";

        try {
            String pemPublicKey = FIOFormatter.Static.convertFIOPublicKeyToPEMFormat(publicKey);
            String conversionResults = FIOFormatter.Static.convertDERSignatureToFIOFormat(Hex.decode(derEncodedSignature),Hex.decode(signableTransaction), pemPublicKey );

            System.out.println(conversionResults);
            System.out.println(eosFormattedSignature);
            assertEquals(eosFormattedSignature, conversionResults);
        } catch (FIOFormatterError e) {
            System.out.println(e.getMessage());
            fail("Not expecting an EOSFormatterError to be thrown!");
        }

    }

    @Test
    public void validatePublicKeyExtractionFromPrivateKey(){
        //Positive key values
        String privateKey1 = "PVT_R1_GrfEfbv5at9kbeHcGagQmvbFLdm6jqEpgE1wsGbrfbZNjpVgT";
        String publicKey1 = "PUB_R1_4ztaVy8L9zbmzTdpfq5GcaFYwGwXTNmN3qW7qcgHMmfUZhpzQQ";
        String privateKey2 = "PVT_R1_wCpPsaY9o8NU9ZsuwaYVQUDkCfj1aWJZGVcmMM6XyYHJVqvqp";
        String publicKey2 = "PUB_R1_5xawnnr3mWayv2wkiqBGWqu4RQLNJffLSXHiL3BofdY7ortMy4";
        //Negative key values
        String privateKey3 = "PVT_R1_2sXhBwN8hCLSWRxxfZg6hqwGymKSudtQ7Qa5wUWyuW54E1Gd7P";
        String publicKey3 = "PUB_R1_6UYnNnXv2CutCtTLgCQxJbHBeWDG3JZaSQJK9tQ7K3JUdzXw9p";
        String privateKey4 = "PVT_R1_2fJmPgaik4rUeU1NDchQjnSPkQkga4iKzdK5hhdbKf2PQFJ57t";
        String publicKey4 = "PUB_R1_5MVdX3uzs6qDHUYpdSksZFc5rAu5P4ba6MDaySuYyzQqmCw96Q";
        String privateKey5 = "PVT_R1_2FBMJryipxmAeiwFYXvBTRhX1y5tdepDYBjCm4VqBWcsmdy1xD";
        String publicKey5 = "PUB_R1_5qjeAbU6mUM4PLRQBw8V4kxuc5pAjnJFpcMrdZmHF6L6uH57dk";
        String privateKey6 = "PVT_R1_2tjkXAnQPi5Jte8H5SihUQDRnJDPTny5hoiWxxeKm7uC1osiet";
        String publicKey6 = "PUB_R1_5BpFt4f1PXzvU2SVmwZdtCiFWbwDRHPzh8Fiao8PCd1R17pH5S";

        String privateKey7 = "5Kbb37EAqQgZ9vWUHoPiC2uXYhyGSFNbL6oiDp24Ea1ADxV1qnu";//"5KDQzVMaD1iUdYDrA2PNK3qEP7zNbUf8D41ZVKqGzZ117PdM5Ap";//"5HwWmL5SQDNcJQyPVBs1BeHdcfVUigwGNHaocG2Baj6WmARNbts"; //"5KDQzVMaD1iUdYDrA2PNK3qEP7zNbUf8D41ZVKqGzZ117PdM5Ap";
        //String publicKey7 = "EOS6D6gSipBmP1KW9SMB5r4ELjooaogFt77gEs25V9TU9FrxKVeFb";//"EOS5C8aKVehYy8scZ7zrru3nGp6e9r3W8VL2B3K2C7qnu2d8zJc5P"; //"EOS6D6gSipBmP1KW9SMB5r4ELjooaogFt77gEs25V9TU9FrxKVeFb";

        try {
            PEMProcessor pemProcessor1 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey1));
            assertEquals(publicKey1, pemProcessor1.extractFIOPublicKeyFromPrivateKey(false));
            System.out.println(pemProcessor1.extractFIOPublicKeyFromPrivateKey(false));

            PEMProcessor pemProcessor2 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey2));
            assertEquals(publicKey2, pemProcessor2.extractFIOPublicKeyFromPrivateKey(false));
            System.out.println(pemProcessor2.extractFIOPublicKeyFromPrivateKey(false));

            PEMProcessor pemProcessor3 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey3));
            assertEquals(publicKey3, pemProcessor3.extractFIOPublicKeyFromPrivateKey(false));
            System.out.println(pemProcessor3.extractFIOPublicKeyFromPrivateKey(false));

            PEMProcessor pemProcessor4 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey4));
            assertEquals(publicKey4, pemProcessor4.extractFIOPublicKeyFromPrivateKey(false));
            System.out.println(pemProcessor4.extractFIOPublicKeyFromPrivateKey(false));

            PEMProcessor pemProcessor5 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey5));
            assertEquals(publicKey5, pemProcessor5.extractFIOPublicKeyFromPrivateKey(false));
            System.out.println(pemProcessor5.extractFIOPublicKeyFromPrivateKey(false));

            PEMProcessor pemProcessor6 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey6));
            assertEquals(publicKey6, pemProcessor6.extractFIOPublicKeyFromPrivateKey(false));
            System.out.println(pemProcessor6.extractFIOPublicKeyFromPrivateKey(false));

            PEMProcessor pemProcessor7 = new PEMProcessor(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey7));
            //assertEquals(publicKey7, pemProcessor7.extractFIOPublicKeyFromPrivateKey(true));
            System.out.println(pemProcessor7.extractFIOPublicKeyFromPrivateKey(true));
            //System.out.println(FIOFormatter.Static.convertFIOPrivateKeyToPEMFormat(privateKey7));

        } catch (PEMProcessorError e) {
            fail("Not expecting a PEMProcessorError to be thrown!");
        } catch (Exception e){
            fail("Not expecting an Exception to be thrown!");
        }

    }

    @Test
    public void validatePEMProcessorMethodsWorkWithPEMFormattedSECP256K1PrivateKey(){
        String keyType = "EC PRIVATE KEY";
        AlgorithmEmployed algorithmEmployed = AlgorithmEmployed.SECP256K1;
        String derFormat = "302e0201010420425208a9b2474926c72da982413092aae8767da31bd9a15d1861c60b277ae06ca00706052b8104000a";
        String keyData = "425208a9b2474926c72da982413092aae8767da31bd9a15d1861c60b277ae06c";

        String pemFormattedPrivateKey = "-----BEGIN EC PRIVATE KEY-----\n"
                + "MC4CAQEEIEJSCKmyR0kmxy2pgkEwkqrodn2jG9mhXRhhxgsneuBsoAcGBSuBBAAK\n"
                + "-----END EC PRIVATE KEY-----";

        try {
            PEMProcessor pemProcessor = new PEMProcessor(pemFormattedPrivateKey);
            assertEquals(keyType, pemProcessor.getType());
            assertEquals(algorithmEmployed, pemProcessor.getAlgorithm());
            assertEquals(derFormat, pemProcessor.getDERFormat());
            assertEquals(keyData, Hex.toHexString(pemProcessor.getKeyData()));

            System.out.println(pemProcessor.getType());
            System.out.println(pemProcessor.getAlgorithm());
            System.out.println(pemProcessor.getDERFormat());
            System.out.println(Hex.toHexString(pemProcessor.getKeyData()));

        } catch (PEMProcessorError e) {
            fail("Not expecting an PEMProcessorError to be thrown!");
        }

    }

    @Test
    public void testGenerateActor()
    {
        String p_key = "FIO6D6gSipBmP1KW9SMB5r4ELjooaogFt77gEs25V9TU9FrxKVeFb";
        System.out.println(new Authorization(p_key,"action").getActor());

    }

    @Test
    public void testRegisterFioAddress()
    {

        String fio_address = "shawnmullen123.brd";
        String fio_public_key = "FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o";
        String wallet_fio_address = "rewards:wallet";
        int max_fee = 300000000;
        String actor = Utils.Static.generateActor(fio_public_key);

//        FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
//        RegisterFIOAddressRequest request = new RegisterFIOAddressRequest(fio_address,"",wallet_fio_address,max_fee,actor);
//
//        System.out.println("Actor Public Key: " + request.getActorPublicKey());
//        System.out.println("FIO Address: " + request.getFioAddress());
//        System.out.println("FIO Private: " + request.getOwnerPublicKey());
    }

    @Test
    public void testRegisterFioNameForUser()
    {

        //pvt key: 5KiGdMtgmjeMQPKeo9nbiEpA4Vu3Q91spGUCqHN3Gk8MAdWFYVP
        //pblk key: FIO87MK3VsNmCjSTtscRKBnEwzbNYsCnGaUWdFgGuCLCV3tVW4Wai

        String fio_name = "shawnmullen223:brd";
        String fio_public_key = "FIO87MK3VsNmCjSTtscRKBnEwzbNYsCnGaUWdFgGuCLCV3tVW4Wai";

        FIONetworkProvider provider = new FIONetworkProvider(baseUrl,baseMockUrl);
        RegisterFIONameForUserRequest request = new RegisterFIONameForUserRequest(fio_name,fio_public_key);

        try
        {
            RegisterFIONameForUserResponse response= provider.registerFioNameOnBehalfOfUser(request);

            System.out.println("Status: " + response.getStatus());
        }
        catch(RegisterFIONameForUserError e)
        {
            System.out.println(e.getResponseError().getMessage());
        }


    }

    @Test
    public void testGenerateSharedSecret()
    {
        //alice-public: FIO7zsqi7QUAjTAdyynd6DVe8uv4K8gCTRHnAoMN9w9CA1xLCTDVv
        String alicePrivate = "5J9bWm2ThenDm3tjvmUgHtWCVMUdjRR1pxnRtnJjvKA4b2ut5WK";

        String bobPublic = "FIO5VE6Dgy9FUmd1mFotXwF88HkQN1KysCWLPqpVnDMjRvGRi1YrM";
        //bob-private: 5JoQtsKQuH8hC9MyvfJAqo6qmKLm8ePYNucs7tPu2YxG12trzBt

        String pubKey_str = "FIO5kJKNHwctcfUM5XZyiWSqSTM5HTzznJP9F3ZdbhaQAHEVq575o";
        String privKey = "5JLxoeRoMDGBbkLdXJjxuh3zHsSS7Lg6Ak9Ft8v8sSdYPkFuABF";

        try
        {

            String message = new String("secret message".getBytes(), UTF_8);
            byte[] sharedSecret = CryptoUtils.INSTANCE.generateSharedSecret(alicePrivate,bobPublic);

            String sharedSecretHex = ExtensionsKt.toHexString(sharedSecret);

            Cryptography crypt = new Cryptography(sharedSecret,null);

            byte[] encResults = crypt.encrypt(message);

            byte[] decResults = crypt.decrypt(encResults);
            String decResultsAsString = crypt.decryptAsString(encResults);

            String ivHex = crypt.getIVasHex();

            System.out.println("DEC STR1: " + decResultsAsString);
            System.out.println("IV: " + ivHex);

            Cryptography crypt2 = new Cryptography(sharedSecret,ExtensionsKt.hexStringToByteArray(ivHex,false));

            byte[] ReEncResults = crypt.encrypt(message);

            String stop = "";


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetPendingFioRequests()
    {
        String fio_public_key = "FIO87MK3VsNmCjSTtscRKBnEwzbNYsCnGaUWdFgGuCLCV3tVW4Wai";

        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetPendingFIORequestsRequest request = new GetPendingFIORequestsRequest(fio_public_key);

            System.out.println(request.toJson());

            GetPendingFIORequestsResponse response = provider.getPendingFIORequests(request);

            System.out.println(response.getRequests());
            System.out.println(response.toJson());
        }
        catch (GetPendingFIORequestsError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void testGetSentFioRequests()
    {
        String fio_public_key = "FIO87MK3VsNmCjSTtscRKBnEwzbNYsCnGaUWdFgGuCLCV3tVW4Wai";

        try{
            FIONetworkProvider provider = new FIONetworkProvider(baseUrl);
            GetSentFIORequestsRequest request = new GetSentFIORequestsRequest(fio_public_key);

            System.out.println(request.toJson());

            GetSentFIORequestsResponse response = provider.getSentFIORequests(request);

            System.out.println(response.getRequests());
            System.out.println(response.toJson());
        }
        catch (GetSentFIORequestsError e)
        {
            System.out.println(e.getResponseError());
            System.out.println(e.toJson());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

}