package app.miyuki.bukkit.hook.clan.impl;

import app.miyuki.bukkit.hook.clan.ClanAPI;
import yclans.api.yClansAPI;

import java.util.Set;

public class YClans implements ClanAPI {

    private final yClansAPI clanAPI = yClansAPI.yclansapi;

    @Override
    public boolean hasClan(String playerName) {
        return clanAPI.getPlayer(playerName).hasClan();
    }

    @Override
    public boolean hasPlayer(String playerName) {
        return false;
    }

    @Override
    public String getClanTagByPlayer(String playerName) {
        return clanAPI.getPlayer(playerName).getClanTag();
    }

    @Override
    public String getClanColorTag(String clanTag) {
        return clanAPI.getClan(clanTag).getColoredTag();
    }

    @Override
    public String getClanName(String clanTag) {
        return clanAPI.getClan(clanTag).getNameClan();
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
