package app.miyuki.miyukievents.bukkit.util.random;

import lombok.experimental.UtilityClass;
import lombok.val;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

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
        int randomNumber;

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

    @Nullable
    public <E> E getWeightedRandom(Map<E, BigDecimal> weights) {
        E result = null;
        BigDecimal bestValue = BigDecimal.valueOf(Double.MAX_VALUE);

        for (E element : weights.keySet()) {
            BigDecimal value = BigDecimal.valueOf(-Math.log(RANDOM.nextDouble())).divide(weights.get(element), RoundingMode.HALF_UP);

            if (value.compareTo(bestValue) < 0) {
                bestValue = value;
                result = element;
            }
        }

        return result;
    }

    public <E> BigDecimal getChance(Map<E, BigDecimal> weights, E e) {

        val sum = weights.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);
        val chance = weights.get(e).divide(sum, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return chance.setScale(2, RoundingMode.HALF_UP);
    }

}
