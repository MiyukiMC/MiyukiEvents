package app.miyuki.bukkit.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtils {

    private final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private final char[] ALPHANUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public String generateRandomKey(int length) {
        final StringBuilder STRING_BUILDER = new StringBuilder();

        for (int i = 0; i <= length; i++)
            STRING_BUILDER.append(ALPHANUMERIC_CHARS[RANDOM.nextInt(ALPHANUMERIC_CHARS.length)]);

        return STRING_BUILDER.toString();
    }

}
