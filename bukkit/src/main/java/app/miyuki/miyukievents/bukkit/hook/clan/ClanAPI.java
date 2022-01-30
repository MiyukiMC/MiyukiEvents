package app.miyuki.miyukievents.bukkit.hook.clan;

import java.util.Set;

public interface ClanAPI {

    boolean hasClan(String playerName);

    boolean hasPlayer(String playerName);

    String getClanTagByPlayer(String playerName);

    String getClanColorTag(String clanTag);

    String getClanName(String clanTag);

    Set<String> getLeaders(String clanTag);

    Set<String> getMembers(String clanTag);

    void disableFriendlyFire();

    void enableFriendlyFire();

}
