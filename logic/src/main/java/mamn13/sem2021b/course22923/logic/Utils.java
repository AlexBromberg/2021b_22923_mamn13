package mamn13.sem2021b.course22923.logic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    static byte XOR(byte x, byte y) {
        return (byte) ((x | y) & (~x | ~y));
    }

    static byte[] getHash(String string) {
        byte[] bytes = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add string bytes to digest
            md.update(string.getBytes());
            //Get the hash's bytes
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
