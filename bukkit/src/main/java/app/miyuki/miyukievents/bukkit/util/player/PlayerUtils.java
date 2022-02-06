package app.miyuki.miyukievents.bukkit.util.player;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PlayerUtils {

    public boolean inventoryIsEmpty(@NotNull Player player) {
        val inventory = player.getInventory();

        for (ItemStack content : inventory.getContents())
            if (content != null && content.getType() != Material.AIR)
                return false;

        for (ItemStack content : inventory.getArmorContents())
            if (content != null && content.getType() != Material.AIR)
                return false;

        val cursor = player.getItemOnCursor();

        return cursor == null && cursor.getType() == Material.AIR;
    }

    public void clearInventory(@NotNull Player player) {
        player.closeInventory();
        val inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);
    }

    public void setInventoryFromString(@NotNull Player player, @NotNull String serializedInventory) {
        var i = 0;
        val inventory = player.getInventory();
        for (ItemStack itemStack : JavaPlugin.getPlugin(MiyukiEvents.class).getItemSerialAdapter().restore(serializedInventory)) {
            inventory.setItem(i, itemStack);
            i++;
        }
    }

}
