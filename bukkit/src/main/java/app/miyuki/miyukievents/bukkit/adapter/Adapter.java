package app.miyuki.miyukievents.bukkit.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Adapter<T, V> {

    @Nullable T adapt(@NotNull V v);

}
