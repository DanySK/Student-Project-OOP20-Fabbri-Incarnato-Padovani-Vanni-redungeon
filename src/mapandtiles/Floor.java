package mapandtiles;

import java.awt.Color;



import java.awt.Graphics2D;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.imageio.ImageIO;
import entity.*;
import game.*;
import utilities.SpriteSheet;
import utilities.AABB;
/**
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 */
public class Floor extends AbsFloor {
	private Maputil util= new Maputil();
	private int level;
	private int screenw,screenh;
	private int width;
	private int height;
	private HashMap<Point,Tile> tilestate= new HashMap<>();
	private int tilesize=32;
	private Vector<Leaf> leaves= new Vector<Leaf>();
	private Vector<Vector<Point>> rooms = new Vector<Vector<Point>>();
	private final int MAX_LEAF_SIZE=24;
	private Random r= new Random();
	SpriteSheet sprite;
	private int offsetX=0;
	private int offsetY=0;
	//generate a Floor with level, width ,height,screen width and screen height
	/**
	 * 
	 * @param l 
	 * level of this floor
	 * @param w
	 * width of the floor in pixels
	 * @param h
	 * height of the floor in pixels
	 * @param screenw
	 * width of the window in pixels
	 * @param screenh
	 * height of the screen in pixels
	 */
   public Floor(int l,int w, int h,int screenw,int screenh) {
	   super(w, h, ID.Floor);
	   this.level=l;
	   this.height=h;
	   this.width=w;
	   this.screenw=screenw;
	   this.screenh=screenh;
	   int floorseed=2;
	   if(l>5 && l<=10) {
		   floorseed=1;
	   }
	   else if(l>10 && l<=15) {floorseed=3;}
	   else if(l>15) {floorseed=4;}
	   try 
		{
			sprite = new SpriteSheet(ImageIO.read(new File("data/tiles"+floorseed+".png")));
			
		}	catch(IOException e) 
			{
			
			}
	   this.floorGenner(this.width,this.height);
   }

   /**
    * generate a pseudorandom floor using bsp algorithm
    * @param w
    * width of the map in pixels
    * @param h
    * height of the map in pixels
    */
   void floorGenner(int w,int h) {
	   roomsCreate();
	   healCreate();
	   trapCreate();
	   teleportCreate();
	   gemstoneCreate();
	   exitCreate();
	   for(int i=0;i<w/tilesize;i++) {
		   for(int j=0;j<h/tilesize;j++) {
			   if(!(tilestate.containsKey(new Point(i,j))))
		   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
	   }
	   }
	   for(int p=1;p<width/tilesize-1;p++) {
		   for(int l=1;l<height/tilesize-1;l++) {
			   if(this.tilestate.get(new Point(p,l)).gettype()==tiletype.OFF) {
				   choosetile(new Point(p,l));
			  
		   }
	   }}
	   
	   
   }
   //use a BSP algorithm to create the rooms and halls through a vector of Leaf
   void roomsCreate() {
		Leaf root = new Leaf(1,1,width/tilesize-1,height/tilesize-1,this.sprite);
		leaves.add(root);
		boolean didsplit= true;
		while (didsplit)
		{
			didsplit = false;
			for(int i=0; i<leaves.size();i++)
			{ Leaf l= leaves.get(i);
				if (l.leftChild == null && l.rightChild == null) // if this Leaf is not already split...
				{
					// if this Leaf is too big, or 75% chance...
					if (l.w > MAX_LEAF_SIZE || l.h > MAX_LEAF_SIZE ||  r.nextFloat() > 0.25)
					{
						if (l.split()) // split the Leaf!
						{
							// if we did split, push the child leafs to the Vector so we can loop into them next
							leaves.add(l.leftChild);
							leaves.add(l.rightChild);
							didsplit = true;
						}
					}
				}
			}
		}
		root.createRooms(this.tilestate,rooms);
		
		
	}
   /**
    * creates the exit of this floor in a pseudorandom position inside a room
    */
   public void exitCreate() {
	   if((this.level+1)%5 ==0) {
		   Random r= new Random();
			int a=r.nextInt(rooms.size());
			Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
			this.tilestate.replace(rpos, new Tile(rpos,tiletype.LockedExit,sprite));
			int b=r.nextInt(rooms.size());
			Point kpos = rooms.get(b).get(r.nextInt(rooms.get(b).size()));
			this.tilestate.replace(kpos, new Tile(kpos,tiletype.Key,sprite));
			
	   }
	   else {
		Random r= new Random();
		int a=r.nextInt(rooms.size());
		Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
		this.tilestate.replace(rpos, new Tile(rpos,tiletype.Exit,sprite));}
		
	}
   //create a special heal tile 
   public void healCreate() {
		Random r= new Random();
		int a=r.nextInt(rooms.size());
		Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
		if(tilestate.get(rpos).gettype()!=tiletype.Exit)
		this.tilestate.replace(rpos, new Tile(rpos,tiletype.Heal,sprite));
		
	}
   /**
    * create a teleport tile
    */
   public void teleportCreate() {
		Random r= new Random();
		int a=r.nextInt(rooms.size());
		Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
		if(tilestate.get(rpos).gettype()!=tiletype.Exit)
		this.tilestate.replace(rpos, new Tile(rpos,tiletype.Teleport,sprite));
		
	}
   /**
    * create a trap that damages the player
    */
   public void trapCreate() {
	   for(int i=0;i<=this.width/1000;i++) {
		Random r= new Random();
		int a=r.nextInt(rooms.size());
		Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
		if(tilestate.get(rpos).gettype()!=tiletype.Exit)
		this.tilestate.replace(rpos, new Tile(rpos,tiletype.Trap,sprite));
	   }
	}
   /**
    * creates a rare gem to pick up
    */
   public void gemstoneCreate() {
	  if( Math.random()<0.3) {
		  Random r= new Random();
			int a=r.nextInt(rooms.size());
			Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
			if(tilestate.get(rpos).gettype()!=tiletype.Exit)
			this.tilestate.replace(rpos, new Tile(rpos,tiletype.Gemstone,sprite));
	  }
   }
   
