package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.Identifier;

/**
 * 1.7 {@code ModelArmorDigamma}: Fau's suit. The {@code Cassette} copies the body's pose and is drawn with
 * alpha blending in its own texture; upstream then left blending on for the arms, whose texture is opaque,
 * so they stay cutout here.
 */
public final class DigammaArmorRenderer extends ArmorRenderer {

    private static final Identifier OBJ = Ids.of("geontm", "models/armor/fau.obj");

    private static final Surface HELMET = skin("fau_helmet");
    private static final Surface CHEST = skin("fau_chest");
    private static final Surface CASSETTE = Surface.translucent("geontm:textures/armor/fau_cassette.png");
    private static final Surface ARM = skin("fau_arm");
    private static final Surface LEG = skin("fau_leg");

    private static Surface skin(String name) {
        return Surface.cutout("geontm:textures/armor/" + name + ".png");
    }

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(OBJ);

        piece(declaration, ArmorSlot.HELMET, model, "Head", HELMET, Pivot.HEAD);

        piece(declaration, ArmorSlot.CHESTPLATE, model, "Body", CHEST, Pivot.BODY);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "Cassette", CASSETTE, Pivot.BODY);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "LeftArm", ARM, Pivot.LEFT_ARM);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "RightArm", ARM, Pivot.RIGHT_ARM);

        piece(declaration, ArmorSlot.LEGGINGS, model, "LeftLeg", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.LEGGINGS, model, "RightLeg", LEG, Pivot.RIGHT_LEG);

        piece(declaration, ArmorSlot.BOOTS, model, "LeftBoot", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.BOOTS, model, "RightBoot", LEG, Pivot.RIGHT_LEG);
    }
}
