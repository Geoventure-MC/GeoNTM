package fr.geoventure.geontm.items.weapon.sedna.mods;
import fr.geoventure.geontm.items.weapon.sedna.GunConfig;
import fr.geoventure.geontm.items.weapon.sedna.Receiver;
import net.minecraft.world.item.ItemStack;
public class WeaponModUziSaturnite extends WeaponModBase {
    public WeaponModUziSaturnite(int id) { super(id, "FURNITURE"); setPriority(PRIORITY_ADDITIVE); }
    @Override public <T> T eval(T base, ItemStack gun, String key, Object parent) {
        if (GunConfig.F_DURABILITY.equals(key)) return cast((Float) base * 5F, base);
        if (Receiver.F_BASEDAMAGE.equals(key)) return cast((Float) base + 3F, base);
        return base;
    }
}
