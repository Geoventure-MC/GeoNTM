package fr.geoventure.geontm.delegacy.de7;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

/** Item components DE and Continuum spell differently, as raw NBT before the game decodes the stack. */
final class Components {
    private static final Map<String, String> CELL_FLUIDS = Map.of("cell_uf6", "geontm:uf6", "cell_puf6", "geontm:puf6",
            "cell_deuterium", "geontm:deuterium", "cell_tritium", "geontm:tritium", "cell_sas3", "geontm:sas3",
            "cell_antimatter", "geontm:amat", "cell_anti_schrabidium", "geontm:aschrab");
    private static final Map<String, String> BEDROCK_SUFFIX = Map.of("light_metal", "light", "heavy_metal", "heavy",
            "rare_earth", "rare", "actinide", "actinide", "non_metal", "nonmetal", "crystalline", "crystal");
    /** Block positions DE packs with {@code BlockPos.asLong} and Continuum writes as {@code [I; x, y, z]}. */
    private static final Map<String, String> POSITIONS = Map.of("geontm:detonator_pos", "geontm:detonator_target",
            "geontm:mirror_tool_target", "geontm:mirror_target", "geontm:radar_linker_target", "geontm:coordinate_position",
            "geontm:anchor_pos", "geontm:anchor_position", "geontm:wire_target", "geontm:wire_start", "geontm:wrench_target",
            "geontm:wire_start", "geontm:drone_link_target", "geontm:drone_linker_position", "geontm:rebar_corner", "geontm:rebar_pos");
    private static final Map<String, String> RENAMES = Map.of("geontm:haz_radiation", "geontm:hazard_radiation",
            "geontm:redd_charge", "geontm:battery_redd_power", "geontm:muffled", "geontm:battery_redd_muffled",
            "geontm:potato_timer", "geontm:potatos_timer", "geontm:rtty_pager_channel", "geontm:pager_channel",
            "geontm:broken_item_source", "geontm:broken_item", "geontm:settings_tool_data", "geontm:settings_tool",
            "geontm:contagion", "geontm:ntm_contagion", "geontm:decay_heat", "geontm:fuel_waste_hot",
            "geontm:reactor_pos", "geontm:reactor_sensor_target");
    /** Items whose contents DE keeps in {@code minecraft:container} and Continuum in its own positional list. */
    private static final Map<String, String> BAGS = Map.of("geontm:ammo_bag", "geontm:ammo_bag",
            "geontm:ammo_bag_infinite", "geontm:ammo_bag", "geontm:casing_bag", "geontm:casing_bag", "geontm:plastic_bag",
            "geontm:plastic_bag", "geontm:containment_box", "geontm:containment_box", "geontm:kit_toolbox", "geontm:toolbox");

    private Components() {}

