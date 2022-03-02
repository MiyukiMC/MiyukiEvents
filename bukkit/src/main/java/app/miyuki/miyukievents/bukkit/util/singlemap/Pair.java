package app.miyuki.miyukievents.bukkit.util.singlemap;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Pair<K, V> {

    private final K first;
    private final V second;

    public Pair(@NotNull K first, @NotNull V second) {
        this.first = first;
        this.second = second;
    }

}
