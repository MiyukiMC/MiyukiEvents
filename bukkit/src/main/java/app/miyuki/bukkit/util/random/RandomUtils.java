package app.miyuki.bukkit.util.random;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtils {

    private final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private final char[] ALPHANUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public String generateRandomString(int length) {
        final StringBuilder STRING_BUILDER = new StringBuilder();

        for (int i = 0; i <= length; i++)
            STRING_BUILDER.append(ALPHANUMERIC_CHARS[RANDOM.nextInt(ALPHANUMERIC_CHARS.length)]);

        return STRING_BUILDER.toString();
    }

    public int generateRandomNumber(int min, int max) {
        int randomNumber = 0;

        randomNumber = RANDOM.nextInt(max);

        while (randomNumber < min) randomNumber = RANDOM.nextInt(max);

        return randomNumber;
    }

}
