package fr.geoventure.geontm.space.mob;

import fr.geoventure.geontm.space.mob.MobWeaponsSpace;
import fr.geoventure.geocore.dejson.BiomeModifiers;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.items.machine.ItemBattery;
import fr.geoventure.geontm.items.special.ItemChopper;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.space.mob.EntityBFAngel;
import fr.geoventure.geontm.space.mob.EntityDepthSquid;
import fr.geoventure.geontm.space.mob.EntityFish;
import fr.geoventure.geontm.space.mob.EntityMoonCow;
import fr.geoventure.geontm.space.mob.EntityScrapFish;
import fr.geoventure.geontm.space.mob.EntityScutterfish;
import fr.geoventure.geontm.space.mob.EntityScuttlecrab;
import fr.geoventure.geontm.space.mob.EntitySifterEel;
import fr.geoventure.geontm.space.mob.EntityTankbot;
import fr.geoventure.geontm.space.mob.EntityWarBehemoth;
import fr.geoventure.geontm.space.mob.ai.EntityAITankshell;
import fr.geoventure.geontm.space.mob.siege.EntitySiegeUFO;
import fr.geoventure.geocore.dejson.Tints;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.Entities;
import fr.geoventure.geocore.registry.EntityTag;
import fr.geoventure.geocore.registry.LivingAttributes;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.registry.Tabs;
import fr.geoventure.geocore.world.SpawnEggs;
import fr.geoventure.geocore.world.Spawns;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * hbm-space's mobs (History §19fb-mobs): the eleven entity types its {@code EntityMappings} added with {@code addMob}
 * (ids kept, under {@code ntmspace:}), their 1.7 sizes and categories, spawn eggs in the 1.7 colours, the three items
 * the mobs drop or are summoned by, the three sounds hbm-space added for them, and the entity tag
 * {@code ntmspace:suffocation_immune} (1.7 {@code ISuffocationImmune}) for the atmosphere system.
 *
 * <p>1.7 registered every mob through {@code ModEntityList.registerEntity} with tracking range 80 (5 chunks) and
 * update interval 3. Natural spawns are the planet biomes' own spawner lists (authored biome JSON); the placements here
 * are 1.7 {@code SpawnerAnimals}' (land mobs on the ground, water creatures in water over a liquid).
 */
public final class SpaceMobs {

