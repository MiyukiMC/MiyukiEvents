package app.miyuki.miyukievents.bukkit.util.singlemap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class Pair<K, V> {

    private final K first;
    private final V second;

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

}