    static void convert(String deItem, String cItem, CompoundTag c, StackContext ctx, String where) {
        String dePath = deItem.substring(4);
        if (CELL_FLUIDS.containsKey(dePath)) {
            c.putString("geontm:fluid", CELL_FLUIDS.get(dePath));
            c.putInt("geontm:fluid_amount", 1000);
        }
        fluidContent(cItem, c, ctx);
        if (c.contains("geontm:fluid_identifier")) {
            CompoundTag id = c.getCompound("geontm:fluid_identifier");
            CompoundTag multi = new CompoundTag();
            multi.putString("fluid1", Tables.get().fluid(id.getString("primary")));
            multi.putString("fluid2", Tables.get().fluid(id.getString("secondary")));
            c.put("geontm:fluid_multi", multi);
            c.remove("geontm:fluid_identifier");
        }
        machineContents(c);
        energy(cItem, c);
        RENAMES.forEach((from, to) -> move(c, from, to));
        POSITIONS.forEach((from, to) -> {
            if (!c.contains(from, Tag.TAG_LONG)) return;
            c.put(to, unpack(c.getLong(from)));
            c.remove(from);
        });
        if (c.contains("geontm:detonator_pos_list", Tag.TAG_LIST)) {
            ListTag out = new ListTag();
            for (Tag t : c.getList("geontm:detonator_pos_list", Tag.TAG_LONG)) out.add(unpack(((LongTag) t).getAsLong()));
            c.put("geontm:detonator_positions", out);
            c.remove("geontm:detonator_pos_list");
        }
        if (c.contains("geontm:target_designator", Tag.TAG_LONG)) {
            long v = c.getLong("geontm:target_designator");
            c.putInt("geontm:designator_x", (int) (v >> 32));
            c.putInt("geontm:designator_z", (int) v);
            c.remove("geontm:target_designator");
        }
        if (c.contains("geontm:linker_target", Tag.TAG_COMPOUND)) {
            CompoundTag global = c.getCompound("geontm:linker_target");
            int[] pos = global.getIntArray("pos");
            if (pos.length == 3) {
                CompoundTag link = new CompoundTag();
                link.putInt("x", pos[0]);
                link.putInt("y", pos[1]);
                link.putInt("z", pos[2]);
                link.putString("dim", global.getString("dimension"));
                c.put("geontm:tele_link", link);
            }
            c.remove("geontm:linker_target");
        }
        heat(dePath, cItem, c, ctx);
        if (c.contains("geontm:food_additives")) {
            int bits = c.getInt("geontm:food_additives");
            if ((bits & 1) != 0) c.putBoolean("geontm:ntm_cyanide", true);
            if ((bits & 2) != 0) c.putBoolean("geontm:ntm_red_pill", true);
            c.remove("geontm:food_additives");
        }
        for (String damage : List.of("geontm:canteen_cooldown", "geontm:arc_electrode_durability")) {
            if (!c.contains(damage)) continue;
            c.putInt("minecraft:damage", c.getInt(damage));
            c.remove(damage);
        }
        if (c.getBoolean("geontm:polarized")) c.putInt("minecraft:damage", 1);
        c.remove("geontm:polarized");
        c.remove("geontm:roasted");
        kitColors(c);
        if (c.contains("geontm:scrap", Tag.TAG_COMPOUND)) {
            CompoundTag scrap = c.getCompound("geontm:scrap");
            String name = Tables.get().material(scrap.getInt("material"));
            if (name != null) {
                c.putString("geontm:material", name);
                c.putInt("geontm:mat_amount", scrap.getInt("amount"));
                if (scrap.getBoolean("liquid")) c.putInt("minecraft:custom_model_data", 1);
                c.remove("geontm:scrap");
            }
        }
        missile(c);
        armorMods(c);
        gunState(cItem, c, ctx, where);
        if (c.contains("geontm:arty_cargo", Tag.TAG_COMPOUND)) c.put("geontm:arty_cargo", oneSlot(counted(c.getCompound("geontm:arty_cargo"))));
        for (Tag t : c.getList("geontm:kit_contents", Tag.TAG_COMPOUND)) counted((CompoundTag) t);
        String bag = BAGS.get(cItem);
        if (bag != null && c.contains("minecraft:container", Tag.TAG_LIST)) {
            c.put(bag, positional(c.getList("minecraft:container", Tag.TAG_COMPOUND), 0));
            c.remove("minecraft:container");
        }
        bedrockOreSample(c);
        selections(c);
        customMachine(c, ctx, where);
        icfPellet(c);
        loreBook(c);
        tankContents(cItem, c);
        toolPresets(c);
        unstable(c, ctx);
        persistentData(c);
        lockedContents(c, ctx, where);
        if (c.contains("geontm:block_inventory", Tag.TAG_COMPOUND)) {
            CompoundTag inv = c.getCompound("geontm:block_inventory");
            CompoundTag data = inv.getCompound("contents").copy();
            data.putString("id", inv.getString("type"));
            c.put("minecraft:block_entity_data", data);
            c.remove("geontm:block_inventory");
        }
    }

    /** DE components Continuum lacks; run after the addon hooks, which may take a DE component for their own item. */
    static void parkUnknown(String deItem, CompoundTag c, StackContext ctx, String where) {
        for (String key : List.copyOf(c.getAllKeys())) {
            if (!key.startsWith("geontm:") || ctx.hasComponent(key)) continue;
            ctx.park("component", where, deItem + " " + key, c.get(key));
            c.remove(key);
        }
    }

