package com.example.demo.Util;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MD5Util {
    private static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}
