package fr.geoventure.geontm.itempool;

import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.world.ChestLoot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

/**
 * 1.7 {@code MainRegistry.java:327-339}: the thirteen {@code ChestGenHooks.addItem} calls that
 * put GEONTM items into vanilla chests (village blacksmith, mineshaft corridor, dungeon, desert and jungle temple,
 * bonus chest), all {@code 1-1} of one item at the given weight.
 *
 * <p>1.7 added each item to the category's single weighted list and drew {@code ChestGenHooks.getCount} times
 * from it. Each category here is one extra pool on its modern counterpart, rolled the 1.7 number of times, holding
 * the GEONTM items at their 1.7 weights plus an empty entry carrying the 1.7 vanilla list's total weight. Each draw
 * then yields an GEONTM item with exactly the 1.7 probability {@code w / (W + sum of GEONTM weights)}. The vanilla
 * totals and draw counts come from the bytecode of the 1.7.10 client jar and Forge 10.13.4.1614's
 * {@code ChestGenHooks.init}, which also adds an enchanted book of weight 1 to the mineshaft, both temples and
 * the dungeon; {@code getCount} draws {@code min + rand(max - min)}:
 * <ul>
 *   <li>dungeon ({@code WorldGenDungeons}, 15 entries): 119 + 1 book = 120, {@code addInfo(8, 8)}: 8 draws</li>
 *   <li>mineshaft corridor: 79 + 1 = 80, {@code addInfo(3, 7)}: 3..6 draws</li>
 *   <li>village blacksmith: 94 (no book), {@code addInfo(3, 9)}: 3..8 draws</li>
 *   <li>desert pyramid, jungle pyramid: 72 + 1 = 73 each, {@code addInfo(2, 7)}: 2..6 draws</li>
 *   <li>bonus chest ({@code WorldServer}): 64, {@code addInfo(10, 10)}: 10 draws</li>
 * </ul>
 * The blacksmith's modern successor is the weaponsmith chest. HBM's own library dungeon uses
 * {@code chests/simple_dungeon} for the chests 1.7 filled from {@code DUNGEON_CHEST}, so it gets the dungeon
 * extras too.
 */
public final class VanillaChestLoot {

    private VanillaChestLoot() {}

    private static ChestLoot.Entry one(String id, int weight) {
        Supplier<Item> item = () -> BuiltInRegistries.ITEM.getValue(Ids.parse(id));
        return new ChestLoot.Entry(item, 1, 1, weight);
    }

    /** Call once during common initialisation. */
    public static void init() {
        ChestLoot.inject("minecraft:chests/village/village_weaponsmith", "geontm:village_blacksmith", 3, 8, 94, List.of(
                one("geontm:armor_polish", 3),
                one("geontm:bathwater", 1)));
        ChestLoot.inject("minecraft:chests/abandoned_mineshaft", "geontm:mineshaft_corridor", 3, 6, 80, List.of(
                one("geontm:bathwater", 1),
                one("geontm:serum", 5),
                one("geontm:no9", 5),
                one("geontm:key_red_cracked", 5)));
        ChestLoot.inject("minecraft:chests/simple_dungeon", "geontm:dungeon_chest", 8, 8, 120, List.of(
                one("geontm:heart_piece", 1),
                one("geontm:key_red_cracked", 5),
                one("geontm:scrumpy", 1)));
        ChestLoot.inject("minecraft:chests/desert_pyramid", "geontm:pyramid_desert", 2, 6, 73, List.of(
                one("geontm:heart_piece", 1),
                one("geontm:scrumpy", 1)));
        ChestLoot.inject("minecraft:chests/jungle_temple", "geontm:pyramid_jungle", 2, 6, 73, List.of(
                one("geontm:heart_piece", 1)));
        ChestLoot.inject("minecraft:chests/spawn_bonus_chest", "geontm:bonus_chest", 10, 10, 64, List.of(
                one("geontm:no9", 7)));
    }
}
