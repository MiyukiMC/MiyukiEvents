package app.miyuki.miyukievents.bukkit.util.number;

import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class NumberEvaluator {

    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public boolean isDouble(String value) {
        try {
            val number = Double.parseDouble(value);
            return !Double.isNaN(number) && !Double.isInfinite(number);
        } catch (NumberFormatException exception) {
            return false;
        }
    }


}
