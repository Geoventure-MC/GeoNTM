package fr.geoventure.geontm.space.mob;

import fr.geoventure.geontm.items.weapon.sedna.BulletConfig;
import fr.geoventure.geontm.items.weapon.sedna.BulletConfigs;
import fr.geoventure.geontm.items.weapon.sedna.factory.XFactory762mm;
import fr.geoventure.geontm.items.weapon.sedna.factory.XFactoryEnergy;
import fr.geoventure.geontm.items.weapon.sedna.factory.XFactoryFlamer;
import fr.geoventure.geocore.Ids;

/** Mob-specific ammunition keeps the source knockback without changing the players' shared rounds. */
public final class MobWeaponsSpace {
    public static final BulletConfig R762_AP_STILL = still("mob/r762_ap", XFactory762mm.r762_ap);
    public static final BulletConfig EMERALD_OVERCHARGE_STILL =
            still("mob/emerald_overcharge", XFactoryEnergy.energy_emerald_overcharge);
    public static final BulletConfig TESLA_OVERCHARGE_STILL =
            still("mob/tesla_overcharge", XFactoryEnergy.energy_tesla_overcharge);
    public static final BulletConfig FLAME_BALEFIRE_STILL = still("mob/flame_balefire", XFactoryFlamer.flame_balefire);

    private MobWeaponsSpace() {}

    private static BulletConfig still(String name, BulletConfig source) {
        return BulletConfigs.register(Ids.of("geontm", name), source.copy().setKnockback(0));
    }

    /** Register every config during bootstrap, before either side receives a projectile's numeric config id. */
    public static void init() {}
}
