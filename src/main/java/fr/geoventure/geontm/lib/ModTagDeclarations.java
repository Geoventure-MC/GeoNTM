package fr.geoventure.geontm.lib;

import fr.geoventure.geocore.dejson.Tags;

/**
 * geontm's own gameplay tags and its additions to vanilla's, declared through GeoCore's {@code dejson}
 * vocabulary instead of written as JSON. Touches no Minecraft class.
 *
 * <p>Material ({@code c:}) tags are {@link fr.geoventure.geontm.inventory.material.ModMaterialTags}; tool-mining tags are
 * stated on each block's registration ({@code .tag(BlockTag.X)}, {@link fr.geoventure.geontm.registration.Reg}).
 *
 * <p>Block lists that name other machines or another mod's blocks use {@link Tags.Tag#optional} members, so a
 * block that is not ported yet leaves the tag loadable instead of failing it. The armour repair tags of sets
 * whose 1.7 repair material is not ported ({@code cobalt}, {@code starmetal}, {@code t51}) are declared empty:
 * the armour material names the tag, so it has to exist.
 */
public final class ModTagDeclarations {

    private static boolean declared;

    private ModTagDeclarations() {}

    public static void declare() {
        if (declared) return;
        declared = true;

        lists();
        repairs();
    }

    private static void lists() {
        Tags.of("block", "geontm", "fbi_breakable")
                .values("#minecraft:wooden_doors", "minecraft:iron_door", "#minecraft:wooden_trapdoors", "minecraft:chest",
                        "minecraft:trapped_chest")
                .optional("geontm:machine_press", "geontm:machine_chemical_plant", "geontm:machine_chemical_factory",
                        "geontm:machine_purex", "geontm:machine_crystallizer", "geontm:machine_cyclotron", "geontm:machine_epress",
                        "geontm:machine_diesel", "geontm:machine_industrial_turbine", "geontm:machine_chungus",
                        "geontm:machine_rtg_grey", "geontm:crate_iron", "geontm:crate_steel")
                .register();
        Tags.of("block", "geontm", "fleija_immune")
                .register();
        Tags.of("block", "geontm", "radiation_resistant")
                .register();
        // 1.7 isRadImmune's classes as the types they cover; bogged and parched exist only on newer versions
        Tags.of("entity_type", "geontm", "radiation_immune")
                .values("geontm:entity_mob_nuclear_creeper", "geontm:entity_elder_one", "minecraft:mooshroom",
                        "minecraft:zombie", "minecraft:husk", "minecraft:drowned", "minecraft:zombie_villager",
                        "minecraft:zombified_piglin", "minecraft:skeleton", "minecraft:stray", "minecraft:wither_skeleton",
                        "minecraft:ocelot", "minecraft:cat", "minecraft:zombie_horse", "minecraft:skeleton_horse",
                        "minecraft:armor_stand")
                .optional("minecraft:bogged", "minecraft:parched")
                .register();
        // 1.7 EntityChicken.fall did nothing; vanilla's chicken is in this tag by its own id
        Tags.of("entity_type", "minecraft", "fall_damage_immune")
                .values("geontm:entity_fucc_a_ducc", "geontm:entity_elder_one")
                .register();
        Tags.of("item", "geontm", "certus_quartz_clusters")
                .optional("ae2:quartz_cluster")
                .register();
        // Neutron producers' shape tag for the demon core (read by the neutron activation census).
        Tags.of("item", "geontm", "hazard/demon_core_closed")
                .values("geontm:demon_core_closed")
                .register();
        // 1.20.1 AE2 tags its crystals only as forge:gems/certus_quartz or c:certus_quartz
        Tags.of("item", "c", "gems/certus_quartz")
                .optional("ae2:certus_quartz_crystal")
                .register();
        Tags.of("block", "geontm", "meteorite_replaceable")
                .optional("geontm:block_meteor_molten", "geontm:ore_meteor_iron", "geontm:ore_meteor_copper",
                        "geontm:ore_meteor_aluminium", "geontm:ore_meteor_rareearth", "geontm:ore_meteor_cobalt", "geontm:block_meteor",
                        "geontm:block_meteor_cobble", "geontm:block_meteor_broken", "geontm:block_meteor_treasure")
                .register();
        Tags.block("geontm", "shatters_when_shot",
                "#minecraft:impermeable", "minecraft:glass_pane", "minecraft:white_stained_glass_pane",
                "minecraft:orange_stained_glass_pane", "minecraft:magenta_stained_glass_pane",
                "minecraft:light_blue_stained_glass_pane", "minecraft:yellow_stained_glass_pane",
                "minecraft:lime_stained_glass_pane", "minecraft:pink_stained_glass_pane",
                "minecraft:gray_stained_glass_pane", "minecraft:light_gray_stained_glass_pane",
                "minecraft:cyan_stained_glass_pane", "minecraft:purple_stained_glass_pane",
                "minecraft:blue_stained_glass_pane", "minecraft:brown_stained_glass_pane",
                "minecraft:green_stained_glass_pane", "minecraft:red_stained_glass_pane",
                "minecraft:black_stained_glass_pane");
        // 1.7 TileEntityMachinePumpBase.validBlocks: the ground a groundwater pump draws from (GeoNtmBlockTags.PUMP_GROUND)
        Tags.block("geontm", "pump_ground",
                "minecraft:grass_block", "minecraft:dirt", "minecraft:sand", "minecraft:mycelium", "geontm:waste_earth",
                "geontm:dirt_dead", "geontm:dirt_oily", "geontm:sand_dirty", "geontm:sand_dirty_red");
        // OreVeinPlacer.STONE and the bubble's sand: the default hosts of the configurable world features
        Tags.block("geontm", "legacy_stone",
                "minecraft:stone", "minecraft:granite", "minecraft:diorite", "minecraft:andesite", "minecraft:deepslate",
                "minecraft:tuff");
        Tags.block("geontm", "tar_sand_hosts", "minecraft:sand", "minecraft:red_sand");
        Tags.block("geontm", "uranium_ores",
                "#c:ores/uranium");
        Tags.entityType("geontm", "bosses",
                "geontm:entity_elder_one", "geontm:entity_mob_mask_man", "geontm:entity_ufo", "geontm:entity_balls_o_tron",
                "geontm:entity_mob_hunter_chopper");
        // every ScrapType of 1.7 scrap_plastic (the wildcard smelting input); literal ids, because this class runs in
        // DejsonMain's plain JVM, where the item classes must not initialise
        Tags.item("geontm", "scrap_plastic",
                "geontm:scrap_plastic_board_blank", "geontm:scrap_plastic_board_transistor",
                "geontm:scrap_plastic_board_converter", "geontm:scrap_plastic_bridge_north",
                "geontm:scrap_plastic_bridge_south", "geontm:scrap_plastic_bridge_io", "geontm:scrap_plastic_bridge_bus",
                "geontm:scrap_plastic_bridge_chipset", "geontm:scrap_plastic_bridge_cmos", "geontm:scrap_plastic_bridge_bios",
                "geontm:scrap_plastic_cpu_register", "geontm:scrap_plastic_cpu_clock", "geontm:scrap_plastic_cpu_logic",
                "geontm:scrap_plastic_cpu_cache", "geontm:scrap_plastic_cpu_ext", "geontm:scrap_plastic_cpu_socket",
                "geontm:scrap_plastic_mem_socket", "geontm:scrap_plastic_mem_16k_a", "geontm:scrap_plastic_mem_16k_b",
                "geontm:scrap_plastic_mem_16k_c", "geontm:scrap_plastic_mem_16k_d", "geontm:scrap_plastic_card_board",
                "geontm:scrap_plastic_card_processor");
        // 1.7 crafting turns a bare Block ingredient into ItemStack(block, 1, WILDCARD_VALUE): every metadata of
        // the block is accepted. These are the port's items for those metadata (exactly 1.7's, no later variants).
        // steel_scaffold meta 0..3 (23 1.7 crafting recipes)
        Tags.item("geontm", "steel_scaffolds",
                "geontm:steel_scaffold", "geontm:steel_scaffold_red", "geontm:steel_scaffold_white", "geontm:steel_scaffold_yellow");
        // Blocks.sandstone meta 0..2: plain, chiseled, "smooth" (1.7's smooth sandstone is today's cut sandstone)
        Tags.item("geontm", "sandstones",
                "minecraft:sandstone", "minecraft:chiseled_sandstone", "minecraft:cut_sandstone");
        // Blocks.sand meta 0..1 (sand, red sand) is #c:sands (ModMaterialTags)
        // Blocks.stone_slab meta 0..7: stone (today's smooth stone slab), sandstone, wood (petrified oak), cobblestone,
        // brick, stone brick, nether brick, quartz. Blocks.stonebrick meta 0..3 is vanilla's #minecraft:stone_bricks.
        Tags.item("geontm", "stone_slabs",
                "minecraft:smooth_stone_slab", "minecraft:sandstone_slab", "minecraft:petrified_oak_slab",
                "minecraft:cobblestone_slab", "minecraft:brick_slab", "minecraft:stone_brick_slab",
                "minecraft:nether_brick_slab", "minecraft:quartz_slab");
        // 1.7 Items.fish with the wildcard meta: raw cod, salmon, clownfish and pufferfish, not the cooked fish
        // (Mixer FISHOIL, Liquefaction FISHOIL). Vanilla's fishes tag also holds the cooked ones.
        Tags.item("geontm", "raw_fishes",
                "minecraft:cod", "minecraft:salmon", "minecraft:tropical_fish", "minecraft:pufferfish");
        Tags.item("geontm", "zirnox_rods",
                "geontm:rod_zirnox_natural_uranium_fuel", "geontm:rod_zirnox_uranium_fuel", "geontm:rod_zirnox_th232_fuel",
                "geontm:rod_zirnox_thorium_fuel", "geontm:rod_zirnox_mox_fuel", "geontm:rod_zirnox_plutonium_fuel",
                "geontm:rod_zirnox_u233_fuel", "geontm:rod_zirnox_u235_fuel", "geontm:rod_zirnox_les_fuel",
                "geontm:rod_zirnox_lithium_fuel", "geontm:rod_zirnox_zfb_mox_fuel");
        Tags.item("geontm", "zirnox_depleted_rods",
                "geontm:rod_zirnox_depleted_natural_uranium_fuel", "geontm:rod_zirnox_depleted_uranium_fuel",
                "geontm:rod_zirnox_depleted_thorium_fuel", "geontm:rod_zirnox_depleted_mox_fuel",
                "geontm:rod_zirnox_depleted_plutonium_fuel", "geontm:rod_zirnox_depleted_u233_fuel",
                "geontm:rod_zirnox_depleted_u235_fuel", "geontm:rod_zirnox_depleted_les_fuel",
                "geontm:rod_zirnox_depleted_zfb_mox_fuel");
        Tags.block("minecraft", "beacon_base_blocks",
                "geontm:block_actinium", "geontm:block_bakelite", "geontm:block_bismuth", "geontm:block_cadmium",
                "geontm:block_coltan", "geontm:block_lanthanium", "geontm:block_niobium", "geontm:block_polymer",
                "geontm:block_rubber", "geontm:block_tantalium", "geontm:block_zirconium", "geontm:block_tcalloy",
                "geontm:block_cdalloy", "geontm:block_australium", "geontm:block_lithium", "geontm:block_waste",
                "geontm:block_waste_painted", "geontm:block_waste_vitrified", "geontm:block_yellowcake",
                // the rest of 1.7's beacon bases: every BlockBeaconable and every BlockHazard().makeBeaconable() storage block
                "geontm:block_aluminium", "geontm:block_beryllium", "geontm:block_boron", "geontm:block_cobalt", "geontm:block_combine_steel",
                "geontm:block_desh", "geontm:block_dineutronium", "geontm:block_dura_steel", "geontm:block_euphemium",
                "geontm:block_fluorite", "geontm:block_lead", "geontm:block_magnetized_tungsten", "geontm:block_mox_fuel",
                "geontm:block_neptunium", "geontm:block_niter", "geontm:block_plutonium", "geontm:block_plutonium_fuel",
                "geontm:block_polonium", "geontm:block_pu238", "geontm:block_pu239", "geontm:block_pu240", "geontm:block_pu_mix",
                "geontm:block_ra226", "geontm:block_red_copper", "geontm:block_red_phosphorus", "geontm:block_schrabidate",
                "geontm:block_schrabidium", "geontm:block_schrabidium_fuel", "geontm:block_schraranium", "geontm:block_solinium",
                "geontm:block_starmetal", "geontm:block_steel", "geontm:block_sulfur", "geontm:block_thorium", "geontm:block_thorium_fuel",
                "geontm:block_titanium", "geontm:block_trinitite", "geontm:block_tungsten", "geontm:block_u233", "geontm:block_u235",
                "geontm:block_u238", "geontm:block_uranium", "geontm:block_uranium_fuel", "geontm:block_white_phosphorus");
        Tags.entityType("minecraft", "arthropod",
                "geontm:entity_glyphid", "geontm:entity_glyphid_behemoth", "geontm:entity_glyphid_blaster",
                "geontm:entity_glyphid_bombardier", "geontm:entity_glyphid_brawler", "geontm:entity_glyphid_brenda",
                "geontm:entity_glyphid_digger", "geontm:entity_glyphid_nuclear", "geontm:entity_glyphid_scout",
                "geontm:entity_parasite_maggot");
        Tags.entityType("minecraft", "undead",
                "geontm:entity_undead_soldier");
        // Créatures de NTM: Space : faites pour les astres d'Ad Astra, qui étouffe et gèle toute entité hors de ces tags
        // (retour joueur 23/11 : la vache lunaire mourait sur la Lune).
        Tags.entityType("ad_astra", "lives_without_oxygen",
                "geontm:entity_moon_cow", "geontm:entity_tankbot", "geontm:entity_war_behemoth",
                "geontm:entity_scutterfish", "geontm:entity_siftereel", "geontm:entity_scuttlecrab", "geontm:entity_scrapfish",
                "geontm:entity_depthsquid", "geontm:entity_bfa", "geontm:entity_meme_ufo");
        Tags.entityType("ad_astra", "can_survive_extreme_cold",
                "geontm:entity_moon_cow", "geontm:entity_tankbot", "geontm:entity_war_behemoth",
                "geontm:entity_scutterfish", "geontm:entity_siftereel", "geontm:entity_scuttlecrab", "geontm:entity_scrapfish",
                "geontm:entity_depthsquid", "geontm:entity_bfa", "geontm:entity_meme_ufo");
        Tags.entityType("ad_astra", "can_survive_extreme_heat",
                "geontm:entity_moon_cow", "geontm:entity_tankbot", "geontm:entity_war_behemoth",
                "geontm:entity_scutterfish", "geontm:entity_siftereel", "geontm:entity_scuttlecrab", "geontm:entity_scrapfish",
                "geontm:entity_depthsquid", "geontm:entity_bfa", "geontm:entity_meme_ufo");
        Tags.item("minecraft", "dyes",
                "geontm:chemical_dye_black", "geontm:chemical_dye_blue", "geontm:chemical_dye_brown", "geontm:chemical_dye_cyan",
                "geontm:chemical_dye_gray", "geontm:chemical_dye_green", "geontm:chemical_dye_lightblue",
                "geontm:chemical_dye_lime", "geontm:chemical_dye_magenta", "geontm:chemical_dye_orange",
                "geontm:chemical_dye_pink", "geontm:chemical_dye_purple", "geontm:chemical_dye_red",
                "geontm:chemical_dye_silver", "geontm:chemical_dye_white", "geontm:chemical_dye_yellow", "geontm:crayon_black",
                "geontm:crayon_blue", "geontm:crayon_brown", "geontm:crayon_cyan", "geontm:crayon_gray", "geontm:crayon_green",
                "geontm:crayon_lightblue", "geontm:crayon_lime", "geontm:crayon_magenta", "geontm:crayon_orange",
                "geontm:crayon_pink", "geontm:crayon_purple", "geontm:crayon_red", "geontm:crayon_silver", "geontm:crayon_white",
                "geontm:crayon_yellow", "geontm:cinnabar", "geontm:fluorite", "geontm:oil_tar_coal", "geontm:oil_tar_crack",
                "geontm:oil_tar_crude", "geontm:oil_tar_paraffin", "geontm:oil_tar_wax", "geontm:oil_tar_wood",
                "geontm:powder_ash_coal", "geontm:powder_ash_fly", "geontm:powder_ash_fullerene", "geontm:powder_ash_misc",
                "geontm:powder_ash_soot", "geontm:powder_ash_wood", "geontm:powder_cadmium", "geontm:powder_coal",
                "geontm:powder_lapis", "geontm:powder_lignite", "geontm:powder_titanium", "geontm:sulfur");
        Tags.item("minecraft", "wolf_food",
                "geontm:ingot_semtex", "geontm:glyphid_meat", "geontm:glyphid_meat_grilled");
    }

    /** One tag per armour set, naming what repairs it on the anvil (1.7 {@code ArmorMaterial.customCraftingMaterial}). */
    private static void repairs() {
        repair("asbestos", "geontm:asbestos_cloth");
        repair("bj", "geontm:plate_armor_lunar");
        repair("cobalt");
        repair("dns", "geontm:plate_armor_dnt");
        repair("hazmat_cloth", "geontm:hazmat_cloth");
        repair("hazmat_cloth_grey", "geontm:hazmat_cloth_grey");
        repair("hazmat_cloth_red", "geontm:hazmat_cloth_red");
        repair("paa", "geontm:plate_paa");
        repair("schrabidium", "geontm:ingot_schrabidium");
        repair("security", "geontm:plate_kevlar");
        repair("starmetal");
        repair("steel", "geontm:ingot_steel");
        repair("t51");
        repair("taurun", "geontm:plate_iron");
        repair("titanium", "geontm:ingot_titanium");
    }

    private static void repair(String set, String... materials) {
        Tags.of("item", "geontm", "repairs/" + set).values(materials).register();
    }
}
