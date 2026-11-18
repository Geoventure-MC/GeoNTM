package fr.geoventure.geontm.inventory.gui;

import fr.geoventure.geontm.inventory.container.ContainerCompactLauncher;
import fr.geoventure.geocore.Ids;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

/** 1.7 {@code GUIMachineCompactLauncher}: {@code gui_launch_table_small.png}. */
public class GUIMachineCompactLauncher extends GUICustomMissileLauncher<ContainerCompactLauncher> {

    public GUIMachineCompactLauncher(ContainerCompactLauncher menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, Ids.of("geontm", "textures/gui/weapon/gui_launch_table_small.png"));
    }

    @Override
    protected List<Component> sizeHint() {
        return List.of(Component.literal("Only accepts custom missiles"), Component.literal("of size 10 and 10/15."));
    }
}
