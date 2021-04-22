package mapandtiles;

import java.awt.Point;

import java.util.HashMap;

import entity.Entity;


public interface AbsFloor {
	

	
	public void setTile(Point p) ;
    public void placeEntity(Entity e);
	public void moveCam(int x,int y);
	public HashMap<Point,Tile> getMap();
	public int getOffsetX();
	public int getOffsetY() ;
	public int getScreenw() ;
	public int getScreenh() ;
	public int getWidth() ;
	public int getHeight();
}
