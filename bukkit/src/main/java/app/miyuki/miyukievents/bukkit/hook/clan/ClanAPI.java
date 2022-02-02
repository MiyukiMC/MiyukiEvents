package app.miyuki.miyukievents.bukkit.hook.clan;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ClanAPI {

    boolean hasClan(String playerName);

    @Nullable String getClanTagByPlayer(String playerName);

    @Nullable String getClanColorTag(String clanTag);

    @Nullable String getClanName(String clanTag);

    @Nullable List<String> getLeaders(String clanTag);

    @Nullable List<String> getMembers(String clanTag);

    void disableFriendlyFire(Player player);

    void enableFriendlyFire(Player player);

}
