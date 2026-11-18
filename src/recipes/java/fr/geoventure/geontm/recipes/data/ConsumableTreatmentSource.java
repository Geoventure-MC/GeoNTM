package fr.geoventure.geontm.recipes.data;

import fr.geoventure.geontm.api.data.GeoNtmDataMaps;
import fr.geoventure.geontm.handler.ConsumableTreatment;
import fr.geoventure.geontm.handler.ConsumableTreatment.Action;
import fr.geoventure.geontm.handler.ConsumableTreatment.AddEffect;
import fr.geoventure.geontm.handler.ConsumableTreatment.ClearEffects;
import fr.geoventure.geontm.handler.ConsumableTreatment.ConsumeItem;
import fr.geoventure.geontm.handler.ConsumableTreatment.Counter;
import fr.geoventure.geontm.handler.ConsumableTreatment.CounterType;
import fr.geoventure.geontm.handler.ConsumableTreatment.Damage;
import fr.geoventure.geontm.handler.ConsumableTreatment.DrainHealth;
import fr.geoventure.geontm.handler.ConsumableTreatment.Experience;
import fr.geoventure.geontm.handler.ConsumableTreatment.Gate;
import fr.geoventure.geontm.handler.ConsumableTreatment.GiveItem;
import fr.geoventure.geontm.handler.ConsumableTreatment.Heal;
import fr.geoventure.geontm.handler.ConsumableTreatment.Ignite;
import fr.geoventure.geontm.handler.ConsumableTreatment.Motion;
import fr.geoventure.geontm.handler.ConsumableTreatment.Operation;
import fr.geoventure.geontm.handler.ConsumableTreatment.PotionSickness;
import fr.geoventure.geontm.handler.ConsumableTreatment.RemoveEffects;
import fr.geoventure.geontm.handler.ConsumableTreatment.RestoreHealth;
import fr.geoventure.geontm.handler.ConsumableTreatment.Sound;
import fr.geoventure.geontm.handler.ConsumableTreatment.Trigger;
import fr.geoventure.geontm.items.food.ItemConserve.EnumFoodType;
import fr.geoventure.geocore.data.DataMapSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Every GEONTM consumable's treatment, in the order its 1.7 / 1.12.2 handler ran: {@code ItemPill}, {@code ItemCanteen},
 * the syringe table of {@code ItemSyringe} / {@code ItemSimpleConsumable}, RadAway and the IV bags,
 * {@code ItemEnergy}, the {@code ItemFood.onFoodEaten} overrides, {@code ItemConserve}, {@code ItemCigarette} and
 * {@code ItemBDCL}. Checked against the item classes' old code by the {@code treatment_*} GameTests.
 */
public final class ConsumableTreatmentSource extends DataMapSource<ConsumableTreatment> {

    private static final List<Trigger> CONSUME = List.of(Trigger.CONSUME);
    private static final List<Trigger> USE = List.of(Trigger.USE);
    private static final List<Trigger> USE_OR_HIT = List.of(Trigger.USE, Trigger.HIT);
    private static final int MAX = Integer.MAX_VALUE;
    /** 1.7 {@code ItemPill}, {@code med_bag} and the bag: nausea, blindness and the other "negative effects". */
    private static final List<String> NEGATIVE = List.of("minecraft:blindness", "minecraft:nausea",
            "minecraft:mining_fatigue", "minecraft:hunger", "minecraft:slowness", "minecraft:poison",
            "minecraft:weakness", "minecraft:wither", "geontm:radiation");

    public ConsumableTreatmentSource() {
        super(GeoNtmDataMaps.CONSUMABLE_TREATMENT, false);
    }

    @Override
    protected void values() {
        pills();
        put("geontm:canteen_vodka", new ConsumableTreatment(CONSUME, true, List.of(
                effect("nausea", 10 * 20, 0), effect("strength", 30 * 20, 2), sickness(5))));
        syringes();
        bags();
        drinks();
        foods();
    }

