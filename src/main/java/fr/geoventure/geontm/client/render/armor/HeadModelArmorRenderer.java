package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;

/**
 * The single-OBJ helmets of 1.7 {@code ModelArmorBase}: {@code ModelGlasses} (the ash goggles, {@code goggles.obj}
 * with {@code textures/armor/goggles.png}) and {@code ModelHat} (the Fabulous Hat, {@code hat.obj}). Both render the
 * whole OBJ on the head bone for the helmet slot only.
 */
public final class HeadModelArmorRenderer extends ArmorRenderer {

    private final String obj;
    private final String texture;

    public HeadModelArmorRenderer(String obj, String texture) {
        this.obj = obj;
        this.texture = texture;
    }

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(Ids.of("geontm", "models/armor/" + obj + ".obj"));
        declaration.part(model.all(), Surface.cutout("geontm:textures/armor/" + texture + ".png"))
                .visibleIf(state -> state.slot == ArmorSlot.HELMET)
                .pose(state -> Pivot.HEAD.pose(state.wearer))
                .scale(1F / 16F);
    }
}
