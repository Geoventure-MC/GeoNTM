package fr.geoventure.geontm.recipes.tables;

import fr.geoventure.geontm.inventory.recipes.PedestalRecipes;
import fr.geoventure.geontm.inventory.recipes.PedestalRecipes.Input;
import fr.geoventure.geontm.inventory.recipes.PedestalRecipes.PedestalExtraCondition;
import fr.geoventure.geontm.inventory.recipes.PedestalRecipes.PedestalRecipe;
import fr.geoventure.geontm.inventory.recipes.PedestalRecipes.Row;
import fr.geoventure.geocore.world.RecipeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 1.7 {@code PedestalRecipes.registerDefaults}. Rows name their items by id (the row type holds ids), so 1.7's
 * {@code new ComparableStack(item, n)} and {@code new OreDictStack(MAT.shape(), n)} read {@code i("geontm:item", n)} and
 * {@code i("#c:shapes/material", n)}; {@code null} is a pedestal that must stay empty.
 */
public final class PedestalTable extends RecipeSource<Row> {

    // 1.7 weapon_mod_special metadata SCOPE / SILENCER / FURNITURE_BLACK, items of their own
    private static final String SCOPE = "geontm:weapon_mod_special_scope";
    private static final String SILENCER = "geontm:weapon_mod_special_silencer";
    private static final String FURNITURE_BLACK = "geontm:weapon_mod_special_furniture_black";

    public PedestalTable() {
        super(PedestalRecipes.TABLE.table);
    }

