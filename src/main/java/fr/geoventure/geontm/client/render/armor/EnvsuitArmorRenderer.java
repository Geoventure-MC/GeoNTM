package fr.geoventure.geontm.client.render.armor;

import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import fr.geoventure.geocore.world.ArmorSlot;
import net.minecraft.resources.ResourceLocation;

/**
 * 1.7 {@code ModelArmorEnvsuit}: the helmet is alpha-blended; its {@code Lamps} copy the head pose and are drawn
 * untextured in (1, 1, 0.8) at full brightness with lighting off - here a white emissive surface with that tint.
 * The OBJ's {@code Tail} group was never drawn. Feet are {@code LeftFoot}/{@code RightFoot} on the leg bones.
 */
public final class EnvsuitArmorRenderer extends ArmorRenderer {

    private static final ResourceLocation OBJ = Ids.of("geontm", "models/armor/envsuit.obj");

    private static final Surface HELMET = Surface.translucent("geontm:textures/armor/envsuit_helmet.png");
    private static final Surface LAMPS = Surface.emissive("geontm:textures/models/misc/white.png");
    private static final Surface CHEST = skin("envsuit_chest");
    private static final Surface ARM = skin("envsuit_arm");
    private static final Surface LEG = skin("envsuit_leg");

    private static Surface skin(String name) {
        return Surface.cutout("geontm:textures/armor/" + name + ".png");
    }

    @Override
    protected void setup(Gem.Model<ArmorPose> declaration) {
        WavefrontObj model = WavefrontObj.load(OBJ);

        piece(declaration, ArmorSlot.HELMET, model, "Helmet", HELMET, Pivot.HEAD);
        piece(declaration, ArmorSlot.HELMET, model, "Lamps", LAMPS, Pivot.HEAD)
                .tint(state -> 0xFFFFFFCC)
                .light(state -> 0xF000F0);

        piece(declaration, ArmorSlot.CHESTPLATE, model, "Chest", CHEST, Pivot.BODY);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "LeftArm", ARM, Pivot.LEFT_ARM);
        piece(declaration, ArmorSlot.CHESTPLATE, model, "RightArm", ARM, Pivot.RIGHT_ARM);

        piece(declaration, ArmorSlot.LEGGINGS, model, "LeftLeg", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.LEGGINGS, model, "RightLeg", LEG, Pivot.RIGHT_LEG);

        piece(declaration, ArmorSlot.BOOTS, model, "LeftFoot", LEG, Pivot.LEFT_LEG);
        piece(declaration, ArmorSlot.BOOTS, model, "RightFoot", LEG, Pivot.RIGHT_LEG);
    }
}
