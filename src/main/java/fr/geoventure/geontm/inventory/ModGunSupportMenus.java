package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.tool.ItemAmmoBag;
import fr.geoventure.geontm.items.tool.ItemCasingBag;
import fr.geoventure.geontm.items.tool.ItemPlasticBag;
import fr.geoventure.geontm.inventory.container.MenuItemBox;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Menu registrations for stack-backed gun support items. */
public final class ModGunSupportMenus {

    private ModGunSupportMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<MenuItemBox>> AMMO_BAG = REG.menu("ammo_bag", () ->
            Menus.typeWithHand((id, inventory, hand) -> new MenuItemBox(ammoBagType(), id, inventory,
                    ItemAmmoBag.LAYOUT, ModDataComponents.AMMO_BAG, hand)));

    public static final RegistryObject<MenuType<MenuItemBox>> CASING_BAG = REG.menu("casing_bag", () ->
            Menus.typeWithHand((id, inventory, hand) -> new MenuItemBox(casingBagType(), id, inventory,
                    ItemCasingBag.LAYOUT, ModDataComponents.CASING_BAG, hand)));

    public static final RegistryObject<MenuType<MenuItemBox>> PLASTIC_BAG = REG.menu("plastic_bag", () ->
            Menus.typeWithHand((id, inventory, hand) -> new MenuItemBox(plasticBagType(), id, inventory,
                    ItemPlasticBag.LAYOUT, ModDataComponents.PLASTIC_BAG, hand)));

    private static MenuType<MenuItemBox> ammoBagType() { return AMMO_BAG.get(); }
    private static MenuType<MenuItemBox> casingBagType() { return CASING_BAG.get(); }
    private static MenuType<MenuItemBox> plasticBagType() { return PLASTIC_BAG.get(); }

    public static void init() {}
}
