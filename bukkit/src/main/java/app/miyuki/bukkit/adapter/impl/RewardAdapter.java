package app.miyuki.bukkit.adapter.impl;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.adapter.Adapter;
import app.miyuki.bukkit.reward.Reward;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class RewardAdapter implements Adapter<Reward, ConfigurationSection> {

    private final MiyukiEvents plugin;

    @Override
    public @Nullable Reward adapt(@NotNull ConfigurationSection configurationSection) {
        return Reward.builder()
                .money(configurationSection.getDouble("Money"))
                .cash(configurationSection.getDouble("Cash"))
                .commands(configurationSection.getStringList("Commands"))
                .build();
    }

}
