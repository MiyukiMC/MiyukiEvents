package app.miyuki.miyukievents.bukkit.util.random;

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.security.SecureRandom;
import java.util.List;

@UtilityClass
public class RandomUtils {

    private final SecureRandom RANDOM = new SecureRandom();
    private final char[] ALPHANUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public String generateRandomString(int length) {
        final StringBuilder STRING_BUILDER = new StringBuilder();

        for (int i = 0; i <= length; i++)
            STRING_BUILDER.append(ALPHANUMERIC_CHARS[RANDOM.nextInt(ALPHANUMERIC_CHARS.length)]);

        return STRING_BUILDER.toString();
    }

    public String generateRandomString(char[] chars, int length) {
        final StringBuilder STRING_BUILDER = new StringBuilder();

        for (int i = 0; i <= length; i++)
            STRING_BUILDER.append(chars[RANDOM.nextInt(chars.length)]);

        return STRING_BUILDER.toString();
    }

    public int generateRandomNumber(int min, int max) {
        int randomNumber = 0;

        randomNumber = RANDOM.nextInt(max);

        while (randomNumber < min)
            randomNumber = RANDOM.nextInt(max);

        return randomNumber;
    }

    @Nullable
    public<T> T getRandomElement(List<T> elements) {
        if (elements.size() < 1)
            return null;

        return elements.get(RANDOM.nextInt(elements.size()));
    }

    @Nullable
    public<T> T getRandomElement(T[] array) {
        if (array.length < 1)
            return null;

        return array[RANDOM.nextInt(array.length)];
    }

}
