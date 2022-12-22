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
    //    Integer cardNumber2 = (random.nextInt(9999 - 0001) + 0001);
        Integer cardNumber2 = 23;
        Integer cardNumber3 = (random.nextInt(9999 - 0001) + 0001);
        Integer cardNumber4 = (random.nextInt(9999 - 0001) + 0001);

        String num1 = String.format("%0" + 4 + "d",cardNumber1);
        String num2 = String.format("%0" + 4 + "d",cardNumber2);
        String num3 = String.format("%0" + 4 + "d",cardNumber3);
        String num4 = String.format("%0" + 4 + "d",cardNumber4);

        String cardNumber = num1 + "-" + num2 + "-" + num3 + "-" + num4;
        return cardNumber;
    }
}
