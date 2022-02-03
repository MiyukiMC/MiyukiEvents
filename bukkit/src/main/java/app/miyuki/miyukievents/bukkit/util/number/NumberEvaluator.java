package app.miyuki.miyukievents.bukkit.util.number;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberEvaluator {

    public boolean isValid(Double value) {
        return value > 0.0 && !value.isNaN() && !value.isInfinite();
    }

}
