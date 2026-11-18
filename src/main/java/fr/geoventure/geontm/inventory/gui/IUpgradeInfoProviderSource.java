package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.tileentity.IUpgradeInfoProvider;
import org.jspecify.annotations.Nullable;

/**
 * Implemented by machine screens whose BlockEntity is an {@link IUpgradeInfoProvider} (the 1.12.2
 * {@code IUpgradeInfoProviderSource}). Lets {@code ItemMachineUpgrade} tooltips resolve the open
 * machine and print its exact per-tier effect numbers.
 */
public interface IUpgradeInfoProviderSource {

    @Nullable IUpgradeInfoProvider getUpgradeInfoProvider();
}
