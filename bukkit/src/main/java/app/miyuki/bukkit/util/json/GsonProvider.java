package app.miyuki.bukkit.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GsonProvider {

    @Getter
    private final Gson normal = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    @Getter
    private final Gson prettyPrinting = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

}