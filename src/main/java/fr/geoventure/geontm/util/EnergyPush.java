package fr.geoventure.geontm.util;

import fr.geoventure.geontm.api.energymk2.EnergyConversion;
import fr.geoventure.geontm.api.energymk2.ForeignEnergyBridge;
import fr.geoventure.geontm.api.energymk2.IEnergyHandlerMK2;
import fr.geoventure.geontm.api.energymk2.IEnergyReceiverMK2;
import fr.geoventure.geontm.api.energymk2.PowerGraph;
import fr.geoventure.geontm.api.energymk2.PowerNetwork;
import fr.geoventure.geontm.lib.DirPos;
import fr.geoventure.geontm.lib.ForgeDirection;
import fr.geoventure.geontm.capability.MachineEnergyExposure;
import fr.geoventure.geontm.capability.NativeContacts;
import fr.geoventure.geontm.capability.CapRole;
import fr.geoventure.geocore.world.EnergyCompat;
import fr.geoventure.geocore.world.EnergyView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

/**
 * The 1.7 {@code tryProvide} that could aim at a conductor: a diode's output face and a capacitor's bus
 * push straight into whatever stands there. A machine takes the power directly; a cable hands it to its
 * net's injected pool, which the next round offers as a provider, capped by the demand that round left
 * unmet -- so nothing is ever pushed that the net could not use, and a net feeding itself is refused.
 */
public final class EnergyPush {

    private EnergyPush() {}

    /** What a push of any size at {@code target} (arriving from {@code dir}) could deliver right now. */
    public static long capacity(ServerLevel level, BlockPos target, ForgeDirection dir, @Nullable Object self) {
        IEnergyReceiverMK2 rec = receiverAt(level, target, dir, self);
        if (rec != null) return Math.min(rec.getMaxPower() - rec.getPower(), rec.getReceiverSpeed());
        ForeignEnergyBridge bridge = bridgeAt(level, target, dir);
        return bridge == null ? 0L : bridge.insertableHe();
    }

    /** Pushes up to {@code amount} into {@code target}; returns what was accepted. */
    public static long push(ServerLevel level, BlockPos target, ForgeDirection dir, long amount, @Nullable Object self) {
        if (amount <= 0) return 0L;
        IEnergyReceiverMK2 rec = receiverAt(level, target, dir, self);
        if (rec != null) {
            long toTransfer = Math.min(amount, Math.min(rec.getMaxPower() - rec.getPower(), rec.getReceiverSpeed()));
            if (toTransfer <= 0) return 0L;
            return toTransfer - rec.transferPower(toTransfer, false);
        }
        ForeignEnergyBridge bridge = bridgeAt(level, target, dir);
        return bridge == null ? 0L : bridge.insertHe(amount);
    }

    /**
     * port: a provider's port touching another mod's energy block pushes into it, as 1.7 {@code tryProvide} handed power
     * straight to a receiver standing at the port. A cable or an GEONTM machine there is left to the power net.
     *
     * @return whether another mod's energy block may stand there, so a caller can ask a connection with none less often.
     */
    public static boolean toForeign(ServerLevel level, IEnergyHandlerMK2 provider, DirPos connection) {
        if (!EnergyCompat.supported() || !provider.ownsEnergyBuffer()) return false;
        BlockPos target = connection.getPos();
        if (level.getChunkSource().getChunkNow(target.getX() >> 4, target.getZ() >> 4) == null
                || level.getBlockEntity(target) == null || PowerGraph.get(level).containsCell(target.asLong())) return false;
        if (MachineEnergyExposure.isMachine(level, target)) return false;
        Direction face = connection.getDir().getOpposite().toDirection();
        long available = provider.getPowerAvailable();
        // nothing was looked up yet: an empty provider asks again next tick
        if (available <= 0L) return true;
        EnergyView view = EnergyCompat.find(level, target, face);
        if (view == null) return false;
        long fe = EnergyConversion.feFromHe(available);
        fe -= fe % EnergyConversion.feQuantum();
        long accepted = fe <= 0L ? 0L : view.insert(fe);
        if (accepted <= 0L) return true;
        // a store that took a part quantum is charged the whole HE it is worth, never less
        long he = EnergyConversion.heFromFe(accepted);
        if (EnergyConversion.feFromHe(he) < accepted) he++;
        provider.usePower(Math.min(he, available));
        return true;
    }

    private static @Nullable IEnergyReceiverMK2 receiverAt(ServerLevel level, BlockPos target, ForgeDirection dir,
                                                         @Nullable Object self) {
        var contact = NativeContacts.powerAt(level, target, dir.getOpposite().toDirection(), CapRole.POWER_IN);
        if (contact instanceof IEnergyReceiverMK2 rec && rec != self && rec.allowDirectProvision()) {
            return rec;
        }
        return null;
    }

    private static @Nullable ForeignEnergyBridge bridgeAt(ServerLevel level, BlockPos target, ForgeDirection dir) {
        long key = target.asLong();
        var graph = PowerGraph.get(level);
        if (!graph.containsCell(key) || !graph.isOpenAt(key, dir.getOpposite().toDirection())) return null;
        return PowerNetwork.bridgeAt(level, target);
    }
}
