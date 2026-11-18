package fr.geoventure.geontm.inventory.recipes;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.recipes.loader.DataGenericRecipes;
import fr.geoventure.geontm.inventory.recipes.loader.GenericRecipe;

/**
 * The precision assembler (1.7 {@code PrecAssRecipes}): the datapack table {@code geontm:precision_assembler}, one file per
 * row in {@code data/geontm/recipe/precision_assembler/}, converted once from the former Java table and the source now.
 *
 * <p>The circuits, upgrades and their {@code .recycle} rows exist only in 528 mode ({@code "requires": ["enable_528"]});
 * a product is a weighted choice between the item and its broken form ({@code geontm:broken_item} with
 * {@code item_data.broken_item}), and the recycle row returns each input at the reclaim chance. 1.7 lowered most of
 * those chances in expensive mode, so such rows come as two files, one per mode. The recycle rows were derived from
 * their product rows at registration ({@code registerPair}) and are literal rows now. The two blueprint folders are in
 * every mode.
 */
public final class PrecAssRecipes extends DataGenericRecipes<GenericRecipe> {

    public static final PrecAssRecipes INSTANCE = new PrecAssRecipes();

    private PrecAssRecipes() {
        super(Constants.MOD_ID, "precision_assembler");
    }

    /** Declares the table at mod construction (its recipe type and serializer register with HBM's content). */
    public static void declare() {}
}
