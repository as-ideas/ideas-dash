package de.axelspringer.ideas.tools.dash.business.jira;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    private final static byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
    private final static SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");


    public static byte[] encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key,
                    new IvParameterSpec(new byte[keyBytes.length]));
            return cipher.doFinal(value.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key,
                    new IvParameterSpec(new byte[keyBytes.length]));
            byte[] original = cipher.doFinal(encrypted);

            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
