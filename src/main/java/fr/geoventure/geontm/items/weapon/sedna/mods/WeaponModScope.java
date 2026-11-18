package fr.geoventure.geontm.items.weapon.sedna.mods;
import fr.geoventure.geontm.items.weapon.sedna.GunConfig;
import fr.geoventure.geontm.items.ModGunItems;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT.WeaponQuality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
/** Scope stat hook; the client renderer supplies the actual overlay texture when one is available. */
public class WeaponModScope extends WeaponModBase {
    private final ResourceLocation overrideTexture;
    public WeaponModScope(int id) { this(id, null); }
    public WeaponModScope(int id, ResourceLocation texture) { super(id, "SCOPE"); overrideTexture = texture; }
    @Override public <T> T eval(T base, ItemStack gun, String key, Object parent) {
        if (GunConfig.O_SCOPETEXTURE.equals(key)) {
            if (overrideTexture != null) return cast(overrideTexture, base);
            if (gun.is(ModGunItems.gun_heavy_revolver.get())) return cast(Ids.of(Constants.MOD_ID, "textures/misc/scope_44.png"), base);
            if (((ItemGunBaseNT) gun.getItem()).quality == WeaponQuality.UTILITY) return cast(Ids.of(Constants.MOD_ID, "textures/misc/scope_tool.png"), base);
            return cast(Ids.of(Constants.MOD_ID, "textures/misc/scope_bolt.png"), base);
        }
        return GunConfig.B_HIDECROSSHAIR.equals(key) ? cast(true, base) : base;
    }
}
