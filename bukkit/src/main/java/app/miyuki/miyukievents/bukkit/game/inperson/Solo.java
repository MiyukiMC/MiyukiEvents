package app.miyuki.miyukievents.bukkit.game.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Solo<W> extends InPerson<W> {


    private Location entry;

    private ItemStack[] kit;

    public Solo(@NotNull GameConfigProvider configProvider) {
        super(configProvider);

        val data = configProvider.provide(ConfigType.DATA);

        val locationAdapter = plugin.getLocationAdapter();
        val itemSerialAdapter = plugin.getItemSerialAdapter();

        this.entry = data.contains("Entry") ? locationAdapter.adapt(data.getString("Entry")) : null;
        this.kit = data.contains("Kit") ? itemSerialAdapter.restore(data.getString("Kit")) : null;
    }

    public void setEntry(@NotNull Location entry) {
        saveLocation("Entry", entry);
        this.entry = entry;
    }

    public void setKit(@NotNull PlayerInventory inventory) {
        this.kit = (ItemStack[]) ArrayUtils.addAll(inventory.getContents(), inventory.getArmorContents());

        val serializedInventory = plugin.getItemSerialAdapter().adapt(this.kit);

        val data = configProvider.provide(ConfigType.DATA);
        data.set("Kit", serializedInventory);
        data.saveConfig();
    }


}
