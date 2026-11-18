package fr.geoventure.geontm.items;

import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import java.util.List;
import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.creativetabs.ModTabs;
import fr.geoventure.geontm.items.food.ItemLemon;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import fr.geoventure.geocore.world.CompatConsumables;
import fr.geoventure.geocore.world.Effects;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/** Hand-registered plain foods formerly constructed by {@code ItemLemon}. */
public final class ModFoodItems {

    private ModFoodItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);

    private static Reg.Handle<ItemLemon> food(String id, int nutrition, float saturation, boolean wolfFood,
                                                  GeoNtmTab tab) {
        return food(id, nutrition, saturation, wolfFood, tab, List::of, false, false);
    }

    private static Reg.Handle<ItemLemon> food(String id, int nutrition, float saturation, boolean wolfFood,
                                                  GeoNtmTab tab, Supplier<List<MobEffectInstance>> effects,
                                                  boolean stacksToOne, boolean bowlRemainder) {
        Reg.Handle<ItemLemon> item = REG.item(id,
                p -> new ItemLemon(p, id), p -> {
                    p = CompatConsumables.food(p, nutrition, saturation, effects.get());
                    if (bowlRemainder) p = CompatConsumables.usingConvertsTo(p, Items.BOWL);
                    if (stacksToOne) p = p.stacksTo(1);
                    return p;
                });
        if (tab != null) ModTabs.addTo(tab, item);
        return item;
    }

    public static final RegistryObject<ItemLemon> bio_wafer = food("bio_wafer", 8, 8F, false, GeoNtmTab.MATERIALS)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> ingot_semtex = food("ingot_semtex", 4, 5F, true, GeoNtmTab.MATERIALS)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> lemon = food("lemon", 3, 5F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> definitelyfood = food("definitelyfood", 3, 0.5F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> med_ipecac = food("med_ipecac", 0, 0F, false, GeoNtmTab.CONSUMABLES,
            () -> List.of(Effects.instance(MobEffects.HUNGER, 50, 49)), false, false)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> med_ptsd = food("med_ptsd", 0, 0F, false, GeoNtmTab.CONSUMABLES,
            () -> List.of(Effects.instance(MobEffects.HUNGER, 50, 49)), false, false)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> med_schizophrenia = food("med_schizophrenia", 0, 0F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> loops = food("loops", 4, 5F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> loop_stew = food("loop_stew", 10, 10F, false, GeoNtmTab.CONSUMABLES,
            () -> List.of(
                    Effects.instance(MobEffects.REGENERATION, 20 * 20, 1),
                    Effects.instance(Effects.resistance(), 60 * 20, 2),
                    Effects.instance(Effects.speed(), 60 * 20, 1),
                    Effects.instance(Effects.strength(), 20 * 20, 2)),
            true, true)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> fooditem = food("fooditem", 2, 5F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> twinkie = food("twinkie", 3, 5F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> static_sandwich = food("static_sandwich", 6, 5F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> nugget = food("nugget", 200, 200F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> marshmallow = food("marshmallow", 2, 2F, false, GeoNtmTab.CONSUMABLES,
            () -> List.of(), true, false)
            .transform(Presets.handheld());
    public static final RegistryObject<ItemLemon> cheese = food("cheese", 5, 0.75F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> cheese_quesadilla = food("cheese_quesadilla", 8, 1F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite("geontm:items/quesadilla"));
    public static final RegistryObject<ItemLemon> marshmallow_roasted = food("marshmallow_roasted", 6, 6F, false,
            GeoNtmTab.CONSUMABLES, () -> List.of(), true, false)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> glyphid_meat = food("glyphid_meat", 3, 0.5F, true, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> glyphid_meat_grilled = food("glyphid_meat_grilled", 8, 0.75F, true,
            GeoNtmTab.CONSUMABLES, () -> List.of(Effects.instance(Effects.strength(), 180, 1)), false, false)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> spongebob_macaroni = food("spongebob_macaroni", 5, 5F, false,
            GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());
    public static final RegistryObject<ItemLemon> pudding = food("pudding", 6, 15F, false, GeoNtmTab.CONSUMABLES)
            .transform(Presets.sprite());

    /** Forces class initialization from the common bootstrap. */
    public static void init() {}
}
