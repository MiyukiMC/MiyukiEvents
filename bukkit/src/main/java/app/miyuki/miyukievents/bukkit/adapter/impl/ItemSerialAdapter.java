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
    @SneakyThrows
    public @Nullable String adapt(ItemStack @NotNull [] items) {
        @Cleanup val outputStream = new ByteArrayOutputStream();
        @Cleanup val dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.write(items.length);

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

        for (int Index = 0; Index < items.length; Index++) {
            val stack = dataInput.readObject();

            if (stack != null) {
                items[Index] = NBTEditor.set(ItemStack.deserialize((Map<String, Object>) stack), "1", "MiyukiEvents_Protect");
            } else {
                items[Index] = null;
            }
        }

        dataInput.close();
        return items;
    }

}
