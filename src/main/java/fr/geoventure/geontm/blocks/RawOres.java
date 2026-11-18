package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.inventory.RecipesCommon.TagStack;
import fr.geoventure.geontm.inventory.material.NTMMaterial;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.registry.Loot;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** port: ntm-next's raw ore drops, picked at each drop by geontm.cfg's {@code 1.90_enableRawOres} (flag {@code raw_ores}). */
public final class RawOres {

    /** A raw item's share of its ore's processing yield; Fortune multiplies raw drops, so a raw item makes less. */
    public static final float SHARE = 2F / 3F;

    private RawOres() {}

    /** Silk touch keeps the ore, else one {@code raw} on vanilla's ore fortune; {@code legacy} while raw ores are off. */
    public static Loot drop(String raw, Loot legacy) {
        return Loot.either("raw_ores", Loot.oreMultiplied(raw, 1, 1), legacy);
    }

    /** {@link #SHARE} of an ore row's amount, rounded. */
    public static int share(int oreAmount) {
        return Math.round(oreAmount * SHARE);
    }

    /** {@code c:raw_materials/<material>}. */
    public static TagKey<Item> tag(NTMMaterial material) {
        return TagKey.create(Registries.ITEM, Ids.of("c", "raw_materials/" + material.name));
    }

    public static TagStack stack(NTMMaterial material) {
        return new TagStack(tag(material));
    }
}