    private void pills() {
        pill("radx", effect("geontm:radx", 3 * 60 * 20, 0));
        pill("siox", counter(CounterType.ASBESTOS, Operation.SET, 0), blackLungFifth());
        pill("pill_herbal", counter(CounterType.ASBESTOS, Operation.SET, 0), blackLungFifth(),
                counter(CounterType.RADIATION, Operation.ADD, -100),
                effect("nausea", 10 * 20, 0), effect("weakness", 10 * 60 * 20, 2),
                effect("mining_fatigue", 10 * 60 * 20, 2), effect("poison", 5 * 20, 2),
                effect("geontm:potionsickness", 10 * 60 * 20, 0));
        pill("xanax", counter(CounterType.DIGAMMA, Operation.ADD, -0.5));
        pill("fmn", counter(CounterType.DIGAMMA, Operation.MIN, 2), effect("blindness", 60, 0));
        pill("five_htp", counter(CounterType.DIGAMMA, Operation.SET, 0), effect("geontm:stability", 10 * 60 * 20, 0));
        pill("pill_iodine", new RemoveEffects(Gate.ALWAYS, NEGATIVE));
        pill("plan_c", new Damage(Gate.ALWAYS, List.of("geontm:euthanized_self", "geontm:euthanized_self2"), 1000F, 10,
                List.of()));
        pill("pill_red", effect("geontm:death", 60 * 60 * 20, 0));
        // 1.7: rand.nextInt(25) == 0
        pill("chocolate", new Damage(new Gate(1F / 25F, false), List.of("geontm:overdose"), 1000F, 1, List.of()),
                effect("haste", 60 * 20, 3), effect("speed", 60 * 20, 3), effect("jump_boost", 60 * 20, 3));
    }

    /** Every pill is blocked by potion sickness and starts with five seconds of it. */
    private void pill(String id, Action... actions) {
        List<Action> list = new ArrayList<>();
        list.add(sickness(5));
        list.addAll(List.of(actions));
        put("geontm:" + id, new ConsumableTreatment(CONSUME, true, list));
    }

    private void syringes() {
        put("geontm:syringe_antidote", new ConsumableTreatment(USE_OR_HIT, true, List.of(
                new ClearEffects(Gate.ALWAYS), syringe(), consume("geontm:syringe_empty"), sickness(5))));
        put("geontm:syringe_awesome", new ConsumableTreatment(USE_OR_HIT, true, List.of(
                effect("regeneration", 50 * 20, 9), effect("resistance", 50 * 20, 9),
                effect("fire_resistance", 50 * 20, 0), effect("strength", 50 * 20, 24), effect("haste", 50 * 20, 9),
                effect("speed", 50 * 20, 6), effect("jump_boost", 50 * 20, 9), effect("health_boost", 50 * 20, 9),
                effect("absorption", 50 * 20, 4), effect("nausea", 5 * 20, 4), effect("geontm:radx", 50 * 20, 9),
                syringe(), consume("geontm:syringe_empty"), sickness(5))));
        put("geontm:syringe_poison", new ConsumableTreatment(USE_OR_HIT, false, List.of(
                syringe(), consume("geontm:syringe_empty"),
                new Damage(Gate.ALWAYS, List.of("geontm:euthanized_self", "geontm:euthanized_self2"), 30F, 1,
                        List.of("geontm:euthanized")))));
        put("geontm:syringe_metal_stimpak", new ConsumableTreatment(USE_OR_HIT, true, List.of(
                new Heal(5F), sickness(5), syringe(), consume("geontm:syringe_metal_empty"))));
        put("geontm:syringe_metal_medx", new ConsumableTreatment(USE_OR_HIT, true, List.of(
                effect("resistance", 4 * 60 * 20, 2), sickness(5), syringe(), consume("geontm:syringe_metal_empty"))));
        put("geontm:syringe_metal_psycho", new ConsumableTreatment(USE_OR_HIT, true, List.of(
                effect("resistance", 2 * 60 * 20, 0), effect("strength", 2 * 60 * 20, 0), sickness(5), syringe(),
                consume("geontm:syringe_metal_empty"))));
        put("geontm:syringe_metal_super", new ConsumableTreatment(USE_OR_HIT, true, List.of(
                new Heal(25F), effect("slowness", 10 * 20, 0), sickness(15), syringe(),
                consume("geontm:syringe_metal_empty"))));
        put("geontm:med_bag", new ConsumableTreatment(USE, true, List.of(
                new RestoreHealth(Gate.ALWAYS), new RemoveEffects(Gate.ALWAYS, NEGATIVE), sickness(15), consume(null))));
        put("geontm:syringe_taint", new ConsumableTreatment(USE_OR_HIT, false, List.of(
                effect("geontm:taint", 60 * 20, 0), effect("nausea", 5 * 20, 0), syringe(),
                consume("geontm:syringe_metal_empty"), give("geontm:bottle2_empty", true))));
        put("geontm:cbt_device", new ConsumableTreatment(USE, false, List.of(
                effect("geontm:bang", 30, 0), consume(null), new Sound("geontm:item.vice"))));
        // 1.7: the MKU syringe only works as a melee injection
        put("geontm:syringe_mkunicorn", new ConsumableTreatment(List.of(Trigger.HIT), false, List.of(
                counter(CounterType.CONTAGION, Operation.SET, 3 * 60 * 60 * 20), syringe(), consume(null))));
        put("geontm:euphemium_stopper", new ConsumableTreatment(USE, false, List.of(
                effect("weakness", 30 * 20, 9), effect("slowness", 30 * 20, 9), effect("mining_fatigue", 30 * 20, 9))));
    }

