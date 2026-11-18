package fr.geoventure.geontm.hazard.transformer;

import fr.geoventure.geontm.config.GeneralConfig;
import fr.geoventure.geontm.hazard.HazardEntry;
import fr.geoventure.geontm.hazard.HazardRegistry;
import fr.geoventure.geontm.hazard.HazardSystem;
import fr.geoventure.geocore.storage.CellContents;
import java.util.List;
import net.minecraft.world.item.ItemStack;

/** 1.7 {@code HazardTransformerRadiationME}: an AE2 item cell or portable cell carries its contents' radiation in full. */
public class HazardTransformerRadiationME implements IHazardTransformer {

    @Override
    public void transformPost(ItemStack stack, List<HazardEntry> entries) {
        // 1.7 registers this transformer only while LBSM's safe ME drives are off
        if (GeneralConfig.enableLBSM && GeneralConfig.enableLBSMSafeMEDrives) return;
        float radiation = 0;
        for (CellContents.Stored held : CellContents.items(stack)) {
            radiation += (float) (HazardSystem.getHazardLevelFromStack(held.type(), HazardRegistry.RADIATION) * held.count());
        }
        if (radiation > 0) entries.add(new HazardEntry(HazardRegistry.RADIATION, radiation));
    }
}
