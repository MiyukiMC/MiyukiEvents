package app.miyuki.bukkit.config;

import app.miyuki.bukkit.MiyukiEvents;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Config {

    private final MiyukiEvents plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

    private final String path;
    private final String internalPath;

    private final File file;

    @Getter
    private YamlConfiguration config;

    public Config(@NotNull String path) {
        this(path, null);
    }

    public Config(@NotNull String path, @Nullable String internalPath) {
        this.path = path;
        this.internalPath = internalPath;

        file = new File(plugin.getDataFolder(), path);

        saveDefaultConfig();
        reloadConfig();
    }


    @SneakyThrows
    public void saveDefaultConfig() {
        if (path.isEmpty() || file.exists())
            return;

        InputStream in;

        if (internalPath != null) {
            in = plugin.getResource(internalPath);
        } else {
            in = plugin.getResource(path);
        }

        if (in == null)
            return;

        val dirs = file.getParentFile();
        dirs.mkdirs();

        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
        reloadConfig();
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public void saveConfig() {
        config.save(file);
    }

    @NotNull
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    @NotNull
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    public boolean contains(@NotNull String path) {
        return config.contains(path);
    }

    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return config.contains(path, ignoreDefault);
    }

    public boolean isSet(@NotNull String path) {
        return config.isSet(path);
    }

    @Nullable
    public String getCurrentPath() {
        return config.getCurrentPath();
    }

    @NotNull
    public String getName() {
        return config.getName();
    }

    @Nullable
    public Configuration getRoot() {
        return config.getRoot();
    }

    @Nullable
    public ConfigurationSection getParent() {
        return config.getParent();
    }

    @Nullable
    public Object get(@NotNull String path) {
        return config.get(path);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull String path, @Nullable Object def) {
        return config.get(path, def);
    }

    public void set(@NotNull String path, @Nullable Object value) {
        config.set(path, value);
    }

    @NotNull
    public ConfigurationSection createSection(@NotNull String path) {
        return config.createSection(path);
    }

    @NotNull
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        return config.createSection(path, map);
    }

    @NotNull
    public String getString(@NotNull String path) {
        return Objects.requireNonNull(config.getString(path));
    }

    @NotNull
    public String getString(@NotNull String path, @Nullable String def) {
        return Objects.requireNonNull(config.getString(path, def));
    }

    public boolean isString(@NotNull String path) {
        return config.isString(path);
    }

    public int getInt(@NotNull String path) {
        return config.getInt(path);
    }

    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    public boolean isInt(@NotNull String path);

    public boolean getBoolean(@NotNull String path);

    public boolean getBoolean(@NotNull String path, boolean def);

    public boolean isBoolean(@NotNull String path);

    public double getDouble(@NotNull String path);

    public double getDouble(@NotNull String path, double def);

    public boolean isDouble(@NotNull String path);

    public long getLong(@NotNull String path);

    public long getLong(@NotNull String path, long def);

    public boolean isLong(@NotNull String path);

    @Nullable
    public List<?> getList(@NotNull String path);

    @Contract("_, !null -> !null")
    @Nullable
    public List<?> getList(@NotNull String path, @Nullable List<?> def);

    public boolean isList(@NotNull String path);

    @NotNull
    public List<String> getStringList(@NotNull String path);

    @NotNull
    public List<Integer> getIntegerList(@NotNull String path);

    @NotNull
    public List<Boolean> getBooleanList(@NotNull String path);

    @NotNull
    public List<Double> getDoubleList(@NotNull String path);

    @NotNull
    public List<Float> getFloatList(@NotNull String path);

    @NotNull
    public List<Long> getLongList(@NotNull String path);

    @NotNull
    public List<Byte> getByteList(@NotNull String path);

    @NotNull
    public List<Character> getCharacterList(@NotNull String path);

    @NotNull
    public List<Short> getShortList(@NotNull String path);

    @NotNull
    public List<Map<?, ?>> getMapList(@NotNull String path);

    @Nullable
    public <T extends Object> T getObject(@NotNull String path, @NotNull Class<T> clazz);

    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends Object> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def);

    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz);

    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def);

    @Nullable
    public Vector getVector(@NotNull String path);

    @Contract("_, !null -> !null")
    @Nullable
    public Vector getVector(@NotNull String path, @Nullable Vector def);

    public boolean isVector(@NotNull String path);

    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String path);

    @Contract("_, !null -> !null")
    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String path, @Nullable OfflinePlayer def);

    public boolean isOfflinePlayer(@NotNull String path);

    @Nullable
    public ItemStack getItemStack(@NotNull String path);

    @Contract("_, !null -> !null")
    @Nullable
    public ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def);

    public boolean isItemStack(@NotNull String path);

    @Nullable
    public Color getColor(@NotNull String path);

    @Contract("_, !null -> !null")
    @Nullable
    public Color getColor(@NotNull String path, @Nullable Color def);

    public boolean isColor(@NotNull String path);

    @Nullable
    public Location getLocation(@NotNull String path);

    @Contract("_, !null -> !null")
    @Nullable
    public Location getLocation(@NotNull String path, @Nullable Location def);

    public boolean isLocation(@NotNull String path);

    @Nullable
    public ConfigurationSection getConfigurationSection(@NotNull String path);

    public boolean isConfigurationSection(@NotNull String path);

    @Nullable
    public ConfigurationSection getDefaultSection();

    public void addDefault(@NotNull String path, @Nullable Object value);

}
