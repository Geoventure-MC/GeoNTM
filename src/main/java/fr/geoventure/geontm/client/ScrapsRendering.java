package fr.geoventure.geontm.client;

import fr.geoventure.geontm.inventory.material.NTMMaterial.SmeltingBehavior;
import fr.geoventure.geontm.items.ScrapsModels;
import fr.geoventure.geontm.items.machine.ItemScraps;
import fr.geoventure.geocore.client.ItemModelProperties;
import fr.geoventure.geocore.client.Tints;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/** Material selection and molten colours from the authored scraps icon rules. */
public final class ScrapsRendering {
    private ScrapsRendering() {}

    public static void install() {
        ItemModelProperties.registerSelect("geontm", "scrap_material", ScrapsRendering::material);
        Tints.registerItemSource("geontm", "scraps_color", ScrapsRendering::color);
    }

    private static @Nullable String material(ItemStack stack) {
        var contents = ItemScraps.getMats(stack);
        if (contents == null) return null;
        if (ItemScraps.isLiquid(stack)) {
            if (contents.material.smeltable == SmeltingBehavior.SMELTABLE) return "liquid";
            if (contents.material.smeltable == SmeltingBehavior.ADDITIVE) return "additive";
            return ScrapsModels.key(contents.material) + "_liquid";
        }
        return ScrapsModels.key(contents.material);
    }

    private static int color(ItemStack stack) {
        var contents = ItemScraps.getMats(stack);
        return contents == null ? CommonColors.WHITE : 0xFF000000 | contents.material.moltenColor;
    }
}
