package app.miyuki.miyukievents.bukkit.util.singlemap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Triple<K, V, T> {

    private final K first;
    private final V second;
    private final T third;

    public static <K, V, T> Triple<K, V, T> of(K k, V v, T t) {
        return new Triple<>(k, v, t);
    }

}
