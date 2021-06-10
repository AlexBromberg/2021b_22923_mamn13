package mamn13.sem2021b.course22923.logic;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import static mamn13.sem2021b.course22923.logic.Utils.XOR;
import static mamn13.sem2021b.course22923.logic.Utils.getHash;


public class Crypto {

    private static final int LENGTH = 16;

    public static void processFile(String mode, String key, String pathToFile) throws IOException {
        switch (OPERATION.getOperation(mode)) {
            case DECRYPT:
                Crypto.decryptFile(key, pathToFile);
                break;
            case ENCRYPT:
                Crypto.encryptFile(key, pathToFile);
                break;
        }
    }

    public static String encryptString(String key, String plainText) throws IOException {
        byte[] keyBytes = getHash(key);
        return encryptString(keyBytes, plainText);
    }

    public static String decryptString(String key, String encryptedString) throws IOException {
        byte[] keyBytes = getHash(key);
        return decryptString(keyBytes, encryptedString);
    }

    public static String encryptString(byte[] keyBytes, String plainText) throws IOException {
        ByteArrayInputStream streamToEncrypt = new ByteArrayInputStream(plainText.getBytes(Charset.forName("UTF-8")));
        ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream();
        encryptStream(keyBytes, streamToEncrypt, encryptedStream);
        String encryptString = new String(Base64.getEncoder().encode(encryptedStream.toByteArray()), Charset.forName("UTF-8"));
        encryptedStream.close();
        return encryptString;
    }

    public static String decryptString(byte[] keyBytes, String encryptedString) throws IOException {
        ByteArrayInputStream encryptedStream = new ByteArrayInputStream(Base64.getDecoder().decode(encryptedString.getBytes(Charset.forName("UTF-8"))));
        ByteArrayOutputStream decryptedStream = new ByteArrayOutputStream();
        decryptStream(keyBytes, encryptedStream, decryptedStream);
        String decryptString = new String(decryptedStream.toByteArray(), Charset.forName("UTF-8"));
        decryptedStream.close();
        return decryptString;
    }

    public static void decryptFile(String key, String pathToFile) throws IOException {
        File file = new File(pathToFile);
        byte[] keyBytes = getHash(key);
        decryptFile(keyBytes, file);
    }

    public static void encryptFile(String key, String pathToFile) throws IOException {
        File file = new File(pathToFile);
        byte[] keyBytes = getHash(key);
        encryptFile(keyBytes, file);
    }

    public static void encryptFile(byte[] keyBytes, File fileToEncrypt) throws IOException {
        File outputFile = new File(fileToEncrypt.getAbsolutePath() + ".enc");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        InputStream streamToEncrypt = new FileInputStream(fileToEncrypt);
        encryptStream(keyBytes, streamToEncrypt, outputFile);
        streamToEncrypt.close();
        fileToEncrypt.delete();
    }


    public static void decryptFile(byte[] keyBytes, File fileToDecrypt) throws IOException {
        InputStream inputStream = new FileInputStream(fileToDecrypt);
        File outputFile = new File(fileToDecrypt.getAbsolutePath().split(".enc")[0]);
        decryptStream(keyBytes, inputStream, outputFile);
    }


    public static void encryptStream(byte[] keyBytes, InputStream streamToEncrypt, File outputFile) throws IOException {
        if (outputFile.exists()) {
            outputFile.delete();
        }
        OutputStream outputStream = new FileOutputStream(outputFile);
        encryptStream(keyBytes, streamToEncrypt, outputStream);
        outputStream.close();
    }

    public static void decryptStream(byte[] keyBytes, InputStream streamToDecrypt, File outputFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(outputFile);
        decryptStream(keyBytes, streamToDecrypt, outputStream);
        outputStream.close();
    }

    public static void encryptStream(String key, InputStream streamToEncrypt, OutputStream outputStream) throws IOException {
        encryptStream(getHash(key), streamToEncrypt, outputStream);
    }

    public static void decryptStream(String key, InputStream streamToDecrypt, OutputStream outputStream) throws IOException {
        decryptStream(getHash(key), streamToDecrypt, outputStream);
    }

    public static void encryptStream(byte[] keyBytes, InputStream streamToEncrypt, OutputStream outputStream) throws IOException {
        byte[] ivBytes = new byte[LENGTH];
        byte[] currentBytes = new byte[LENGTH];
        int numberOfBytes = 0;
        new Random().nextBytes(ivBytes);
        outputStream.write(ivBytes);
        do {
            numberOfBytes = streamToEncrypt.read(currentBytes);
            if (numberOfBytes > 0) {
                for (int i = 0; i < numberOfBytes; i++) {
                    ivBytes[i] = XOR(XOR(currentBytes[i], ivBytes[i]), keyBytes[i]);
                }
                outputStream.write(Arrays.copyOf(ivBytes, numberOfBytes));
            }
        } while (numberOfBytes == LENGTH);
        streamToEncrypt.close();
    }


    public static void decryptStream(byte[] keyBytes, InputStream streamToDecrypt, OutputStream outputStream) throws IOException {
        byte[] ivBytes = new byte[LENGTH];
        byte[] currentBytes = new byte[LENGTH];
        int numberOfBytes = 0;
        streamToDecrypt.read(ivBytes);
        byte[] nextIvBytes = Arrays.copyOf(ivBytes, LENGTH);
        do {
            numberOfBytes = streamToDecrypt.read(currentBytes);
            if (numberOfBytes > 0) {
                byte[] decryptedBytes = new byte[numberOfBytes];
                if (numberOfBytes == LENGTH) {
                    nextIvBytes = Arrays.copyOf(currentBytes, numberOfBytes);
                }
                for (int i = 0; i < numberOfBytes; i++) {
                    decryptedBytes[i] = XOR(XOR(currentBytes[i], keyBytes[i]), ivBytes[i]);
                }
                outputStream.write(Arrays.copyOf(decryptedBytes, numberOfBytes));
                if (numberOfBytes == LENGTH) {
                    ivBytes = Arrays.copyOf(nextIvBytes, LENGTH);
                }
            }
        } while (numberOfBytes == LENGTH);
        streamToDecrypt.close();
    }


}