    static boolean hasFluid(CompoundTag components) {
        CompoundTag content = components.getCompound("geontm:fluid_content");
        return content.getLong("amount") > 0 && !content.getString("type").isEmpty();
    }

    static @Nullable String bedrockOreId(CompoundTag components) {
        CompoundTag ore = components.getCompound("geontm:bedrock_ore");
        String grade = ore.getString("grade").isEmpty() ? "base" : ore.getString("grade");
        String type = ore.getString("type").isEmpty() ? "light_metal" : ore.getString("type");
        String suffix = BEDROCK_SUFFIX.get(type);
        components.remove("geontm:bedrock_ore");
        return suffix == null ? null : "geontm:bedrock_ore_" + grade + "_" + suffix;
    }

    static String orbitalAssemblyId(CompoundTag components) {
        return "geontm:orbital_assembly_crystal_circuit";
    }

    /** DE's {@code {type, amount, pressure}} on whatever holds it. */
    private static void fluidContent(String cItem, CompoundTag c, StackContext ctx) {
        if (!c.contains("geontm:fluid_content", Tag.TAG_COMPOUND)) return;
        CompoundTag content = c.getCompound("geontm:fluid_content");
        c.remove("geontm:fluid_content");
        String fluid = Tables.get().fluid(content.getString("type"));
        int amount = (int) Math.min(Integer.MAX_VALUE, content.getLong("amount"));
        if (fluid.isEmpty() || amount <= 0) return;
        switch (cItem) {
            case "geontm:blowtorch" -> c.putInt("geontm:fluid_amount", amount);
            case "geontm:fluid_icon" -> {
                c.putString("geontm:fluid", fluid);
                c.putInt("geontm:fluid_amount", amount);
                c.putInt("geontm:fluid_pressure", content.getInt("pressure"));
            }
            default -> {
                if (cItem.contains("pipette")) {
                    CompoundTag pipette = new CompoundTag();
                    pipette.putString("type", fluid);
                    pipette.putInt("fill", amount);
                    pipette.putInt("capacity", c.contains("geontm:pipette_capacity") ? c.getInt("geontm:pipette_capacity") : 1000);
                    c.put("geontm:pipette", pipette);
                    c.remove("geontm:pipette_capacity");
                } else if (cItem.contains("jetpack")) {
                    c.putInt("geontm:jetpack_fuel", amount);
                } else {
                    c.putString("geontm:fluid", fluid);
                    c.putInt("geontm:fluid_amount", amount);
                    CompoundTag filled = ctx.filledComponents(cItem, fluid, amount);
                    if (filled != null) c.merge(filled);
                }
            }
        }
    }

