package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.dejson.ItemModels;
import fr.geoventure.geocore.dejson.Models;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.tool.ItemAmmoBag;
import fr.geoventure.geontm.items.tool.ItemCasingBag;
import fr.geoventure.geontm.items.weapon.ItemAmmoContainer;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.List;

/** Hand-registered items shared by the Sedna guns and their bag menus. */
public final class ModGunSupportItems {

    private ModGunSupportItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    // 1.7 ammo_container metadata 0/1, one item each
    public static final RegistryObject<ItemAmmoContainer> ammo_container = REG.item(
            "ammo_container", p -> new ItemAmmoContainer(p, false), p -> p.stacksTo(1)).transform(Presets.sprite());
    public static final RegistryObject<ItemAmmoContainer> ammo_container_alt = REG.item(
            "ammo_container_alt", p -> new ItemAmmoContainer(p, true), p -> p.stacksTo(1)).transform(Presets.sprite());
    public static final RegistryObject<ItemAmmoBag> ammo_bag = REG.item(
            "ammo_bag", p -> new ItemAmmoBag(p, false), p -> p.stacksTo(1)).transform(Presets.sprite());
    public static final RegistryObject<ItemAmmoBag> ammo_bag_infinite = REG.item(
            "ammo_bag_infinite", p -> new ItemAmmoBag(p, true), p -> p.stacksTo(1)).transform(Presets.sprite());
    public static final RegistryObject<ItemCasingBag> casing_bag = REG.item(
            "casing_bag", ItemCasingBag::new, p -> p.stacksTo(1)).transform(Presets.sprite());

    static {
        ModTabs.addStacksTo(GeoNtmTab.WEAPONS, () -> List.of(new ItemStack(ammo_container.get()),
                new ItemStack(ammo_container_alt.get())));
        ModTabs.addTo(GeoNtmTab.WEAPONS, ammo_bag);
        ModTabs.addTo(GeoNtmTab.WEAPONS, ammo_bag_infinite);
        ModTabs.addTo(GeoNtmTab.WEAPONS, casing_bag);
    }

    public static void init() {}
}
