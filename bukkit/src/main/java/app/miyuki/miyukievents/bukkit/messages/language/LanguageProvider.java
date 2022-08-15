package app.miyuki.miyukievents.bukkit.messages.language;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.Config;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LanguageProvider {

    private final MiyukiEvents plugin;

    public String provide() {
        return new Config(plugin.getDataFolder() + "/lang.yml", "lang.yml").getRoot().node("Language").getString();
    }

}
