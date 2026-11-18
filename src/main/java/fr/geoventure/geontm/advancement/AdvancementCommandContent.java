package fr.geoventure.geontm.advancement;

import fr.geoventure.geontm.commands.GeoNtmServerCommands;
import fr.geoventure.geontm.items.ModAchievementItems;
import fr.geoventure.geontm.items.tool.ItemBoltgun;


/**
 * The advancement + command family's one bootstrap line: registers the criterion triggers and the achievement icon
 * items, hooks the boltgun's melee half (the "Go Fish" route), and installs the server command tree on every loader.
 * Client commands are registered from {@code GeoNtmClientRegistrations}.
 */
public final class AdvancementCommandContent {

    private AdvancementCommandContent() {}

    public static void init() {
        GeoNtmCriteria.init();
        ModAchievementItems.init();
        ItemBoltgun.installAttack();
        GeoNtmServerCommands.init();
    }
}