    private void bags() {
        // 1.7 doRadaway: RadAway I for 140 / 350 / 500 ticks, added to any RadAway already running
        radaway("radaway", 140);
        radaway("radaway_strong", 350);
        radaway("radaway_flush", 500);
        put("geontm:iv_empty", new ConsumableTreatment(USE, false, List.of(
                syringe(), consume("geontm:iv_blood"), new DrainHealth(Gate.ALWAYS, 5F))));
        put("geontm:iv_blood", new ConsumableTreatment(USE, false, List.of(
                new Sound("geontm:item.radaway"), consume("geontm:iv_empty"), new Heal(5F))));
        put("geontm:iv_xp_empty", new ConsumableTreatment(USE, false, List.of(
                syringe(), consume("geontm:iv_xp"), new Experience(Gate.ALWAYS, -100))));
        put("geontm:iv_xp", new ConsumableTreatment(USE, false, List.of(
                new Sound("minecraft:entity.experience_orb.pickup"), consume("geontm:iv_xp_empty"),
                new Experience(Gate.ALWAYS, 100))));
    }

    private void radaway(String id, int ticks) {
        put("geontm:" + id, new ConsumableTreatment(USE, false, List.of(
                new AddEffect(Gate.ALWAYS, "geontm:radaway", ticks, 0, true), new Sound("geontm:item.radaway"),
                consume("geontm:iv_empty"))));
    }

