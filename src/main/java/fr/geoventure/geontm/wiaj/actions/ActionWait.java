package fr.geoventure.geontm.wiaj.actions;

import fr.geoventure.geontm.wiaj.JarScene;
import fr.geoventure.geontm.wiaj.WorldInAJar;

public class ActionWait implements IJarAction {

    private int ticks;

    public ActionWait(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public int getDuration() {
        return this.ticks;
    }

    @Override
    public void act(WorldInAJar world, JarScene scene) { }
}
