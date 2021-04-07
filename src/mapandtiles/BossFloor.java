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

import javax.imageio.ImageIO;

import entity.Entity;
import game.ID;
import utilities.AABB;
import utilities.SpriteSheet;

public class BossFloor extends AbsFloor {

	private int level;
	private HashMap<Point,Tile> tilestate= new HashMap<>();
	private int tilesize=32;
	private int screenw,screenh;
	private int width;
	private int height;
	private int offsetX=0;
	private int offsetY=0;
	SpriteSheet sprite;
	private int border =2;
	public BossFloor(int l, int w, int h,int screenw,int screenh) {
		super(w, h, ID.Floor);
		   this.level=l;
		   this.height=screenh;
		   this.width=screenw;
		   this.screenw=screenw;
		   this.screenh=screenh;
		   try 
			{
				sprite = new SpriteSheet(ImageIO.read(new File("data/tiles.png")));
				
			}	catch(IOException e) 
				{
				
				}
		   bossfloorGenner(this.width,this.height);
		// TODO Auto-generated constructor stub
	}
	void bossfloorGenner(int w,int h) {
		 for(int i=border;i<w/tilesize-border;i++) {
			   for(int j=border;j<h/tilesize-border;j++) {
				   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.ON,sprite));
			   }
		   }
		 for(int i=0;i<width/tilesize;i++) {
			   for(int j=0;j<height/tilesize;j++) {
				   if(!(tilestate.containsKey(new Point(i,j))))
			   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
		   }
			   }
		   
	   }
	 
	void exitCreate(Point p) {
		this.tilestate.replace(p, new Tile(p,tiletype.Exit,sprite));
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
    public HashMap<Point, Tile> getMap(){return this.tilestate;}
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.gray);
		   for(int i =0;i<width/tilesize;i++) {
			   for(int j=0;j <height/tilesize;j++) {
				     g.drawImage(tilestate.get(new Point(i,j)).getImg(),i*tilesize, j*tilesize,null);
				   
			   }
		   }

		// TODO Auto-generated method stub
		
	}
	
	public void placeEntity(Entity e) {
		if(e.getID()== ID.Player){
		e.setX(screenw/(tilesize*2));
	    e.setY(screenh/tilesize-10);
	    e.setBox(new AABB(new Point(screenw/(tilesize*2),screenh/tilesize-10),1,2));}
		if (e.getID()==ID.Boss) {
			e.setX(screenw/(tilesize*2));
			e.setY(10);
			e.setBox(new AABB(new Point(screenw/(tilesize*2),10),6,4));
		}
	}
	public void moveCam(int x,int y) {}

	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		// TODO Auto-generated method stub
		
	}

}
