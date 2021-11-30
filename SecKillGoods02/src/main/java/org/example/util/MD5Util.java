package org.example.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private  static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass){
        String str = ""+ salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        //System.out.println(str);
        return md5(str);
    }

    //第二次进行加密
    public static String formPassToDBPass(String formPass,String salt){
        String str = ""+ salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String input,String saltDB){
        String formPass = inputPassToFormPass(input);
        //System.out.println(formPass);
        String dbPass = formPassToDBPass(formPass,saltDB);
        //System.out.println(dbPass);
        return dbPass;
    }



    public static void main(String[] args) {
        System.out.println(inputPassToDbPass("123456","1a2b3c4d"));
    }
}
