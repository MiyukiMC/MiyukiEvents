package app.miyuki.bukkit.hook.clan.impl;

import app.miyuki.bukkit.hook.clan.Clan;
import lombok.val;

import java.util.Set;

public class YClans implements Clan {


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
        return null;
    }

    @Override
    public String getClanColorTag(String clanTag) {
        return null;
    }

    @Override
    public String getClanName(String clanTag) {
        return null;
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
