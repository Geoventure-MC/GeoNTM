package fr.geoventure.geontm.sound;

import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

/**
 * Sound events of the decorative family: the 1.7 {@code ModSoundTypes.platemetal} step/place sounds
 * ({@code block.platemetalPlace}, {@code step.platemetal} lowercased). Built eagerly because the
 * {@code SoundType} needs the event objects while the blocks are constructed; the registrations hand
 * out the same instances (the {@code ModDoorSounds.OPEN_DOOR} pattern).
 */
public final class ModDecoSounds {
    private ModDecoSounds() {}

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Constants.MOD_ID);
    private static final Reg REG = Reg.of(SOUNDS);

    public static final SoundEvent PLATEMETAL_PLACE_EVENT = SoundEvent.createVariableRangeEvent(Ids.of(Constants.MOD_ID, "block.platemetal_place"));
    public static final SoundEvent PLATEMETAL_STEP_EVENT = SoundEvent.createVariableRangeEvent(Ids.of(Constants.MOD_ID, "step.platemetal"));
    public static final RegistryObject<SoundEvent> PLATEMETAL_PLACE = REG.register(Registries.SOUND_EVENT, "block.platemetal_place", () -> PLATEMETAL_PLACE_EVENT);
    public static final RegistryObject<SoundEvent> PLATEMETAL_STEP = REG.register(Registries.SOUND_EVENT, "step.platemetal", () -> PLATEMETAL_STEP_EVENT);

    public static void init() {}
}
