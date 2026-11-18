package fr.geoventure.geontm.wiaj.actions;

import fr.geoventure.geontm.wiaj.JarScene;
import fr.geoventure.geontm.wiaj.WorldInAJar;
import net.minecraft.nbt.CompoundTag;

public class ActionSetActorData implements IJarAction {

    int id;
    CompoundTag data;

    public ActionSetActorData(int id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void act(WorldInAJar world, JarScene scene) {
        scene.script.actors.get(id).setActorData(data.copy());
    }
}
