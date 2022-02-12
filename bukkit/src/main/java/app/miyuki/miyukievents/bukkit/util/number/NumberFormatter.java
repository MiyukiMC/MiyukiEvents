package app.miyuki.miyukievents.bukkit.util.number;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class NumberFormatter {

    private final String FORMAT;
    private final char GROUP_SEPARATOR;
    private final char DECIMAL_SEPARATOR;
    private final int START;
    private final List<String> SUFFIXIES;

    static {
        val config = JavaPlugin.getPlugin(MiyukiEvents.class).getGlobalConfig();
        val section = config.getConfigurationSection("NumberFormatter");

        FORMAT = section.getString("Format");
        GROUP_SEPARATOR = section.getString("GroupSeparator").charAt(0);
        DECIMAL_SEPARATOR = section.getString("DecimalSeparator").charAt(0);
        START = section.getInt("Start");
        SUFFIXIES = section.getStringList("Suffixes");
    }

    public String format(BigDecimal amount) {

        val roundedAmount = amount.setScale(2, RoundingMode.HALF_DOWN);

        val decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        decimalFormatSymbols.setGroupingSeparator(GROUP_SEPARATOR);

        val decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);

        var formatted = decimalFormat.format(roundedAmount);

        val commaIndex = formatted.indexOf(decimalFormatSymbols.getGroupingSeparator());
        val comma = formatted.split(Pattern.quote(String.valueOf(decimalFormatSymbols.getGroupingSeparator()))).length - 1;

        if (commaIndex == -1) {

            return FORMAT
                    .replace("{number}", formatted)
                    .replace("{suffix}", "")
                    .replace(decimalFormatSymbols.getDecimalSeparator() + "00", "");

        }

        var suffix = "";

        if (new IntRange(1, comma).containsInteger(START)) {

            formatted = formatted.substring(0, commaIndex + 3);

            try {
                suffix = SUFFIXIES.get(comma - 1);
            } catch (IndexOutOfBoundsException exception) {
                return "ERROR";
            }

        }

        return FORMAT
                .replace("{number}", formatted)
                .replace("{suffix}", suffix)
                .replace(decimalFormatSymbols.getDecimalSeparator() + "00", "");
    }

    public String format(double amount) {
        return format(BigDecimal.valueOf(amount));
    }



}
