package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerMachineSatLinker;
import fr.geoventure.geontm.inventory.container.ContainerSatDock;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Position-bound menus of the satellite family. The sat designator's screen has no menu. */
public final class ModSatelliteMenus {

    private ModSatelliteMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineSatLinker>> SATLINKER =
            REG.menu("machine_satlinker", () -> Menus.typeWithPos(ContainerMachineSatLinker::new));
    public static final RegistryObject<MenuType<ContainerSatDock>> SAT_DOCK =
            REG.menu("sat_dock", () -> Menus.typeWithPos(ContainerSatDock::new));

    public static void init() {}
}
