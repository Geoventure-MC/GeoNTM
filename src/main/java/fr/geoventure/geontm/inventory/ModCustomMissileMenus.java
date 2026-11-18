package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerCompactLauncher;
import fr.geoventure.geontm.inventory.container.ContainerLaunchPadRusted;
import fr.geoventure.geontm.inventory.container.ContainerLaunchTable;
import fr.geoventure.geontm.inventory.container.ContainerMachineMissileAssembly;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Position-bound menus of the custom missile system. The large pad reuses the launch pad menu, as in 1.7. */
public final class ModCustomMissileMenus {

    private ModCustomMissileMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineMissileAssembly>> MISSILE_ASSEMBLY =
            REG.menu("machine_missile_assembly", () -> Menus.typeWithPos(ContainerMachineMissileAssembly::new));
    public static final RegistryObject<MenuType<ContainerCompactLauncher>> COMPACT_LAUNCHER =
            REG.menu("compact_launcher", () -> Menus.typeWithPos(ContainerCompactLauncher::new));
    public static final RegistryObject<MenuType<ContainerLaunchTable>> LAUNCH_TABLE =
            REG.menu("launch_table", () -> Menus.typeWithPos(ContainerLaunchTable::new));
    public static final RegistryObject<MenuType<ContainerLaunchPadRusted>> LAUNCH_PAD_RUSTED =
            REG.menu("launch_pad_rusted", () -> Menus.typeWithPos(ContainerLaunchPadRusted::new));

    public static void init() {}
}
