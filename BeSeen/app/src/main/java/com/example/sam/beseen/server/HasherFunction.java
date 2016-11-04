package com.example.sam.beseen.server;

import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The HasherFunction class is used to hash passwords before being sent to the server.
 *
 * @author Eddie
 * @version 1.0
 * @since 29-10-16.
 */
public class HasherFunction {

    // TODO set to something else.
    private static final String SECRET_FACTORY_KEY = "PBKDF2WithHmacSHA1";

    /**
     * Hashes string for one way encryption.
     *
     * @param toBeHashed the input string to be hashed.
     * @return the hashed version of that string.
     */
    public static String hash(final String toBeHashed) {
        // TODO replace magic numbers
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(toBeHashed.toCharArray(), salt, 65536, 128);
        SecretKeyFactory keyFactory;
        byte[] hashBytes;
        try {
            keyFactory = SecretKeyFactory.getInstance(SECRET_FACTORY_KEY);
            hashBytes = keyFactory.generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return "";
        }
        return Base64.encodeToString(hashBytes, Base64.NO_PADDING + Base64.NO_WRAP + Base64.URL_SAFE);
    }

}