   /**
    * places the entity in a random available position. checks that no enemy gets moved on top of the player and moves the camera focus on the player when it moves it.
    * @param e
    * an entity to be placed
    */
   public void placeEntity(Entity e) {
		Random r= new Random();
		int a=r.nextInt(rooms.size());
		Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
		e.setX(rpos.x);
		e.setY(rpos.y);
		e.setBox(new AABB(rpos,1,2));
		if(e.getID()==ID.Enemy) {
			Enemy tempen = (Enemy) e;
			if(tempen.getBox().collides(tempen.getPlayerparameter().getBox())){
				placeEntity(e);
			}
		}
		if(e.getID()==ID.Player) {
			if(this.tilestate.get(new Point(e.getX(),e.getY())).gettype()!= tiletype.ON) {
				placeEntity(e);
			}
			this.setCamera(e);
		}
			
	}
    /**
     * moves the camera on the entity so the view results centered if possible
     * @param e
     * an entity, usually the player
     */
   public void setCamera(Entity e) {
		if(e.getX()-screenw/(tilesize*2) >0 && e.getX()+screenw/(tilesize*2)<width/tilesize) {
			this.offsetX=e.getX()-screenw/64;
		}
			else if (e.getX()-screenw/(tilesize*2) <=0) {this.offsetX=0;}
			else if (e.getX()+screenw/(tilesize*2)>=width/tilesize) {this.offsetX=width/tilesize -screenw/tilesize;}
			if(e.getY()-screenh/(tilesize*2) >0 && e.getY()+screenh/(tilesize*2) < height/tilesize) {
				this.offsetY=e.getY()-screenh/64;
			}
			else if(e.getY()-screenh/(tilesize*2) <=0) {this.offsetY=0;}
			else if(e.getY()+screenh/(tilesize*2) >=height/tilesize) {this.offsetY= height/tilesize-screenh/tilesize;}
			System.out.println("x:"+offsetX+"y:"+offsetY);
		
	}

   @Override
   public void render(Graphics2D g) {
	   g.setColor(Color.gray);
	   for(int i =0;i<screenw/tilesize;i++) {
		   for(int j=0;j <screenh/tilesize;j++) {
				   g.drawImage(tilestate.get(new Point(i+offsetX,j+offsetY)).getImg(),i*tilesize, j*tilesize,null);
			   
		   }
	   }
	   
   }
   
