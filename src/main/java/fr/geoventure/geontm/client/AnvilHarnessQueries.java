package fr.geoventure.geontm.client;

import com.google.gson.JsonObject;
import fr.geoventure.geontm.inventory.gui.GUIAnvil;
import fr.geoventure.geocore.harness.HarnessQueryRegistry;
import net.minecraft.client.Minecraft;

/** The {@code geontm_anvil} harness query: the open anvil screen's selected construction recipe and page. */
public final class AnvilHarnessQueries {

    private AnvilHarnessQueries() {}

    public static void register() {
        HarnessQueryRegistry.register("geontm_anvil", AnvilHarnessQueries::query);
    }

    private static JsonObject query(Minecraft mc, JsonObject params) {
        JsonObject json = new JsonObject();
                boolean open = mc.screen instanceof GUIAnvil;
        json.addProperty("open", open);
        if (open) {
            GUIAnvil anvil = (GUIAnvil) mc.screen;
            json.addProperty("selected", anvil.selectedRecipeId());
            json.addProperty("page", anvil.pageIndex());
        }
        return json;
    }
}
