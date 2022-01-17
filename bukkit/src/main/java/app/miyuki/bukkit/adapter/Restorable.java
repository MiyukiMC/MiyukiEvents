package app.miyuki.bukkit.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Restorable<T, V> {

    @Nullable V restore(@NotNull T t);

}
