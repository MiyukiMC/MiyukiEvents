package app.miyuki.miyukievents.bukkit.util.item;

import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

// Remake
public class ItemBuilder {

    private ItemStack item;

    private ItemBuilder(Material material) {
        item = new ItemStack(material);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }


    public ItemBuilder setName(String name) {

        Component component = ChatUtils.colorize(name);
        component = component.decoration(TextDecoration.ITALIC, false);

        val serialized = GsonComponentSerializer.gson().serialize(component);

        item = NBTEditor.set(item, serialized, "display", "Name");
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {

        val serializedLore = lore.stream()
                .map(ChatUtils::colorize)
                .map(component -> component.decoration(TextDecoration.ITALIC, false))
                .map(GsonComponentSerializer.gson()::serialize);

        item = NBTEditor.set(item, serializedLore, "display", "Lore");
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
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