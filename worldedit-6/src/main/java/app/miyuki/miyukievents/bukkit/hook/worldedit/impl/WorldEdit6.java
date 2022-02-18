package app.miyuki.miyukievents.bukkit.hook.worldedit.impl;

import app.miyuki.miyukievents.bukkit.hook.worldedit.WorldEditAPI;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class WorldEdit6 implements WorldEditAPI {

    private final WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

    @Override
    public @Nullable Location[] getSelector(@NotNull Player player) {
        val selection = worldEdit.getSelection(player);

        if (selection == null)
            return null;

        return new Location[] {
                selection.getMaximumPoint(),
                selection.getMinimumPoint()
        };
    }

    @SneakyThrows
    @Override
    public void pasteSchematic(@NotNull File file, @NotNull Location pos1, @NotNull Location pos2) {
        var format = ClipboardFormat.findByFile(file);
        if (format == null) {
            format = ClipboardFormat.findByAlias("mcedit");
            if (format == null)
                return;
        }
        @Cleanup val closer = Closer.create();

        val world = BukkitUtil.getLocalWorld(pos1.getWorld());

        try {
            FileInputStream fis = closer.register(new FileInputStream(file));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            val reader = format.getReader(bis);
            val clipboard = reader.read(world.getWorldData());

            val editSession = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(world, -1);

            val operation = new ClipboardHolder(clipboard, world.getWorldData())
                    .createPaste(editSession, editSession.getWorld().getWorldData())
                    .to(BukkitUtil.toVector(pos2))
                    .ignoreAirBlocks(false)
                    .build();

            Operations.completeLegacy(operation);
            editSession.flushQueue();
        } catch (IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void copySchematic(@NotNull File file, @NotNull Location pos1, @NotNull Location pos2) {
        val world = BukkitUtil.getLocalWorld(pos1.getWorld());

        val editSession = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(world, -1);

        file.getParentFile().mkdirs();

        val closer = Closer.create();

        try {
            val region = new CuboidRegion(
                    world,
                    BukkitUtil.toVector(pos1),
                    BukkitUtil.toVector(pos2)
            );

            val clipboard = new BlockArrayClipboard(region);
            val copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            Operations.completeLegacy(copy);

            file.createNewFile();

            FileOutputStream fos = closer.register(new FileOutputStream(file));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            val writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bos));
            writer.write(clipboard, LegacyWorldData.getInstance());

        } catch (IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

}
