package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerMachineCustom;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.registration.Reg;

/** The custom machine controller's position-bound menu. */
public final class ModCustomMachineMenus {

    private ModCustomMachineMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineCustom>> CUSTOM_MACHINE =
            REG.menu("custom_machine", () -> Menus.typeWithPos(ContainerMachineCustom::new));

    public static void init() {}
}
