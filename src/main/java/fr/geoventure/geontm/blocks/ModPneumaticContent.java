package fr.geoventure.geontm.blocks;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.net.GeoCoreNetwork;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import fr.geoventure.geontm.inventory.ModPneumaticMenus;
import fr.geoventure.geontm.packet.PneumoAccessInputPayload;
import fr.geoventure.geontm.packet.PneumoAccessSyncPayload;
import fr.geoventure.geontm.tileentity.ModPneumaticBlockEntities;
import fr.geoventure.geontm.uninos.networkproviders.PneumaticNetwork;

/** One common bootstrap hook for the pneumatic tube storage system's feature-local registries. */
public final class ModPneumaticContent {
    private ModPneumaticContent() {}

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Constants.MOD_ID);
    private static final Reg REG = Reg.of(SOUNDS);

    /** 1.7 {@code geontm:weapon.reload.tubeFwoomp}, the compressor's send sound; the event is already in sounds.json. */
    public static final RegistryObject<SoundEvent> TUBE_FWOOMP = REG.sound("weapon.reload.tube_fwoomp");

    public static void init() {
        ModPneumaticBlocks.init();
        ModPneumaticBlockEntities.init();
        ModPneumaticMenus.init();
        PneumaticNetwork.init();
        GeoCoreNetwork.declarePlayToClient(PneumoAccessSyncPayload.CODEC);
        GeoCoreNetwork.registerPlayToServer(PneumoAccessInputPayload.CODEC, PneumoAccessInputPayload::handleServer);
    }
}