    @Override
    protected void registerDefaults() {
        register(new PedestalRecipe("geontm:gun_light_revolver_dani", 1,
                null, i("#c:plates/lead"), null,
                i("#c:plates/gold"), i("geontm:gun_light_revolver"), i("#c:plates/gold"),
                null, i("#c:plates/lead"), null));

        register(new PedestalRecipe("geontm:gun_maresleg_broken", 1,
                i("geontm:barbed_wire"), i("#c:plates/weaponsteel"), i("geontm:barbed_wire"),
                i("#c:plates/weaponsteel"), i("geontm:gun_maresleg"), i("#c:plates/weaponsteel"),
                i("geontm:barbed_wire"), i("#c:plates/weaponsteel"), i("geontm:barbed_wire")));

        register(new PedestalRecipe("geontm:gun_heavy_revolver_lilmac", 1,
                null, i(SCOPE), null,
                i("geontm:powder_magic"), i("geontm:gun_heavy_revolver"), i("#c:plates/weaponsteel"),
                null, i("#c:grips/bone"), i("minecraft:apple", 3)));

        register(new PedestalRecipe("geontm:gun_heavy_revolver_protege", 1,
                i("geontm:dungeon_chain", 16), i("#c:gems/cinnabar"), i("geontm:dungeon_chain", 16),
                i("geontm:scrap_nuclear"), i("geontm:gun_heavy_revolver"), i("geontm:scrap_nuclear"),
                i("geontm:dungeon_chain", 16), i("#c:gems/cinnabar"), i("geontm:dungeon_chain", 16)));

        register(new PedestalRecipe("geontm:gun_amat_subtlety", 1,
                i("#c:ingots/starmetal"), i("#c:plates_cast/aluminium"), i("#c:ingots/starmetal"),
                i("#c:plates_cast/aluminium"), i("geontm:gun_amat"), i("#c:plates_cast/aluminium"),
                i("#c:ingots/starmetal"), i("#c:plates_cast/aluminium"), i("#c:ingots/starmetal")));

        register(new PedestalRecipe("geontm:gun_amat_penance", 1,
                i("#c:ingots/starmetal"), i("#c:plates_cast/dura_steel"), i("#c:ingots/starmetal"),
                i(SILENCER), i("geontm:gun_amat"), i(FURNITURE_BLACK),
                i("#c:ingots/starmetal"), i("#c:plates_cast/dura_steel"), i("#c:ingots/starmetal")));

        register(new PedestalRecipe("geontm:gun_flamer_daybreaker", 1,
                i("#c:plates_cast/gold"), i("geontm:canned_conserve_slime"), i("#c:plates_cast/gold"),
                i("#c:ingots/phosphorus"), i("geontm:gun_flamer"), i("#c:ingots/phosphorus"),
                i("#c:plates_cast/gold"), i("geontm:stick_dynamite"), i("#c:plates_cast/gold"))
                .extra(PedestalExtraCondition.SUN));

        register(new PedestalRecipe("geontm:gun_autoshotgun_sexy", 1,
                i("geontm:bolt_spike", 16), i("geontm:wild_p"), i("geontm:bolt_spike", 16),
                i("geontm:card_qos"), i("geontm:gun_autoshotgun"), i("geontm:card_aos"),
                i("geontm:bolt_spike", 16), i("#c:ingots/starmetal", 16), i("geontm:bolt_spike", 16)));

        register(new PedestalRecipe("geontm:gun_minigun_lacunae", 1,
                null, i("geontm:powder_magic", 4), null,
                i("geontm:item_secret_selenium_steel", 4), i("geontm:gun_minigun"), i("geontm:item_secret_selenium_steel", 4),
                null, i("geontm:powder_magic", 4), null)
                .extra(PedestalExtraCondition.FULL_MOON));

        register(new PedestalRecipe("geontm:gun_laser_pistol_morning_glory", 1,
                null, i("geontm:morning_glory"), null,
                i("geontm:item_secret_selenium_steel", 2), i("geontm:gun_laser_pistol"), i("geontm:item_secret_selenium_steel", 2),
                null, i("#c:gems/emerald", 16), null));

        register(new PedestalRecipe("geontm:gun_folly", 1,
                i("geontm:item_secret_folly", 4), i("geontm:item_secret_controller", 2), i("geontm:item_secret_folly", 4),
                i("#c:ingots/bscco", 16), i("#c:storage_blocks/starmetal", 64), i("#c:ingots/bscco", 16),
                i("geontm:item_secret_folly", 4), i("geontm:item_secret_controller", 2), i("geontm:item_secret_folly", 4))
                .extra(PedestalExtraCondition.FULL_MOON).set(1));

        register(new PedestalRecipe("geontm:gun_aberrator", 1,
                null, i("geontm:item_secret_aberrator"), null,
                i("geontm:item_secret_aberrator"), i("#c:mechanisms/saturnite", 4), i("geontm:item_secret_aberrator"),
                null, i("geontm:item_secret_aberrator"), null).set(1));
        register(new PedestalRecipe("geontm:gun_aberrator_eott", 1,
                i("geontm:item_secret_aberrator"), i("geontm:item_secret_aberrator"), i("geontm:item_secret_aberrator"),
                i("geontm:item_secret_aberrator"), i("#c:mechanisms/saturnite", 16), i("geontm:item_secret_aberrator"),
                i("geontm:item_secret_aberrator"), i("geontm:item_secret_aberrator"), i("geontm:item_secret_aberrator"))
                .extra(PedestalExtraCondition.GOOD_KARMA).set(1));

        register(new PedestalRecipe("geontm:ammo_secret_folly_sm", 1,
                i("#c:ingots/starmetal"), i("geontm:powder_magic"), i("#c:ingots/starmetal"),
                i("geontm:powder_magic"), i("geontm:chunk_ore_moonstone"), i("geontm:powder_magic"),
                i("#c:ingots/starmetal"), i("geontm:powder_magic"), i("#c:ingots/starmetal"))
                .extra(PedestalExtraCondition.FULL_MOON).set(1));
        register(new PedestalRecipe("geontm:ammo_secret_folly_nuke", 1,
                i("#c:ingots/starmetal"), i("geontm:powder_magic"), i("#c:ingots/starmetal"),
                i("geontm:powder_magic"), i("geontm:ammo_standard_nuke_high", 4), i("geontm:powder_magic"),
                i("#c:ingots/starmetal"), i("geontm:powder_magic"), i("#c:ingots/starmetal"))
                .extra(PedestalExtraCondition.FULL_MOON).set(1));
        register(new PedestalRecipe("geontm:ammo_secret_p35_800", 5,
                null, null, null,
                null, i("geontm:item_secret_aberrator"), null,
                null, null, null).set(1));
        register(new PedestalRecipe("geontm:ammo_secret_p35_800_bl", 10,
                null, null, null,
                null, i("geontm:item_secret_aberrator", 3), null,
                null, null, null).set(1));
    }

    /** 1.7 {@code register(recipe)}; the id is the output's item name. */
    private void register(PedestalRecipe recipe) {
        List<Optional<Input>> grid = new ArrayList<>();
        for (Input input : recipe.input) grid.add(Optional.ofNullable(input));
        register(recipe.output.substring(recipe.output.indexOf(':') + 1),
                new Row(recipe.output, recipe.outputCount, grid, recipe.extra, recipe.recipeSet, nextOrder()));
    }

    private static Input i(String id) { return new Input(id, 1); }
    private static Input i(String id, int count) { return new Input(id, count); }
}
