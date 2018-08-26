package unsw.graphics.scene;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Polygon2D;

/**
 * A game object that has a polygonal shape.
 * 
 * This class extend SceneObject to draw polygonal shapes.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * 
 */
public class LineSceneObject extends SceneObject {

    private Polygon2D myPolygon;
    private Color myLineColor;

    /**
     * Create a polygonal scene object and add it to the scene tree
     * 
     * The line and fill colors can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param points A list of points defining the polygon
     * @param fillColor The fill color
     * @param lineColor The outline color
    */
    //Create a LineSceneObject from (0,0) to (1,0)
    public LineSceneObject(SceneObject parent, Color lineColor) {

        super(parent);
        myPolygon = new Polygon2D(0,0, 1,0);
        myLineColor = lineColor;
    }

    //Create a LineSceneObject from (x1,y1) to (x2,y2)
    public LineSceneObject(SceneObject parent, float x0, float y0, float x1, float y1, Color lineColor) {
        
        super(parent);
        myPolygon = new Polygon2D(x0,y0, x1,y1);
        myLineColor = lineColor;
    }

    /**
     * Get the outline color.
     * 
     * @return
     */
    public Color getLineColor() {
        return myLineColor;
    }

    /**
     * Set the outline color.
     * 
     * Setting the color to null means the outline should not be drawn
     * 
     * @param lineColor
     */
    public void setLineColor(Color lineColor) {
        myLineColor = lineColor;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * Draw the polygon
     * 
     * if the fill color is non-null, draw the polygon filled with this color
     * if the line color is non-null, draw the outline with this color
     * 
     */
    @Override
    public void drawSelf(GL3 gl, CoordFrame2D frame) {

        Shader.setPenColor(gl, myLineColor);
        myPolygon.drawOutline(gl, frame);

    }


}
