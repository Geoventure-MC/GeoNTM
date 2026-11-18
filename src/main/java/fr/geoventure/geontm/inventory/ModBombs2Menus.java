package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerNukeCustom;
import fr.geoventure.geontm.inventory.container.ContainerNukeFleija;
import fr.geoventure.geontm.inventory.container.ContainerNukeFstbmb;
import fr.geoventure.geontm.inventory.container.ContainerNukePrototype;
import fr.geoventure.geontm.inventory.container.ContainerNukeSolinium;

/** Position-bound menus for the five special bombs that had a GUI. */
public final class ModBombs2Menus {

    private ModBombs2Menus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerNukeCustom>> NUKE_CUSTOM =
            REG.menu("nuke_custom", () -> Menus.typeWithPos(ContainerNukeCustom::new));
    public static final RegistryObject<MenuType<ContainerNukeFleija>> NUKE_FLEIJA =
            REG.menu("nuke_fleija", () -> Menus.typeWithPos(ContainerNukeFleija::new));
    public static final RegistryObject<MenuType<ContainerNukeSolinium>> NUKE_SOLINIUM =
            REG.menu("nuke_solinium", () -> Menus.typeWithPos(ContainerNukeSolinium::new));
    public static final RegistryObject<MenuType<ContainerNukePrototype>> NUKE_PROTOTYPE =
            REG.menu("nuke_prototype", () -> Menus.typeWithPos(ContainerNukePrototype::new));
    public static final RegistryObject<MenuType<ContainerNukeFstbmb>> NUKE_FSTBMB =
            REG.menu("nuke_fstbmb", () -> Menus.typeWithPos(ContainerNukeFstbmb::new));

    public static void init() {}
}
