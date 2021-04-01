package it.polimi.db2.DB2_project;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    public static String SHA2(String s) throws NoSuchAlgorithmException {
        // Hashing of the password with SHA2 224
        MessageDigest sha224 = MessageDigest.getInstance("SHA-224");
        byte[] passBytes = s.getBytes();
        byte[] passHash = sha224.digest(passBytes);

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, passHash);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }
}
