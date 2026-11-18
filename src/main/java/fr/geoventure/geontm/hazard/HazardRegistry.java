package fr.geoventure.geontm.hazard;

import static fr.geoventure.geontm.inventory.OreDictManager.*;

import fr.geoventure.geontm.api.data.HazardDataMaps;
import fr.geoventure.geontm.hazard.type.HazardTypeAsbestos;
import fr.geoventure.geontm.hazard.type.HazardTypeBlinding;
import fr.geoventure.geontm.hazard.type.HazardTypeCoal;
import fr.geoventure.geontm.hazard.type.HazardTypeCold;
import fr.geoventure.geontm.hazard.type.HazardTypeContaminating;
import fr.geoventure.geontm.hazard.type.HazardTypeDigamma;
import fr.geoventure.geontm.hazard.type.HazardTypeExplosive;
import fr.geoventure.geontm.hazard.type.HazardTypeHot;
import fr.geoventure.geontm.hazard.type.HazardTypeHydroactive;
import fr.geoventure.geontm.hazard.type.HazardTypeRadiation;
import fr.geoventure.geontm.hazard.type.HazardTypeToxic;
import fr.geoventure.geontm.hazard.type.IHazardType;
import fr.geoventure.geontm.hazard.transformer.HazardTransformerCustom;
import fr.geoventure.geontm.hazard.transformer.HazardTransformerRadiationME;
import fr.geoventure.geontm.hazard.transformer.HazardTransformerRadiationNBT;
import fr.geoventure.geontm.hazard.transformer.HazardTransformerZirnox;
import fr.geoventure.geontm.hazard.transformer.IHazardTransformer;
import fr.geoventure.geontm.hazard.modifier.HazardModifierFuelRadiation;
import fr.geoventure.geontm.hazard.modifier.HazardModifierRBMKHot;
import fr.geoventure.geontm.hazard.modifier.HazardModifierRBMKRadiation;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.fluid.FluidType;
import fr.geoventure.geontm.inventory.fluid.Fluids;
import fr.geoventure.geontm.items.ModIngredientItems;
import fr.geoventure.geontm.items.ModRBMKItems;
import fr.geoventure.geontm.items.machine.ItemRBMKPellet;
import fr.geoventure.geontm.items.machine.ItemRBMKRod;
import fr.geoventure.geontm.inventory.material.MaterialShapes;
import fr.geoventure.geontm.inventory.material.NTMMaterial;
import fr.geoventure.geontm.items.special.ItemCell;
import fr.geoventure.geocore.Ids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Registers every item/material hazard, ported from 1.12.2's {@code HazardRegistry} (+ the radioactive
 * material families that the old code registered from {@code OreDictManager.rad(...)} chains, here
 * folded into {@link #registerMat}). All the isotope floats and shape multipliers are copied unchanged.
 *
 * <p>The specification is 1.7.10's {@code HazardRegistry.registerItems} plus the hazard frames of its
 * {@code OreDictManager.registerOres} ({@link #registerMaterialFrames}); {@code HazardGameTests.hazard_registry_17}
 * pins a sample of the values, and a test compares every registration.
 */
@SuppressWarnings("unused")
public class HazardRegistry {

    // Isotope radiation constants (RAD/s), as in 1.7
    public static final float gen_S = 10_000F, gen_H = 2_000F, gen_10D = 100F, gen_100D = 80F,
            gen_1Y = 50F, gen_10Y = 30F, gen_100Y = 10F, gen_1K = 7.5F, gen_10K = 6.25F, gen_100K = 5F,
            gen_1M = 2.5F, gen_10M = 1.5F, gen_100M = 1F, gen_1B = 0.5F, gen_10B = 0.1F;

    public static final float co60 = 30.0F, sr90 = 15.0F, tc99 = 2.75F, i131 = 150.0F, xe135 = 1250.0F,
            cs137 = 20.0F, au198 = 500.0F, pb209 = 10000.0F, at209 = 7500.0F, po210 = 75.0F, ra226 = 7.5F,
            ac227 = 30.0F, th232 = 0.1F, thf = 1.75F, u = 0.35F, u233 = 5.0F, u235 = 1.0F, u238 = 0.25F,
            uf = 0.5F, uzh = 0.125F, np237 = 2.5F, npf = 1.5F, pu = 7.5F, purg = 6.25F, pu238 = 10.0F,
            pu239 = 5.0F, pu240 = 7.5F, pu241 = 25.0F, puf = 4.25F, am241 = 8.5F, am242 = 9.5F, amrg = 9.0F,
            amf = 4.75F, mox = 2.5F, sa326 = 15.0F, sa327 = 17.5F, saf = 5.85F, sas3 = 5F, gh336 = 5.0F,
            mud = 1.0F, radsource_mult = 3.0F;
    public static final float pobe = po210 * radsource_mult, rabe = ra226 * radsource_mult, pube = pu238 * radsource_mult;
    public static final float bf = 300_000.0F, bfb = 500_000.0F;
    public static final float sr = sa326 * 0.1F, sb = sa326 * 0.1F, trx = 25.0F, trn = 0.1F, wst = 15.0F,
            wstv = 7.5F, yc = u, fo = 10F;

    // Shape multipliers
    public static final float nugget = 0.1F, ingot = 1.0F, gem = 1.0F, plate = ingot, plateCast = plate * 3,
            powder_mult = 3.0F, powder = ingot * powder_mult, powder_tiny = nugget * powder_mult, ore = ingot,
            block = 10.0F, crystal = block, billet = 0.5F, rtg = billet * 3, rod = 0.5F, rod_dual = rod * 2,
            rod_quad = rod * 4, rod_rbmk = rod * 8;

    /** Named hazard types, in registration order. Declaration order is the built-ins' order. */
    private static final Map<ResourceLocation, IHazardType> TYPES = new LinkedHashMap<>();
    private static final Map<IHazardType, ResourceLocation> TYPE_IDS = new IdentityHashMap<>();

    /**
     * Names a hazard type so a recipe, a command or another mod's registration pass can find it without the
     * field. Returns the type, to initialise the constant that holds it.
     *
     * <p>{@link HazardSystem} keys everything off type identity, so an unnamed type works everywhere.
     */
    public static <T extends IHazardType> T registerType(final ResourceLocation id, final T type) {
        final IHazardType existing = TYPES.putIfAbsent(id, type);
        if (existing != null && existing != type) {
            throw new IllegalStateException("Hazard type " + id + " is already registered to " + existing);
        }
        TYPE_IDS.putIfAbsent(type, id);
        return type;
    }

    public static @Nullable IHazardType getType(final ResourceLocation id) {
        return TYPES.get(id);
    }

    public static @Nullable ResourceLocation getTypeId(final IHazardType type) {
        return TYPE_IDS.get(type);
    }

    /** Every named hazard type, built-in and third-party alike. */
    public static Map<ResourceLocation, IHazardType> types() {
        return Collections.unmodifiableMap(TYPES);
    }

    private static ResourceLocation hazardId(final String path) {
        return Ids.of(Constants.MOD_ID, path);
    }

    // Hazard types
    public static final IHazardType RADIATION = registerType(hazardId("radiation"), new HazardTypeRadiation());
    public static final IHazardType CONTAMINATING = registerType(hazardId("contaminating"), new HazardTypeContaminating());
    public static final IHazardType DIGAMMA = registerType(hazardId("digamma"), new HazardTypeDigamma());
    public static final IHazardType HOT = registerType(hazardId("hot"), new HazardTypeHot());
    public static final IHazardType BLINDING = registerType(hazardId("blinding"), new HazardTypeBlinding());
    public static final IHazardType ASBESTOS = registerType(hazardId("asbestos"), new HazardTypeAsbestos());
    public static final IHazardType COAL = registerType(hazardId("coal"), new HazardTypeCoal());
    public static final IHazardType HYDROACTIVE = registerType(hazardId("hydroactive"), new HazardTypeHydroactive());
    public static final IHazardType EXPLOSIVE = registerType(hazardId("explosive"), new HazardTypeExplosive());
    public static final IHazardType TOXIC = registerType(hazardId("toxic"), new HazardTypeToxic());
    public static final IHazardType COLD = registerType(hazardId("cold"), new HazardTypeCold());

    public static void registerTrafos() {
        HazardSystem.trafos.add(new HazardTransformerRadiationNBT());
        HazardSystem.trafos.add(new HazardTransformerZirnox());
        HazardSystem.trafos.add(HazardTransformerCustom.INSTANCE);
        // 1.7 registered cell_tritium and cell_sas3 as items; here they are one component-backed cell, whose hazards are
        // the geontm:cell_hazards fluid data map, keyed by the fluid it holds.
        HazardSystem.trafos.add(IHazardTransformer.post((stack, entries) -> {
            if (!(stack.getItem() instanceof ItemCell)) return;
            FluidType fluid = ItemCell.getFluidType(stack);
            if (fluid == null || fluid == Fluids.NONE) return;
            HazardSpec spec = HazardDataMaps.CELL_HAZARDS.get(Fluids.toVanilla(fluid));
            if (spec != null) entries.addAll(spec.toData().entries);
        }));
        // 1.7 HazardRegistry:350-355 (the MK2 pile rods) are item entries of geontm:hazards: one item per rod type here.
        HazardSystem.trafos.add(new HazardTransformerRadiationME());
        // RadiationContainer is added by ModUtilityToolContent; ForgeFluid has no port counterpart.
    }

    // The item and material hazards that 1.7 registered here ({@code registerItems}, the OreDictManager hazard frames,
    // the ZIRNOX / plate / RBMK / Watz fuels and the hazard blocks) are the geontm:hazards item data map
    // (data/geontm/value_maps/item/hazards.json, HazardDataMaps.HAZARDS), pinned by the GameTest
    // value_map_hazards_equal_17. The constants above are the numbers it is built from; transformers and
    // modifiers also use some of them.
}
