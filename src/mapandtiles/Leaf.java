package mapandtiles;

import java.awt.Point;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;


public class Leaf{
 
    private int MIN_LEAF_SIZE = 9;
 
    public  int y,  x,  w,  h; // the position and size of this Leaf
 
    public Leaf leftChild; // the Leaf's left child Leaf
    public Leaf rightChild; // the Leaf's right child Leaf
    public Rectangle room; // the room that is inside this Leaf
    public Vector<Rectangle> halls; // hallways to connect this Leaf to other Leafs
    public HashMap<Point,Tile> tilestate= new HashMap<>();
 
    public  Leaf(int x, int y, int w, int h)
    {
        // initialize our leaf
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
      
    }
 
    public boolean split()
    {
        // begin splitting the leaf into two children
        if (leftChild != null || rightChild != null)
            return false; // we're already split! Abort!
 
        // determine direction of split
        // if the width is >25% larger than height, we split vertically
        // if the height is >25% larger than the width, we split horizontally
        // otherwise we split randomly
        boolean splitH = Math.random()>0.5;
        if (w > h && w / h >= 1.25)
            splitH = false;
        else if (h > w && h / w >= 1.25)
            splitH = true;
 
        int max = (splitH ? h : w) - MIN_LEAF_SIZE; // determine the maximum height or width
        if (max <= MIN_LEAF_SIZE)
            return false; // the area is too small to split any more...
        
        int split = (int)(Math.random() * (max - MIN_LEAF_SIZE+ 1) + MIN_LEAF_SIZE);// determine where we're going to split
 
        // create our left and right children based on the direction of the split
        if (splitH)
        {
            leftChild = new Leaf(x, y, w, split);
            rightChild = new Leaf(x, y + split, w, h - split);
        }
        else
        {
            leftChild = new Leaf(x, y, split, h);
            rightChild = new Leaf(x + split, y, w - split, h);
        }
        return true; // split successful!
    }
    
    public void createRooms(HashMap<Point,Tile> t,Vector<Vector<Point>> rv)
    {
    	// this function generates all the rooms and hallways for this Leaf and all of its children.
    	if (leftChild != null || rightChild != null)
    	{
    		// this leaf has been split, so go into the children leafs
    		if (leftChild != null)
    		{
    			leftChild.createRooms(t,rv);
    		}
    		if (rightChild != null)
    		{
    			rightChild.createRooms(t,rv);
    		}
    		if (leftChild != null && rightChild != null)
    		{
    			createHall(leftChild.getRoom(), rightChild.getRoom(),t);
    		}
    	}
    	else
    	{
    		// this Leaf is the ready to make a room
    		Point roomSize;
    		Point roomPos;
    		// the room can be between 3 x 3 tiles to the size of the leaf - 2.
    		roomSize = new Point((int)(Math.random()*( w - 2-3)+3),(int)(Math.random()*(h - 2-3)+3));
    		// place the room within the Leaf, but don't put it right 
    		// against the side of the Leaf (that would merge rooms together)
    		roomPos = new Point((int)(Math.random()*( w-roomSize.x-1 - 1)+1), (int)(Math.random()*( h-roomSize.y-1- 1)+1));
    		room = new Rectangle(x + roomPos.x, y + roomPos.y, roomSize.x, roomSize.y);
    		Vector<Point> roomph= new Vector<Point>();
    		for(int a=room.x;a<room.x+room.width;a++) {
    			for(int b= room.y;b<room.y+room.height;b++) {
    				t.put(new Point(a,b), new Tile(new Point(a,b),tiletype.ON));
    				roomph.add(new Point(a,b));
    			}
    			
    		}
    		rv.add(roomph);
    		
    	}
    }
    public Rectangle getRoom()
    {
    	// iterate all the way through these leafs to find a room, if one exists.
    	if (room != null)
    		return room;
    	else
    	{
    		Rectangle lRoom= null;
    		Rectangle rRoom= null;
    		if (leftChild != null)
    		{
    			lRoom = leftChild.getRoom();
    		}
    		if (rightChild != null)
    		{
    			rRoom = rightChild.getRoom();
    		}
    		if (lRoom == null && rRoom == null)
    			return null;
    		else if (rRoom == null)
    			return lRoom;
    		else if (lRoom == null)
    			return rRoom;
    		else if (Math.random() > 0.5)
    			return lRoom;
    		else
    			return rRoom;
    	}
    }
    public void createHall(Rectangle l,Rectangle r,HashMap<Point,Tile> t)
    {
    	// now we connect these two rooms together with hallways.
    	// this looks pretty complicated, but it's just trying to figure out which point is where and then either draw a straight line, or a pair of lines to make a right-angle to connect them.
    	// you could do some extra logic to make your halls more bendy, or do some more advanced things if you wanted.

    	halls = new Vector<Rectangle>();

    	Point point1 = new Point((int)Math.random()*(l.width-l.x-3)+l.x, (int)Math.random()*(l.height-3-l.y)+l.y);
    	Point point2 = new Point((int)Math.random()*(r.width-3-r.x)+r.x, (int)Math.random()*(r.height-3-r.y)+r.y);

    	int w = point2.x - point1.x;
    	int h = point2.y - point1.y;

    	if (w < 0)
    	{
    		if (h < 0)
    		{
    			if (Math.random() < 0.5)
    			{
    				halls.add(new Rectangle(point2.x, point1.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h)));
    			}
    			else
    			{
    				halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(h)));
    			}
    		}
    		else if (h > 0)
    		{
    			if (Math.random() < 0.5)
    			{
    				halls.add(new Rectangle(point2.x, point1.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(h)));
    			}
    			else
    			{
    				halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
    			}
    		}
    		else // if (h == 0)
    		{
    			halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
    		}
    		
    	}
    	else if (w > 0)
    	{
    		if (h < 0)
    		{
    			if (Math.random() < 0.5)
    			{
    				halls.add(new Rectangle(point1.x, point2.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(h)));
    			}
    			else
    			{
    				halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h)));
    			}
    		}
    		else if (h > 0)
    		{
    			if (Math.random() < 0.5)
    			{
    				halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(h)));
    			}
    			else
    			{
    				halls.add(new Rectangle(point1.x, point2.y, Math.abs(w), 1));
    				halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
    			}
    		}
    		else // if (h == 0)
    		{
    			halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
    		}
    	}
    	else // if (w == 0)
    	{
    		if (h < 0)
    		{
    			halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h)));
    		}
    		else if (h > 0)
    		{
    			halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
    		}
    	}
    	for(int a=0;a<halls.size();a++) {
			for(int b=0;b<halls.get(a).width;b++) {
				for(int d=0;d<halls.get(a).height;d++) {
			      t.put(new Point(halls.get(a).x+b,halls.get(a).y+d), new Tile(new Point(halls.get(a).x+b,halls.get(a).y+d),tiletype.ON));
				}
			}
		}
    	
    }
    public HashMap<Point, Tile> getTilemap(){return this.tilestate;};
}

