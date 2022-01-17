package app.miyuki.bukkit.language;

import app.miyuki.bukkit.config.Config;

public class LanguageProvider {

    public String provide() {
        return new Config("lang.yml").getString("Language");
    }

}
