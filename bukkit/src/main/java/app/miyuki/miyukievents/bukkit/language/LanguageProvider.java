package app.miyuki.miyukievents.bukkit.language;

import app.miyuki.miyukievents.bukkit.config.Config;

public class LanguageProvider {

    public String provide() {
        return new Config("lang.yml").getString("Language");
    }

}
