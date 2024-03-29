package com.example.lab8.utils.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordEncoder {
    private static final String ALGORITHM = "SHA-256";

    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for(byte b : bytes){
            hexStringBuilder.append(String.format("%02x",b));
        }
        return hexStringBuilder.toString();
    }

    public static String hashPassword(String password, String salt){
        String passwordWithSalt = password + salt;
        try{
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(passwordWithSalt.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("error hashing password",e);
        }
    }

    public static boolean verifyPassword(String enteredPassword, String hassedPassword, String salt){
        String enteredPasswordHashed = hashPassword(enteredPassword,salt);
        return enteredPasswordHashed.equals(hassedPassword);
    }
}
