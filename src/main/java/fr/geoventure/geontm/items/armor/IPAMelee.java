package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT.LambdaContext;
import fr.geoventure.geontm.items.weapon.sedna.GunAnimation;
import fr.geoventure.geocore.render.anim.BusAnimation;
import net.minecraft.world.item.ItemStack;

public interface IPAMelee {
    BusAnimation playAnim(ItemStack stack, GunAnimation type);
    void orchestra(ItemStack stack, LambdaContext ctx);
    void clickPrimary(ItemStack stack, LambdaContext ctx);
    void clickSecondary(ItemStack stack, LambdaContext ctx);
}
