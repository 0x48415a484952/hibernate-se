package ir.sleepycat.cipher.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;


public class AESDecryption extends KeyDerivation {

    // Decrypt using AES with GCM mode
    public static String decrypt(String encryptedData, String password) throws Exception {
        byte[] combinedData = Base64.getDecoder().decode(encryptedData);

        byte[] salt = new byte[IV_LENGTH_BYTE];
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byte[] encryptedBytes = new byte[combinedData.length - (2 * IV_LENGTH_BYTE)];
        System.arraycopy(combinedData, 0, salt, 0, salt.length);
        System.arraycopy(combinedData, salt.length, iv, 0, iv.length);
        System.arraycopy(combinedData, salt.length + iv.length, encryptedBytes, 0, encryptedBytes.length);

        SecretKey secretKey = deriveKey(password, salt);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

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

