package fr.geoventure.geontm.items.tool;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.inventory.ItemStackContainer;
import fr.geoventure.geontm.inventory.ModGunSupportMenus;
import fr.geoventure.geontm.inventory.container.MenuItemBox;
import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.data.ItemContents;
import fr.geoventure.geocore.gui.Menus;
import fr.geoventure.geocore.world.CompatItem;
import fr.geoventure.geocore.world.CompatTooltipContext;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/** A one-slot, one-item bag, dropped by the drifting bag mob. Right-click opens it. */
public class ItemPlasticBag extends CompatItem {

    public static final MenuItemBox.Layout LAYOUT = new MenuItemBox.Layout(1, 1, 80, 65, 8, 134,
            192, 176, 216, -1, 0xFFFFFF, 122, Ids.of(Constants.MOD_ID, "textures/gui/gui_plastic_bag.png"));

    public ItemPlasticBag(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static ItemStack getContents(ItemStack bag) {
        return ModDataComponents.PLASTIC_BAG.getOr(bag, ItemContents.EMPTY).copies().stream()
                .findFirst().map(ItemStack::copy).orElse(ItemStack.EMPTY);
    }

    @Override
    protected int compatUseDuration(ItemStack stack, LivingEntity entity) { return 1; }

    @Override
    protected InteractionResult compatUse(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            Menus.openWithHand(serverPlayer, new SimpleMenuProvider((id, inventory, opener) -> new MenuItemBox(
                    ModGunSupportMenus.PLASTIC_BAG.get(), id, inventory, LAYOUT,
                    new ItemStackContainer(opener, opener.getItemInHand(hand), 1, ModDataComponents.PLASTIC_BAG)),
                    player.getItemInHand(hand).getHoverName()), hand);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void appendCompatHoverText(ItemStack stack, CompatTooltipContext context,
                                         List<Component> tooltip, TooltipFlag flag) {
        ItemStack held = getContents(stack);
        if (!held.isEmpty()) tooltip.add(held.getHoverName());
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
