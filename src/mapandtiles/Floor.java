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
import utilities.Maputil;
import utilities.Maputil.corner;

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
   public Floor(int l,int w, int h,int screenw,int screenh) {
	   super(w, h, ID.Floor);
	   this.level=l;
	   this.height=h;
	   this.width=w;
	   this.screenw=screenw;
	   this.screenh=screenh;
	   int floorseed=1;
	   if(l>5 && l<=10) {
		   floorseed=2;
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

   //generate the pesudorandom floor calling roomscreate
   void floorGenner(int w,int h) {
	   roomsCreate();
	   exitCreate();
	   healCreate();
	   for(int i=0;i<w/tilesize;i++) {
		   for(int j=0;j<h/tilesize;j++) {
			   if(!(tilestate.containsKey(new Point(i,j))))
		   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
	   }
	   }
	   for(int p=1;p<width/tilesize-1;p++) {
		   for(int l=1;l<height/tilesize-1;l++) {
			   if(this.tilestate.get(new Point(p,l)).gettype()==tiletype.OFF) {
			  corner corner= util.cornercheck(tilestate, new Point(p,l));
			  if(corner== corner.INS) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(2, 2, 32, 32));
			  }
			  if(corner== corner.CR) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(2, 8, 32, 32));
			  }
			  if(corner== corner.CL) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(1, 8, 32, 32));
			  }
			  if(corner== corner.CT) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(4, 8, 32, 32));
			  }
			  if(corner== corner.CB) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(3, 8, 32, 32));
			  }
			  if(corner== corner.CO) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(2, 9, 32, 32));
			  }
			  if(corner== corner.CV) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(1, 9, 32, 32));
			  }
			  if(corner== corner.BL) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(4, 7, 32, 32));
			  }
			  if(corner== corner.BR) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(3, 7, 32, 32));
			  }
			  if(corner == corner.TL) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(2, 7, 32, 32));
			  }
			  if(corner== corner.TR) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(1, 7, 32, 32));
			  }
			  if(corner == corner.E) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(4, 5, 32, 32));
			  }
			  if(corner == corner.N) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(2, 5, 32, 32));
			  }
			  if(corner == corner.S) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(1, 5, 32, 32));
			  }
			  if(corner == corner.W) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(3, 5, 32, 32));
			  }
			  if(corner == corner.SW) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(3, 6, 32, 32));
			  }
			  if(corner == corner.NW) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(1, 6, 32, 32));
			  }
			  if(corner == corner.SE) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(4, 6, 32, 32));
			  }
			  if(corner == corner.NE) {
				  tilestate.get(new Point(p,l)).setImg(sprite.grabImage(2, 6, 32, 32));
			  }
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
   //create the floor exit choosing a random room and a random available tile in that room
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
   //initializes the starting position of an Entity on a random valid tile. If the entity is the player call set camera
   public void placeEntity(Entity e) {
		Random r= new Random();
		int a=r.nextInt(rooms.size());
		Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
		e.setX(rpos.x);
		e.setY(rpos.y);
		e.setBox(new AABB(rpos,1,2));
		if(e.getID()==ID.Player) {
			this.setCamera(e);
		}
			
	}
   //called by place entity to initialize the camera offsets to be centered on the player if possible
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
	

	
	//moves the cam if possible
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
	public void setTile(Point p) {
		tilestate.replace(p, new Tile(p,tiletype.ON,sprite));
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
