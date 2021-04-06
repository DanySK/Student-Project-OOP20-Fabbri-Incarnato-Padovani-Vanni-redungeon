package mapandtiles;

import java.awt.Point;
import java.util.HashMap;

import entity.Entity;
import game.*;
import utilities.SpriteSheet;

public abstract class AbsFloor extends GameObject {
	
	private HashMap<Point,Tile> tilestate= new HashMap<>();
	private int level;
	private int screenw,screenh;
	private int width;
	private int height;
	private int offsetX=0;
	private int offsetY=0;
	SpriteSheet sprite;
	

	public AbsFloor(int x, int y, ID id) {
		super(x, y, ID.Floor);
		// TODO Auto-generated constructor stub
	}
    public void placeEntity(Entity e) {};
	public void moveCam(int x,int y) {};
	public HashMap<Point,Tile> getMap(){return tilestate;}
	public int getOffsetX() {return this.offsetX;}
	public int getOffsetY() {return this.offsetY;}
}
