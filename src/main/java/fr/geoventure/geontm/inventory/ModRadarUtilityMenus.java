package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerForceField;
import fr.geoventure.geontm.inventory.container.ContainerMachineRadarNT;
import fr.geoventure.geontm.inventory.container.ContainerSoyuzCapsule;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/**
 * Position-bound menus of the radar and utility machines. The radar map itself is a screen with no menu; its
 * link-slot view is {@link #RADAR}. The orbus opens the barrel's menu.
 */
public final class ModRadarUtilityMenus {

    private ModRadarUtilityMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineRadarNT>> RADAR =
            REG.menu("machine_radar", () -> Menus.typeWithPos(ContainerMachineRadarNT::new));
    public static final RegistryObject<MenuType<ContainerForceField>> FORCEFIELD =
            REG.menu("machine_forcefield", () -> Menus.typeWithPos(ContainerForceField::new));
    public static final RegistryObject<MenuType<ContainerSoyuzCapsule>> SOYUZ_CAPSULE =
            REG.menu("soyuz_capsule", () -> Menus.typeWithPos(ContainerSoyuzCapsule::new));

    public static void init() {}
}