    private void drinks() {
        String can = "geontm:can_empty", pull = "geontm:ring_pull", bottle = "geontm:bottle_empty";
        drink("can_smart", can, pull, effect("speed", 30 * 20, 1), effect("resistance", 30 * 20, 2),
                effect("strength", 30 * 20, 0));
        drink("can_creature", can, pull, effect("speed", 30 * 20, 0), effect("resistance", 30 * 20, 2),
                effect("regeneration", 30 * 20, 1));
        drink("can_redbomb", can, pull, effect("speed", 30 * 20, 0), effect("resistance", 30 * 20, 2),
                effect("jump_boost", 30 * 20, 1));
        drink("can_mrsugar", can, pull, effect("speed", 30 * 20, 0), effect("haste", 30 * 20, 1),
                effect("jump_boost", 30 * 20, 2));
        drink("can_overcharge", can, pull, effect("speed", 30 * 20, 1), effect("resistance", 30 * 20, 2),
                effect("strength", 30 * 20, 0));
        drink("can_luna", can, pull, effect("speed", 30 * 20, 1), effect("resistance", 30 * 20, 2),
                effect("strength", 30 * 20, 1), effect("regeneration", 30 * 20, 2));
        drink("can_bepis", can, pull, effect("speed", 30 * 20, 3), effect("resistance", 30 * 20, 3));
        drink("can_breen", can, pull, effect("nausea", 30 * 20, 0));
        drink("can_mug", can, pull, effect("resistance", 3 * 60 * 20, 2), effect("regeneration", 60 * 20, 2));
        drink("bottle_nuka", bottle, "geontm:cap_nuka", new Heal(4F), effect("speed", 30 * 20, 1),
                effect("haste", 30 * 20, 1), rads(5));
        drink("bottle_cherry", bottle, "geontm:cap_nuka", new Heal(6F), effect("speed", 30 * 20, 0),
                effect("jump_boost", 30 * 20, 2), rads(5));
        drink("bottle_quantum", bottle, "geontm:cap_quantum", new Heal(10F), effect("speed", 30 * 20, 1),
                effect("resistance", 30 * 20, 2), effect("strength", 30 * 20, 1), rads(15));
        drink("bottle2_korl", bottle, "geontm:cap_korl", new Heal(6F), effect("speed", 30 * 20, 1),
                effect("haste", 30 * 20, 2), effect("strength", 30 * 20, 2));
        drink("bottle2_fritz", bottle, "geontm:cap_fritz", new Heal(6F), effect("speed", 30 * 20, 1),
                effect("resistance", 30 * 20, 2), effect("jump_boost", 30 * 20, 2));
        drink("bottle_sparkle", bottle, "geontm:cap_sparkle", new Heal(10F), effect("speed", 120 * 20, 1),
                effect("resistance", 120 * 20, 2), effect("strength", 120 * 20, 2), effect("haste", 120 * 20, 1),
                rads(5));
        drink("bottle_rad", bottle, "geontm:cap_rad", new Heal(10F), effect("speed", 120 * 20, 1),
                effect("resistance", 120 * 20, 2), effect("fire_resistance", 120 * 20, 0),
                effect("strength", 120 * 20, 4), effect("haste", 120 * 20, 1), rads(15));
        // the blast before these is ItemEnergy's
        drink("chocolate_milk", null, null);
        drink("coffee", null, null, new Heal(10F), effect("speed", 60 * 20, 2));
        drink("coffee_radium", null, null, new Heal(10F), effect("speed", 60 * 20, 2), rads(500));
    }

    /** 1.12.2 ItemEnergy: the effects, five seconds of sickness, then (not in creative) the container and its cap. */
    private void drink(String id, String container, String cap, Action... effects) {
        List<Action> list = new ArrayList<>(List.of(effects));
        list.add(sickness(5));
        list.add(new ConsumeItem(new Gate(1F, true), Optional.ofNullable(container)));
        if (cap != null) list.add(new GiveItem(new Gate(1F, true), cap, 1, true));
        put("geontm:" + id, new ConsumableTreatment(CONSUME, true, list));
    }

