package app.miyuki.miyukievents.bukkit.util.singlemap;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Triple<K, V, T> {

    private final K first;
    private final V second;
    private final T third;

    public Triple(@NotNull K first, @NotNull V second, @NotNull T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

}
