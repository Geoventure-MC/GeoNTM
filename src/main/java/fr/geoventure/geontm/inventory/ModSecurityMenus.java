package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerMachineKeyForge;

/** Feature-local position-bound menus: the locksmith table. */
public final class ModSecurityMenus {
    private ModSecurityMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);
    public static final RegistryObject<MenuType<ContainerMachineKeyForge>> MACHINE_KEYFORGE =
            REG.menu("machine_keyforge", () -> Menus.typeWithPos(ContainerMachineKeyForge::new));

    public static void init() {}
}
