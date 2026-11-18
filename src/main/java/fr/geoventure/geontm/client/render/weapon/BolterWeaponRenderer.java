package fr.geoventure.geontm.client.render.weapon;

import com.mojang.math.Axis;
import fr.geoventure.geontm.client.model.WavefrontObj;
import fr.geoventure.geontm.items.weapon.sedna.ItemGunBaseNT;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.geoventure.geocore.Ids;
import fr.geoventure.geocore.render.gem.Gem;
import fr.geoventure.geocore.render.gem.Surface;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/** Exact Gem port of 1.7 ItemRenderBolter. */
public final class BolterWeaponRenderer extends WeaponRenderer {
    private static final ResourceLocation MODEL_ID=Ids.of("geontm","models/weapons/bolter.obj");
    private static final Surface BODY=Surface.cutout("geontm:textures/models/weapons/bolter.png");
    private WavefrontObj model;
    private WavefrontObj model(){if(model==null)model=WavefrontObj.load(MODEL_ID);return model;}
    @Override protected float getTurnMagnitude(ItemStack s){return ItemGunBaseNT.getIsAiming(s)?2.5F:-.25F;}
    @Override public float viewFov(ItemStack s,float f,float p){return aimingFov(s,f,p,.33F);}
    @Override public void setupFirstPerson(ItemStack s,PoseStack p){p.translate(0,0,.875);float o=.8F;standardAimingTransform(s,p,-1.5F*o,-2F*o,2.5F*o,0,-10.5/8D,1.25);}
    @Override protected void setup(Gem.Model<WeaponPose>d){WavefrontObj o=model();Gem.Part<WeaponPose>g=d.joint().visibleIf(s->s.animated).stretch(s->.5F).rotate(Gem.Axes.Y,180F).angle(Gem.Axes.X,s->(float)s.bus("RECOIL")[0]*5).slide(Gem.Axes.Z,s->(float)s.bus("RECOIL")[0]).slide(Gem.Axes.Y,s->(float)s.bus("TILT")[0]).translate(0,0,3).angle(Gem.Axes.X,s->(float)s.bus("TILT")[0]*35).translate(0,0,-3);g.child(o.part("Body"),BODY);Gem.Part<WeaponPose>m=g.joint().spin(Gem.Axes.X,0,0,5,s->(float)(s.bus("MAG")[0]*60*(s.bus("MAG")[2]==1?2.5:1)));m.child(o.part("Mag"),BODY);m.child(o.part("Bullet"),BODY).visibleIf(s->s.bus("MAG")[2]!=1);d.joint().visibleIf(s->!s.animated).rotate(Gem.Axes.Y,180F).child(o.parts("Body","Mag","Bullet"),BODY);}

    @Override
    protected void setupInv(PoseStack pose, ItemStack stack) {
        super.setupInv(pose, stack);
        pose.scale(2.75F, 2.75F, 2.75F);
        pose.mulPose(Axis.XP.rotationDegrees(25F));
        pose.mulPose(Axis.YP.rotationDegrees(45F));
        pose.translate(-0.25F, -0.5F, 0F);
    }

    @Override
    protected void setupThirdPerson(PoseStack pose, ItemStack stack) {
        super.setupThirdPerson(pose, stack);
        pose.scale(2.5F, 2.5F, 2.5F);
        pose.translate(0F, -0.75F, 1.25F);
    }
}
