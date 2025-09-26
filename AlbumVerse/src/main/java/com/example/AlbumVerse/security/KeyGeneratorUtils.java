package com.example.AlbumVerse.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.springframework.stereotype.Component;

@Component
public class KeyGeneratorUtils {
    private KeyGeneratorUtils(){
                // Private constructor to prevent instantiation
        }

        static KeyPair generateRsaKey() {
            KeyPair keyPair;
            try{
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate RSA key pair", e);
            }
            return keyPair;
        }
}
