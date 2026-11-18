package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.ResourceLocation;

/** 1.7 {@code ModelArmorDesh}: {@code steamsuit.obj}, the steam suit's four textures, boots on the leg bones (smooth shading is the OBJ normals). */
public final class SteamsuitArmorRenderer extends ArmorRenderer {

    private static final ResourceLocation OBJ = Ids.of("geontm", "models/armor/steamsuit.obj");

    private static final Surface HELMET = skin("steamsuit_helmet");
    private static final Surface CHEST = skin("steamsuit_chest");
    private static final Surface ARM = skin("steamsuit_arm");
    private static final Surface LEG = skin("steamsuit_leg");

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
