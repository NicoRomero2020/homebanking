package com.mindhub.homebanking.utils;

import java.util.Random;

public final class AccountUtils {
    static Random random = new Random();

    private AccountUtils() {
    }
    public static String generateVinNumber() {
        return "VIN-" + ((int) random.nextInt(99999999 - 1) + 1);
    }
}
