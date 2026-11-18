package fr.geoventure.geontm.items.weapon.sedna.factory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.ModAmmoItems;
import fr.geoventure.geontm.items.weapon.sedna.impl.ItemGunDrill;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

/** Registry boundary for the tool and power-armour guns restored from ntm-next. */
public final class NtmToolGunCatalog {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    public static RegistryObject<ItemGunDrill> gun_drill;
    public static RegistryObject<ItemGunBaseNT> gun_charge_thrower;
    public static RegistryObject<XFactoryPA.ItemGunPA> gun_pa_melee;
    public static RegistryObject<XFactoryPA.ItemGunPA> gun_pa_ranged;
    public static RegistryObject<ItemGunBaseNT> gun_fireext;

    private NtmToolGunCatalog() {}

    /** Resolves the flattened ammo identity without constructing an ItemStack during bootstrap. */
    public static Supplier<Item> standardAmmo(String enumName) {
        return ModAmmoItems.standardAmmo(enumName);
    }

    /** Declares a tool gun and keeps its source order in the ordinary weapons tab. */
    public static <T extends Item> RegistryObject<T> weapon(String id, Function<Item.Properties, ? extends T> factory) {
        RegistryObject<T> item = REG.<T>item(id, factory, p -> p);
        ModTabs.addTo(GeoNtmTab.WEAPONS, item);
        return item;
    }
}
