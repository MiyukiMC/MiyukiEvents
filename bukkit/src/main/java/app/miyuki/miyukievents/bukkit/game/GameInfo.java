package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.commands.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GameInfo {

    String typeName();

    Class<? extends Command> commandClass();

}
