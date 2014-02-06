package jboxGlue;

import jgame.platform.JGEngine;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;


public class WorldManager
{
    public static World ourWorld;
    static {
        ourWorld = null;
    }

    public static World getWorld ()
    {
        // make sure we have a world, just in case...
        if (ourWorld == null) {
            throw new RuntimeException("call initWorld() before getWorld()!");
        }
        return ourWorld;
    }

    public static void initWorld (JGEngine engine)
    {
        AABB worldBounds = new AABB(new Vec2(0, 0),
                                    new Vec2(engine.displayWidth(), engine.displayHeight()));
        Vec2 gravity = new Vec2(0.0f, 0.0f);
        ourWorld = new World(worldBounds, gravity, true);
        //ourWorld.setContactFilter(new NoContactFilter());//Turns off ALL Jbox collisions
    }
}
