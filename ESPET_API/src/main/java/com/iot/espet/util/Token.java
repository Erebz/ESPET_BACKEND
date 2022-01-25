package com.iot.espet.util;

import java.security.SecureRandom;
import java.util.Base64;

public class Token {
    static SecureRandom random = new SecureRandom();

    public static String getToken(int size){
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }
}
