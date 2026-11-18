package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import fr.geoventure.geontm.inventory.container.ContainerMachineAnnihilator;
import fr.geoventure.geontm.inventory.container.ContainerMachineDiFurnaceRTG;
import fr.geoventure.geontm.inventory.container.ContainerMachineExcavator;
import fr.geoventure.geontm.inventory.container.ContainerOreSlopper;
import fr.geoventure.geontm.inventory.container.ContainerRtgFurnace;

/** Feature-local position-bound menus for the production machines (group B). */
public final class ModProduction2Menus {
    private ModProduction2Menus() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<ContainerMachineExcavator>> EXCAVATOR =
            REG.menu("machine_excavator", () -> Menus.typeWithPos(ContainerMachineExcavator::new));
    public static final RegistryObject<MenuType<ContainerOreSlopper>> ORE_SLOPPER =
            REG.menu("machine_ore_slopper", () -> Menus.typeWithPos(ContainerOreSlopper::new));
    public static final RegistryObject<MenuType<ContainerMachineAnnihilator>> ANNIHILATOR =
            REG.menu("machine_annihilator", () -> Menus.typeWithPos(ContainerMachineAnnihilator::new));
    public static final RegistryObject<MenuType<ContainerRtgFurnace>> RTG_FURNACE =
            REG.menu("machine_rtg_furnace", () -> Menus.typeWithPos(ContainerRtgFurnace::new));
    public static final RegistryObject<MenuType<ContainerMachineDiFurnaceRTG>> DIFURNACE_RTG =
            REG.menu("machine_difurnace_rtg", () -> Menus.typeWithPos(ContainerMachineDiFurnaceRTG::new));

    public static void init() {}
}
