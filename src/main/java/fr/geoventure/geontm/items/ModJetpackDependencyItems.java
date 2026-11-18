package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.special.ItemHot;
import fr.geoventure.geontm.items.special.ItemSteelDusted;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;

/** Dedicated registrations for non-plain dependencies of the jetpack progression. */
public final class ModJetpackDependencyItems {

    private ModJetpackDependencyItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    // 1.7 ItemRendererHot: the heat glow over the inventory icon, declared after the sprite it draws over.
    /** Old ItemHotDusted: ten component-backed metadata stages, cooled by the heat countdown. */
    public static final RegistryObject<ItemSteelDusted> ingot_steel_dusted = REG.item(
            "ingot_steel_dusted", ItemSteelDusted::new).transform(Presets.sprite())
            .andThen(h -> ItemRendererDeclarations.overSprite(h.path(), h.path(), "hot", "generated"));

    /** Old ItemHot(100); the effective legacy global scale is preserved by ItemHot. */
    public static final RegistryObject<ItemHot> ingot_chainsteel = REG.item(
            "ingot_chainsteel", p -> new ItemHot(p, ItemHot.LEGACY_HEAT), p -> p).transform(Presets.sprite())
            .andThen(h -> ItemRendererDeclarations.overSprite(h.path(), h.path(), "hot", "generated"));

    // mp_thruster_10_xenon moved to ModCustomMissileItems with the rest of the custom missile parts.

    static {
        ModTabs.addTo(GeoNtmTab.MATERIALS, ingot_steel_dusted);
        ModTabs.addTo(GeoNtmTab.MATERIALS, ingot_chainsteel);
    }

    public static void init() {}
}
