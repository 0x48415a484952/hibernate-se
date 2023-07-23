package ir.sleepycat.cipher.encryption;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class AESEncryption {
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    // Encrypt using AES with GCM mode
    public static String encrypt(String data, String password) throws Exception {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        SecretKey secretKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Combine IV and encrypted data for storage or transmission
        byte[] combinedData = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedData, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedData, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combinedData);
    }

    // Decrypt using AES with GCM mode
    public static String decrypt(String encryptedData, String password) throws Exception {
        byte[] combinedData = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        System.arraycopy(combinedData, 0, iv, 0, iv.length);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        SecretKey secretKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] encryptedBytes = new byte[combinedData.length - IV_LENGTH_BYTE];
        System.arraycopy(combinedData, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedBytes.length);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        String originalText = "Hello, this is a secret message!";
        String password = "MySecretPassword";

        String encryptedText = encrypt(originalText, password);
        log.info("Encrypted: " + encryptedText);
        log.info(Arrays.toString(encryptedText.getBytes()));

        String decryptedText = decrypt(encryptedText, password);
        log.info("Decrypted: " + decryptedText);
    }
}

