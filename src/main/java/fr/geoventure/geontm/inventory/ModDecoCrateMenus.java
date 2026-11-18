package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerCrateTungsten;
import fr.geoventure.geontm.inventory.container.ContainerFileCabinet;
import fr.geoventure.geontm.inventory.container.ContainerSafe;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Position-bound menus of the tungsten crate, the safe and the filing cabinet. */
public final class ModDecoCrateMenus {

    private ModDecoCrateMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerCrateTungsten>> CRATE_TUNGSTEN =
            REG.menu("crate_tungsten", () -> Menus.typeWithPos(ContainerCrateTungsten::new));
    public static final RegistryObject<MenuType<ContainerSafe>> SAFE =
            REG.menu("safe", () -> Menus.typeWithPos(ContainerSafe::new));
    public static final RegistryObject<MenuType<ContainerFileCabinet>> FILE_CABINET =
            REG.menu("filing_cabinet", () -> Menus.typeWithPos(ContainerFileCabinet::new));

    public static void init() {}
}
