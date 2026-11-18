package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerPWR;

/** Feature-local position-bound menu for the PWR controller. */
public final class ModPwrMenus {
    private ModPwrMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);
    public static final RegistryObject<MenuType<ContainerPWR>> PWR_CONTROLLER =
            REG.menu("pwr_controller", () -> Menus.typeWithPos(ContainerPWR::new));

    public static void init() {}
}
