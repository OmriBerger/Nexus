package io.github.omriberger.utils;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.*;
import java.security.KeyStore;
import java.util.Arrays;

import javax.crypto.*;

import javax.crypto.spec.GCMParameterSpec;

import io.github.omriberger.user.User;

public class CryptoManager {

    private static final String KEY_ALIAS = "user_data_key";

    private static SecretKey getOrCreateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (keyStore.containsAlias(KEY_ALIAS)) {
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(
                new KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(true)
                        .build()
        );
        return keyGenerator.generateKey();
    }

    public static String encryptUser(User user) throws Exception {
        SecretKey key = getOrCreateKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (CipherOutputStream cos = new CipherOutputStream(byteStream, cipher);
             ObjectOutputStream oos = new ObjectOutputStream(cos)) {
            oos.writeObject(user);
        }

        byte[] iv = cipher.getIV();
        byte[] encryptedBytes = byteStream.toByteArray();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(encryptedBytes);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static User decryptUser(String encryptedData) throws Exception {
        SecretKey key = getOrCreateKey();

        byte[] decodedData = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] iv = Arrays.copyOfRange(decodedData, 0, 12); // 12-byte IV for GCM
        byte[] encryptedBytes = Arrays.copyOfRange(decodedData, 12, decodedData.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        try (CipherInputStream cis = new CipherInputStream(new ByteArrayInputStream(encryptedBytes), cipher);
             ObjectInputStream ois = new ObjectInputStream(cis)) {
            return (User) ois.readObject();
        }
    }
}
