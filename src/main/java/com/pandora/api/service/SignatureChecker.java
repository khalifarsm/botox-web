package com.pandora.api.service;

import com.pandora.api.exceptions.rest.UnauthorizedRestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import java.security.*;
import java.util.Formatter;

@Slf4j
public class SignatureChecker {

    @Autowired
    private HttpServletRequest request;
    @Value("${coin.payments.ipn.key}")
    private String key;

    @SneakyThrows
    @PostConstruct
    public void setup(){
    }

    @SneakyThrows
    public void check(String body) {
        String signatureGenerated = calculateHMAC(body, key);
        String signatureHeader = request.getHeader("HMAC");
        if (!MessageDigest.isEqual(signatureHeader.getBytes(),signatureGenerated.getBytes())) {
            throw new UnauthorizedRestException("signature is not valid");
        }
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public String calculateHMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        return toHexString(mac.doFinal(data.getBytes()));
    }
}
