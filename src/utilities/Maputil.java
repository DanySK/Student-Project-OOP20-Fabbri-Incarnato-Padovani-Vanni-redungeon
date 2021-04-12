package utilities;

import java.awt.Point;
import java.util.HashMap;

import mapandtiles.Tile;
import mapandtiles.tiletype;

public class Maputil {
	public Maputil() {}
   public enum corner{
	   N,
	   S,
	   W,
	   E,
	   NW,
	   NE,
	   SE,
	   SW;
   }
   
   public corner cornercheck(HashMap<Point,Tile> t,Point p) {
	 Point  p00= new Point(p.x-1,p.y-1);
	 Point p01= new Point(p.x,p.y-1);
	 Point p02= new Point(p.x+1,p.y-1);
	 Point p10= new Point(p.x-1,p.y);
	 Point p11=p;
	 Point p12=new Point(p.x+1,p.y);
	 Point p20= new Point(p.x-1,p.y+1);
	 Point p21=new Point(p.x,p.y+1);
	 Point p22=new Point(p.x+1,p.y+1);
	
	  if(t.get(p01).gettype()!=tiletype.OFF) {
		 return corner.S;
	 }
	 else if(t.get(p10).gettype()!=tiletype.OFF) {
		 return corner.E;
	 }
	 else if(t.get(p12).gettype()!=tiletype.OFF) {
		 return corner.W;
	 }
	 else if(t.get(p21).gettype()!=tiletype.OFF) {
		 return corner.N;
	 }
	 else if(t.get(p20).gettype()!=tiletype.OFF) {
		 return corner.NE;
	 }
	
	 else if(t.get(p02).gettype()!=tiletype.OFF) {
		 return corner.SW;
	 }
	 else if(t.get(p22).gettype()!=tiletype.OFF) {
		 return corner.NW;
	 }
	 else if(t.get(p00).gettype()!=tiletype.OFF )
		   return corner.SE;
	 else return null;
}
   }
