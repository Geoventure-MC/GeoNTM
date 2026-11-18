package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.ResourceLocation;

/** 1.7 {@code ModelArmorBismuth}: one texture over every part, boots on the leg bones. */
public final class BismuthArmorRenderer extends ArmorRenderer {

    private static final ResourceLocation OBJ = Ids.of("geontm", "models/armor/bismuth.obj");
    private static final Surface SKIN = Surface.cutout("geontm:textures/armor/bismuth.png");

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(OBJ);

        piece(declaration, ArmorSlot.HELMET, model, "Head", SKIN, Pivot.HEAD);

        piece(declaration, ArmorSlot.CHESTPLATE, model, "LeftArm", SKIN, Pivot.LEFT_ARM);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "RightArm", SKIN, Pivot.RIGHT_ARM);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "Body", SKIN, Pivot.BODY);

        piece(declaration, ArmorSlot.LEGGINGS, model, "LeftLeg", SKIN, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.LEGGINGS, model, "RightLeg", SKIN, Pivot.RIGHT_LEG);

        piece(declaration, ArmorSlot.BOOTS, model, "LeftFoot", SKIN, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.BOOTS, model, "RightFoot", SKIN, Pivot.RIGHT_LEG);
    }
}
