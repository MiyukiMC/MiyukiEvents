package app.miyuki.bukkit.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigProvider {

    private final Config messages;
    private final Config config;
    private final Config data;

    public ConfigProvider(@NotNull String path) {
        this(path, null);
    }

    public ConfigProvider(@NotNull String path, @Nullable String internalPath) {

        messages = new Config(
                path + ConfigType.MESSAGES.getName(),
                internalPath == null ? null :  internalPath + ConfigType.MESSAGES.getName()
        );
        config = new Config(
                path + ConfigType.CONFIG.getName(),
                internalPath == null ? null :  internalPath + ConfigType.CONFIG.getName()
        );
        data = new Config(
                path + ConfigType.DATA.getName(),
                internalPath == null ? null :  internalPath + ConfigType.DATA.getName()
        );

        load();
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

    public void load() {
        config.saveDefaultConfig();
        messages.saveDefaultConfig();
        data.saveDefaultConfig();
        reload();
    }

    public void reload() {
        config.reloadConfig();
        messages.reloadConfig();
        data.reloadConfig();
    }


}
