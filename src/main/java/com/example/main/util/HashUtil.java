package com.example.main.util;


import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.generators.OpenBSDBCrypt;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String sha256(String original, String salt) throws Exception {
        var originalWithSalt = StringUtils.join(salt, original);
        var digest = MessageDigest.getInstance("SHA-256");
        var hash = digest.digest(originalWithSalt.getBytes(StandardCharsets.UTF_8));

        return new String(Hex.encode(hash));
    }

    public static boolean isSha256Match(String original, String salt, String hashValue) throws Exception {
        var reHashValue = sha256(original, salt);
        return StringUtils.equals(hashValue, reHashValue);
    }

    public static String bcrypt(String original, String salt) {
        return OpenBSDBCrypt.generate(original.getBytes(StandardCharsets.UTF_8), salt.getBytes(), 5);
    }

    public static boolean isBcryptMatch(String original, String hash) {
        return OpenBSDBCrypt.checkPassword(hash, original.getBytes(StandardCharsets.UTF_8));
    }

}
