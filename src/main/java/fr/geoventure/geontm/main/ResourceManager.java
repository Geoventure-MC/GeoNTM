package fr.geoventure.geontm.main;

import fr.geoventure.geontm.lib.Library;
import net.minecraft.resources.ResourceLocation;

/**
 * The texture identifiers the copied ntm-next entities and their renderers name directly. ntm-next's
 * class also owns every OBJ model and shader; the entity meshes live client-side in
 * {@code fr.geoventure.geontm.client.render.entity.NtmEntityModels}, since loading an OBJ needs the client's
 * resource manager and this class is reachable from common code (the glyphid skins are resolved by
 * {@code EntityGlyphid.getSkin}).
 */
public final class ResourceManager {

    private ResourceManager() {}

    public static final ResourceLocation glyphid_tex = Library.id("textures/entity/glyphid.png");
    public static final ResourceLocation glyphid_scout_tex = Library.id("textures/entity/glyphid_scout.png");
    public static final ResourceLocation glyphid_blaster_tex = Library.id("textures/entity/glyphid_blaster.png");
    public static final ResourceLocation glyphid_bombardier_tex = Library.id("textures/entity/glyphid_bombardier.png");
    public static final ResourceLocation glyphid_brawler_tex = Library.id("textures/entity/glyphid_brawler.png");
    public static final ResourceLocation glyphid_behemoth_tex = Library.id("textures/entity/glyphid_behemoth.png");
    public static final ResourceLocation glyphid_brenda_tex = Library.id("textures/entity/glyphid_brenda.png");
    public static final ResourceLocation glyphid_digger_tex = Library.id("textures/entity/glyphid_digger.png");
    public static final ResourceLocation glyphid_nuclear_tex = Library.id("textures/entity/glyphid_nuclear.png");
    public static final ResourceLocation glyphid_infestation_tex = Library.id("textures/entity/glyphid_infestation.png");

    // Mobs
    public static final ResourceLocation quadcopter_tex = Library.id("textures/entity/quadcopter.png");
    public static final ResourceLocation radbeast_tex = Library.id("textures/entity/radbeast.png");
    public static final ResourceLocation teslacrab_tex = Library.id("textures/entity/teslacrab.png");
    public static final ResourceLocation taintcrab_tex = Library.id("textures/entity/taintcrab.png");
    public static final ResourceLocation crab_tex = Library.id("textures/entity/crab.png");
    public static final ResourceLocation blockspider_tex = Library.id("textures/entity/blockspider.png");
    public static final ResourceLocation plasticbag_tex = Library.id("textures/entity/plasticbag.png");
    public static final ResourceLocation duck_tex = Library.id("textures/entity/duck.png");
    public static final ResourceLocation pigeon_tex = Library.id("textures/entity/pigeon.png");
    public static final ResourceLocation dummy_tex = Library.id("textures/entity/dummy.png");
    public static final ResourceLocation boat_rubber_tex = Library.id("textures/entity/boat_rubber.png");
    public static final ResourceLocation maskman_tex = Library.id("textures/entity/maskman.png");
    public static final ResourceLocation iou_tex = Library.id("textures/entity/iou.png");
    public static final ResourceLocation ufo_tex = Library.id("textures/entity/ufo.png");
    public static final ResourceLocation mark_zero_head_tex = Library.id("textures/entity/mark_zero_head.png");
    public static final ResourceLocation mark_zero_body_tex = Library.id("textures/entity/mark_zero_body.png");
    public static final ResourceLocation ghost_tex = Library.id("textures/entity/ghost.png");
    public static final ResourceLocation fbi_tex = Library.id("textures/entity/fbi.png");
    public static final ResourceLocation creeper_nuclear_tex = Library.id("textures/entity/creeper.png");
    public static final ResourceLocation creeper_tainted_tex = Library.id("textures/entity/creeper_tainted.png");
    public static final ResourceLocation creeper_phosgene_tex = Library.id("textures/entity/creeper_phosgene.png");
    public static final ResourceLocation creeper_volatile_tex = Library.id("textures/entity/creeper_volatile.png");
    public static final ResourceLocation creeper_gold_tex = Library.id("textures/entity/creeper_gold.png");
    public static final ResourceLocation creeper_armor_tex = Library.id("textures/entity/creeper_armor.png");
    public static final ResourceLocation creeper_armor_taint_tex = Library.id("textures/entity/creeper_armor_taint.png");
    public static final ResourceLocation parasite_maggot_tex = Library.id("textures/entity/parasite_maggot.png");
    public static final ResourceLocation chopper_tex = Library.id("textures/entity/chopper.png");
    public static final ResourceLocation meteor_tex = Library.id("textures/entity/meteor.png");
    public static final ResourceLocation shrapnel_tex = Library.id("textures/entity/shrapnel.png");

