package app.miyuki.bukkit.config;

import org.jetbrains.annotations.NotNull;

public class ConfigProvider {


    private final Config messages;
    private final Config config;
    private final Config data;

    public ConfigProvider(@NotNull String gameName, @NotNull String language) {

        String defaultPath = "events/" + gameName + "/";
        String internalPath = language + "/" + defaultPath;

        messages = new Config(
                defaultPath + ConfigType.MESSAGES.getName(),
                internalPath + ConfigType.MESSAGES.getName()
        );
        config = new Config(
                defaultPath + ConfigType.CONFIG.getName(),
                internalPath + ConfigType.CONFIG.getName()
        );
        data = new Config(
                defaultPath + ConfigType.DATA.getName(),
                internalPath + ConfigType.DATA.getName()
        );
    }

    public @NotNull Config provide(@NotNull ConfigType type) {
        switch (type) {
            case CONFIG:
                return config;
            case DATA:
                return data;
            case MESSAGES:
                return messages;
        }
        throw new IllegalArgumentException("invalid config type");
    }


}
