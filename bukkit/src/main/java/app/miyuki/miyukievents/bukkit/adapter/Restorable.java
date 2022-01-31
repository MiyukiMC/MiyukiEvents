package app.miyuki.miyukievents.bukkit.adapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Restorable<V, T> {

    @Nullable V restore(@NotNull T t);

}
