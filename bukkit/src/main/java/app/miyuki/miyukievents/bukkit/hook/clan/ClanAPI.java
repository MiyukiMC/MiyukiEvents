package app.miyuki.miyukievents.bukkit.hook.clan;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClanAPI {

    boolean hasClan(@NotNull String playerName);

    @Nullable String getClanTagByPlayer(@NotNull String playerName);

    @Nullable String getClanColorTag(@NotNull String clanTag);

    @Nullable String getClanName(@NotNull String clanTag);

    @Nullable List<String> getLeaders(@NotNull String clanTag);

    @Nullable List<String> getMembers(@NotNull String clanTag);

    void disableFriendlyFire(@NotNull Player player);

    void enableFriendlyFire(@NotNull Player player);

}
