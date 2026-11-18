package fr.geoventure.geontm.recipes.tables;

import static fr.geoventure.geontm.inventory.OreDictManager.*;

import fr.geoventure.geontm.inventory.RecipesCommon.AStack;
import fr.geoventure.geontm.inventory.RecipesCommon.ComparableStack;
import fr.geoventure.geontm.inventory.RecipesCommon.TagStack;
import fr.geoventure.geontm.inventory.fluid.FluidStack;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.inventory.material.Mats;
import fr.geoventure.geontm.inventory.material.NTMMaterial;
import fr.geoventure.geontm.inventory.recipes.CombinationRecipes;
import fr.geoventure.geontm.inventory.recipes.CombinationRecipes.Row;
import fr.geoventure.geontm.inventory.recipes.loader.ItemSpec;
import fr.geoventure.geontm.items.ItemEnums.EnumAshType;
import fr.geoventure.geontm.items.ItemEnums.EnumChunkType;
import fr.geoventure.geontm.items.ItemEnums.EnumCokeType;
import fr.geoventure.geontm.items.ItemEnums.EnumTarType;
import fr.geoventure.geontm.items.ModBedrockOreItems;
import fr.geoventure.geontm.items.ModFinalConsumableItems;
import fr.geoventure.geontm.items.ModIngredientItems;
import fr.geoventure.geontm.items.ModIntermediateItems;
import fr.geoventure.geontm.items.ModItems;
import fr.geoventure.geontm.items.ModOilItems;
import fr.geoventure.geontm.items.special.BedrockOreType;
import fr.geoventure.geontm.items.special.ItemBedrockOreGraded.Grade;
import fr.geoventure.geocore.world.RecipeSource;
import fr.geoventure.geocore.Ids;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** 1.7 {@code CombinationRecipes.registerDefaults}. */
public final class CombinationTable extends RecipeSource<Row> {

    private static final NTMMaterial COAL = Mats.MAT_COAL;
    private static final NTMMaterial LIGNITE = Mats.MAT_LIGNITE;

    public CombinationTable() {
        super(CombinationRecipes.TABLE.table);
    }

