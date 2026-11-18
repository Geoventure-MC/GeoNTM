package fr.geoventure.geontm.items.armor;

import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT.LambdaContext;
import net.minecraft.world.item.ItemStack;

public interface IPARanged {
    void clickPrimary(ItemStack stack, LambdaContext ctx);
    void clickSecondary(ItemStack stack, LambdaContext ctx);
}
