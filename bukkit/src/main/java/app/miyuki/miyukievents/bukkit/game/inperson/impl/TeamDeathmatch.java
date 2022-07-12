package app.miyuki.miyukievents.bukkit.game.inperson.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch.TeamDeathmatchCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.inperson.Teams;
import app.miyuki.miyukievents.bukkit.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@GameInfo(typeName = "TeamDeathmatch", commandClass = TeamDeathmatchCommand.class)
public class TeamDeathmatch extends Teams<Set<User>> {

    public TeamDeathmatch(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Set<User> users) {

    }

    @Override
    protected void giveReward(Set<User> users) {

    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

    @Override
    public void join(Player player) {

    }

    @Override
    public void leave(Player player) {

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {

    }

    @Override
    public boolean isKitRequired() {
        return configProvider.provide(ConfigType.CONFIG).getBoolean("KitSetted");
    }

    @Override
    public boolean isClanRequired() {
        return false;
    }

    @Override
    public boolean isWorldEditRequired() {
        return configProvider.provide(ConfigType.CONFIG).getBoolean("Schematic.Enabled");
    }


}


