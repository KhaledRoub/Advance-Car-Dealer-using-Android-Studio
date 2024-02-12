package com.example.project_temp2.shared;

public class Dealer {

    private static volatile Dealer INSTANCE = null;

    public String dealer;

    private Dealer() {
    }

    public static Dealer getInstance() {
        if (INSTANCE == null) {
            synchronized (Dealer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Dealer();
                }
            }
        }
        return INSTANCE;
    }
}
