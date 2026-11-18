package fr.geoventure.geontm.wiaj.actors;

import fr.geoventure.geontm.wiaj.JarScene;
import fr.geoventure.geontm.wiaj.WorldInAJar;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.geoventure.geocore.render.CameraInfo;
import fr.geoventure.geocore.render.SubmitSink;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.nbt.CompoundTag;

public class ActorTileEntity extends ActorBase {

    ITileActorRenderer renderer;

    public ActorTileEntity(ITileActorRenderer renderer) {
        this.renderer = renderer;
    }

    public ActorTileEntity(ITileActorRenderer renderer, CompoundTag data) {
        this(renderer);
        this.data = data;
    }

    @Override
    public void drawForegroundComponent(GuiGraphicsExtractor graphics, int w, int h, int ticks, float interp) { }

    @Override
    public void drawBackgroundComponent(WorldInAJar world, PoseStack pose, SubmitSink sink, CameraInfo camera, int ticks, float interp) {
        renderer.renderActor(world, pose, sink, camera, ticks, interp, data);
    }

    @Override
    public void updateActor(JarScene scene) {
        renderer.updateActor(scene.script.ticksElapsed, data);
    }
}
