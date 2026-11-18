package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerLeadBox;
import fr.geoventure.geontm.inventory.container.ContainerLemegeton;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Menus of the utility tools and books: the containment box and the Lemegeton. */
public final class ModUtilityToolMenus {

    private ModUtilityToolMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerLeadBox>> LEAD_BOX = REG.menu("containment_box", () ->
            Menus.typeWithHand((id, inventory, hand) -> new ContainerLeadBox(leadBoxType(), id, inventory, hand)));

    public static final RegistryObject<MenuType<ContainerLemegeton>> LEMEGETON = REG.menu("book_lemegeton", () ->
            Menus.typeWithHand((id, inventory, hand) -> new ContainerLemegeton(lemegetonType(), id, inventory)));

    private static MenuType<ContainerLeadBox> leadBoxType() { return LEAD_BOX.get(); }
    private static MenuType<ContainerLemegeton> lemegetonType() { return LEMEGETON.get(); }

    public static void init() {}
}