    /** Machine items carry their block entity's carried data in {@code minecraft:custom_data} in Continuum. */
    private static void machineContents(CompoundTag c) {
        CompoundTag data = c.getCompound("minecraft:custom_data");
        boolean changed = false;
        if (c.contains("geontm:fluid_tank_contents", Tag.TAG_COMPOUND)) {
            CompoundTag tank = c.getCompound("geontm:fluid_tank_contents");
            tankKeys(data, "tank", tank.getCompound("tank"), tank.getInt("capacity"));
            data.putShort("mode", (short) tank.getInt("mode"));
            data.putBoolean("exploded", tank.getBoolean("damaged"));
            data.putBoolean("onFire", tank.getBoolean("on_fire"));
            c.remove("geontm:fluid_tank_contents");
            changed = true;
        }
        if (c.contains("geontm:refinery_contents", Tag.TAG_COMPOUND)) {
            CompoundTag refinery = c.getCompound("geontm:refinery_contents");
            ListTag tanks = refinery.getList("tanks", Tag.TAG_COMPOUND);
            for (int i = 0; i < tanks.size(); i++) tankKeys(data, "tank" + i, tanks.getCompound(i), 0);
            data.putBoolean("exploded", refinery.getBoolean("has_exploded"));
            data.putBoolean("onFire", refinery.getBoolean("on_fire"));
            c.remove("geontm:refinery_contents");
            changed = true;
        }
        if (c.contains("geontm:drill_contents", Tag.TAG_COMPOUND)) {
            CompoundTag drill = c.getCompound("geontm:drill_contents");
            ListTag tanks = drill.getList("tanks", Tag.TAG_COMPOUND);
            for (int i = 0; i < tanks.size(); i++) tankKeys(data, "t" + i, tanks.getCompound(i), 0);
            data.putLong("power", drill.getLong("power"));
            c.remove("geontm:drill_contents");
            changed = true;
        }
        if (c.contains("geontm:lock_state", Tag.TAG_COMPOUND)) {
            CompoundTag lock = c.getCompound("geontm:lock_state");
            if (lock.getBoolean("isLocked")) {
                data.putInt("lock", lock.getInt("lock"));
                data.putDouble("lockMod", lock.getDouble("lockMod"));
            }
            c.remove("geontm:lock_state");
            changed = true;
        }
        if (c.contains("geontm:spiders")) {
            data.putBoolean("spiders", true);
            c.remove("geontm:spiders");
            changed = true;
        }
        if (c.contains("geontm:stacklock", Tag.TAG_LONG)) {
            data.putLong("stacklock", c.getLong("geontm:stacklock"));
            c.remove("geontm:stacklock");
            changed = true;
        }
        if (c.contains("geontm:mass_stockpile")) {
            data.putInt("stack", c.getInt("geontm:mass_stockpile"));
            data.putBoolean("output", false);
            data.putBoolean("redstone", false);
            c.remove("geontm:mass_stockpile");
            changed = true;
        }
        if (c.contains("geontm:pneumatic_bulk", Tag.TAG_LIST)) {
            ListTag list = c.getList("geontm:pneumatic_bulk", Tag.TAG_INT);
            int[] amounts = new int[3];
            for (int i = 0; i < 3 && i < list.size(); i++) amounts[i] = list.getInt(i);
            data.putIntArray("amounts", amounts);
            c.remove("geontm:pneumatic_bulk");
            changed = true;
        }
        if (changed) c.put("minecraft:custom_data", data);
    }

    private static void tankKeys(CompoundTag data, String key, CompoundTag stack, int capacity) {
        String fluid = Tables.get().fluid(stack.getString("type"));
        data.putInt(key, (int) Math.min(Integer.MAX_VALUE, stack.getLong("amount")));
        if (capacity > 0) data.putInt(key + "_max", capacity);
        data.putString(key + "_type", fluid.isEmpty() ? "minecraft:empty" : fluid);
    }

    /** A DE FEnSU item is Continuum's REDD, whose charge is an unbounded number of its own; other batteries hold 1 MHE. */
    private static void energy(String cItem, CompoundTag c) {
        if (c.contains("geontm:machine_battery_state", Tag.TAG_COMPOUND)) {
            long power = c.getCompound("geontm:machine_battery_state").getLong("power");
            if (power > 0 && cItem.equals("geontm:machine_battery_redd")) c.putString("geontm:battery_redd_power", Long.toString(power));
            else if (power > 0) c.putLong("geontm:battery_power", Math.min(power, 1_000_000L));
            c.remove("geontm:machine_battery_state");
        }
        // an addon's capacitor keeps DE's state for its own reader
        if (c.contains("geontm:capacitor_state", Tag.TAG_COMPOUND) && cItem.startsWith("geontm:")) {
            c.putLong("geontm:capacitor_power", c.getCompound("geontm:capacitor_state").getLong("power"));
            c.remove("geontm:capacitor_state");
        }
    }

