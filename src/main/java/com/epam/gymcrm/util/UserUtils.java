package com.epam.gymcrm.util;

import java.security.SecureRandom;
import java.util.List;

public class UserUtils {

    private static final int PASSWORD_CHAR_LENGTH = 10;

    public static String generateUniqueUsername(String firstName, String lastName, List<String> existingUsername) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;
        while (existingUsername.contains(username)) {
            username = baseUsername + counter;
            counter++;
        }
        return username;
    }

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_CHAR_LENGTH);
        for (int i = 0; i < PASSWORD_CHAR_LENGTH; i++) {
            int ascii = 33 + random.nextInt(94);
            sb.append((char) ascii);
        }
        return sb.toString();
    }
}
