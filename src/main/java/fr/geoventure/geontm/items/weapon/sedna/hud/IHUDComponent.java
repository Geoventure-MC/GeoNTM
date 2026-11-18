package fr.geoventure.geontm.items.weapon.sedna.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** One source-shaped row in a Sedna gun's per-config HUD. */
public interface IHUDComponent {

    /** Height reserved before the next component in this rail is rendered. */
    int getComponentHeight(Player player, ItemStack stack);

    void renderHUDComponent(GuiGraphics graphics, Player player, ItemStack stack, int bottomOffset,
                            int configIndex);
}
