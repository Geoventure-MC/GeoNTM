package fr.geoventure.geontm.wiaj.actions;

import fr.geoventure.geontm.wiaj.JarScene;
import fr.geoventure.geontm.wiaj.WorldInAJar;

public interface IJarAction {

    /** Time taken by this action in ticks */
    public int getDuration();
    /** Perform this action */
    public void act(WorldInAJar world, JarScene scene);
}
