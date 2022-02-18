package app.miyuki.miyukievents.bukkit.hook.worldedit.impl;

import app.miyuki.miyukievents.bukkit.hook.worldedit.WorldEditAPI;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import lombok.Cleanup;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Objects;

public class WorldEdit7 implements WorldEditAPI {

    private final WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

    @Override
    public @Nullable Location[] getSelector(@NotNull Player player) {
        Region region;
        val world = player.getWorld();
        try {
            region = worldEdit.getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
        } catch (Exception ignored) {
            return null;
        }
        return new Location[]{
                BukkitAdapter.adapt(world, region.getMaximumPoint()),
                BukkitAdapter.adapt(world, region.getMinimumPoint())
        };
    }

    @Override
    public void pasteSchematic(@NotNull File file, @NotNull Location pos1, @NotNull Location pos2) {
        var format = ClipboardFormats.findByAlias("sponge");
        if (format == null) {
            for (ClipboardFormat otherFormat : ClipboardFormats.getAll()) {
                format = otherFormat;
            }
            if (format == null)
                return;
        }

        val closer = Closer.create();

        val world = BukkitAdapter.adapt(Objects.requireNonNull(pos1.getWorld()));

        try {
            FileInputStream fis = closer.register(new FileInputStream(file));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            val reader = format.getReader(bis);
            val clipboard = reader.read();

            val editSession = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(world, -1);

            val operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BukkitAdapter.asBlockVector(pos2))
                    .ignoreAirBlocks(false)
                    .build();

            Operations.completeLegacy(operation);
            editSession.flushSession();
        } catch (IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void copySchematic(@NotNull File file, @NotNull Location pos1, @NotNull Location pos2) {
        file.getParentFile().mkdir();

        var format = ClipboardFormats.findByAlias("sponge");
        if (format == null) {
            for (ClipboardFormat otherFormat : ClipboardFormats.getAll()) {
                format = otherFormat;
            }
            if (format == null)
                return;
        }

        val world = BukkitAdapter.adapt(Objects.requireNonNull(pos1.getWorld()));

        val editSession = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(world, -1);
        val selection = new CuboidRegion(
                world,
                BukkitAdapter.asBlockVector(pos1),
                BukkitAdapter.asBlockVector(pos2)
        );

        val clipboard = new BlockArrayClipboard(selection);
        clipboard.setOrigin(BukkitAdapter.asBlockVector(pos2));
        val copy = new ForwardExtentCopy(
                editSession,
                selection,
                clipboard,
                selection.getMinimumPoint()
        );

        try {
            Operations.completeLegacy(copy);
            @Cleanup val closer = Closer.create();
            FileOutputStream fos = closer.register(new FileOutputStream(file));
            val bos = closer.register(new BufferedOutputStream(fos));
            val writer = closer.register(format.getWriter(bos));
            writer.write(clipboard);
        } catch (MaxChangedBlocksException | IOException e) {
            e.printStackTrace();
        }

    }

}
