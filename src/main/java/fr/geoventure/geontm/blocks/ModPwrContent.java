package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.items.machine.ItemPWRFuel.EnumPWRFuel;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import fr.geoventure.geontm.inventory.ModPwrMenus;
import fr.geoventure.geontm.items.ModPwrItems;
import fr.geoventure.geontm.tileentity.ModPwrBlockEntities;

/** One common bootstrap hook for the pressurized water reactor's feature-local registries. */
public final class ModPwrContent {
    private ModPwrContent() {}

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Constants.MOD_ID);
    private static final Reg REG = Reg.of(SOUNDS);

    /** 1.7 {@code NTMSounds.REACTOR_GEIGER_LOOP} ({@code geontm:block.reactorLoop}), the loaded-core loop. */
    public static final RegistryObject<SoundEvent> REACTOR_LOOP = REG.sound("block.reactor_loop");

    public static void init() {
        ModPwrBlocks.init();
        ModPwrItems.init();
        ModPwrBlockEntities.init();
        ModPwrMenus.init();
    }

    // 1.7 HazardRegistry.registerPWRFuel (fresh rod = twice its billet, hot and depleted ten times that, hot also HOT 5)
    // is the geontm:hazards item data map.
}
