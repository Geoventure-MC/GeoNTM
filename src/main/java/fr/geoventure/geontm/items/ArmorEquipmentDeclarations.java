package fr.geoventure.geontm.items;

import fr.geoventure.geocore.dejson.Equipment;

/**
 * Equipment assets of every armour set: the texture each worn piece draws on the body and leggings
 * layers. Below 26.1 {@code legacy-assets} copies these textures to the paths the old armour layer reads.
 *
 * <p>Touches no Minecraft class: Dejson runs {@link #declare()} in a plain JVM at build time.
 */
public final class ArmorEquipmentDeclarations {

    private static final String NS = "geontm";
    private static boolean declared;

    private ArmorEquipmentDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;
        part0();
        part1();
    }

    private static void part0() {
        Equipment.humanoid(NS, "ajr", "geontm:ajr");
        Equipment.humanoid(NS, "alloy", "geontm:alloy");
        Equipment.humanoid(NS, "asbestos", "geontm:asbestos");
        Equipment.humanoid(NS, "ashglasses", "geontm:ashglasses");
        Equipment.humanoid(NS, "bismuth", "geontm:bismuth");
        Equipment.humanoid(NS, "bj", "geontm:bj");
        Equipment.humanoid(NS, "cmb", "geontm:cmb");
        Equipment.humanoid(NS, "cobalt", "geontm:cobalt");
        Equipment.humanoid(NS, "dieselsuit", "geontm:dieselsuit");
        Equipment.humanoid(NS, "dns", "geontm:dns");
        Equipment.humanoid(NS, "dnt", "geontm:dnt");
        Equipment.humanoid(NS, "envsuit", "geontm:envsuit");
        Equipment.humanoid(NS, "euphemium", "geontm:euphemium");
        Equipment.humanoid(NS, "fau", "geontm:fau");
        Equipment.humanoid(NS, "gas_mask", "geontm:gas_mask");
        Equipment.humanoid(NS, "goggles", "geontm:goggles");
        Equipment.humanoid(NS, "hazmat", "geontm:hazmat");
        Equipment.humanoid(NS, "hazmat_grey", "geontm:hazmat_grey");
        Equipment.humanoid(NS, "hazmat_grey_hood", "geontm:hazmat_grey_hood");
        Equipment.humanoid(NS, "hazmat_paa", "geontm:hazmat_paa");
        Equipment.humanoid(NS, "hazmat_red", "geontm:hazmat_red");
        Equipment.humanoid(NS, "hazmat_red_hood", "geontm:hazmat_red_hood");
        Equipment.humanoid(NS, "hev", "geontm:hev");
        Equipment.of(NS, "infamy").layer("humanoid", "geontm:infamy").register();
        Equipment.humanoid(NS, "jackt", "geontm:jackt");
        Equipment.humanoid(NS, "jackt2", "geontm:jackt2");
        Equipment.of(NS, "jetpack").layer("humanoid", "geontm:jetpack").register();
        Equipment.humanoid(NS, "liquidator", "geontm:liquidator");
        Equipment.humanoid(NS, "no9", "geontm:no9");
        Equipment.humanoid(NS, "nossy_hat", "geontm:nossy_hat");
        Equipment.humanoid(NS, "paa", "geontm:paa");
        Equipment.humanoid(NS, "rag_damp", "geontm:rag_damp");
        Equipment.humanoid(NS, "rag_piss", "geontm:rag_piss");
    }

    private static void part1() {
        Equipment.humanoid(NS, "robes", "geontm:robes");
        Equipment.humanoid(NS, "schrabidium", "geontm:schrabidium");
        Equipment.humanoid(NS, "security", "geontm:security");
        Equipment.humanoid(NS, "starmetal", "geontm:starmetal");
        Equipment.humanoid(NS, "steamsuit", "geontm:steamsuit");
        Equipment.humanoid(NS, "steel", "geontm:steel");
        Equipment.humanoid(NS, "t51", "geontm:t51");
        Equipment.humanoid(NS, "taurun", "geontm:taurun");
        Equipment.humanoid(NS, "titanium", "geontm:titanium");
        Equipment.humanoid(NS, "trenchmaster", "geontm:trenchmaster");
        Equipment.humanoid(NS, "zirconium", "geontm:zirconium");
    }
}
