import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Crypto {

    private static final Crypto instance = new Crypto();
    private String version = "1.0";

    //port
    public Port port;

    //private constructor
    private Crypto() {
        port = new Port();
    }

    // static method getInstance
    public static Crypto getInstance() {
        return instance;
    }

    public String getVersion() {
        return version;
    }

    private void doCrypto(int cipherMode, String key, File inputFile, File outputFile) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public class Port{

        public String version() {
            return getVersion();
        }

        public void encrypt(String key, File inputFile, File outputFile) {
            doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        }

        public void decrypt(String key, File inputFile, File outputFile) {
            doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        }
    }

}
