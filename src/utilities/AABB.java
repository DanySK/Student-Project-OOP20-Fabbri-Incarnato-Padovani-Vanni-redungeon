package utilities;
import java.awt.Point;

public class AABB {
    private Point pos;
    private int w;
    private int h;
    private int size;

    public AABB(Point p,int w,int h) {
        this.pos=p;
        this.w=w;
        this.h=h;
        this.size=Math.max(w, h);
    }


    public Point getpos() {return pos;}
    public int getsize() {return size;}
    public int getX() {return pos.x;}
    public int getY() {return pos.y;}
    public void sumX(int sum) {this.pos.x+=sum;}
    public void sumY(int sum) {this.pos.y+=sum;}
    public void setpos(Point p) {this.pos=p;}

    public boolean collides(AABB box) {
    	
    	if(this.pos.x< (box.pos.x+box.w) && this.pos.x+this.w>box.pos.x && this.pos.y<box.pos.y+box.h && this.pos.y+this.h>box.pos.y)    		
            return true;

        else
        	return false;

    }
}
  
