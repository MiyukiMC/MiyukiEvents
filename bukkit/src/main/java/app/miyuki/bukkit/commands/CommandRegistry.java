package app.miyuki.bukkit.commands;

import app.miyuki.bukkit.MiyukiEvents;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.google.common.reflect.ClassPath;
import lombok.Data;
import lombok.val;

import java.io.IOException;

@Data(staticConstructor = "of")
public class CommandRegistry {

    private final PaperCommandManager commandManager;

    public void register() {
        try {
            val classPath = ClassPath.from(MiyukiEvents.class.getClassLoader());

            val i = 0;

            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive("app.miyuki.bukkit.commands.impl")) {

                try {

                    val command = classInfo.load().newInstance();

                    if (command instanceof BaseCommand) {
                        commandManager.registerCommand((BaseCommand) command);
                    }

                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
