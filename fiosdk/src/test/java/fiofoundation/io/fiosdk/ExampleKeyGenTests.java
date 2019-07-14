package fiofoundation.io.fiosdk;

import android.provider.SyncStateContract;
import com.google.common.collect.ImmutableList;
import fiofoundation.io.fiosdk.enums.AlgorithmEmployed;
import fiofoundation.io.fiosdk.formatters.FIOFormatter;

import fiofoundation.io.fiosdk.models.PEMProcessor;
import fiofoundation.io.fiosdk.utilities.PrivateKeyUtils;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.crypto.*;
import org.bitcoinj.core.ECKey;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointUtil;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.*;

import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExampleKeyGenTests {

    @Test
    public void genKey()
    {
        try
        {
            // prepare (unused) random seed to save time when constructing coupon wallets for invoices

            List<String> mnemonic = getRandomSeedWords();

            DeterministicSeed couponRandomSeed = new DeterministicSeed(new SecureRandom().generateSeed(16), mnemonic, MnemonicCode.BIP39_STANDARDISATION_TIME_SECS);

            System.out.println(String.join(" ",mnemonic));

            List<ChildNumber> path_list = new ArrayList<>();
            path_list.add(new ChildNumber(44,true));
            path_list.add(new ChildNumber(235,true));
            path_list.add(new ChildNumber(0,true));
            path_list.add(new ChildNumber(0,false));


            DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(couponRandomSeed.getSeedBytes());

            //DeterministicKey rootKey = HDKeyDerivation.deriveChildKey(masterPrivateKey, new ChildNumber(44 | ChildNumber.HARDENED_BIT));
            DeterministicHierarchy root_hierarchy = new DeterministicHierarchy(masterPrivateKey);


            DeterministicKey fioKey = root_hierarchy.deriveChild(path_list, false,true,new ChildNumber(0,false));

//           DeterministicKey rootPrivateKey = masterPrivateKey.derive(44);
//            DeterministicKey fioKey = rootPrivateKey.derive(235);
//
            System.out.println(fioKey.getPathAsString());

            byte[] privateKeyBytes = fioKey.getPrivKeyBytes();
            byte[] resultWIFBytes = new byte[37];
            resultWIFBytes[0] = (byte)0x80;
            System.arraycopy(privateKeyBytes, (privateKeyBytes.length > 32) ? 1 : 0, resultWIFBytes, 1, 32);
            byte[] hash = Sha256Hash.hashTwice(resultWIFBytes, 0, 33);
            System.arraycopy(hash, 0, resultWIFBytes, 33, 4);
            //Base58.encode(resultWIFBytes);
            System.out.println(Base58.encode(resultWIFBytes));



        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void generatePrivateKey()
    {
        //String mn = "valley alien library bread worry brother bundle hammer loyal barely dune brave";//"ability sport fly alarm pool spin cupboard quarter laptop write comic torch";
        String mn = String.join(" ", getRandomSeedWords());

        try
        {
            String privateKey = FIOSDK.Static.createPrivateKey(mn);
            String publicKey = FIOSDK.Static.derivePublicKey(privateKey);

            System.out.println("FIO Private: " + privateKey);
            System.out.println("FIO Public: " + publicKey);

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }


    }

    private List<String> getRandomSeedWords() {
        List<String> seedWords;
        try {
            final MnemonicCode mnemonicCode = new MnemonicCode();

            seedWords = mnemonicCode.toMnemonic(new SecureRandom().generateSeed(16));
        } catch (IOException | MnemonicException.MnemonicLengthException e) {
            throw new RuntimeException(e);
        }
        return seedWords;
    }

}
