package unsw.graphics.scene;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.*;
import unsw.graphics.geometry.Point2D;

/**
 * A SceneObject is an object that can move around in the world.
 * 
 * SceneObjects form a scene tree.
 * 
 * Each SceneObject is offset from its parent by a translation, a rotation and a scale factor. 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class SceneObject {
    
    // the links in the scene tree
    private SceneObject myParent;
    private List<SceneObject> myChildren;

    // the local transformation
    private Point2D myTranslation;
    private float myRotation; //normalised to the range [-180..180)
    private float myScale;
    
    // Is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special constructor for creating the root node. Do not use otherwise.
     */
    public SceneObject() {
        myParent = null;
        myChildren = new ArrayList<SceneObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        amShowing = true;
    }

    /**
     * Public constructor for creating SceneObjects, connected to a parent.
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public SceneObject(SceneObject parent) {
        myParent = parent;
        myChildren = new ArrayList<SceneObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        // initially showing
        amShowing = true;
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
	    List<SceneObject> childrenList = new ArrayList<SceneObject>(myChildren);
        for (SceneObject child : childrenList) {
            child.destroy();
        }
        if(myParent != null)
            myParent.myChildren.remove(this);
    }

    /**
     * Get the parent of this scene object
     * 
     * @return
     */
    public SceneObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<SceneObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public float getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(float rotation) {
        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(float angle) {
        myRotation += angle;
        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public float getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(float scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(float factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public Point2D getPosition() {
        return myTranslation;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        setPosition(new Point2D(x,y));
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(Point2D p) {
        myTranslation = p;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {
        myTranslation = myTranslation.translate(dx, dy);
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object and all it's children. This method is called once per frame. 
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(float dt) {
        updateSelf(dt);
        
        // Make a copy of all the children to avoid concurrently modification issues if new objects
        // are added to the scene during the update.
        List<SceneObject> children = new ArrayList<SceneObject>(myChildren);
        for (SceneObject so : children) {
            so.update(dt);
        }
    }

    /** 
     * Update the object itself. Does nothing in the default case. Subclasses can override this
     * for animation or interactivity.
     * 
     * @param dt
     */
    public void updateSelf(float dt) {
        // Do nothing by default
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base SceneObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
        // Do nothing by default
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * TODO: Complete this method
     * 
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame2D frame) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }

        // TODO: Compute the coordinate frame for this object
        // draw the object (Call drawSelf() to draw the object itself) 
        // and all its children recursively
       
        //Calculate the frame transformation
        CoordFrame2D transform = frame.translate(myTranslation)
                                    .rotate(myRotation)
                                    .scale(myScale, myScale);
        
        //Calling the drawSelf function
        drawSelf(gl, transform);

        //Going through all the child objects and calling draw on them
        //with this object's frame's context
        for (SceneObject o : myChildren) {
            o.draw(gl, transform);
        }
        
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinats
     */
    public Point2D getGlobalPosition() {

        //Create an ArrayList of object heirarchy from this.myParent->root
        ArrayList<SceneObject> heirarchy = new ArrayList<>();
        SceneObject current = this.myParent;
        while (current != null) {
            heirarchy.add(0, current); //Add to beginning of list
            current = current.myParent;
        }

        //Iterate through objects and apply transformations
        CoordFrame2D frame = CoordFrame2D.identity(); //The frame we are applying transformations on
        for (SceneObject o : heirarchy) {
            frame = frame.translate(o.myTranslation)
                        .rotate(o.myRotation)
                        .scale(o.myScale, o.myScale);
        }

        //Multiply the frame with the translation to get point
        return frame.getMatrix().multiply(myTranslation.asHomogenous()).asPoint2D();
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public float getGlobalRotation() {
        
        //Create an ArrayList of object heirarchy from this.myParent->root
        ArrayList<SceneObject> heirarchy = new ArrayList<>();
        SceneObject current = this;
        while (current != null) {
            heirarchy.add(0, current); //Add to beginning of list
            current = current.myParent;
        }

        //Adding all rotations together
        float returnRotation = 0F;
        for (SceneObject o : heirarchy) {
            returnRotation += o.myRotation;
        }

        return MathUtil.normaliseAngle(returnRotation);
    }

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public float getGlobalScale() {

        //Create an ArrayList of object heirarchy from this.myParent->root
        ArrayList<SceneObject> heirarchy = new ArrayList<>();
        SceneObject current = this;
        while (current != null) {
            heirarchy.add(0, current); //Add to beginning of list
            current = current.myParent;
        }

        float returnScale = 1;
        for (SceneObject o : heirarchy) {
            returnScale *= o.myScale;
        }

        return returnScale;
    }

    /**
     * Change the parent of a scene object.
     * 
     * @param parent
     */
    public void setParent(SceneObject parent) {
        // TODO: add code so that the object does not change its global position, rotation or scale
        // when it is reparented. You may need to add code before and/or after 
        // the fragment of code that has been provided - depending on your approach
        
        //Getting the global attributes of this object
        Point2D globalTranslation = getGlobalPosition();
        float globalRotation = getGlobalRotation();
        float globalScale = getGlobalScale();

        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);
        
        //Create an ArrayList of object heirarchy from this.myParent->root
        ArrayList<SceneObject> heirarchy = new ArrayList<>();
        SceneObject current = this.myParent;
        while (current != null) {
            heirarchy.add(current); //Add to beginning of list
            current = current.myParent;
        }

        //Iterate through objects and apply transformations
        CoordFrame2D frame = CoordFrame2D.identity(); //The frame we are applying transformations on
        for (SceneObject o : heirarchy) {
            frame = frame.scale(1/o.myScale, 1/o.myScale)
                        .rotate(o.myRotation*-1)
                        .translate(new Point2D(o.myTranslation.getX()*-1,
                                            o.myTranslation.getY()*-1));
        }

        //Setting the local translation
        myTranslation = frame.getMatrix().multiply(new Vector3(globalTranslation.getX(), 
                                                globalTranslation.getY(), 
                                                1)).asPoint2D();
                                        

        //Subtracting from globalRotation all rotations in the heirarchy
        for (SceneObject o : heirarchy) {
            globalRotation -= o.myRotation;
        }

        //Setting the local rotation
        myRotation =  MathUtil.normaliseAngle(globalRotation);

        //Dividing the globalScale by all scales in the heirarchy
        for (SceneObject o : heirarchy) {
            globalScale /= o.myScale;
        }

        //Setting the local scale
        myScale = globalScale;
    }
    

}
