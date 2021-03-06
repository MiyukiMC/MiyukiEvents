package app.miyuki.miyukievents.bukkit.adapter.impl;

import app.miyuki.miyukievents.bukkit.adapter.Adapter;
import app.miyuki.miyukievents.bukkit.adapter.Restorable;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class ItemSerialAdapter implements Adapter<String, ItemStack[]>, Restorable<ItemStack[], String> {

    @Override
    @Nullable
    @SneakyThrows
    public String adapt(ItemStack @NotNull [] items) {
        @Cleanup val outputStream = new ByteArrayOutputStream();
        @Cleanup val dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(items.length);

        for (ItemStack itemStack : items) {
            if (itemStack != null) {
                dataOutput.writeObject(itemStack.serialize());
            } else {
                dataOutput.writeObject(null);
            }
        }

        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    @Override
    @SneakyThrows
    @Nullable
    public ItemStack[] restore(@NotNull String data) {
        @Cleanup val inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        @Cleanup val dataInput = new BukkitObjectInputStream(inputStream);

        val items = new ItemStack[dataInput.readInt()];

        for (int index = 0; index < items.length; index++) {
            val stack = dataInput.readObject();

            if (stack != null) {

                val item = ItemStack.deserialize((Map<String, Object>) stack);

                items[index] = NBTEditor.set(item, "0", "MiyukiEvents_Protect");

            } else {
                items[index] = null;
            }
        }


        dataInput.close();
        return items;
    }

}