package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerBook;
import fr.geoventure.geontm.inventory.container.ContainerToolBox;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Menus of the special melee / kit family: the toolbox and the Book of Boxcars. */
public final class ModMeleeKitMenus {

    private ModMeleeKitMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerToolBox>> TOOLBOX = REG.menu("toolbox", () ->
            Menus.typeWithHand((id, inventory, hand) -> new ContainerToolBox(toolboxType(), id, inventory, hand)));

    public static final RegistryObject<MenuType<ContainerBook>> BOOK = REG.menu("book_of_", () ->
            Menus.typeWithHand((id, inventory, hand) -> new ContainerBook(bookType(), id, inventory)));

    private static MenuType<ContainerToolBox> toolboxType() { return TOOLBOX.get(); }
    private static MenuType<ContainerBook> bookType() { return BOOK.get(); }

    public static void init() {}
}
