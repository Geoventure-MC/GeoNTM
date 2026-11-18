package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.world.GeoCoreArmorMaterial;

/**
 * GEONTM armour materials. Physical-defense values and material properties are copied from the matching
 * 1.12.2 {@code ArmorMaterial} declarations; radiation resistance is registered separately because it
 * is not a vanilla armour-material property.
 *
 * <p>GeoCore owns native material construction. GEONTM declares each material exactly once.
 */
public final class GeoNtmArmorMaterials {

    private GeoNtmArmorMaterials() {}

    // 1.12.2 {1,4,5,2} in [boots,legs,chest,helmet] order -> helmet 2, chest 5, legs 4, boots 1.
    public static final GeoCoreArmorMaterial HAZMAT = hazmat("hazmat", "hazmat_cloth");
    public static final GeoCoreArmorMaterial HAZMAT_RED = hazmat("hazmat_red", "hazmat_cloth_red");
    public static final GeoCoreArmorMaterial HAZMAT_GREY = hazmat("hazmat_grey", "hazmat_cloth_grey");
    /**
     * The red and grey hoods: 1.7 {@code ArmorHazmatMask} on {@code aMatHaz2}/{@code aMatHaz3} with an M65 model
     * ({@code ModelHazRed.png} / {@code ModelHazGrey.png}) instead of the armour layer. Same numbers as the rest of
     * the suit; a material of their own only so their equipment layer can be the transparent one the model needs.
     */
    public static final GeoCoreArmorMaterial HAZMAT_RED_HOOD = hazmat("hazmat_red_hood", "hazmat_cloth_red");
    public static final GeoCoreArmorMaterial HAZMAT_GREY_HOOD = hazmat("hazmat_grey_hood", "hazmat_cloth_grey");

