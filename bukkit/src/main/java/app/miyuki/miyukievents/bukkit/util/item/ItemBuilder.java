package app.miyuki.miyukievents.bukkit.util.item;

import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

// Remake
public class ItemBuilder {

    private final ItemStack item;

    private ItemBuilder(Material material) {
        item = new ItemStack(material);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatUtils.colorize(name));
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lores) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lores) {
        return setLore(Arrays.asList(lores));
    }

    public ItemBuilder setGlow(boolean state) {
        if (!state)
            return this;

        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() {
        return item;
    }

}