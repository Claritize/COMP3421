/**
 * 
 */
package unsw.graphics.geometry;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.Point2DBuffer;
import unsw.graphics.Shader;

/**
 * A triangle in 2D space
 *
 * This class is immutable.
 * 
 * @author Robert Clifton-Everest
 *
 */
public class Triangle2D {
    private List<Point2D> points;

    public Triangle2D() {
        points = new ArrayList<Point2D>();
    }
    
    /**
     * @precondition points.size() == 3
     * @param points
     */
    public Triangle2D(List<Point2D> points) {
        this.points = new ArrayList<Point2D>(points);
    }

    public void draw(GL3 gl) {        
        Point2DBuffer buffer = new Point2DBuffer(points);

        int[] names = new int[1];
        gl.glGenBuffers(1, names, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, names[0]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, points.size() * 2 * Float.BYTES,
                buffer.getBuffer(), GL.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(Shader.POSITION, 2, GL.GL_FLOAT, false, 0, 0);
        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, points.size());

        gl.glDeleteBuffers(1, names, 0);
    }
}
