package app.miyuki.miyukievents.bukkit.hook.clan.impl;

import app.miyuki.miyukievents.bukkit.hook.clan.ClanAPI;
import lombok.val;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleClans implements ClanAPI {

    private final net.sacredlabyrinth.phaed.simpleclans.SimpleClans clanAPI = net.sacredlabyrinth.phaed.simpleclans.SimpleClans.getInstance();

    @Override
    public boolean hasClan(@NotNull String playerName) {
        return clanAPI.getClanManager().getClanByPlayerName(playerName) != null;
    }

    @Override
    @Nullable
    public String getClanTagByPlayer(@NotNull String playerName) {
        if (!hasClan(playerName))
            return null;

        return clanAPI.getClanManager().getClanByPlayerName(playerName).getTag();
    }

    @Override
    @Nullable
    public String getClanColorTag(@NotNull String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getColorTag();
    }

    @Override
    @Nullable
    public String getClanName(@NotNull String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getName();
    }

    @Override
    @Nullable
    public List<String> getLeaders(@NotNull String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getLeaders().stream()
                .map(ClanPlayer::getName)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public List<String> getMembers(@NotNull String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getMembers().stream()
                .map(ClanPlayer::getName)
                .collect(Collectors.toList());
    }

    // ?
    @Override
    public void disableFriendlyFire(@NotNull Player player) {
        val clanPlayer = clanAPI.getClanManager().getCreateClanPlayer(player.getName());
        clanPlayer.setFriendlyFire(true);
        clanAPI.getStorageManager().updateClanPlayer(clanPlayer);
    }

    // ?
    @Override
    public void enableFriendlyFire(@NotNull Player player) {
        val clanPlayer = clanAPI.getClanManager().getCreateClanPlayer(player.getName());
        clanPlayer.setFriendlyFire(true);
        clanAPI.getStorageManager().updateClanPlayer(clanPlayer);
    }

}
