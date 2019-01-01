package io.github.nbupsource.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static int KEY_LENGTH = 16; // 128 bit key

    private static String INIT_VECTOR = "RandomInitVector"; // 16 bytes IV

    private static final String UTF_8 = "UTF-8";

    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(getEncryptionKeyInBytes(encryptionKey), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encode(encrypted);
    }

    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception {

        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(getEncryptionKeyInBytes(encryptionKey), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(original);
    }

    private static byte[] getEncryptionKeyInBytes(String encryptionKey) throws UnsupportedEncodingException {
        String normalizedKey = encryptionKey;
        if (encryptionKey.length() > KEY_LENGTH) {
            normalizedKey = encryptionKey.substring(0, KEY_LENGTH - 1);
        }
        else if (encryptionKey.length() < KEY_LENGTH) {
            normalizedKey = String.format("%1$" + KEY_LENGTH + "s", encryptionKey);
        }
        return normalizedKey.getBytes(UTF_8);
    }
}
