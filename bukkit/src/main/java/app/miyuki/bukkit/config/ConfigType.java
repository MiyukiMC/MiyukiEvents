package app.miyuki.bukkit.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigType {

    MESSAGES("messages.yml"),
    DATA("data.yml"),
    CONFIG("config.yml");

    private final String name;

}
