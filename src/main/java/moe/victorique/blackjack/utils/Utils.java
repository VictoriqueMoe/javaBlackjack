package moe.victorique.blackjack.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String deviceHash(final String ip, final String ua) throws NoSuchAlgorithmException {
        final var digest = MessageDigest.getInstance("SHA-256");
        final var hash = digest.digest((ip + ua).getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (final var b : hash) {
            final var hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