    private void foods() {
        food("apple_lead", effect("geontm:lead", 15 * 20, 2));
        food("apple_lead_ingot", effect("geontm:lead", 60 * 20, 4));
        food("apple_lead_block", new Damage(Gate.ALWAYS, List.of("geontm:lead"), 500F, 1, List.of()));
        food("apple_schrabidium", effect("regeneration", 600, 4), effect("resistance", 6000, 0),
                effect("fire_resistance", 6000, 0));
        food("apple_schrabidium_ingot", effect("regeneration", 1200, 4), effect("resistance", 1200, 4),
                effect("fire_resistance", 1200, 0), effect("strength", 1200, 4), effect("haste", 1200, 2),
                effect("speed", 1200, 2), effect("jump_boost", 1200, 4), effect("health_boost", 1200, 9),
                effect("absorption", 1200, 4), effect("saturation", 1200, 9));
        food("apple_schrabidium_block", effect("regeneration", MAX, 4), effect("resistance", MAX, 1),
                effect("fire_resistance", MAX, 0), effect("strength", MAX, 9), effect("haste", MAX, 4),
                effect("speed", MAX, 3), effect("jump_boost", MAX, 4), effect("health_boost", MAX, 24),
                effect("absorption", MAX, 14), effect("saturation", MAX, 99));
        food("apple_euphemium", effect("resistance", MAX, 120), effect("fire_resistance", MAX, 0),
                effect("saturation", MAX, 120));
        food("cotton_candy", effect("poison", 15 * 20, 0), effect("wither", 5 * 20, 0),
                effect("weakness", 25 * 20, 2), effect("speed", 25 * 20, 2), effect("resistance", 30 * 20, 4));
        food("mucho_mango", effect("speed", 200, 0));
        // 1.7 setFire takes seconds, so 100 s of fire; motionY = 2 reaches the client with the velocity packet
        food("schnitzel_vegan", effect("blindness", 10 * 20, 0), effect("nausea", 30 * 20, 0),
                effect("hunger", 3 * 60 * 20, 4), effect("wither", 3 * 20, 0), new Ignite(Gate.ALWAYS, 5 * 20),
                new Motion(Gate.ALWAYS, Optional.empty(), Optional.of(2D), Optional.empty()));
        food("tem_flakes", new Heal(2F));
        food("tem_flakes_normal", new Heal(2F));
        food("tem_flakes_expensive", new Heal(2F));
        for (EnumFoodType type : EnumFoodType.values()) {
            String id = "canned_conserve_" + type.name().toLowerCase(Locale.US);
            List<Action> list = new ArrayList<>();
            // 1.7 addItemStackToInventory: a key that does not fit is lost
            list.add(give("geontm:can_key", false));
            if (type == EnumFoodType.RECURSION) {
                // 1.7: rand.nextInt(10) > 0
                list.add(new GiveItem(new Gate(0.9F, false), "geontm:" + id, 1, false));
            } else if (type == EnumFoodType.FIST) {
                list.add(new Damage(Gate.ALWAYS, List.of("minecraft:magic"), 2F, 1, List.of()));
            }
            put("geontm:" + id, new ConsumableTreatment(CONSUME, false, list));
        }
        Sound cough = new Sound("geontm:player.cough");
        food("cigarette", counter(CounterType.BLACK_LUNG, Operation.ADD, 2000),
                counter(CounterType.ASBESTOS, Operation.ADD, 2000), counter(CounterType.RADIATION, Operation.ADD, 100),
                cough);
        food("crackpipe", counter(CounterType.BLACK_LUNG, Operation.ADD, 500), effect("nausea", 200, 0),
                new Heal(10F), cough);
        food("bdcl", new Sound("geontm:player.groan"));
    }

    private void food(String id, Action... actions) {
        put("geontm:" + id, new ConsumableTreatment(CONSUME, false, List.of(actions)));
    }

    private static Action effect(String id, int duration, int amplifier) {
        return new AddEffect(id.contains(":") ? id : "minecraft:" + id, duration, amplifier);
    }

    private static Action sickness(int seconds) {
        return new PotionSickness(seconds);
    }

    private static Action counter(CounterType counter, Operation operation, double amount) {
        return new Counter(counter, operation, amount);
    }

    /** 1.7: {@code min(blacklung, maxBlacklung / 5)}. */
    private static Action blackLungFifth() {
        return new Counter(Gate.ALWAYS, CounterType.BLACK_LUNG, Operation.MIN, 0, Optional.of(5));
    }

    private static Action rads(double amount) {
        return counter(CounterType.RADIATION, Operation.ADD, amount);
    }

    private static Action syringe() {
        return new Sound("geontm:item.syringe");
    }

    private static Action consume(String remainder) {
        return new ConsumeItem(Gate.ALWAYS, Optional.ofNullable(remainder));
    }

    private static Action give(String item, boolean dropIfFull) {
        return new GiveItem(Gate.ALWAYS, item, 1, dropIfFull);
    }
}