    /**
     * Continuum keeps a heat count where DE keeps a deadline in game time, or the ticks left while it is held still; the
     * share of DE's own scale that is left becomes the same share of Continuum's. A deadline already past is cold: DE
     * drops it only when the stack ticks in an inventory, so a stack that cooled in a chest still has one.
     */
    private static void heat(String dePath, String cItem, CompoundTag c, StackContext ctx) {
        if (c.contains("geontm:hot_item_heat") || c.contains("geontm:hot_until")) {
            long left = c.contains("geontm:hot_item_heat") ? c.getInt("geontm:hot_item_heat") : c.getLong("geontm:hot_until") - ctx.gameTime();
            int deMax = switch (dePath) {
                case "ingot_chainsteel" -> 100;
                case "ingot_steel_dusted" -> 200 - 10 * c.getInt("geontm:forge_count");
                default -> 200;
            };
            int heat = deMax <= 0 ? 0 : (int) Math.round(Math.clamp((double) left / deMax, 0D, 1D) * ctx.maxHeat(cItem));
            if (heat > 0) c.putInt("geontm:heat", heat);
        }
        c.remove("geontm:hot_item_heat");
        c.remove("geontm:hot_until");
    }

    private static void kitColors(CompoundTag c) {
        if (!c.contains("geontm:kit_color_1")) return;
        int c1 = c.getInt("geontm:kit_color_1"), c2 = c.getInt("geontm:kit_color_2");
        CompoundTag dyed = new CompoundTag();
        dyed.putInt("rgb", c1);
        dyed.putBoolean("show_in_tooltip", false);
        c.put("minecraft:dyed_color", dyed);
        CompoundTag data = c.getCompound("minecraft:custom_data");
        data.putIntArray("GeoCoreModelColors", new int[]{c1, c2});
        c.put("minecraft:custom_data", data);
        c.remove("geontm:kit_color_1");
        c.remove("geontm:kit_color_2");
    }

    private static void missile(CompoundTag c) {
        if (!c.contains("geontm:missile_parts", Tag.TAG_COMPOUND)) return;
        CompoundTag parts = c.getCompound("geontm:missile_parts").copy();
        if (parts.contains("fins")) {
            parts.put("stability", parts.get("fins"));
            parts.remove("fins");
        }
        if (c.contains("geontm:missile_chip", Tag.TAG_STRING)) parts.putString("chip", c.getString("geontm:missile_chip"));
        if (parts.contains("chip") && parts.contains("warhead") && parts.contains("fuselage") && parts.contains("thruster")) {
            c.put("geontm:missile_custom_parts", parts);
            c.remove("geontm:missile_parts");
            c.remove("geontm:missile_chip");
        }
    }

    private static final List<String> GUN_DATA_PREFIXES = List.of("wear_", "mode_", "KEY_MOD_NAMES_", "coincount",
            "coincharge", "colors", "flashlight");

    /**
     * DE keeps a gun's wear, modes, mods and magazines with its transient animation state; Continuum keeps the
     * persistent part in {@code gun_data}, naming the loaded ammo and the mods where DE stores runtime numbers. A
     * chemical thrower's magazine holds a fluid's raw registry number, which no save records, so it is parked.
     */
    private static void gunState(String cItem, CompoundTag c, StackContext ctx, String where) {
        if (!c.contains("geontm:gun_state", Tag.TAG_COMPOUND) || c.contains("geontm:gun_data")) return;
        CompoundTag state = c.getCompound("geontm:gun_state");
        CompoundTag data = new CompoundTag();
        CompoundTag parked = new CompoundTag();
        for (String key : state.getAllKeys()) {
            if (!key.startsWith("magtype")) continue;
            String index = key.substring("magtype".length());
            String ammo = cItem.contains("chemthrower") ? null : Tables.get().magtypes.get(Integer.toString(state.getInt(key)));
            for (String mag : List.of("magtype", "magcount", "magprev", "magafter")) {
                if (!state.contains(mag + index)) continue;
                if (ammo == null) parked.put(mag + index, state.get(mag + index).copy());
                else data.put(mag + index, state.get(mag + index).copy());
            }
            if (ammo != null) data.putString(key, ammo);
        }
        for (String key : state.getAllKeys()) {
            if (key.startsWith("mag")) continue;
            if (key.startsWith("KEY_MOD_LIST_")) {
                List<String> names = new ArrayList<>();
                for (int id : state.getIntArray(key)) {
                    String name = ctx.weaponModName(id);
                    if (name != null) names.add(name);
                }
                data.putString("KEY_MOD_NAMES_" + key.substring("KEY_MOD_LIST_".length()), String.join(",", names));
            } else if (GUN_DATA_PREFIXES.stream().anyMatch(key::startsWith)) {
                data.put(key, state.get(key).copy());
            }
        }
        if (!parked.isEmpty()) ctx.park("gun_magazine", where, cItem, parked);
        if (!data.isEmpty()) c.put("geontm:gun_data", data);
        c.remove("geontm:gun_state");
    }

