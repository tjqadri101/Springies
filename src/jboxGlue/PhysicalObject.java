package jboxGlue;

import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;
import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;


public abstract class PhysicalObject extends JGObject
{
    protected JGEngineInterface myEngine;
    protected boolean myHasImage;
    protected JGColor myColor;
    protected Body myBody;
    protected float myRotation;

    protected PhysicalObject (String name, int collisionId, JGColor color)
    {
        super(name, false, 0, 0, collisionId, null);
        init(color, false);
    }

    protected PhysicalObject (String name, int collisionId, String gfxname)
    {
        super(name, false, 0, 0, collisionId, gfxname);
        init(null, true);
        if (gfxname == null) {
            throw new IllegalArgumentException("gfxname cannot be null!");
        }
    }
    
    protected PhysicalObject (String name, double x, double y, int collisionId, JGColor color)
    {
    	super(name, false, x, y, collisionId, null);
    	init(color, false);
    }
    
    protected PhysicalObject (String name, double x, double y, int collisionId, JGColor color, double xspeed, double yspeed){
    	super(name, false, x, y, collisionId, null, xspeed, yspeed);
    	init(color, false);
    }

    private void init (JGColor color, boolean hasImage)
    {
        // init defaults
        myEngine = eng;
        myColor = color;
        myHasImage = hasImage;
    }

    protected void createBody (ShapeDef shapeDefinition)
    {
        myBody = WorldManager.getWorld().createBody(new BodyDef());
        myBody.createShape(shapeDefinition);
        myBody.setUserData(this); // for following body back to JGObject
        myBody.setMassFromShapes();
        myBody.m_world = WorldManager.getWorld();
    }

    public Body getBody ()
    {
        return myBody;
    }

    public JGColor getColor ()
    {
        return myColor;
    }

    public void setColor (JGColor val)
    {
        myColor = val;
    }

    @Override
    public void setBBox (int x, int y, int width, int height)
    {
        // NOTE: 
        //  If bounding box is same size as physical object,
        //  JGame will never see the bounding boxes overlap.
        // So fudge box size a little bit so JGame can see the collisions.
        final int FUDGE_TERM = 4;
        x -= FUDGE_TERM;
        y -= FUDGE_TERM;
        width += FUDGE_TERM * 2;
        height += FUDGE_TERM * 2;
        super.setBBox(x, y, width, height);
    }

    @Override
    public void move ()
    {
        // if the JGame object was deleted, remove the physical object too
        if (myBody.m_world != WorldManager.getWorld()) {
            remove();
            return;
        }
        // copy the position and rotation from the JBox world to the JGame world
        Vec2 position = myBody.getPosition();
        x = position.x;
        y = position.y;
        myRotation = -myBody.getAngle();
    }

    @Override
    public void setPos (double x, double y)
    {
        // there's no body while the game object is initializing
        if (myBody != null) { 
            // set position of jbox2d object, not jgame object
            myBody.setXForm(new Vec2((float)x, (float)y), -myRotation);
        }
    }

    public Vec2 getVel(){
    	return myBody.getLinearVelocity();
    }
    public void setVel(double x, double y){
    	myBody.setLinearVelocity(new Vec2((float)x, (float)y));
    }
    public void setVecVel(Vec2 x){
    	myBody.setLinearVelocity(x);
    }
    public void revVel(){
    	Vec2 cur = myBody.getLinearVelocity();
    	float rev = -1;
    	Vec2 rever = cur.mul(rev);
    	myBody.setLinearVelocity(rever);
    }
    public void setForce (double x, double y)
    {
        myBody.applyForce(new Vec2((float)x, (float)y), myBody.m_xf.position);
    }

    @Override
    public void destroy ()
    {
        // body may not be in actual world. If not, do not call destroyBody.
        if (myBody.m_world == WorldManager.getWorld()) {
            // also destroys associated joints
            WorldManager.getWorld().destroyBody(myBody);
        }
    }

    @Override
    public void paint ()
    {
        // only paint something if we need to draw a shape. Images are already drawn
        if (!myHasImage) {
            paintShape();
        }
    }

    protected abstract void paintShape ();
}
