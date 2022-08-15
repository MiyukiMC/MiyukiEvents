package app.miyuki.miyukievents.bukkit.util.random;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

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
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i <= length; i++)
            stringBuilder.append(ALPHANUMERIC_CHARS[RANDOM.nextInt(ALPHANUMERIC_CHARS.length)]);

        return stringBuilder.toString();
    }

    public String generateRandomString(char[] chars, int length) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i <= length; i++)
            stringBuilder.append(chars[RANDOM.nextInt(chars.length)]);

        return stringBuilder.toString();
    }

    public int generateRandomNumber(int min, int max) {
        if (min == max)
            return max;

        int randomNumber;

        randomNumber = RANDOM.nextInt(max);

        while (randomNumber < min)
            randomNumber = RANDOM.nextInt(max);

        return randomNumber;
    }

    @Nullable
    public<T> T getRandomElement(@NotNull List<T> elements) {
        if (elements.isEmpty())
            return null;

        return elements.get(RANDOM.nextInt(elements.size()));
    }

    @Nullable
    public<T> T getRandomElement(@NotNull T[] array) {
        if (array.length < 1)
            return null;

        return array[RANDOM.nextInt(array.length)];
    }

    @Nullable
    public <E> E getWeightedRandom(@NotNull Map<E, BigDecimal> weights) {
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

    public <E> BigDecimal getChance(@NotNull Map<E, BigDecimal> weights, @NotNull E e) {
        val sum = weights.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);
        val chance = weights.get(e).divide(sum, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return chance.setScale(2, RoundingMode.HALF_UP);
    }

}
