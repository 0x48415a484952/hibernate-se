package ir.sleepycat.cipher.cli;

import ir.sleepycat.cipher.crypto.AESDecryption;
import ir.sleepycat.cipher.crypto.AESEncryption;

import java.util.Scanner;

public class CommandLine {
    private static final Scanner scanner = new Scanner(System.in);

    public static void runCli(String[] args) {
        System.out.println("Choose an option:");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (choice == 1) {
            System.out.print("Enter the password: ");
            String password = scanner.nextLine();

            System.out.print("Enter the message: ");
            String originalText = scanner.nextLine();

            try {
                String encryptedText = AESEncryption.encrypt(originalText, password);
                System.out.println("Encrypted: " + encryptedText);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else if (choice == 2) {
            System.out.print("Enter the password: ");
            String password = scanner.nextLine();

            System.out.print("Enter the encrypted message: ");
            String encryptedText = scanner.nextLine();

            try {
                String decryptedText = AESDecryption.decrypt(encryptedText, password);
                System.out.println("Decrypted: " + decryptedText);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        scanner.close();
    }
}
