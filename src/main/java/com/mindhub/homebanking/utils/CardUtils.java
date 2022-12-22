package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {
    static Random random = new Random();

    private CardUtils() {
    }
    public static Integer getCvvNumber() {
        Integer cvvNumber = (random.nextInt(999 - 001) + 001);
        return cvvNumber;
    }
    public static String getCardNumber() {
        Integer cardNumber1 = (random.nextInt(9999 - 0001) + 0001);
        Integer cardNumber2 = (random.nextInt(9999 - 0001) + 0001);
        Integer cardNumber3 = (random.nextInt(9999 - 0001) + 0001);
        Integer cardNumber4 = (random.nextInt(9999 - 0001) + 0001);

        String cardNumber = cardNumber1 + "-" + cardNumber2 + "-" + cardNumber3 + "-" + cardNumber4;
        return cardNumber;
    }
}
