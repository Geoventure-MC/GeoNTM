package fr.geoventure.geontm.space.client;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geocore.Ids;
import net.minecraft.resources.ResourceLocation;

/**
 * The OBJ meshes and skins of hbm-space's mobs ({@code ResourceManager} there), copied byte for byte from hbm-space
 * into {@code ntmspace:models/mobs} and {@code ntmspace:textures/entity}. Meshes load on first use (the client
 * resource manager must exist), as HBM's {@code NtmEntityModels}.
 */
public final class SpaceMobModels {

    private SpaceMobModels() {}

    private static WavefrontObj load(String path) {
        return WavefrontObj.load(Ids.of(Constants.MOD_ID, "models/mobs/" + path));
    }

    public static ResourceLocation texture(String name) {
        return Ids.of(Constants.MOD_ID, "textures/entity/" + name + ".png");
    }

    private static WavefrontObj scutterfish;
    private static WavefrontObj scuttlecrab;
    private static WavefrontObj scrapfish;
    private static WavefrontObj depthsquid;
    private static WavefrontObj siftereel;
    private static WavefrontObj tankbot;
    private static WavefrontObj behemoth;
    private static WavefrontObj bfangel;
    private static WavefrontObj miniUfo;

    public static WavefrontObj scutterfish() {
        if (scutterfish == null) scutterfish = load("scutterfish.obj");
        return scutterfish;
    }

    public static WavefrontObj scuttlecrab() {
        if (scuttlecrab == null) scuttlecrab = load("scuttlecrab.obj");
        return scuttlecrab;
    }

    public static WavefrontObj scrapfish() {
        if (scrapfish == null) scrapfish = load("scrapfish.obj");
        return scrapfish;
    }

    public static WavefrontObj depthsquid() {
        if (depthsquid == null) depthsquid = load("depthsquid.obj");
        return depthsquid;
    }

    public static WavefrontObj siftereel() {
        if (siftereel == null) siftereel = load("siftereel.obj");
        return siftereel;
    }

    public static WavefrontObj tankbot() {
        if (tankbot == null) tankbot = load("tankbot.obj");
        return tankbot;
    }

    /** hbm-space {@code ResourceManager.behemoth}: {@code war_machine.obj}. */
    public static WavefrontObj behemoth() {
        if (behemoth == null) behemoth = load("war_machine.obj");
        return behemoth;
    }

    public static WavefrontObj bfangel() {
        if (bfangel == null) bfangel = load("balefire_angel.obj");
        return bfangel;
    }

    public static WavefrontObj miniUfo() {
        if (miniUfo == null) miniUfo = load("mini_ufo.obj");
        return miniUfo;
    }

    public static final ResourceLocation MOON_COW = texture("moon_cow");
    public static final ResourceLocation SCUTTERFISH = texture("scutterfish");
    public static final ResourceLocation TANKBOT = texture("robtex");
    public static final ResourceLocation BEHEMOTH_BODY = texture("wm_body");
    public static final ResourceLocation BEHEMOTH_EYE = texture("wm_eye");
    public static final ResourceLocation BEHEMOTH_HELMET = texture("wm_helmet");
    public static final ResourceLocation BEHEMOTH_LEG = texture("wm_leg");
    public static final ResourceLocation BEHEMOTH_KNEE = texture("wm_knee");
    public static final ResourceLocation BEHEMOTH_HIP = texture("wm_hip");
    public static final ResourceLocation BFANGEL = texture("bfa_body");
    public static final ResourceLocation CREEPER_FLESH = texture("creeper_flesh");
    /** hbm-space's {@code ModelMoonCow} helmet: HBM's boron glass. */
    public static final ResourceLocation GLASS_BORON = Ids.of("geontm", "textures/blocks/glass_boron.png");
}
