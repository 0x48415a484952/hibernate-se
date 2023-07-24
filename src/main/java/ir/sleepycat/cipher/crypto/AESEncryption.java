package ir.sleepycat.cipher.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

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

        SecretKeySpec secretKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Combine IV and encrypted data for storage or transmission
        byte[] combinedData = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedData, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedData, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combinedData);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the password: ");
        String password = scanner.nextLine();

        System.out.print("Enter the message: ");
        String originalText = scanner.nextLine();

        System.out.print("Enter the filename to save the encrypted data: ");
        String filename = scanner.nextLine();

        scanner.close();

        try {
            String encryptedText = encrypt(originalText, password);
            System.out.println("Encrypted: " + encryptedText);

            try (FileOutputStream fos = new FileOutputStream(filename)) {
                fos.write(encryptedText.getBytes(StandardCharsets.UTF_8));
                System.out.println("Encrypted data written to the file: " + filename);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}