    // Vehicles, planes and props
    public static final ResourceLocation train_tram_tex = Library.id("textures/block/models/trains/tram.png");
    public static final ResourceLocation tram_trailer_tex = Library.id("textures/block/models/trains/tram_trailer.png");
    public static final ResourceLocation boxcar_tex = Library.id("textures/block/models/boxcar.png");
    public static final ResourceLocation duchessgambit_tex = Library.id("textures/block/models/duchessgambit.png");
    public static final ResourceLocation building_tex = Library.id("textures/models/weapons/building.png");
    public static final ResourceLocation torpedo_tex = Library.id("textures/models/weapons/torpedo.png");
    public static final ResourceLocation c130_0_tex = Library.id("textures/models/weapons/c130_0.png");
    public static final ResourceLocation dornier_1_tex = Library.id("textures/models/dornier_1.png");
    public static final ResourceLocation dornier_2_tex = Library.id("textures/models/dornier_2.png");
    public static final ResourceLocation dornier_4_tex = Library.id("textures/models/dornier_4.png");
    public static final ResourceLocation b29_0_tex = Library.id("textures/models/b29_0.png");
    public static final ResourceLocation b29_1_tex = Library.id("textures/models/b29_1.png");
    public static final ResourceLocation b29_2_tex = Library.id("textures/models/b29_2.png");
    public static final ResourceLocation b29_3_tex = Library.id("textures/models/b29_3.png");
    public static final ResourceLocation bomblet_zeta_tex = Library.id("textures/models/bomblet_zeta_texture.png");
    public static final ResourceLocation delivery_drone_tex = Library.id("textures/models/machines/drone.png");
    public static final ResourceLocation delivery_drone_request_tex = Library.id("textures/models/machines/drone_request.png");
    public static final ResourceLocation delivery_drone_express_tex = Library.id("textures/models/machines/drone_express.png");
    public static final ResourceLocation supply_crate_tex = Library.id("textures/block/crate_can.png");
    public static final ResourceLocation soyuz_lander_tex = Library.id("textures/models/soyuz_capsule/soyuz_lander.png");
    public static final ResourceLocation soyuz_chute_tex = Library.id("textures/models/soyuz_capsule/soyuz_chute.png");
    public static final ResourceLocation soyuz_memento_tex = Library.id("textures/models/misc/polaroid_memento.png");
    public static final ResourceLocation[][] soyuz_skin_tex = soyuzSkins("soyuz", "soyuz_luna", "soyuz_authentic");
    public static final ResourceLocation miner_rocket_tex = Library.id("textures/models/miner_rocket.png");
    public static final ResourceLocation bobmazon_tex = Library.id("textures/models/bobmazon.png");
    public static final ResourceLocation custom_nuke_tex = Library.id("textures/models/custom_nuke.png");
    public static final ResourceLocation emp_ring_tex = Library.id("textures/models/emp_blast.png");
    public static final ResourceLocation sat_foeq_burning_tex = Library.id("textures/models/sat_foeq_burning.png");
    public static final ResourceLocation bullet_tex = Library.id("textures/models/bullet.png");
    public static final ResourceLocation bullet_chopper_tex = Library.id("textures/models/emplacer.png");
    public static final ResourceLocation bullet_critical_tex = Library.id("textures/models/tau.png");
    public static final ResourceLocation chopper_bomb_tex = Library.id("textures/models/chopper_bomb.png");
    public static final ResourceLocation tomblast_tex = Library.id("textures/models/explosion/tomblast.png");
    public static final ResourceLocation tom_main_tex = Library.id("textures/models/weapons/tom_main.png");
    public static final ResourceLocation tom_flame_tex = Library.id("textures/models/weapons/tom_flame.png");
    public static final ResourceLocation mini_nuke_tex = Library.id("textures/models/projectiles/mini_nuke.png");
    public static final ResourceLocation stirling_tex = Library.id("textures/models/machines/stirling.png");
    public static final ResourceLocation stirling_steel_tex = Library.id("textures/models/machines/stirling_steel.png");
    public static final ResourceLocation sawmill_tex = Library.id("textures/models/machines/sawmill.png");
    public static final ResourceLocation white_tex = Library.id("textures/models/misc/white.png");

    /**
     * 1.7's per-part Soyuz sheets: one folder per skin, one file per structural group, in the order
     * {@code SoyuzMesh} indexes them.
     */
    private static ResourceLocation[][] soyuzSkins(String... folders) {
        String[] parts = {"engineblock", "bottomstage", "topstage", "payload", "payloadblocks", "les", "lesthrusters",
                "mainengines", "sideengines", "booster", "boosterside"};
        ResourceLocation[][] skins = new ResourceLocation[folders.length][parts.length];
        for (int skin = 0; skin < folders.length; skin++)
            for (int part = 0; part < parts.length; part++)
                skins[skin][part] = Library.id("textures/models/" + folders[skin] + "/" + parts[part] + ".png");
        return skins;
    }
}
