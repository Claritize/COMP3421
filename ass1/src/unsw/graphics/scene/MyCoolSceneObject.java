package unsw.graphics.scene;

import java.awt.Color;
import java.util.ArrayList;

import unsw.graphics.geometry.Polygon2D;

/**
 * A cool scene object
 *
 */
public class MyCoolSceneObject extends SceneObject {

    private ArrayList<SceneObject> myChildren = new ArrayList<>();

    public MyCoolSceneObject(SceneObject parent) {
        
        super(parent);
        this.setScale(0.05F);
        SceneObject pacman = new CircularSceneObject(this, 2, Color.YELLOW, null);
        myChildren.add(pacman);
        SceneObject mouth = new PolygonalSceneObject(this, new Polygon2D(0,0, 2,1.5F, 2,-1.5F), Color.BLACK, null);
        SceneObject dot1 = new CircularSceneObject(this, Color.WHITE, null);
        dot1.translate(4, 0);
        myChildren.add(dot1);
        SceneObject dot2 = new CircularSceneObject(this, Color.WHITE, null);
        dot2.translate(8, 0);
        myChildren.add(dot2);
        SceneObject dot3 = new CircularSceneObject(this, Color.WHITE, null);
        dot3.translate(12, 0);
        myChildren.add(dot3);
    }

}
