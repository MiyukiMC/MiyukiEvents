package app.miyuki.miyukievents.bukkit.util.reflection;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {

    @SneakyThrows
    public ImmutableSet<ClassPath.ClassInfo> getClasses(String... packages) {
        ClassPath classPath = ClassPath.from(MiyukiEvents.class.getClassLoader());

        ImmutableSet.Builder<ClassPath.ClassInfo> builder = ImmutableSet.builder();

        for (String packageName : packages)
            builder.addAll(classPath.getTopLevelClassesRecursive(packageName));

        return builder.build();
    }

}
