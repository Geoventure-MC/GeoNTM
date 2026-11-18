package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerMachineSiren;

/** Feature-local position-bound menus: only the siren opens one. */
public final class ModPowerGenMenus {
    private ModPowerGenMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineSiren>> SIREN =
            REG.menu("machine_siren", () -> Menus.typeWithPos(ContainerMachineSiren::new));

    public static void init() {}
}
