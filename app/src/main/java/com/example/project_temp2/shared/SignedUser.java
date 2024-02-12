package com.example.project_temp2.shared;

import com.example.project_temp2.database.User;

public class SignedUser {
    private static volatile SignedUser INSTANCE = null;

    public User user = new User();

    private SignedUser() {
    }

    public static SignedUser getInstance() {
        if (INSTANCE == null) {
            synchronized (SignedUser.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SignedUser();
                }
            }
        }
        return INSTANCE;
    }
}
