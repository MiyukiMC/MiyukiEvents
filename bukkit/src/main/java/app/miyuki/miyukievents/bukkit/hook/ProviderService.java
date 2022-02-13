package app.miyuki.miyukievents.bukkit.hook;

import java.util.Optional;

public interface ProviderService<T> {

    boolean hook();

    Optional<T> provide();

}