    public static final DeferredRegister.EntityTypes ENTITY_TYPES = DeferredRegister.createEntityTypes(Constants.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister.Sounds SOUNDS = DeferredRegister.createSounds(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ENTITY_TYPES, ITEMS, SOUNDS);

    /** 1.7 {@code ISuffocationImmune}: breathes no air. Read by the atmosphere system. */
    public static final EntityTag SUFFOCATION_IMMUNE = EntityTag.of(Constants.MOD_ID + ":suffocation_immune");
    private static final EntityTag RADIATION_IMMUNE = EntityTag.of("geontm:radiation_immune");

    // ---------------------------------------------------------------------------------------- sounds

    /** {@code entity.beep} (hbm-space {@code hbm:entity.beep}): the war machines' step beep. */
    public static final RegistryObject<SoundEvent> BEEP = REG.sound("entity.beep");
    /** {@code entity.bfatalk}: the angel's voice line while its beam charges. */
    public static final RegistryObject<SoundEvent> BFA_TALK = REG.sound("entity.bfatalk");
    /** {@code entity.bfashoot}: the angel's (and the mini UFO's) beam. */
    public static final RegistryObject<SoundEvent> BFA_SHOOT = REG.sound("entity.bfashoot");

    // ---------------------------------------------------------------------------------------- entities

    public static final Reg.EntityHandle<EntityMoonCow> MOON_COW = REG.entity("entity_moon_cow", key -> Entities.build(
                    EntityType.Builder.<EntityMoonCow>of(EntityMoonCow::new, MobCategory.CREATURE)
                            .sized(0.9F, 1.3F).clientTrackingRange(5).updateInterval(3), key))
            .tag(SUFFOCATION_IMMUNE).tag(RADIATION_IMMUNE);
    public static final Reg.EntityHandle<EntityScutterfish> SCUTTERFISH = REG.entity("entity_scutterfish", key -> Entities.build(
            EntityType.Builder.<EntityScutterfish>of(EntityScutterfish::new, MobCategory.WATER_CREATURE)
                    .sized(2.0F, 2.0F).clientTrackingRange(5).updateInterval(3), key));
    public static final Reg.EntityHandle<EntityScuttlecrab> SCUTTLECRAB = REG.entity("entity_scuttlecrab", key -> Entities.build(
            EntityType.Builder.<EntityScuttlecrab>of(EntityScuttlecrab::new, MobCategory.WATER_CREATURE)
                    .sized(0.6F, 1.8F).clientTrackingRange(5).updateInterval(3), key));
    public static final Reg.EntityHandle<EntityDepthSquid> DEPTH_SQUID = REG.entity("entity_depthsquid", key -> Entities.build(
            EntityType.Builder.<EntityDepthSquid>of(EntityDepthSquid::new, MobCategory.WATER_CREATURE)
                    .sized(0.95F, 0.95F).clientTrackingRange(5).updateInterval(3), key));
    public static final Reg.EntityHandle<EntityScrapFish> SCRAPFISH = REG.entity("entity_scrapfish", key -> Entities.build(
            EntityType.Builder.<EntityScrapFish>of(EntityScrapFish::new, MobCategory.WATER_CREATURE)
                    .sized(2.0F, 2.0F).clientTrackingRange(5).updateInterval(3), key));
    public static final Reg.EntityHandle<EntitySifterEel> SIFTER_EEL = REG.entity("entity_siftereel", key -> Entities.build(
            Entities.noLoot(EntityType.Builder.<EntitySifterEel>of(EntitySifterEel::new, MobCategory.WATER_CREATURE))
                    .sized(2.0F, 2.0F).clientTrackingRange(5).updateInterval(3), key));
    public static final Reg.EntityHandle<EntityWarBehemoth> WAR_BEHEMOTH = REG.entity("entity_war_behemoth", key -> Entities.build(
                    Entities.noLoot(EntityType.Builder.<EntityWarBehemoth>of(EntityWarBehemoth::new, MobCategory.CREATURE))
                            .sized(1.75F, 6.35F).clientTrackingRange(5).updateInterval(3), key))
            .tag(SUFFOCATION_IMMUNE);
    public static final Reg.EntityHandle<EntityTankbot> TANKBOT = REG.entity("entity_tankbot", key -> Entities.build(
                    Entities.noLoot(EntityType.Builder.<EntityTankbot>of(EntityTankbot::new, MobCategory.CREATURE))
                            .sized(2.45F, 3.25F).fireImmune().clientTrackingRange(5).updateInterval(3), key))
            .tag(SUFFOCATION_IMMUNE).tag(RADIATION_IMMUNE);
    public static final Reg.EntityHandle<EntityBFAngel> BF_ANGEL = REG.entity("entity_bfa", key -> Entities.build(
                    EntityType.Builder.<EntityBFAngel>of(EntityBFAngel::new, MobCategory.MONSTER)
                            .sized(5.0F, 4.0F).fireImmune().clientTrackingRange(5).updateInterval(3), key))
            .tag(SUFFOCATION_IMMUNE).tag(RADIATION_IMMUNE);
    /** 1.7 {@code EntityUFOBase} is {@code ISuffocationImmune} in hbm-space, so the siege saucer is too. */
    public static final Reg.EntityHandle<EntitySiegeUFO> SIEGE_UFO = REG.entity("entity_meme_ufo", key -> Entities.build(
                    EntityType.Builder.<EntitySiegeUFO>of(EntitySiegeUFO::new, MobCategory.MONSTER)
                            .sized(1.5F, 1.0F).clientTrackingRange(5).updateInterval(3), key))
            .tag(SUFFOCATION_IMMUNE).tag(RADIATION_IMMUNE);

    static {
        // GeoNTM : pas d'astres NTM (Mun, Duna, Laythe...) ici, les créatures vivent sur les planètes Ad Astra, jamais
        // sur Terre. Vache lunaire : Lune ; machines de guerre : Mars ; faune de Laythe : eaux de Glacio. Poids de
        // hbm-space (biomes mun, thatmo, laythe_ocean/coast).
        String ns = Constants.MOD_ID;
        BiomeModifiers.addSpawns(ns, "space_moon_cow", "#geontm:space/moon", null, "geontm:entity_moon_cow", 10, 4, 4);
        BiomeModifiers.addSpawns(ns, "space_tankbot", "#geontm:space/mars", null, "geontm:entity_tankbot", 6, 1, 1);
        BiomeModifiers.addSpawns(ns, "space_war_behemoth", "#geontm:space/mars", null, "geontm:entity_war_behemoth", 2, 1, 1);
        BiomeModifiers.addSpawns(ns, "space_scutterfish", "#geontm:space/glacio", null, "geontm:entity_scutterfish", 10, 4, 4);
        BiomeModifiers.addSpawns(ns, "space_siftereel", "#geontm:space/glacio", null, "geontm:entity_siftereel", 10, 1, 3);
        BiomeModifiers.addSpawns(ns, "space_scuttlecrab", "#geontm:space/glacio", null, "geontm:entity_scuttlecrab", 4, 1, 3);
        BiomeModifiers.addSpawns(ns, "space_scrapfish", "#geontm:space/glacio", null, "geontm:entity_scrapfish", 2, 1, 4);
        BiomeModifiers.addSpawns(ns, "space_depthsquid", "#geontm:space/glacio", null, "geontm:entity_depthsquid", 1, 1, 4);
    }

    static {
        // hbm-space marks these HBM mobs ISuffocationImmune too (History: its space-hook rows for entity/mob/*)
        for (String id : new String[] {"geontm:entity_cyber_crab", "geontm:entity_tesla_crab", "geontm:entity_taint_crab",
                "geontm:entity_test_dummy", "geontm:entity_fbi", "geontm:entity_mob_hunter_chopper", "geontm:entity_mob_mask_man",
                "geontm:entity_parasite_maggot", "geontm:entity_plastic_bag", "geontm:entity_radiation_blaze", "geontm:entity_ufo",
                "geontm:entity_fbi_drone", "geontm:entity_glyphid", "geontm:entity_glyphid_behemoth", "geontm:entity_glyphid_brenda",
                "geontm:entity_glyphid_scout", "geontm:entity_glyphid_nuclear", "geontm:entity_glyphid_blaster",
                "geontm:entity_glyphid_bombardier", "geontm:entity_glyphid_brawler", "geontm:entity_glyphid_digger"}) {
            fr.geoventure.geocore.dejson.Tags.member("entity_type", SUFFOCATION_IMMUNE.namespace(), SUFFOCATION_IMMUNE.path(), id);
        }
    }

    // ---------------------------------------------------------------------------------------- items

    /** 1.7: {@code Item}, parts tab; the scutterfish's drop. */
    public static final RegistryObject<Item> SCUTTERTAIL = REG.simpleItem("scuttertail", p -> p)
            .transform(Presets.sprite()).tab(GeoNtmTab.MATERIALS);
    /**
     * 1.7: {@code ItemBattery(16,000,000, 16,000,000, 800,000)}, stack 1, control tab, the angel's drop. 1.7 drew it
     * with {@code ItemRenderAngelCore} (the angel's body mesh); here it keeps its 1.7 icon {@code battery_creative_new}.
     */
    public static final RegistryObject<ItemBattery> CORE_ANGEL = REG.<ItemBattery>item("core_angel",
                    p -> new ItemBattery(p, 16_000_000L, 16_000_000L, 800_000L), p -> p.stacksTo(1))
            .transform(Presets.sprite("geontm:items/battery_creative_new")).tab(GeoNtmTab.CONTROL);
    /** 1.7: {@code ItemChopper}, stack 1, consumables tab: summons the angel 25 blocks up. */
    public static final RegistryObject<ItemChopper> SPAWN_ANGEL = REG.<ItemChopper>item("spawn_angel",
                    p -> new ItemChopper(p, () -> BF_ANGEL.get(), 25, mob -> {}), p -> p.stacksTo(1))
            .transform(Presets.sprite()).tab(GeoNtmTab.CONSUMABLES);

    // ---------------------------------------------------------------------------------------- spawn eggs

    /** Every egg, in hbm-space's {@code addMob} order. */
    private static final List<RegistryObject<Item>> EGGS = new ArrayList<>();

    static {
        egg("entity_meme_ufo", () -> SIEGE_UFO.get(), 0x303030, 0x800000);
        egg("entity_war_behemoth", () -> WAR_BEHEMOTH.get(), 0x204131, 0x75CE00);
        egg("entity_tankbot", () -> TANKBOT.get(), 0x204131, 0x75CE00);
        egg("entity_bfa", () -> BF_ANGEL.get(), 0x204131, 0x75CE00);
        egg("entity_moon_cow", () -> MOON_COW.get(), 0xECD376, 0x262A44);
        egg("entity_scutterfish", () -> SCUTTERFISH.get(), 0xC8C9CD, 0x858894);
        egg("entity_scuttlecrab", () -> SCUTTLECRAB.get(), 0xF17951, 0xEDDABB);
        egg("entity_depthsquid", () -> DEPTH_SQUID.get(), 0x00B4DF, 0x016085);
        egg("entity_scrapfish", () -> SCRAPFISH.get(), 0xDF9835, 0x510E13);
        egg("entity_siftereel", () -> SIFTER_EEL.get(), 0x5B963E, 0xC0B286);
    }

    /** As HBM's {@code ModSpawnEggs}: {@code <entity id>_spawn_egg}, the two-layer egg tinted with the 1.7 colours. */
    private static void egg(String entity, Supplier<EntityType<? extends Mob>> type, int primary, int secondary) {
        EGGS.add(REG.<Item>item(entity + "_spawn_egg", p -> SpawnEggs.create(type, primary, secondary, p))
                .transform(Presets.sprite("geontm:items/spawn_egg", "geontm:items/spawn_egg_overlay")
                        .tints(Tints.constant(0xFF000000 | primary), Tints.constant(0xFF000000 | secondary))));
    }

    /** The eggs, read-only, in hbm-space's order. */
    public static List<RegistryObject<Item>> eggs() {
        return Collections.unmodifiableList(EGGS);
    }

    private SpaceMobs() {}

    public static void init() {
        MobWeaponsSpace.init();
        LivingAttributes.register(MOON_COW, Cow::createAttributes);
        LivingAttributes.register(SCUTTERFISH, EntityFish::createAttributes);
        LivingAttributes.register(SCRAPFISH, EntityFish::createAttributes);
        LivingAttributes.register(SIFTER_EEL, EntityFish::createAttributes);
        LivingAttributes.register(SCUTTLECRAB, EntityScuttlecrab::createAttributes);
        LivingAttributes.register(DEPTH_SQUID, Squid::createAttributes);
        LivingAttributes.register(WAR_BEHEMOTH, EntityWarBehemoth::createAttributes);
        LivingAttributes.register(TANKBOT, EntityTankbot::createAttributes);
        LivingAttributes.register(BF_ANGEL, EntityBFAngel::createAttributes);
        LivingAttributes.register(SIEGE_UFO, EntitySiegeUFO::createAttributes);

        // 1.7 SpawnerAnimals: creatures on a solid floor, water creatures in water over a liquid. The 1.7 per-mob
        // rules (grass and light for the cow, darkness for the war machines, y 45..63 for the squid, tumor for the
        // flesh creeper) are each mob's checkSpawnRules; 1.7's chunk-generation spawning asked none of them.
        Spawns.placement(() -> MOON_COW.get(), Spawns.Placement.ON_GROUND, Spawns.Rule.GENERATION_ANIMAL);
        Spawns.placement(() -> TANKBOT.get(), Spawns.Placement.ON_GROUND, Spawns.Rule.GENERATION_MONSTER);
        Spawns.placement(() -> WAR_BEHEMOTH.get(), Spawns.Placement.ON_GROUND, Spawns.Rule.GENERATION_MONSTER);
        Spawns.placement(() -> SCUTTERFISH.get(), Spawns.Placement.IN_WATER, Spawns.Rule.LIQUID_BELOW);
        Spawns.placement(() -> SCRAPFISH.get(), Spawns.Placement.IN_WATER, Spawns.Rule.LIQUID_BELOW);
        Spawns.placement(() -> SIFTER_EEL.get(), Spawns.Placement.IN_WATER, Spawns.Rule.LIQUID_BELOW);
        Spawns.placement(() -> SCUTTLECRAB.get(), Spawns.Placement.IN_WATER, Spawns.Rule.LIQUID_BELOW);
        Spawns.placement(() -> DEPTH_SQUID.get(), Spawns.Placement.IN_WATER, Spawns.Rule.LIQUID_BELOW);

        EntityAITankshell.registerCasing();

        // The vanilla spawn-egg tab by id, as HBM's eggs (ModSpawnEggs.init)
        Tabs.modifyEntries(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
                fr.geoventure.geocore.Ids.of("minecraft", "spawn_eggs")), entries -> {
            for (RegistryObject<Item> egg : EGGS) entries.accept(new ItemStack(egg.get()));
        });
    }
}