    /** DE lists {@code {slot, item}}; Continuum keeps nine positions with {@code {}} for an empty one. */
    private static void armorMods(CompoundTag c) {
        if (!c.contains("geontm:armor_mods", Tag.TAG_LIST)) return;
        ListTag mods = c.getList("geontm:armor_mods", Tag.TAG_COMPOUND);
        if (mods.isEmpty() || !mods.getCompound(0).contains("slot")) return;
        c.put("geontm:armor_mods", positional(mods, 9));
    }

    /** DE's item templates leave out a count of one; the walker only knows a stack by its count. */
    private static CompoundTag counted(CompoundTag template) {
        if (template.contains("id", Tag.TAG_STRING) && !template.contains("count")) template.putInt("count", 1);
        return template;
    }

    private static ListTag positional(ListTag entries, int size) {
        List<CompoundTag> slots = new ArrayList<>();
        for (Tag t : entries) {
            CompoundTag e = (CompoundTag) t;
            int slot = e.getInt("slot");
            while (slots.size() <= slot) slots.add(new CompoundTag());
            slots.set(slot, counted(e.getCompound("item")));
        }
        while (slots.size() < size) slots.add(new CompoundTag());
        ListTag out = new ListTag();
        out.addAll(slots);
        return out;
    }

    private static ListTag oneSlot(CompoundTag stack) {
        CompoundTag entry = new CompoundTag();
        entry.putInt("slot", 0);
        entry.put("item", stack);
        ListTag list = new ListTag();
        list.add(entry);
        return list;
    }

    private static final String[] BEDROCK_KINDS = {"light", "heavy", "rare", "actinide", "nonmetal", "crystal"};
    private static final String[] ICF_FUELS = {"hydrogen", "deuterium", "tritium", "helium3", "helium4", "lithium",
            "beryllium", "boron", "carbon", "oxygen", "sodium", "chlorine", "calcium"};
    private static final Set<String> AREA_ABILITIES = Set.of("recursion", "hammer", "hammer_flat", "explosion");

    /** HBM's own six kinds go to the density record; an addon set's sample Continuum reads in DE's shape. */
    private static void bedrockOreSample(CompoundTag c) {
        if (!(c.get("geontm:bedrock_ore_sample") instanceof CompoundTag sample)) return;
        String set = sample.getString("set");
        if (!set.isEmpty() && !set.equals("geontm")) return;
        CompoundTag density = new CompoundTag();
        for (String kind : BEDROCK_KINDS) density.putDouble(kind, sample.getDouble(kind));
        c.put("geontm:bedrock_ore_density", density);
        c.remove("geontm:bedrock_ore_sample");
    }

    /** The custom machine structure tool's corners and the conveyor wand's run, in 1.7's persistent keys. */
    private static void selections(CompoundTag c) {
        CompoundTag data = c.getCompound("geontm:persistent_data");
        if (c.get("geontm:cm_structure") instanceof CompoundTag selection) {
            putPos(data, selection.getIntArray("anchor"), "anchorX", "anchorY", "anchorZ");
            putPos(data, selection.getIntArray("first"), "x1", "y1", "z1");
            putPos(data, selection.getIntArray("second"), "x2", "y2", "z2");
            c.remove("geontm:cm_structure");
        }
        if (c.get("geontm:conveyor_run") instanceof CompoundTag run) {
            putPos(data, run.getIntArray("anchor"), "run_x", "run_y", "run_z");
            data.putString("run_side", run.getString("side"));
            data.putInt("run_count", run.getInt("budget"));
            c.remove("geontm:conveyor_run");
        }
        if (!data.isEmpty()) c.put("geontm:persistent_data", data);
    }

