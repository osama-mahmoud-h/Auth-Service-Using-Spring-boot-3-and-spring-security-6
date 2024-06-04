package osama_mh.ecommerce.utils.sms;

import java.util.UUID;

public class SmsUtils {
        /**
         * Generates a six-digit OTP (One-Time Password).
         *
         * The method generates a random six-digit number by multiplying a random number between 0 and 1
         * with the difference between the maximum and minimum six-digit numbers (999999 and 100000 respectively),
         * and then adding the minimum six-digit number (100000).
         *
         * The generated number is then converted to a string and returned.
         *
         * The probability of generating the same number is 1/900_000, as the permutation of 6 digits is 9 *10^5 = 900_000.
         *
         * @return A string representing a six-digit OTP.
         */
    public static String generateOtp() {
        return String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
    }
}
