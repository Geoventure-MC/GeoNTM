package fr.geoventure.geontm.client.render.turret;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import net.minecraft.resources.ResourceLocation;

/** The turret models and the standalone textures their parts sample. */
public final class TurretResources {

    public static final WavefrontObj CHEKHOV = model("turret_chekhov");
    public static final WavefrontObj JEREMY = model("turret_jeremy");
    public static final WavefrontObj SENTRY_MODEL = model("turret_sentry");
    public static final WavefrontObj HOWARD = model("turret_howard");
    public static final WavefrontObj HOWARD_DAMAGED = model("turret_howard_damaged");
    public static final WavefrontObj TAUON = model("turret_tauon");
    public static final WavefrontObj MAXWELL = model("turret_microwave");
    public static final WavefrontObj RICHARD = model("turret_richard");
    public static final WavefrontObj FRITZ = model("turret_fritz");
    public static final WavefrontObj HIMARS = model("turret_himars");
    public static final WavefrontObj ARTY = model("turret_arty");

    public static final ResourceLocation BASE = texture("base");
    public static final ResourceLocation BASE_FRIENDLY = texture("base_friendly");
    public static final ResourceLocation CARRIAGE = texture("carriage");
    public static final ResourceLocation CARRIAGE_FRIENDLY = texture("carriage_friendly");
    public static final ResourceLocation CARRIAGE_CIWS = texture("carriage_ciws");
    public static final ResourceLocation CONNECTOR = texture("connector");
    public static final ResourceLocation CHEKHOV_BODY = texture("chekhov");
    public static final ResourceLocation CHEKHOV_BARRELS = texture("chekhov_barrels");
    public static final ResourceLocation JEREMY_GUN = texture("jeremy");
    public static final ResourceLocation SENTRY = texture("sentry");
    public static final ResourceLocation SENTRY_DAMAGED = texture("sentry_damaged");
    public static final ResourceLocation HOWARD_BODY = texture("howard");
    public static final ResourceLocation HOWARD_BARRELS = texture("howard_barrels");
    public static final ResourceLocation TAUON_BODY = texture("tauon");
    public static final ResourceLocation MAXWELL_BODY = texture("maxwell");
    public static final ResourceLocation RICHARD_BODY = texture("richard");
    public static final ResourceLocation FRITZ_BODY = texture("fritz");
    public static final ResourceLocation HIMARS_BODY = texture("himars");
    public static final ResourceLocation ARTY_BODY = texture("arty");
    public static final ResourceLocation BASE_RUSTED = texture("rusted/base");
    public static final ResourceLocation CARRIAGE_CIWS_RUSTED = texture("rusted/carriage_ciws");
    public static final ResourceLocation HOWARD_RUSTED = texture("rusted/howard");
    public static final ResourceLocation HOWARD_BARRELS_RUSTED = texture("rusted/howard_barrels");

    private TurretResources() {}

    private static WavefrontObj model(String name) {
        return WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/turrets/" + name + ".obj"));
    }

    private static ResourceLocation texture(String name) {
        return Ids.of(Constants.MOD_ID, "textures/models/turrets/" + name + ".png");
    }
}
