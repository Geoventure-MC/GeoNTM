package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.inventory.container.ContainerMachineRTG;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

/** Feature-local menu registration for the grey RTG. */
public final class ModRtgMenus {
    private ModRtgMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineRTG>> MACHINE_RTG = REG.menu(
            "machine_rtg_grey", () -> Menus.typeWithPos(ContainerMachineRTG::new));

    public static void init() {}
}
