package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerAutocrafter;
import fr.geoventure.geontm.inventory.container.ContainerMachineRotaryFurnace;
import fr.geoventure.geontm.inventory.container.ContainerMicrowave;
import fr.geoventure.geontm.inventory.container.ContainerFunnel;

/** Feature-local position-bound menus for the production machines. */
public final class ModProductionMenus {
    private ModProductionMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineRotaryFurnace>> ROTARY_FURNACE =
            REG.menu("machine_rotary_furnace", () -> Menus.typeWithPos(ContainerMachineRotaryFurnace::new));
    public static final RegistryObject<MenuType<ContainerMicrowave>> MICROWAVE =
            REG.menu("machine_microwave", () -> Menus.typeWithPos(ContainerMicrowave::new));
    public static final RegistryObject<MenuType<ContainerAutocrafter>> AUTOCRAFTER =
            REG.menu("machine_autocrafter", () -> Menus.typeWithPos(ContainerAutocrafter::new));
    public static final RegistryObject<MenuType<ContainerFunnel>> FUNNEL =
            REG.menu("machine_funnel", () -> Menus.typeWithPos(ContainerFunnel::new));

    public static void init() {}
}