    private static void putPos(CompoundTag data, int[] pos, String x, String y, String z) {
        if (pos.length != 3) return;
        data.putInt(x, pos[0]);
        data.putInt(y, pos[1]);
        data.putInt(z, pos[2]);
    }

    private static void customMachine(CompoundTag c, StackContext ctx, String where) {
        if (!c.contains("geontm:custom_machine_type", Tag.TAG_STRING)) return;
        String name = ctx.customMachineName(c.getString("geontm:custom_machine_type"));
        if (name == null) return;
        c.putString("geontm:custom_machine", name);
        c.remove("geontm:custom_machine_type");
    }

    private static void icfPellet(CompoundTag c) {
        if (!(c.get("geontm:icf_pellet") instanceof CompoundTag pellet)) return;
        int type1 = pellet.getInt("type1"), type2 = pellet.getInt("type2");
        if (type1 >= 0 && type1 < ICF_FUELS.length) c.putString("geontm:icf_type1", ICF_FUELS[type1]);
        if (type2 >= 0 && type2 < ICF_FUELS.length) c.putString("geontm:icf_type2", ICF_FUELS[type2]);
        c.putBoolean("geontm:icf_muon", pellet.getBoolean("muon"));
        c.putLong("geontm:icf_depletion", pellet.getLong("depletion"));
        c.remove("geontm:icf_pellet");
    }

    /** DE's five lore book components to 1.7's one compound. */
    private static void loreBook(CompoundTag c) {
        if (!c.contains("geontm:lore_book_key")) return;
        CompoundTag book = new CompoundTag();
        book.putString("k", c.getString("geontm:lore_book_key"));
        book.putInt("p", c.getInt("geontm:lore_book_pages"));
        book.putInt("cov_col", c.getInt("geontm:lore_book_cover"));
        book.putInt("tit_col", c.getInt("geontm:lore_book_title"));
        CompoundTag args = c.getCompound("geontm:lore_book_args");
        for (String page : args.getAllKeys()) {
            CompoundTag pageArgs = new CompoundTag();
            ListTag list = args.getList(page, Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) pageArgs.putString("a" + (i + 1), list.getString(i));
            book.put("p" + page, pageArgs);
        }
        c.put("geontm:book_lore", book);
        for (String key : List.of("key", "pages", "cover", "title", "args")) c.remove("geontm:lore_book_" + key);
    }

    /** Only the acetylene torch keeps its tanks on the item in Continuum; the machines' items stay parked. */
    private static void tankContents(String cItem, CompoundTag c) {
        if (!cItem.equals("geontm:acetylene_torch") || !(c.get("geontm:tank_contents") instanceof ListTag tanks)) return;
        if (tanks.size() > 0) c.putInt("geontm:torch_unsaturateds", (int) tanks.getCompound(0).getLong("amount"));
        if (tanks.size() > 1) c.putInt("geontm:torch_oxygen", (int) tanks.getCompound(1).getLong("amount"));
        c.remove("geontm:tank_contents");
    }

    /** DE's tool presets to 1.7's ability config, abilities named by their 1.7 translation keys. */
    private static void toolPresets(CompoundTag c) {
        if (!(c.get("geontm:tool_presets") instanceof ListTag presets)) return;
        ListTag out = new ListTag();
        for (Tag t : presets) {
            CompoundTag preset = (CompoundTag) t;
            CompoundTag entry = new CompoundTag();
            entry.putString("area", ability(preset.getString("area")));
            entry.putInt("areaLevel", preset.getInt("areaLevel"));
            String harvest = preset.getString("harvest");
            entry.putString("harvest", ability(harvest.equals("silk") ? "silktouch" : harvest));
            entry.putInt("harvestLevel", preset.getInt("harvestLevel"));
            out.add(entry);
        }
        CompoundTag config = new CompoundTag();
        config.putInt("ability", c.getInt("geontm:tool_preset_index"));
        config.put("abilityPresets", out);
        c.put("geontm:tool_ability", config);
        c.remove("geontm:tool_presets");
        c.remove("geontm:tool_preset_index");
    }

