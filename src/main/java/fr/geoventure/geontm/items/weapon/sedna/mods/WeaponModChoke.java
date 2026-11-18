package fr.geoventure.geontm.items.weapon.sedna.mods;
import fr.geoventure.geontm.items.weapon.sedna.Receiver;
import net.minecraft.world.item.ItemStack;
public class WeaponModChoke extends WeaponModBase {
    public WeaponModChoke(int id) { super(id, "BARREL"); }
    @Override public <T> T eval(T base, ItemStack gun, String key, Object parent) {
        return Receiver.F_SPREADAMMO.equals(key) ? cast((Float) base * 0.5F, base) : base;
    }
}
