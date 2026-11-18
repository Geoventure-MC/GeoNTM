package fr.geoventure.geontm.hazard;

import fr.geoventure.geontm.handler.HazmatRegistry;
import fr.geoventure.geontm.items.ModArmorItems;
import fr.geoventure.geontm.items.ModArmorModItems;
import fr.geoventure.geontm.items.ModArmorSetBItems;
import fr.geoventure.geontm.items.ModArmorSetItems;
import fr.geoventure.geontm.items.ModRBMKItems;
import fr.geoventure.geontm.util.DamageResistanceHandler;


/**
 * Post-registration hazard setup entrypoint. Must run <b>after</b> all items/blocks are registered
 * (so {@link HazardRegistry#registerItems()} can resolve items from the registry), i.e. from each
 * loader's post-registration hook (NeoForge {@code FMLCommonSetupEvent}, Fabric after
 * {@code DeferredRegister.flushAll}).
 */
public final class Hazards {

    private Hazards() {}

    private static boolean done = false;

    public static void init() {
        if (done) return;
        done = true;
        ModRBMKItems.linkPellets();
        HazardRegistry.registerTrafos();
        // item hazards, hazmat resistance, hazard classes, armor sets, damage resistance and block emission are data maps
        // (HazardDataMaps, ArmorDataMaps); what is left here is code: transformers, suit behaviour, the config layers
        ModArmorItems.configure();      // power-armour set bonuses, rad resistance, hazard classes
        ModArmorModItems.configure();   // PaA/liquidator suits, gas-mask attachment hazard class
        ModArmorSetBItems.configure();  // final armor B: steam/diesel/env suits, masks, jackets
        ModArmorSetItems.configure();   // final armor sets A: alloy, cmb, bismuth, euphemium, AJR/AJRO, HEV, Fau, trenchmaster
        HazmatRegistry.registerHazmats(); // 1.7 geontmRadResist.json override (or its template)
        DamageResistanceHandler.init();  // threshold/resistance profiles + damage event seam
        HazardSystem.install();                       // carried hazards, resolved per slot edit
    }
}