    /**
     * 1.7 {@code ArmorGasMask}: vanilla {@code ArmorMaterial.IRON} (15, {2, 6, 5, 2}, 9), repaired with iron. The
     * worn mask is a model ({@code ArmorModRenderLayer}); the equipment layer texture is transparent, as the
     * goggles' is, because 1.7's {@code getArmorModel} replaced the armour layer outright.
     */
    public static final GeoCoreArmorMaterial GAS_MASK = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "gas_mask", 15, 2, 6, 5, 2, 9, 0.0F, 0.0F, "repairs/infamy");

    /** 1.12.2 {@code aMatSteel}: multiplier 30, defense {3, 6, 8, 3}, enchantability 5. */
    public static final GeoCoreArmorMaterial STEEL = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "steel", 30, 3, 8, 6, 3, 5, 0.0F, 0.0F, "repairs/steel");

    /** 1.7 {@code ArmorMaterial.CHAIN}: 15, {2, 5, 4, 1}, 12; its own armor texture path. */
    public static final GeoCoreArmorMaterial ROBES = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "robes", 15, 2, 5, 4, 1, 12, 0.0F, 0.0F, "repairs/steel");

    /** 1.12.2 {@code aMatTitan}: multiplier 25, defense {3, 6, 8, 3}, enchantability 9. */
    public static final GeoCoreArmorMaterial TITANIUM = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "titanium", 25, 3, 8, 6, 3, 9, 0.0F, 0.0F, "repairs/titanium");

    /**
     * T-51b power armour (1.12.2 {@code enumArmorMaterialT51}): durability 150, defense {3, 8, 6, 3} in
     * [helmet, chest, legs, boots] order, enchantability 0, no toughness.
     *
     * <p>The repair tag is the same armour plate the suit is built from. Enchantability is zero because
     * upstream refuses enchantments on power armour outright; see {@code ArmorFSBPowered}.
     */
    public static final GeoCoreArmorMaterial T51 = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "t51", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/t51");

    /** Legacy Blackjack armor material: the separate identity is required for full-set detection. */
    public static final GeoCoreArmorMaterial BJ = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "bj", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/bj");

    /** Legacy DNT nanosuit material: the separate identity is required for full-set detection. */
    public static final GeoCoreArmorMaterial DNS = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "dns", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/dns");

    public static final GeoCoreArmorMaterial AJR = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "ajr", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/ajr");

    /** Jetpack modules were not armor themselves in 1.12; this material keeps direct wear non-protective. */
    public static final GeoCoreArmorMaterial JETPACK = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "jetpack", 1, 0, 0, 0, 0, 0, 0.0F, 0.0F, "repairs/steel");

    /** 1.12.2 {@code aMatCobalt}: multiplier 70, defense {3, 6, 8, 3}, enchantability 60. */
    public static final GeoCoreArmorMaterial COBALT = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "cobalt", 70, 3, 8, 6, 3, 60, 0.0F, 0.0F, "repairs/cobalt");

    /** 1.12.2 {@code aMatStarmetal}: multiplier 150, defense {3, 6, 8, 3}, enchantability 100. */
    public static final GeoCoreArmorMaterial STARMETAL = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "starmetal", 150, 3, 8, 6, 3, 100, 0.0F, 0.0F, "repairs/starmetal");

    /** 1.12.2 {@code aMatSchrab}: multiplier 100, defense {3, 6, 8, 3}, enchantability 50. */
    public static final GeoCoreArmorMaterial SCHRABIDIUM = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "schrabidium", 100, 3, 8, 6, 3, 50, 0.0F, 0.0F, "repairs/schrabidium");

    /** 1.12.2 {@code aMatTaurun} ("GEONTM_TRENCH"): multiplier 150, defense {3, 8, 6, 3}, enchantability 10, repaired by iron plate. */
    public static final GeoCoreArmorMaterial TAURUN = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "taurun", 150, 3, 8, 6, 3, 10, 0.0F, 0.0F, "repairs/taurun");

    /** 1.12.2 {@code aMatSecurity}: multiplier 100, defense {3, 6, 8, 3}, enchantability 15. */
    public static final GeoCoreArmorMaterial SECURITY = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "security", 100, 3, 8, 6, 3, 15, 0.0F, 0.0F, "repairs/security");

    /** 1.12.2 {@code aMatAsbestos}: multiplier 20, defense {1, 3, 4, 1}, enchantability 5. */
    public static final GeoCoreArmorMaterial ASBESTOS = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "asbestos", 20, 1, 4, 3, 1, 5, 0.0F, 0.0F, "repairs/asbestos");

    /** 1.12.2 {@code aMatPaa}: multiplier 75, defense {3, 6, 8, 3}, enchantability 25. */
    public static final GeoCoreArmorMaterial PAA = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "paa", 75, 3, 8, 6, 3, 25, 0.0F, 0.0F, "repairs/paa");

    // Armor sets A (1.7 MainRegistry / ModItemsArmor). 1.7 defense arrays are {helmet, chest,
    // legs, boots}; 1.7 has no toughness, so these materials declare 0. Each EnumHelper call made its own
    // enum constant, and set identity is reference equality, so every 1.7 material is its own object here.

    /** 1.7 {@code aMatAlloy} ("GEONTM_ALLOY", deprecated): 40, {3, 8, 6, 3}, 12; it never had a repair material. */
    public static final GeoCoreArmorMaterial ALLOY = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "alloy", 40, 3, 8, 6, 3, 12, 0.0F, 0.0F, "repairs/alloy");
    /** 1.7 {@code aMatCMB}: 60, {3, 8, 6, 3}, 50, repaired by CMB steel. */
    public static final GeoCoreArmorMaterial CMB = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "cmb", 60, 3, 8, 6, 3, 50, 0.0F, 0.0F, "repairs/cmb");
    /** 1.7 {@code aMatBismuth}: 100, {3, 8, 6, 3}, 100, repaired by bismuth plate. */
    public static final GeoCoreArmorMaterial BISMUTH = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "bismuth", 100, 3, 8, 6, 3, 100, 0.0F, 0.0F, "repairs/bismuth_armor");
    /** 1.7 {@code aMatEuph}: 15,000,000, {3, 8, 6, 3}, 100. ArmorEuphemium also ignores every damage write. */
    public static final GeoCoreArmorMaterial EUPHEMIUM = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "euphemium", 15_000_000, 3, 8, 6, 3, 100, 0.0F, 0.0F, "repairs/euphemium");
    /** 1.7 {@code aMatZirconium} ("GEONTM_ZIRCONIUM"): 1000, {2, 5, 3, 1}, 1000, repaired by zirconium. */
    public static final GeoCoreArmorMaterial ZIRCONIUM = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "zirconium", 1000, 2, 5, 3, 1, 1000, 0.0F, 0.0F, "repairs/zirconium");
    /** 1.7 {@code aMatDNT} ("GEONTM_DNT_LOLOLOL"): durability 3, {1, 1, 1, 1}, 0 - the joke set. */
    public static final GeoCoreArmorMaterial DNT = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "dnt", 3, 1, 1, 1, 1, 0, 0.0F, 0.0F, "repairs/dnt");
    /** 1.7 {@code aMatHEV}: 150, {3, 8, 6, 3}, 0, repaired by HEV plating. */
    public static final GeoCoreArmorMaterial HEV = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "hev", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/hev");
    /** 1.7 {@code aMatFau} ("GEONTM_DIGAMMA"): 150, {3, 8, 6, 3}, 0, repaired by Fau plating. */
    public static final GeoCoreArmorMaterial FAU = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "fau", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/fau");
    /**
     * 1.7 {@code aMatTrench} ("GEONTM_TRENCH"): 150, {3, 8, 6, 3}, 0, repaired by iron plate. It shares its enum
     * name with Taurun's material but is a distinct constant, so the two suits never form a mixed set.
     */
    public static final GeoCoreArmorMaterial TRENCHMASTER = new GeoCoreArmorMaterial(
            Constants.MOD_ID, "trenchmaster", 150, 3, 8, 6, 3, 0, 0.0F, 0.0F, "repairs/taurun");

    private static GeoCoreArmorMaterial hazmat(String name, String repairIngredient) {
        return new GeoCoreArmorMaterial(Constants.MOD_ID, name, 60, 2, 5, 4, 1, 5,
                0.0F, 0.0F, "repairs/" + repairIngredient);
    }
}
