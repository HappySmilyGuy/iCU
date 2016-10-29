package com.example.sam.beseen.server;

import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by Eddie on 29-Oct-16.
 */

public class HasherFunction {
    public static String hash(String to_be_hashed)
    {
        return createPasswordHash(to_be_hashed);
    }

    private static String createPasswordHash(String password) {
        SecureRandom secureRandom = new SecureRandom();
        byte salt[] = new byte[20];
        secureRandom.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory keyFactory;
        byte hashBytes[];
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hashBytes = keyFactory.generateSecret(spec).getEncoded();
        }
        catch (GeneralSecurityException e) {
            e.printStackTrace();
            return "";
        }
        return Base64.encodeToString(hashBytes, Base64.NO_PADDING + Base64.NO_WRAP + Base64.URL_SAFE);
    }
}