    private static String ability(String name) {
        return name.isEmpty() || name.equals("none") ? "" : "tool.ability." + name;
    }

    /** DE keeps the tick a fuse ends; Continuum the ticks since it was armed, firing at exactly 200. */
    private static void unstable(CompoundTag c, StackContext ctx) {
        if (!c.contains("geontm:unstable_at", Tag.TAG_ANY_NUMERIC)) return;
        long left = c.getLong("geontm:unstable_at") - ctx.gameTime();
        c.putInt("geontm:unstable_timer", (int) Math.max(0, Math.min(199, 200 - left)));
        c.remove("geontm:unstable_at");
    }

    private static void persistentData(CompoundTag c) {
        if (c.contains("geontm:armor_mod_nv_active") || c.contains("geontm:no9_lamp")) {
            CompoundTag data = c.getCompound("geontm:persistent_data");
            if (c.contains("geontm:armor_mod_nv_active")) data.putBoolean("ITEM_MOD_NV_ACTIVE", true);
            if (c.contains("geontm:no9_lamp")) data.putBoolean("isOn", c.getBoolean("geontm:no9_lamp"));
            c.put("geontm:persistent_data", data);
            c.remove("geontm:armor_mod_nv_active");
            c.remove("geontm:no9_lamp");
        }
        if (!c.contains("geontm:persistent_data", Tag.TAG_COMPOUND)) return;
        CompoundTag data = c.getCompound("geontm:persistent_data");
        if (data.contains("depletion")) c.putDouble("geontm:pile_rod_depletion", data.getDouble("depletion"));
        if (data.contains("pins")) c.putInt("geontm:key_pins", data.getInt("pins"));
        if (data.contains("filter_stack", Tag.TAG_COMPOUND)) c.put("geontm:gas_mask_filter", oneSlot(data.getCompound("filter_stack")));
        data.remove("depletion");
        data.remove("pins");
        data.remove("filter_stack");
    }

    /** DE seals crate contents with AES-GCM under the world's key; the crate opens as a plain container. */
    private static void lockedContents(CompoundTag c, StackContext ctx, String where) {
        if (!c.contains("geontm:locked_contents", Tag.TAG_COMPOUND)) return;
        CompoundTag locked = c.getCompound("geontm:locked_contents");
        byte[] key = ctx.lockedContentsKey();
        try {
            if (key == null) throw new GeneralSecurityException("no key file");
            byte[] sealed = locked.getByteArray("sealed");
            ByteBuffer aad = ByteBuffer.allocate(14);
            aad.put((byte) (locked.getBoolean("heavy") ? 1 : 0));
            aad.putInt(Float.floatToIntBits(locked.getFloat("radiation")));
            aad.putLong(locked.contains("minimum_deadline") ? locked.getLong("minimum_deadline") : Long.MAX_VALUE);
            aad.put((byte) (locked.getBoolean("has_references") ? 1 : 0));
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, sealed, 0, 12));
            cipher.updateAAD(aad.array());
            byte[] plain = cipher.doFinal(sealed, 12, sealed.length - 12);
            CompoundTag contents = NbtIo.readCompressed(new ByteArrayInputStream(plain), NbtAccounter.unlimitedHeap());
            c.put("minecraft:container", contents.getList("items", Tag.TAG_COMPOUND));
            c.remove("geontm:locked_contents");
            ctx.count("locked crates opened");
        } catch (GeneralSecurityException | IOException e) {
            ctx.park("locked_contents", where, e.getMessage(), locked);
            c.remove("geontm:locked_contents");
        }
    }

    private static void move(CompoundTag c, String from, String to) {
        if (!c.contains(from)) return;
        c.put(to, c.get(from));
        c.remove(from);
    }

    private static IntArrayTag unpack(long packed) {
        return new IntArrayTag(new int[]{(int) (packed >> 38), (int) (packed << 52 >> 52), (int) (packed << 26 >> 38)});
    }
}
