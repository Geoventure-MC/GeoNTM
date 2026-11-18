package fr.geoventure.geontm.deimport.steps;

import fr.geoventure.geontm.deimport.engine.Ctx;
import fr.geoventure.geontm.deimport.engine.Dimensions;
import fr.geoventure.geontm.deimport.engine.RegionIo;
import fr.geoventure.geontm.deimport.engine.Step;
import fr.geoventure.geontm.deimport.engine.Unit;
import fr.geoventure.geontm.deimport.source.de7.Radiation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;
import org.jetbrains.annotations.Nullable;

/**
 * DE's radiation ({@code <dim>/geontm/radiation/}) becomes one density per pocket for the live phase to set; DE's folder is
 * then renamed {@code radiation.imported}, so nothing reads it twice.
 */
public final class RadiationStep implements Step {
    @Override
    public String id() {
        return "radiation@1";
    }

    @Override
    public boolean applies(Ctx ctx) throws IOException {
        for (Path dim : Dimensions.all(ctx.world).values()) if (Files.isDirectory(dim.resolve("geontm/radiation"))) return true;
        return false;
    }

    @Override
    public boolean run(Ctx ctx, @Nullable MinecraftServer server) throws IOException {
        for (Map.Entry<String, Path> dim : Dimensions.all(ctx.world).entrySet()) {
            Path folder = dim.getValue().resolve("geontm/radiation");
            if (!Files.isDirectory(folder)) continue;
            // one unit per dimension, committed before the folder is renamed: a crash in between reads it all again
            Unit unit = ctx.begin("radiation " + dim.getKey());
            for (Path file : RegionIo.regionFiles(folder)) {
                if (!RegionIo.opens(file)) {
                    ctx.report.line("radiation file " + file.getFileName() + " in " + dim.getKey() + " cannot be read; it stays in radiation.imported");
                    continue;
                }
                try (RegionFile region = RegionIo.open(file)) {
                    for (ChunkPos pos : RegionIo.chunks(region, file)) {
                        try {
                            CompoundTag tag = RegionIo.read(region, pos);
                            if (tag == null) continue;
                            for (Radiation.Pocket pocket : Radiation.decode(pos.x, pos.z, tag.getByteArray("payload")))
                                ctx.radiation(dim.getKey(), pocket.pos().asLong(), pocket.density());
                        } catch (IOException | RuntimeException e) {
                            ctx.report.line("radiation of chunk " + pos.x + "," + pos.z + " in " + dim.getKey() + " cannot be read: " + e);
                            ctx.report.count("radiation chunks damaged");
                        }
                    }
                }
            }
            ctx.report.count("radiation pockets", unit.pendingCount("radiation"));
            ctx.commit(unit);
            Files.move(folder, folder.resolveSibling("radiation.imported"));
        }
        return true;
    }
}
