package fr.geoventure.geontm.entity.cart;

import fr.geoventure.geontm.entity.ModNtmEntities;
import fr.geoventure.geontm.items.tool.ItemModMinecart;
import fr.geoventure.geontm.items.tool.ItemModMinecart.EnumCartBase;
import fr.geoventure.geontm.items.tool.ItemModMinecart.EnumMinecart;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 1.7 {@code entity/cart/EntityMinecartOre}: the plain NTM cart - no payload, just the chassis.
 */
public class EntityMinecartOre extends EntityMinecartNTM {

    public EntityMinecartOre(EntityType<? extends EntityMinecartOre> type, Level level) {
        super(type, level);
    }

    public EntityMinecartOre(Level level, double x, double y, double z, EnumCartBase base) {
        super(ModNtmEntities.CART_ORE.get(), level, x, y, z, base);
    }

    @Override
    public ItemStack getCartItem() {
        return ItemModMinecart.createCartItem(getBase(), EnumMinecart.EMPTY);
    }
}
