package app.miyuki.miyukievents.bukkit.hook.clan.impl;

import app.miyuki.miyukievents.bukkit.hook.clan.ClanAPI;
import com.google.common.collect.Lists;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yclans.api.yClansAPI;

import java.util.Collections;
import java.util.List;

public class YClans implements ClanAPI {

    private final yClansAPI clanAPI = yClansAPI.yclansapi;

    @Override
    public boolean hasClan(@NotNull String playerName) {
        return clanAPI.getPlayer(playerName).getClan() != null;
    }

    @Override
    @Nullable
    public String getClanTagByPlayer(@NotNull String playerName) {
        if (!hasClan(playerName))
            return null;

        return clanAPI.getPlayer(playerName).getClan().getTag();
    }

    @Override
    @Nullable
    public String getClanColorTag(@NotNull String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getTag();
    }

    @Override
    @Nullable
    public String getClanName(@NotNull String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getNameClan();
    }

    @Override
    @Nullable
    public List<String> getLeaders(@NotNull String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return Collections.singletonList(clan.getLeader());
    }

    @Override
    @Nullable
    public List<String> getMembers(@NotNull String clanTag) {
        val clan = clanAPI.getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getMembers();
    }

    // ?
    @Override
    public void disableFriendlyFire(@NotNull Player player) {

    }

    // ?
    @Override
    public void enableFriendlyFire(@NotNull Player player) {

    }
}
