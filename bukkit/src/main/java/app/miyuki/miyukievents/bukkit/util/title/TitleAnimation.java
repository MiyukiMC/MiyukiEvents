package app.miyuki.miyukievents.bukkit.util.title;

import app.miyuki.miyukievents.bukkit.game.manager.GameSchedulerManager;
import lombok.Builder;
import lombok.val;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.title.Title;

import java.util.List;

@Builder(builderMethodName = "Builder")
public class TitleAnimation {

    private final GameSchedulerManager schedulerManager;

    @Builder.Default
    public long period = 20L;

    public List<Title> animation;
    public Runnable callback;

    public BukkitAudiences adventure;

    public void start() {
        int calls = animation.size() - 1;
        schedulerManager.run(0L, period, task -> {

            if (calls < 0) {
                task.cancel();
                return;
            }

            val title = animation.get(calls);

            adventure.all().showTitle(title);

            if (calls == 0) {

                if (callback != null)
                    callback.run();
                task.cancel();

            }

        });
    }


}
