package com.ptithcm2021.laptopshop.util;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class ValidPaymentSignature {
    public static boolean isValidMomoSignature(Map<String, String> request, String secretKey, String accessKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String signature  = request.get("signature");

        Map<String, Object> rawDataMap = new TreeMap<>(request);
        rawDataMap.remove("signature");
        rawDataMap.put("accessKey", accessKey);

        StringBuilder rawData = new StringBuilder();
        for (Map.Entry<String, Object> entry : rawDataMap.entrySet()){
            if (entry.getValue() != null) {
                rawData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        rawData.setLength(rawData.length()-1);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);

        byte[] hash = mac.doFinal(rawData.toString().getBytes());
        StringBuilder hashString = new StringBuilder();
        for( byte b: hash){
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString().equals(signature);
    }
}
