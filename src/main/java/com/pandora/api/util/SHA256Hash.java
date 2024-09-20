package com.pandora.api.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class SHA256Hash {

    public static String hash(String data) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Convert the input data to bytes and calculate the hash
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            
            // Convert the byte array to a hex string
            return HexFormat.of().formatHex(hashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
