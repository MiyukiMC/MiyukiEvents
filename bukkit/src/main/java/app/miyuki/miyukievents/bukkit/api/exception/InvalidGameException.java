package app.miyuki.miyukievents.bukkit.api.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidGameException extends IllegalArgumentException {

    public InvalidGameException(@NotNull String message) {
        super(message);
    }

}
