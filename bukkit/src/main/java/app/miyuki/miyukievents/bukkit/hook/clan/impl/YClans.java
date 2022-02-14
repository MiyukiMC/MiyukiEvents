package app.miyuki.miyukievents.bukkit.hook.clan.impl;

import app.miyuki.miyukievents.bukkit.hook.clan.ClanAPI;
import com.google.common.collect.Lists;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import yclans.api.yClansAPI;

import java.util.List;

public class YClans implements ClanAPI {

    private final yClansAPI clanAPI = yClansAPI.yclansapi;

    @Override
    public boolean hasClan(String playerName) {
        return clanAPI.getPlayer(playerName).getClan() != null;
    }

    @Override
    public @Nullable String getClanTagByPlayer(String playerName) {
        if (!hasClan(playerName))
            return null;

        return clanAPI.getPlayer(playerName).getClan().getTag();
    }

    @Override
    public @Nullable String getClanColorTag(String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getTag();
    }

    @Override
    public @Nullable String getClanName(String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getNameClan();
    }

    @Override
    public @Nullable List<String> getLeaders(String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return Lists.newArrayList(clan.getLeader());
    }

    @Override
    public @Nullable List<String> getMembers(String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getMembers();
    }

    @Override
    public void disableFriendlyFire(Player player) {
    }

    @Override
    public void enableFriendlyFire(Player player) {
    }
}
