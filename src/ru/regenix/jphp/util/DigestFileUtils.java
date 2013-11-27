package ru.regenix.jphp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final public class DigestFileUtils {

    private DigestFileUtils(){}

    public static String convertToHashHex(byte messageDigest[]) {
        StringBuilder hexString = new StringBuilder();
        for(byte b : messageDigest){
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }

    public static String hash(File file, String algorithm) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        InputStream is = new FileInputStream(file);

        is = new DigestInputStream(is, md);
        byte digest[] = md.digest();
        is.close();

        return convertToHashHex(digest);
    }

    public static String hashMD5(File file) throws IOException {
        try {
            return hash(file, "MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashSHA256(File file) throws IOException {
        try {
            return hash(file, "SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
