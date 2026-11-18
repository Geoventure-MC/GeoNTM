package fr.geoventure.geontm.items;

import fr.geoventure.geontm.Constants;
import fr.geoventure.geontm.creativetabs.GeoNtmTab;
import fr.geoventure.geontm.registration.Presets;
import fr.geoventure.geontm.registration.Reg;
import fr.geoventure.geontm.items.machine.ItemZirnoxRod;
import fr.geoventure.geontm.items.machine.ItemZirnoxWaste;
import fr.geoventure.geontm.items.machine.ZirnoxFuel;
import fr.geoventure.geocore.dejson.Tints;
import fr.geoventure.geocore.registry.DeferredRegister;
import fr.geoventure.geocore.registry.RegistryObject;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Hand-registered functional ZIRNOX fuel and spent-fuel items. */
public final class ModZirnoxItems {

    private ModZirnoxItems() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    private static final Reg REG = Reg.of(ITEMS);
    private static final Map<ZirnoxFuel, RegistryObject<ItemZirnoxRod>> ACTIVE = new EnumMap<>(ZirnoxFuel.class);
    private static final Map<ZirnoxFuel, RegistryObject<Item>> DEPLETED = new EnumMap<>(ZirnoxFuel.class);
    private static final Map<ZirnoxFuel, RegistryObject<ItemZirnoxWaste>> WASTE = new EnumMap<>(ZirnoxFuel.class);

    static {
        for (ZirnoxFuel fuel : ZirnoxFuel.values()) {
            RegistryObject<ItemZirnoxRod> active = REG.item("rod_zirnox_" + fuel.id,
                    (Item.Properties p) -> new ItemZirnoxRod(p.stacksTo(1), fuel))
                    .transform(Presets.sprite())
                    .tab(GeoNtmTab.CONTROL);
            ACTIVE.put(fuel, active);

            if (fuel.wasteId != null) {
                RegistryObject<Item> depleted = REG.simpleItem("rod_zirnox_depleted_" + fuel.id,
                        (Item.Properties p) -> p.craftRemainder(ModItems.rod_zirnox_empty.get()))
                        .transform(Presets.sprite())
                        .tab(GeoNtmTab.CONTROL);
                DEPLETED.put(fuel, depleted);

                // 1.7 ItemDepletedFuel: its own sprite (setTextureName), tinted 0xFFBFA5 while hot, partsTab
                RegistryObject<ItemZirnoxWaste> waste = REG.item("waste_" + fuel.wasteId,
                        ItemZirnoxWaste::new)
                        .transform(Presets.sprite("geontm:items/" + fuel.wasteTexture())
                                .tints(Tints.customModelData(0, 0xFFFFFF)))
                        .tab(GeoNtmTab.MATERIALS);
                WASTE.put(fuel, waste);
            }
        }
    }

    public static ItemZirnoxRod active(ZirnoxFuel fuel) { return ACTIVE.get(fuel).get(); }
    public static Item depleted(ZirnoxFuel fuel) { return DEPLETED.get(fuel).get(); }
    public static ItemZirnoxWaste waste(ZirnoxFuel fuel) { return WASTE.get(fuel).get(); }
    public static boolean hasDepleted(ZirnoxFuel fuel) { return DEPLETED.containsKey(fuel); }
    public static ItemStack hotWaste(ZirnoxFuel fuel, int count) { return ItemZirnoxWaste.makeHot(waste(fuel), count); }

    public static void init() {}
}
