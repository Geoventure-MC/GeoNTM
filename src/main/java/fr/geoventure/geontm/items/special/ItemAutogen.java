package fr.geoventure.geontm.items.special;

import fr.geoventure.geontm.inventory.material.MaterialShapes;
import fr.geoventure.geontm.inventory.material.NTMMaterial;
import fr.geoventure.geontm.items.ModAutogenItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * One auto-generated metal-shape item, a single {@link MaterialShapes shape} of a single
 * {@link NTMMaterial material} (e.g. {@code plate_cast_iron} = an iron cast plate). The 1.12.2
 * {@code ItemAutogen} packed all materials into one item via metadata + a runtime-recoloured sprite; 1.21
 * has no metadata, so the port is <b>one registered item per material×shape</b> with a plain
 * {@code item/generated} model, no {@code custom_model_data}/select indirection, and tag-native.
 *
 * <p>The recolouring is done at load time, as in 1.7: the shape's greyscale art is
 * remapped onto the material's two solid colours while the atlas is stitched, declared by
 * {@link fr.geoventure.geontm.client.AutogenSprites}. None of these sprites is a file.
 *
 * <p>The name is 1.7's composition: the shape's template key ({@code item.geontm.part_barrel_light}, "Light %s
 * Barrel") formatted with the material's {@code geontmmat.} name. Recipes match these via {@link fr.geoventure.geontm.inventory.RecipesCommon.MaterialStack} (which resolves the exact
 * item for a material+shape).
 */
public class ItemAutogen extends MaterialShapeItem {

    public ItemAutogen(Properties properties, MaterialShapes shape, NTMMaterial material) {
        super(properties, material, shape);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.geontm." + ModAutogenItems.base(shape), material.getLocalizedName());
    }
}
