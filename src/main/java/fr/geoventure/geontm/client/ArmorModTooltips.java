package fr.geoventure.geontm.client;

import fr.geoventure.geontm.handler.ArmorModHandler;
import fr.geoventure.geontm.inventory.gui.GUIArmorTable;
import fr.geoventure.geontm.items.armor.ItemArmorMod;
import fr.geoventure.geontm.items.armor.JetpackBase;
import fr.geoventure.geontm.util.TooltipColours;
import fr.geoventure.geocore.client.Tooltips;
import fr.geoventure.geocore.client.KeyMappings;
import fr.geoventure.geocore.world.Armors;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** The old client tooltip event's shift-held listing of installed armor modules. */
public final class ArmorModTooltips {

    private ArmorModTooltips() {}

    public static void register() {
        Tooltips.onItemTooltip(ArmorModTooltips::addLines);
    }

    private static void addLines(ItemStack armor, List<Component> lines) {
        if (!Armors.isArmor(armor) || !ArmorModHandler.hasMods(armor)) return;

        Minecraft minecraft = Minecraft.getInstance();
        boolean show = KeyMappings.isHeld(minecraft.options.keyShift) || minecraft.screen instanceof GUIArmorTable;
        if (!show) {
            lines.add(Component.literal("Hold <").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                    .append(Component.literal("LSHIFT").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC))
                    .append(Component.literal("> to display installed armor mods")
                            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
            return;
        }
        int first = lines.size();
        lines.add(Component.translatable("desc.mods").withStyle(ChatFormatting.YELLOW));
        ItemStack[] mods = ArmorModHandler.pryMods(armor);
        // The 1.12 client listed slots 0..7 only; its battery slot was omitted.
        for (int slot = 0; slot < ArmorModHandler.battery; slot++) {
            ItemStack mod = mods[slot];
            if (mod.getItem() instanceof ItemArmorMod armorMod) armorMod.addArmorModDescription(lines, mod, armor);
            else if (mod.getItem() instanceof JetpackBase jetpack) jetpack.addArmorModDescription(lines, mod, armor);
            // port: the elytra module (ArmorModHandler.isElytra) is a vanilla item, so it gets 1.7's base
            // ItemArmorMod.addDesc line: the stack's display name.
            else if (ArmorModHandler.isElytra(mod)) lines.add(mod.getHoverName());
        }
        TooltipColours.grayFrom(lines, first);
    }
}
