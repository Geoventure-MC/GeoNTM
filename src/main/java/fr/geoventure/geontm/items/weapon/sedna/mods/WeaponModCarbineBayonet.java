package fr.geoventure.geontm.items.weapon.sedna.mods;

import fr.geoventure.geontm.items.weapon.sedna.GunAnimation;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT;
import fr.geoventure.geontm.items.weapon.sedna.factory.Orchestras;
import fr.geoventure.geontm.items.weapon.sedna.factory.XFactory762mm;
import fr.geoventure.geocore.render.anim.BusAnimation;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.world.item.ItemStack;

public final class WeaponModCarbineBayonet extends WeaponModBayonet {
    public WeaponModCarbineBayonet(int id) { super(id); }
    @Override float stabDamage() { return XFactory762mm.GUN_CARBINE_BAYONET_DAMAGE.get(); }
    @Override BiConsumer<ItemStack, ItemGunBaseNT.LambdaContext> normalOrchestra() { return Orchestras.ORCHESTRA_CARBINE; }
    @Override BiFunction<ItemStack, GunAnimation, BusAnimation> inspectionAnimation() { return (stack, type) -> stabAnimation(); }
}
