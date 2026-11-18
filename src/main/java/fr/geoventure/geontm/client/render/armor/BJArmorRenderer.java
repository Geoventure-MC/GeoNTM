package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.ResourceLocation;

/** BJ suit model, including the common body/limb passes; its Jetpack group is owned by JetpackRenderLayer. */
public final class BJArmorRenderer extends ArmorRenderer {

    private static final ResourceLocation OBJ = Ids.of("geontm", "models/armor/bj.obj");

    private static final Surface HELMET = skin("bj_eyepatch");
    private static final Surface CHEST = skin("bj_chest");
    private static final Surface ARM = skin("bj_arm");
    private static final Surface LEG = skin("bj_leg");

    private static Surface skin(String name) {
        return Surface.cutout("geontm:textures/armor/" + name + ".png");
    }

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(OBJ);

        piece(declaration, ArmorSlot.HELMET, model, "Head", HELMET, Pivot.HEAD);

        piece(declaration, ArmorSlot.CHESTPLATE, model, "Body", CHEST, Pivot.BODY);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "LeftArm", ARM, Pivot.LEFT_ARM);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "RightArm", ARM, Pivot.RIGHT_ARM);

        piece(declaration, ArmorSlot.LEGGINGS, model, "LeftLeg", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.LEGGINGS, model, "RightLeg", LEG, Pivot.RIGHT_LEG);

        piece(declaration, ArmorSlot.BOOTS, model, "LeftFoot", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.BOOTS, model, "RightFoot", LEG, Pivot.RIGHT_LEG);
    }
}
