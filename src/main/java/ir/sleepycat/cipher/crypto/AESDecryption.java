package ir.sleepycat.cipher.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class AESDecryption {
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    // Decrypt using AES with GCM mode
    public static String decrypt(String encryptedData, String password) throws Exception {
        byte[] combinedData = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        System.arraycopy(combinedData, 0, iv, 0, iv.length);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        SecretKeySpec secretKey = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] encryptedBytes = new byte[combinedData.length - IV_LENGTH_BYTE];
        System.arraycopy(combinedData, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedBytes.length);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the password: ");
        String password = scanner.nextLine();

        System.out.print("Enter the filename with encrypted data: ");
        String filename = scanner.nextLine();

        scanner.close();

        try {
            String encryptedData;
            try (Scanner fileScanner = new Scanner(new FileInputStream(filename))) {
                encryptedData = fileScanner.useDelimiter("\\A").next();
            }

            String decryptedText = decrypt(encryptedData, password);
            System.out.println("Decrypted: " + decryptedText);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

