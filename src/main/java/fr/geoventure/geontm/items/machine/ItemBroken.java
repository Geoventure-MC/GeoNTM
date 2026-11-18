package fr.geoventure.geontm.items.machine;

import fr.geoventure.geontm.items.ModDataComponents;
import fr.geoventure.geontm.items.ModMachineItems;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.world.CompatItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/** Carries the old precision-assembler faulty-output item id in a modern data component. */
public final class ItemBroken extends CompatItem {

    public ItemBroken(Properties properties) {
        super(properties);
    }

    public static ItemStack make(ItemStack original) {
        ItemStack broken = new ItemStack(ModMachineItems.broken_item.get(), original.getCount());
        ModDataComponents.brokenItem(broken, BuiltInRegistries.ITEM.getKey(original.getItem()).toString());
        return broken;
    }

    public static @Nullable ItemStack original(ItemStack broken) {
        String id = ModDataComponents.brokenItem(broken);
        if (id == null) return null;
        return BuiltInRegistries.ITEM.getOptional(Ids.parse(id)).map(item -> new ItemStack(item, broken.getCount())).orElse(null);
    }

    @Override
    public Component getName(ItemStack stack) {
        ItemStack original = original(stack);
        return original == null ? Component.literal("Broken Item")
                : Component.literal("Broken ").append(original.getHoverName());
    }
}
