package app.miyuki.miyukievents.bukkit.config;


import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
public class Config {

    private CommentedConfigurationNode root;

    private final YamlConfigurationLoader loader;

    @SneakyThrows
    public Config(@NotNull String path, @Nullable String internalPath) {
        val finalPath = Paths.get(path);

        val parent = finalPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        if (Files.notExists(finalPath)) {
            val resources = getClass().getClassLoader().getResourceAsStream(internalPath != null ? internalPath : path);

            if (resources != null)
                Files.copy(resources, finalPath);
            else
                Files.createFile(finalPath);
        }

        loader = YamlConfigurationLoader.builder()
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .path(finalPath)
                .build();

        this.root = loader.load();
    }

    public Config(@NotNull String path) {
        this(path, null);
    }

    @SneakyThrows
    public void reload() {
        this.root = loader.load();
    }

    @SneakyThrows
    public void save() {
        loader.save(root);
    }

}