    @Override
    protected void registerDefaults() {
        put(COAL.gem(),     new Pair(ModOilItems.coke(EnumCokeType.COAL, 1), new FluidStack(Fluids.COALCREOSOTE, 100)));
        put(COAL.dust(),    new Pair(ModOilItems.coke(EnumCokeType.COAL, 1), new FluidStack(Fluids.COALCREOSOTE, 100)));
        put(new ComparableStack(ModFinalConsumableItems.briquette_coal.get()), new Pair(ModOilItems.coke(EnumCokeType.COAL, 1), new FluidStack(Fluids.COALCREOSOTE, 150)));

        put(LIGNITE.gem(),                                                    new Pair(ModOilItems.coke(EnumCokeType.LIGNITE, 1), new FluidStack(Fluids.COALCREOSOTE, 50)));
        put(LIGNITE.dust(),                                                   new Pair(ModOilItems.coke(EnumCokeType.LIGNITE, 1), new FluidStack(Fluids.COALCREOSOTE, 50)));
        put(new ComparableStack(ModFinalConsumableItems.briquette_lignite.get()), new Pair(ModOilItems.coke(EnumCokeType.LIGNITE, 1), new FluidStack(Fluids.COALCREOSOTE, 100)));

        put(CHLOROCALCITE.dust(),                           new Pair(new ItemStack(ModItems.powder_calcium.get()), new FluidStack(Fluids.CHLORINE, 250)));
        put(MOLYSITE.dust(),                                new Pair(new ItemStack(Items.IRON_INGOT), new FluidStack(Fluids.CHLORINE, 250)));
        put(CINNABAR.gem(),                                 new Pair(new ItemStack(ModItems.sulfur.get()), new FluidStack(Fluids.MERCURY, 100))); // 1.7 CINNABAR.crystal() "crystalCinnabar" holds only the cinnebar gem (OreDictManager CINNABAR.crystal(cinnebar)), as c:gems/cinnabar holds geontm:cinnabar; the port's c:crystals/cinnabar is the crystallizer's crystal_cinnabar, which 1.7 never registered under that name
        put(new ComparableStack(Items.GLOWSTONE_DUST),      new Pair(new ItemStack(ModItems.sulfur.get()), new FluidStack(Fluids.CHLORINE, 100)));
        put(SODALITE.gem(),                                 new Pair(new ItemStack(ModItems.powder_sodium.get()), new FluidStack(Fluids.CHLORINE, 100)));
        put(new ComparableStack(ModIntermediateItems.chunk(EnumChunkType.CRYOLITE)), new Pair(new ItemStack(ModItems.powder_aluminium.get(), 1), new FluidStack(Fluids.LYE, 150)));
        put(NA.dust(),                                      new Pair(null, new FluidStack(Fluids.SODIUM, 100)));
        put(dust("limestone"),                              new Pair(new ItemStack(ModItems.powder_calcium.get()), new FluidStack(Fluids.CARBONDIOXIDE, 50))); // 1.7 LIMESTONE.dust()

        put(new TagStack(ItemTags.LOGS),        new Pair(new ItemStack(Items.CHARCOAL),                     new FluidStack(Fluids.WOODOIL, 250))); // 1.7 KEY_LOG, coal:1
        put(new TagStack(ItemTags.SAPLINGS),    new Pair(ModIngredientItems.ash(EnumAshType.WOOD, 1),       new FluidStack(Fluids.WOODOIL, 50))); // 1.7 KEY_SAPLING
        put(new ComparableStack(ModFinalConsumableItems.briquette_wood.get()), new Pair(new ItemStack(Items.CHARCOAL), new FluidStack(Fluids.WOODOIL, 500)));

        put(new ComparableStack(ModOilItems.tar(EnumTarType.CRUDE)),    new Pair(ModOilItems.coke(EnumCokeType.PETROLEUM, 1), null));
        put(new ComparableStack(ModOilItems.tar(EnumTarType.CRACK)),    new Pair(ModOilItems.coke(EnumCokeType.PETROLEUM, 1), null));
        put(new ComparableStack(ModOilItems.tar(EnumTarType.COAL)),     new Pair(ModOilItems.coke(EnumCokeType.COAL, 1), null));
        put(new ComparableStack(ModOilItems.tar(EnumTarType.WOOD)),     new Pair(ModOilItems.coke(EnumCokeType.COAL, 1), null));

        put(new ComparableStack(Items.SUGAR_CANE), new Pair(new ItemStack(Items.SUGAR, 2), new FluidStack(Fluids.ETHANOL, 50)));
        put(new ComparableStack(Items.CLAY), new Pair(new ItemStack(Items.BRICKS, 1), null));

        for (BedrockOreType type : BedrockOreType.VALUES) { // 1.7 BedrockOreType.values(): the six GEONTM types
            put(new ComparableStack(ModBedrockOreItems.get(Grade.BASE, type)), new Pair(ModBedrockOreItems.make(Grade.BASE_ROASTED, type), new FluidStack(Fluids.VITRIOL, 50)));
            put(new ComparableStack(ModBedrockOreItems.get(Grade.PRIMARY, type)), new Pair(ModBedrockOreItems.make(Grade.PRIMARY_ROASTED, type), new FluidStack(Fluids.VITRIOL, 50)));
            put(new ComparableStack(ModBedrockOreItems.get(Grade.SULFURIC_BYPRODUCT, type)), new Pair(ModBedrockOreItems.make(Grade.SULFURIC_ROASTED, type), new FluidStack(Fluids.VITRIOL, 50)));
            put(new ComparableStack(ModBedrockOreItems.get(Grade.SOLVENT_BYPRODUCT, type)), new Pair(ModBedrockOreItems.make(Grade.SOLVENT_ROASTED, type), new FluidStack(Fluids.VITRIOL, 50)));
            put(new ComparableStack(ModBedrockOreItems.get(Grade.RAD_BYPRODUCT, type)), new Pair(ModBedrockOreItems.make(Grade.RAD_ROASTED, type), new FluidStack(Fluids.VITRIOL, 50)));
        }
    }

    /** 1.7 {@code new Pair(itemOutput, fluidOutput)}; either may be null. */
    private record Pair(ItemStack item, FluidStack fluid) {}

    /**
     * 1.7 {@code recipes.put(input, output)}. The id is the input's item name, or its tag path with {@code _} for
     * {@code /} ({@code minecraft:logs} is {@code logs}).
     */
    private void put(AStack input, Pair output) {
        String id = input instanceof TagStack tag ? tag.tag.location().getPath().replace('/', '_')
                : BuiltInRegistries.ITEM.getKey(((ComparableStack) input).item).getPath();
        register(id, new Row(input, Optional.ofNullable(output.item()).map(stack -> ItemSpec.of(stack).getOrThrow()),
                Optional.ofNullable(output.fluid()), nextOrder()));
    }

    /** A 1.7 {@code dust<Name>} ore name the port has no material for: its {@code c:dusts/} tag. */
    private static AStack dust(String name) {
        return new TagStack(TagKey.create(Registries.ITEM, Ids.of("c", "dusts/" + name)));
    }
}
