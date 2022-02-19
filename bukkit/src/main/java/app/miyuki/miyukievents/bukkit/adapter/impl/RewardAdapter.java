package app.miyuki.miyukievents.bukkit.adapter.impl;

import app.miyuki.miyukievents.bukkit.adapter.Adapter;
import app.miyuki.miyukievents.bukkit.reward.Reward;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

@AllArgsConstructor
public class RewardAdapter implements Adapter<Reward, ConfigurationSection> {

    @Override
    public @Nullable Reward adapt(@NotNull ConfigurationSection configurationSection) {
        return Reward.builder()
                .money(new BigDecimal(configurationSection.getString("Money")))
                .cash(new BigDecimal(configurationSection.getString("Cash")))
                .commands(configurationSection.getStringList("Commands"))
                .build();
    }

}
