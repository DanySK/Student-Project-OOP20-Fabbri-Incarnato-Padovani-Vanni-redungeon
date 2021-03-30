package mapandtiles;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

import game.*;

public class Floor extends GameObject {
	
	private int level;
	private int screenw,screenh;
	private int width;
	private int height;
	private HashMap<Point,Tile> tilestate= new HashMap<>();
	private int tilesize=32;
	
	
   public Floor(int l,int w, int h,int screenw,int screenh) {
	   super(w, h, ID.Floor);
	   this.level=l;
	   this.height=h;
	   this.width=w;
	   this.screenw=screenw;
	   this.screenh=screenh;
	   this.floorGenner(this.width,this.height);
   }

   
   void floorGenner(int w,int h) {
	   
	   
   }
   

   @Override
   public void render(Graphics2D g) {
	   
   }

	public HashMap <Point,Tile> getMap(){return this.tilestate;}
	
	
	@Override
	public void tick() {
		
		//cambio degli offset y e x per individuare nella matrice
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}





   
}
