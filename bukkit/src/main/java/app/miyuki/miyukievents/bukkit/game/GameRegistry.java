package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.util.reflection.ReflectionUtils;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import lombok.Getter;
import lombok.val;

import java.util.List;

@Getter
public class GameRegistry {

    private final List<Class<?>> games = Lists.newArrayList();

    public void register(Class<?> typeClass) throws IllegalArgumentException {

        if (!typeClass.isAnnotationPresent(GameInfo.class)) {
            throw new IllegalArgumentException("The informed class does not contain the GameInfo annotation");
        }

        games.add(typeClass);
    }

    public void load() {

        val classes = ReflectionUtils.getClasses(
                "app.miyuki.miyukievents.bukkit.game.inperson.impl",
                "app.miyuki.miyukievents.bukkit.game.command.impl",
                "app.miyuki.miyukievents.bukkit.game.chat.impl"
        );

        for (ClassPath.ClassInfo classInfo : classes) {

            register(classInfo.load());

        }

    }

}
