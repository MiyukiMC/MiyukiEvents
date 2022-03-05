package app.miyuki.miyukievents.bukkit.adapter.impl;

import app.miyuki.miyukievents.bukkit.adapter.Adapter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextColorAdapter implements Adapter<String, String> {

    @Override
    public @Nullable String adapt(@NotNull String text) {

        for (TextColor textColor : TextColor.values()) {
            text = text.replace(textColor.getFormattingCode(), textColor.getTag());
        }

        return null;
    }

    private enum TextColor {
        BLACK("&0", "<black>"),
        DARK_BLUE("&1", "<dark_blue>"),
        DARK_GREEN("&2", "<dark_green>"),
        DARK_AQUA("&3", "<dark_aqua>"),
        DARK_RED("&4", "<dark_red>"),
        DARK_PURPLE("&5", "<dark_purple>"),
        GOLD("&6", "<gold>"),
        GRAY("&7", "<gray>"),
        DARK_GRAY("&8", "<dark_gray>"),
        BLUE("&9", "<blue>"),
        GREEN("&a", "<green>"),
        AQUA("&b", "<aqua>"),
        RED("&c", "<red>"),
        LIGHT_PURPLE("&d", "<light_purple>"),
        YELLOW("&e", "<yellow>"),
        WHITE("&f", "<white>");

        @Getter private final String formattingCode;
        @Getter private final String tag;

        TextColor(String formattingCode, String tag) {
            this.formattingCode = formattingCode;
            this.tag = tag;
        }

    }

}
