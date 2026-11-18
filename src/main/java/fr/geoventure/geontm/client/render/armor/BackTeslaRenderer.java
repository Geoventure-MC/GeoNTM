package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.Identifier;

/**
 * 1.7 {@code ModelBackTesla}: a {@code ModelArmorBase} whose body is the whole {@code mod_tesla} OBJ,
 * posed on the torso. Drawn for a chestplate carrying the back-mounted tesla module.
 */
public class BackTeslaRenderer extends ArmorRenderer {

    private static final Identifier OBJ = Ids.of("geontm", "models/armor/mod_tesla.obj");
    private static final Surface SKIN = Surface.cutout("geontm:textures/models/armor/mod_tesla.png");

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(OBJ);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "Cube", SKIN, Pivot.BODY);
    }
}
