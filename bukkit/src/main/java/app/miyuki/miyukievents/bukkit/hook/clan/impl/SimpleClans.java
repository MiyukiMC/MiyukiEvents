package app.miyuki.miyukievents.bukkit.hook.clan.impl;

import app.miyuki.miyukievents.bukkit.hook.clan.ClanAPI;
import lombok.val;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleClans implements ClanAPI {

    private final net.sacredlabyrinth.phaed.simpleclans.SimpleClans clanAPI = net.sacredlabyrinth.phaed.simpleclans.SimpleClans.getInstance();


    @Override
    public boolean hasClan(String playerName) {
        return clanAPI.getClanManager().getClanByPlayerName(playerName) != null;
    }

    @Override
    public @Nullable String getClanTagByPlayer(String playerName) {
        if (!hasClan(playerName))
            return null;

        return clanAPI.getClanManager().getClanByPlayerName(playerName).getTag();
    }

    @Override
    public @Nullable String getClanColorTag(String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getColorTag();
    }

    @Override
    public @Nullable String getClanName(String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getName();
    }

    @Override
    public @Nullable List<String> getLeaders(String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getLeaders().stream().map(ClanPlayer::getName).collect(Collectors.toList());
    }

    @Override
    public @Nullable List<String> getMembers(String clanTag) {
        val clan = clanAPI.getClanManager().getClan(clanTag);

        if (clan == null)
            return null;

        return clan.getMembers().stream().map(ClanPlayer::getName).collect(Collectors.toList());
    }

    @Override
    public void disableFriendlyFire(Player player) {
        val clanPlayer = clanAPI.getClanManager().getCreateClanPlayer(player.getName());
        clanPlayer.setFriendlyFire(true);
        clanAPI.getStorageManager().updateClanPlayer(clanPlayer);
    }

    @Override
    public void enableFriendlyFire(Player player) {
        val clanPlayer = clanAPI.getClanManager().getCreateClanPlayer(player.getName());
        clanPlayer.setFriendlyFire(true);
        clanAPI.getStorageManager().updateClanPlayer(clanPlayer);
    }

}
