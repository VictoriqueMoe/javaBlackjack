package moe.victorique.blackjack.utils;

import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Utils {

    public static String deviceHash(final @NonNull String ip, final @NonNull String ua) throws NoSuchAlgorithmException {
        final var digest = MessageDigest.getInstance("SHA-256");
        final var hash = digest.digest((ip + ua).getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}
