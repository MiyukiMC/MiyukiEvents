package app.miyuki.bukkit.hook.clan.impl;

import app.miyuki.bukkit.hook.clan.ClanAPI;

import java.util.Set;

public class SimpleClans implements ClanAPI {

    private final net.sacredlabyrinth.phaed.simpleclans.SimpleClans clanAPI = net.sacredlabyrinth.phaed.simpleclans.SimpleClans.getInstance();

    @Override
    public boolean hasClan(String playerName) {
        return false;
    }

    @Override
    public boolean hasPlayer(String playerName) {
        return false;
    }

    @Override
    public String getClanTagByPlayer(String playerName) {
        return clanAPI.getClanManager().getClanPlayer(playerName).getClan().getTag();
    }

    @Override
    public String getClanColorTag(String clanTag) {
        return clanAPI.getClanManager().getClan(clanTag).getColorTag();
    }

    @Override
    public String getClanName(String clanTag) {
        return clanAPI.getClanManager().getClan(clanTag).getName();
    }

    @Override
    public Set<String> getLeaders(String clanTag) {
        return null;
    }

    @Override
    public Set<String> getMembers(String clanTag) {
        return null;
    }

    @Override
    public void disableFriendlyFire() {

    }

    @Override
    public void enableFriendlyFire() {

    }

}
