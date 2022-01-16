package app.miyuki.bukkit.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Adapter<T, V> {

    @Nullable T adapt(@NotNull V v);

    @Nullable V restore(@NotNull T t);

}
