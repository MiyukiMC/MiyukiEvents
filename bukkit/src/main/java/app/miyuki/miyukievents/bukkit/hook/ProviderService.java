package app.miyuki.miyukievents.bukkit.hook;

import org.jetbrains.annotations.Nullable;

public interface ProviderService<T> {

    boolean hook();

    @Nullable T provide();

}
