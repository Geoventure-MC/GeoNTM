package fr.geoventure.geontm.inventory;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.container.ContainerCrateTungsten;
import fr.geoventure.geontm.inventory.container.ContainerSafe;
import fr.geoventure.geontm.inventory.container.MenuCrate;
import fr.geoventure.geontm.items.block.CrateHeldOpening;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.MenuType;

/**
 * The menus of a crate opened from the hand ({@code /ntmserver CRATE_OPEN_HELD}, {@link CrateHeldOpening}): the client
 * receives the server's crate kind and locked hotbar slot and builds the layout over a stand-in. Neither depends on
 * which item is currently selected when the open packet arrives; the contents sync as usual.
 */
public final class ModHeldCrateMenus {

    private ModHeldCrateMenus() {}

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final Reg REG = Reg.of(MENUS);

    public static final RegistryObject<MenuType<MenuCrate>> CRATE_HELD = REG.menu("crate_held",
            () -> Menus.typeWithInt((id, inventory, openingData) -> {
                var kind = CrateHeldOpening.openingKind(openingData);
                return new MenuCrate(ModHeldCrateMenus.CRATE_HELD.get(), id, inventory,
                        CrateHeldOpening.crateType(kind),
                        new SimpleContainer(CrateHeldOpening.slots(kind)), CrateHeldOpening.openingSlot(openingData));
            }));
    public static final RegistryObject<MenuType<ContainerCrateTungsten>> CRATE_TUNGSTEN_HELD = REG.menu(
            "crate_tungsten_held", () -> Menus.typeWithInt((id, inventory, openingData) ->
                    new ContainerCrateTungsten(id, inventory, new SimpleContainer(27),
                            CrateHeldOpening.openingSlot(openingData))));
    public static final RegistryObject<MenuType<ContainerSafe>> SAFE_HELD = REG.menu(
            "safe_held", () -> Menus.typeWithInt((id, inventory, openingData) ->
                    new ContainerSafe(id, inventory, new SimpleContainer(15),
                            CrateHeldOpening.openingSlot(openingData))));

    public static void init() {}
}
