package utilities;
import java.awt.Point;

/**
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see java.awt.Point
 *
 */
public class AABB {
    private Point pos;
    private int w;
    private int h;
    private int size;

    /**
     * Constructor
     * @param p	starting point of the box
     * @param w	box width
     * @param h	box height
     */
    public AABB(Point p,int w,int h) {
        this.pos=p;
        this.w=w;
        this.h=h;
        this.size=Math.max(w, h);
    }

    /**
     * 
     * @return position point
     */
    public Point getpos() {return pos;}
    /**
     * 
     * @return box size
     */
    public int getsize() {return size;}
    /**
     * 
     * @return horizontal position
     */
    public int getX() {return pos.x;}
    /**
     * 
     * @return vertical position
     */
    public int getY() {return pos.y;}
    /**
     * Used in horizontal velocity movement
     * @param sum 
     */
    public void sumX(int sum) {this.pos.x+=sum;}
    /**
     * Used in vertical velocity movement
     * @param sum
     */
    public void sumY(int sum) {this.pos.y+=sum;}
    /**
     * Set box position
     * @param p position point
     */
    public void setpos(Point p) {this.pos=p;}
    /**
     * Used as control for hit on box 
     * @param box
     * @return
     */
    public boolean collides(AABB box) {
    	
    	if(this.pos.x< (box.pos.x+box.w) && this.pos.x+this.w>box.pos.x && this.pos.y<box.pos.y+box.h && this.pos.y+this.h>box.pos.y)    		
            return true;

        else
        	return false;

    }
}
  