   public int getOffsetX() {return this.offsetX;}
   public int getOffsetY() {return this.offsetY;}
   public void setOffsetX(int x) {this.offsetX=x;}
   public void setOffsetY(int y) {this.offsetY=y;}
   public int getScreenw() {return this.screenw;}
   public int getScreenh() {return this.screenh;}
   public void setScreenw(int x) {this.screenw=x;}
   public void setScreenh(int y) {this.screenh=y;}
   public int getWidth() {return this.width;}
   public int getHeight() {return this.height;}
   public HashMap <Point,Tile> getMap(){return this.tilestate;}
	

	/**
	 * updates the offset of the map view
	 */
	public void moveCam(int x,int y) {
		this.velX=x;
		this.velY=y;
		if(offsetX+velX<0 || offsetX+velX>width/tilesize-screenw/tilesize) {velX=0;}
		if(offsetY+velY<0 || offsetY+velY>height/tilesize-screenh/tilesize) {velY=0;}
		this.setOffsetX(offsetX+=velX);
		this.setOffsetY(offsetY+=velY);
		velX=0;
		velY=0;
		// TODO Auto-generated method stub
		
	}
	/**
	 * sets a tile to ON (called after picking up objects)
	 * @param p
	 * the position of the tile
	 */
	public void setTile(Point p) {
		tilestate.replace(p, new Tile(p,tiletype.ON,sprite));
	}

	/**
	 * used when creating a floor to choose the right image for a given tile
	 * @param p
	 * the point of the tile on the map
	 */
	private void choosetile(Point p) {
		corner corner= util.cornercheck(tilestate, p);
	  if(corner== mapandtiles.corner.INS) {
		  tilestate.get(p).setImg(sprite.grabImage(3, 9, 32, 32));
	  }
	  if(corner== mapandtiles.corner.CR) {
		  tilestate.get(p).setImg(sprite.grabImage(2, 8, 32, 32));
	  }
	  if(corner== mapandtiles.corner.CL) {
		  tilestate.get(p).setImg(sprite.grabImage(1, 8, 32, 32));
	  }
	  if(corner== mapandtiles.corner.CT) {
		  tilestate.get(p).setImg(sprite.grabImage(4, 8, 32, 32));
	  }
	  if(corner== mapandtiles.corner.CB) {
		  tilestate.get(p).setImg(sprite.grabImage(3, 8, 32, 32));
	  }
	  if(corner== mapandtiles.corner.CO) {
		  tilestate.get(p).setImg(sprite.grabImage(2, 9, 32, 32));
	  }
	  if(corner== mapandtiles.corner.CV) {
		  tilestate.get(p).setImg(sprite.grabImage(1, 9, 32, 32));
	  }
	  if(corner== mapandtiles.corner.BL) {
		  tilestate.get(p).setImg(sprite.grabImage(4, 7, 32, 32));
	  }
	  if(corner== mapandtiles.corner.BR) {
		  tilestate.get(p).setImg(sprite.grabImage(3, 7, 32, 32));
	  }
	  if(corner == mapandtiles.corner.TL) {
		  tilestate.get(p).setImg(sprite.grabImage(2, 7, 32, 32));
	  }
	  if(corner== mapandtiles.corner.TR) {
		  tilestate.get(p).setImg(sprite.grabImage(1, 7, 32, 32));
	  }
	  if(corner == mapandtiles.corner.E) {
		  tilestate.get(p).setImg(sprite.grabImage(4, 5, 32, 32));
	  }
	  if(corner == mapandtiles.corner.N) {
		  tilestate.get(p).setImg(sprite.grabImage(2, 5, 32, 32));
	  }
	  if(corner == mapandtiles.corner.S) {
		  tilestate.get(p).setImg(sprite.grabImage(1, 5, 32, 32));
	  }
	  if(corner == mapandtiles.corner.W) {
		  tilestate.get(p).setImg(sprite.grabImage(3, 5, 32, 32));
	  }
	  if(corner == mapandtiles.corner.SW) {
		  tilestate.get(p).setImg(sprite.grabImage(3, 6, 32, 32));
	  }
	  if(corner == mapandtiles.corner.NW) {
		  tilestate.get(p).setImg(sprite.grabImage(1, 6, 32, 32));
	  }
	  if(corner == mapandtiles.corner.SE) {
		  tilestate.get(p).setImg(sprite.grabImage(4, 6, 32, 32));
	  }
	  if(corner == mapandtiles.corner.NE) {
		  tilestate.get(p).setImg(sprite.grabImage(2, 6, 32, 32));
	  }
		
	}
	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}





   
}
