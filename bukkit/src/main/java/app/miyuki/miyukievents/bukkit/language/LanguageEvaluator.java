package app.miyuki.miyukievents.bukkit.language;

import com.google.common.collect.ImmutableList;
import lombok.val;

import java.util.Locale;

public class LanguageEvaluator {

    private static final ImmutableList<String> AVAILABLE_LANGS = ImmutableList.of(
            "pt-br"
    );

    public String evaluate(String lang) {

        if (AVAILABLE_LANGS.contains(lang.toLowerCase(Locale.ROOT)))
            return lang;

        val locale = Locale.getDefault();

        if (locale == null) {
            return AVAILABLE_LANGS.stream().findFirst().get();
        }

        val langFromLocale = (locale.getLanguage() + "-" + locale.getCountry()).toLowerCase(Locale.ROOT);

        if (AVAILABLE_LANGS.contains(langFromLocale))
            return langFromLocale;
        else
            return AVAILABLE_LANGS.stream().findFirst().get();

    }

}
