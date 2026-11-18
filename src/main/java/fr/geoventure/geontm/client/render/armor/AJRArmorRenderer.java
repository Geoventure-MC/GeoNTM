package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.ResourceLocation;

/**
 * 1.7 {@code ModelArmorAJR} and {@code ModelArmorAJRO}: the same {@code AJR.obj}, four textures named by the
 * set ({@code ajr_*} or {@code ajro_*}). The OBJ's {@code RocketBox} group is never drawn by either model.
 */
public final class AJRArmorRenderer extends ArmorRenderer {

    private static final ResourceLocation OBJ = Ids.of("geontm", "models/armor/ajr.obj");

    private final String set;

    public AJRArmorRenderer(String set) {
        this.set = set;
    }

    private Surface skin(String part) {
        return Surface.cutout("geontm:textures/armor/" + set + "_" + part + ".png");
    }

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(OBJ);

        piece(declaration, ArmorSlot.HELMET, model, "Head", skin("helmet"), Pivot.HEAD);

        piece(declaration, ArmorSlot.CHESTPLATE, model, "Body", skin("chest"), Pivot.BODY);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "LeftArm", skin("arm"), Pivot.LEFT_ARM);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "RightArm", skin("arm"), Pivot.RIGHT_ARM);

        piece(declaration, ArmorSlot.LEGGINGS, model, "LeftLeg", skin("leg"), Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.LEGGINGS, model, "RightLeg", skin("leg"), Pivot.RIGHT_LEG);

        piece(declaration, ArmorSlot.BOOTS, model, "LeftBoot", skin("leg"), Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.BOOTS, model, "RightBoot", skin("leg"), Pivot.RIGHT_LEG);
    }
}
