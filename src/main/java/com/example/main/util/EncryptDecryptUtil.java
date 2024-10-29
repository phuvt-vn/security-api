package com.example.main.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class EncryptDecryptUtil {

    private static final String ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String IV = "LqV4g8gT1DqjSPB6"; //16 byte long
    private static IvParameterSpec paramSpec;
    private static Cipher cipher;

    static {
        Security.addProvider(new BouncyCastleProvider());
        paramSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        try {
            cipher = Cipher.getInstance(AES_TRANSFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encryptAES(String original, String secret) throws Exception {
        final var secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);

        var resultBytes = cipher.doFinal(original.getBytes(StandardCharsets.UTF_8));

        return new String(Hex.encode(resultBytes));
    }

    public static String decryptAES(String encrypted, String secret) throws Exception {
        final var secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec);

        var encryptedBytes = Hex.decode(encrypted);
        var resultBytes = cipher.doFinal(encryptedBytes);

        return new String(resultBytes, StandardCharsets.UTF_8);
    }
}
