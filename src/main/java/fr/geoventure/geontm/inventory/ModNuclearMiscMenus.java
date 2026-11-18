package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerMachineReactorBreeding;
import fr.geoventure.geontm.inventory.container.ContainerRadiolysis;
import fr.geoventure.geontm.inventory.container.ContainerReactorControl;
import fr.geoventure.geontm.inventory.container.ContainerReactorResearch;

/** Feature-local position-bound menus. */
public final class ModNuclearMiscMenus {
    private ModNuclearMiscMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerRadiolysis>> RADIOLYSIS =
            REG.menu("machine_radiolysis", () -> Menus.typeWithPos(ContainerRadiolysis::new));
    public static final RegistryObject<MenuType<ContainerMachineReactorBreeding>> REACTOR_BREEDING =
            REG.menu("machine_reactor", () -> Menus.typeWithPos(ContainerMachineReactorBreeding::new));
    public static final RegistryObject<MenuType<ContainerReactorResearch>> REACTOR_RESEARCH =
            REG.menu("machine_reactor_small", () -> Menus.typeWithPos(ContainerReactorResearch::new));
    public static final RegistryObject<MenuType<ContainerReactorControl>> REACTOR_CONTROL =
            REG.menu("machine_controller", () -> Menus.typeWithPos(ContainerReactorControl::new));

    public static void init() {}
}
