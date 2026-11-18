package fr.geoventure.geontm.client.render.util;

import fr.geoventure.geontm.main.MainRegistry;
import fr.geoventure.geontm.util.ShadyUtil;
import fr.geoventure.geocore.Ids;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

/**
 * 1.7 {@code RenderAccessoryUtility.getCloakFromPlayer}: the GEONTM cape each listed player wears, installed through
 * Core's {@link fr.geoventure.geocore.client.Capes}.
 *
 * <p>1.7's table, plus 1.12.2's one addition (GeoCore). Where the two disagree 1.7 is kept: Bob's {@code CapeHbm3}
 * (1.12.2 used {@code CapeHbm} and inverted the polaroid swap), {@code CapeNostalgia2}, {@code CapeHoboy_mk3}, and
 * ShimmeringBlaze's polaroid variant. Alcater's cape is not ported. Pheo's
 * {@code CapePheo} stays commented out as in 1.7.
 *
 * <p>The test cape goes to any name starting with "Player", which is what a 1.7 dev client is called. The port's
 * dev clients are {@code Player###} on Fabric and {@code Dev} on NeoForge/Forge, so {@code Dev} wears it too.
 */
public final class RenderAccessoryUtility {

    private RenderAccessoryUtility() {}

    private static final ResourceLocation geontm = cape("capehbm3");
    private static final ResourceLocation geontm2 = cape("capehbm2");
    private static final ResourceLocation drillgon = cape("capedrillgon");
    private static final ResourceLocation dafnik = cape("capedafnik");
    private static final ResourceLocation lpkukin = cape("capeshield");
    private static final ResourceLocation vertice = cape("capevertice_2");
    private static final ResourceLocation nostalgia2 = cape("capenostalgia2");
    private static final ResourceLocation sam = cape("capesam");
    private static final ResourceLocation hoboy = cape("capehoboy_mk3");
    private static final ResourceLocation master = cape("capemaster");
    private static final ResourceLocation mek = cape("capemek");
    private static final ResourceLocation zippy = cape("capezippysqrl");
    private static final ResourceLocation test = cape("capetest");
    private static final ResourceLocation schrabby = cape("capeschrabbyalt");
    private static final ResourceLocation swiggs = cape("capesweatyswiggs");
    private static final ResourceLocation doctor17 = cape("capedoctor17");
    private static final ResourceLocation shimmeringblaze = cape("capeblaze");
    private static final ResourceLocation blaze2 = cape("capeblaze2");
    private static final ResourceLocation wiki = cape("capewiki");
    private static final ResourceLocation leftnugget = cape("capeleftnugget");
    private static final ResourceLocation rightnugget = cape("caperightnugget");
    private static final ResourceLocation tankish = cape("capetankish");
    private static final ResourceLocation frizzlefrazzle = cape("capefrizzlefrazzle");
    private static final ResourceLocation vaer = cape("capevaer");
    private static final ResourceLocation adam = cape("capeadam");
    private static final ResourceLocation jame = cape("capejame");
    private static final ResourceLocation geocore = cape("capegolem"); // 1.12.2

    private static ResourceLocation cape(String name) {
        return Ids.of("geontm", "textures/models/capes/" + name + ".png");
    }

    public static @Nullable ResourceLocation getCloakFromPlayer(UUID id, String name) {
        String uuid = id.toString();

        if (uuid.equals(ShadyUtil.HbMinecraft)) return MainRegistry.polaroidID == 11 ? geontm2 : geontm;
        if (uuid.equals(ShadyUtil.Drillgon)) return drillgon;
        if (uuid.equals(ShadyUtil.Dafnik)) return dafnik;
        if (uuid.equals(ShadyUtil.LPkukin)) return lpkukin;
        if (uuid.equals(ShadyUtil.LordVertice)) return vertice;
        if (uuid.equals(ShadyUtil.Dr_Nostalgia)) return nostalgia2;
        if (uuid.equals(ShadyUtil.Samino2)) return sam;
        if (uuid.equals(ShadyUtil.Hoboy03new)) return hoboy;
        if (uuid.equals(ShadyUtil.Dragon59MC)) return master;
        if (uuid.equals(ShadyUtil.Steelcourage)) return mek;
        if (uuid.equals(ShadyUtil.ZippySqrl)) return zippy;
        if (uuid.equals(ShadyUtil.Schrabby)) return schrabby;
        if (uuid.equals(ShadyUtil.SweatySwiggs)) return swiggs;
        if (uuid.equals(ShadyUtil.Doctor17) || uuid.equals(ShadyUtil.Doctor17PH)) return doctor17;
        if (uuid.equals(ShadyUtil.ShimmeringBlaze)) return MainRegistry.polaroidID == 11 ? blaze2 : shimmeringblaze;
        if (uuid.equals(ShadyUtil.FifeMiner)) return leftnugget;
        if (uuid.equals(ShadyUtil.lag_add)) return rightnugget;
        if (uuid.equals(ShadyUtil.Tankish)) return tankish;
        if (uuid.equals(ShadyUtil.FrizzleFrazzle)) return frizzlefrazzle;
        // 1.7: if(uuid.equals(ShadyUtil.Barnaby99_x)) return pheo; (commented out upstream)
        if (uuid.equals(ShadyUtil.Ma118)) return vaer;
        if (uuid.equals(ShadyUtil.Adam29Adam29)) return adam;
        if (uuid.equals(ShadyUtil.ege444)) return jame;
        if (uuid.equals(ShadyUtil.GeoCore)) return geocore;
        if (ShadyUtil.contributors.contains(uuid)) return wiki;
        if (name.startsWith("Player") || name.equals("Dev")) return test;

        return null;
    }
}
