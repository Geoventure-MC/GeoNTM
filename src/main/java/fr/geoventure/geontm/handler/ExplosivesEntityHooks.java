package fr.geoventure.geontm.handler;

import fr.geoventure.geontm.items.armor.ArmorHat;
import fr.geoventure.geontm.items.armor.ItemModObsidian;
import fr.geoventure.geontm.items.special.ItemDrop;
import fr.geoventure.geontm.items.special.ItemDigamma;
import fr.geoventure.geontm.items.special.ItemRag;
import fr.geoventure.geontm.items.special.ItemSingularity;
import fr.geoventure.geontm.items.special.ItemCell;
import fr.geoventure.geontm.items.special.ItemAntimatterPellet;
import fr.geoventure.geontm.items.special.ItemDemonCore;
import fr.geoventure.geontm.items.weapon.WeaponizedCell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import fr.geoventure.geontm.items.armor.ItemModDefuser;
import fr.geoventure.geontm.items.tool.ItemDropDetonator;

/** Dropped-item behaviors and defused creepers, after their server entity tick. */
public final class ExplosivesEntityHooks {
    private ExplosivesEntityHooks() {}

    public static void tick(Entity entity) {
        if (entity.level().isClientSide()) return;
        if (entity instanceof ItemEntity item) {
            ItemDropDetonator.tickDropped(item);
            WeaponizedCell.tickDropped(item); // 1.7 WeaponizedCell.onEntityItemUpdate
            ItemModObsidian.protectDropped(item);
            ArmorHat.tickDropped(item); // 1.7 ArmorHat.onEntityItemUpdate: setDead
            ItemDrop.tickDropped(item); // 1.7 ItemDrop.onEntityItemUpdate: beta setDead
            ItemDigamma.tickDropped(item);
            ItemRag.tickDropped(item); // 1.7 ItemRag.onEntityItemUpdate: cloth in water -> damp
            ItemSingularity.tickDropped(item);
            ItemCell.tickDropped(item);
            ItemAntimatterPellet.tickDropped(item);
            ItemDemonCore.tickDropped(item);
            return;
        }
        if (entity instanceof Creeper creeper) {
            ItemModDefuser.tickDefused(creeper);
        }
        // The wire cutter's own modUpdate now runs from ArmorModEvents with every other armor module.
    }
}
