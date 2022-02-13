package app.miyuki.miyukievents.bukkit.util;


import app.miyuki.miyukievents.bukkit.game.manager.GameSchedulerManager;
import com.cryptomorin.xseries.messages.Titles;
import javafx.util.Pair;
import lombok.Builder;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.List;

@Builder(builderMethodName = "Builder")
public class TitleAnimation {

    private final GameSchedulerManager schedulerManager;

    @Builder.Default
    public long period = 20L;

    public List<Pair<String, String>> animation;
    public Runnable callback;

    public void start() {
        int calls = animation.size() - 1;
        schedulerManager.run(0L, period, task -> {

            if (calls < 0) {
                task.cancel();
                return;
            }

            val title = animation.get(calls);

            Bukkit.getOnlinePlayers().forEach(player -> Titles.sendTitle(player, title.getKey(), title.getValue()));

            if (calls == 0) {

                if (callback != null)
                    callback.run();
                task.cancel();

            }

        });
    }


}
