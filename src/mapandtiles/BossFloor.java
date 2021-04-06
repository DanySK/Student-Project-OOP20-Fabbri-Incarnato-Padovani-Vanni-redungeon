package mapandtiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
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
	private int border =4;
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
		 for(int i=border;i<width/tilesize-border;i++) {
			   for(int j=border;j<height/tilesize-border;j++) {
				   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.ON,sprite));
			   }
		   }
		   for(int i=0;i<border;i++) {
			   for(int j=0;j<h/tilesize;j++) {
				   
			   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
		   }
		   }
		   for(int i=0;i<w/tilesize;i++) {
			   for(int j=0;j<border;j++) {
				   
			   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
		   }
		   }
		   for(int i=width/tilesize-border;i<w/tilesize;i++) {
			   for(int j=0;j<height/tilesize;j++) {
				   
			   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
		   }
		   }
		   for(int i=0;i<w/tilesize;i++) {
			   for(int j=height/tilesize-border;j<height/tilesize;j++) {
				   
			   this.tilestate.put(new Point(i, j), new Tile(new Point(i, j),tiletype.OFF,sprite));
		   }
		   }
		   
	   }

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

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

	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		// TODO Auto-generated method stub
		
	}

}
