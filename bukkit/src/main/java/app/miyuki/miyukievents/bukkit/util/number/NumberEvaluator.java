package app.miyuki.miyukievents.bukkit.util.number;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberEvaluator {

    public boolean isValidDoubleValue(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

}
