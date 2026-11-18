package fr.geoventure.geontm.hazard.transformer;

import fr.geoventure.geontm.api.data.ReactorDataMaps;
import fr.geoventure.geontm.hazard.HazardEntry;
import fr.geoventure.geontm.hazard.HazardRegistry;
import fr.geoventure.geontm.items.machine.ItemZirnoxRod;
import fr.geoventure.geontm.items.machine.ItemZirnoxWaste;
import fr.geoventure.geontm.items.machine.ZirnoxFuel;
import java.util.List;
import net.minecraft.world.item.ItemStack;

/** Applies the original component-sensitive radiation curves for active rods and hot spent fuel. */
public final class HazardTransformerZirnox implements IHazardTransformer {

    @Override
    public void transformPre(ItemStack stack, List<HazardEntry> entries) {}

    @Override
    public void transformPost(ItemStack stack, List<HazardEntry> entries) {
        if (stack.getItem() instanceof ItemZirnoxRod rod) {
            ReactorDataMaps.ZirnoxFuel fuel = rod.stats();
            float progress = Math.min(1F, (float) rod.getLife(stack) / fuel.maxLife());
            float radiation = (float) (fuel.baseRadiation()
                    + (fuel.depletedRadiation() - fuel.baseRadiation()) * Math.pow(progress, 0.4D));
            entries.add(new HazardEntry(HazardRegistry.RADIATION, radiation));
        }
        if (stack.getItem() instanceof ItemZirnoxWaste && ItemZirnoxWaste.isHot(stack)) {
            // 1.7 registerOtherWaste: the cold waste (meta 0) is 7.5% of the base radiation, the hot one (meta 1) the
            // full base plus HOT 5. The registry holds the cold value, so the hot stack scales it back up.
            entries.replaceAll(entry -> entry.type == HazardRegistry.RADIATION ? entry.clone(1D / 0.075D) : entry);
            entries.add(new HazardEntry(HazardRegistry.HOT, 5F));
        }
    }
}
