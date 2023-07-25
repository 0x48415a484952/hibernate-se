package ir.sleepycat.cipher.crypto;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;


public class KeyDerivation {
    protected static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    protected static final int TAG_LENGTH_BIT = 128;
    protected static final int IV_LENGTH_BYTE = 12;
    protected static final String ALGORITHM = "AES";
    protected static final int PBKDF2_ITERATIONS = 10000;
    protected static final int KEY_LENGTH_BIT = 256; // Use 256-bit AES key

    // Derive AES key from the password and salt using PBKDF2
    static SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH_BIT);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}
