package com.infovine.tour.utils;

/**
 * Created by chlee on 2020-11-30.
 */

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Chiper {

    public static String ivBytes = "1234567890123456";
    public static String secretKey = "12345678901234567890123456789012";

//    KEY : 2C E3 BE D7 41 E0 D9 EC 5A 63010D23C23F4A0E3C04104B6E0A3A605BC2E58B596E70
//    IV: B4 3C FF D0 3D1D6A12142C797A2B7F3D88

//    KEY : 2CE3BED741E0D9EC5A63010D23C23F4A0E3C04104B6E0A3A605BC2E58B596E70
//    IV: B43CFFD03D1D6A12142C797A2B7F3D88

    //AES256 암호화
    public static String AES_Encode(String str)    throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,    IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes = str.getBytes(StandardCharsets.UTF_8);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes.getBytes());
        SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        return Base64.encodeToString(cipher.doFinal(textBytes), Base64.NO_WRAP);
    }

    //AES256 복호화
    public static String AES_Decode(String str)    throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] textBytes =Base64.decode(str,Base64.NO_WRAP);
        //byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes.getBytes());
        SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ( (Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16) );
        }
        return data;
    }
}