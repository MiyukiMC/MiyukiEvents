package app.miyuki.miyukievents.bukkit.adapter.impl;

import app.miyuki.miyukievents.bukkit.adapter.Adapter;
import app.miyuki.miyukievents.bukkit.reward.Reward;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.math.BigDecimal;
import java.util.ArrayList;

@AllArgsConstructor
public class RewardAdapter implements Adapter<Reward, CommentedConfigurationNode> {


    @SneakyThrows
    @Override
    public @Nullable Reward adapt(@NotNull CommentedConfigurationNode node) {
        return Reward.builder()
                .money(new BigDecimal(node.node("Money").getString("0")))
                .cash(new BigDecimal(node.node("Cash").getString("0")))
                .commands(node.node("Commands").getList(String.class, ArrayList::new))
                .build();
    }

}
