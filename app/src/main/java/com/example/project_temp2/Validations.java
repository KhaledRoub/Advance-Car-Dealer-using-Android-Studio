package com.example.project_temp2;

import android.util.Patterns;

import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Validations {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[a-z\\d@$!%*?&]{5,}$";

    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidName(String name) {
        return name.length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static String hashPassword(String password) {
        int strength = 12;
        return BCrypt.withDefaults().hashToString(strength, password.toCharArray());
    }

    public static boolean checkPassword(String enteredPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(enteredPassword.toCharArray(), hashedPassword).verified;
    }
}
