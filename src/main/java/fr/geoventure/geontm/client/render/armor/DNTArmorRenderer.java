package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.ResourceLocation;

/** DNT suit model. The old DNT model has no physical Jetpack group; exhaust is packet-driven. */
public final class DNTArmorRenderer extends ArmorRenderer {

    private static final ResourceLocation OBJ = Ids.of("geontm", "models/armor/dnt.obj");

    private static final Surface HELMET = skin("dnt_helmet");
    private static final Surface CHEST = skin("dnt_chest");
    private static final Surface ARM = skin("dnt_arm");
    private static final Surface LEG = skin("dnt_leg");

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

        piece(declaration, ArmorSlot.BOOTS, model, "LeftBoot", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.BOOTS, model, "RightBoot", LEG, Pivot.RIGHT_LEG);
    }
}
