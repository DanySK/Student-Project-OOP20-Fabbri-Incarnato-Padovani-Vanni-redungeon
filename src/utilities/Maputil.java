package utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collector;

import mapandtiles.Tile;
import mapandtiles.tiletype;

public class Maputil {
	public Maputil() {}
   public enum corner{
	   INS, //insulated tile
	   CL,  
	   CR,
	   CT,
	   CB,
	   CO,
	   CV,
	   TR,
	   TL,
	   BR,
	   BL,
	   N,
	   S,
	   W,
	   E,
	   NW,
	   NE,
	   SE,
	   SW;
   }
   //checks for the adjacent tiles to determine the type of wall that has to be drawn
   public corner cornercheck(HashMap<Point,Tile> t,Point p) {
	 ArrayList<Point> neartiles= new ArrayList<Point>();
	 Point  p00= new Point(p.x-1,p.y-1);
	 neartiles.add(p00);
	 Point p01= new Point(p.x,p.y-1);
	 neartiles.add(p01);
	 Point p02= new Point(p.x+1,p.y-1);
	 neartiles.add(p02);
	 Point p10= new Point(p.x-1,p.y);
	 neartiles.add(p10);
	 Point p12=new Point(p.x+1,p.y);
	 neartiles.add(p12);
	 Point p20= new Point(p.x-1,p.y+1);
	 neartiles.add(p20);
	 Point p21=new Point(p.x,p.y+1);
	 neartiles.add(p21);
	 Point p22=new Point(p.x+1,p.y+1);
	 neartiles.add(p22);
	if( neartiles.stream().allMatch(e->t.get(e).gettype()!=tiletype.OFF)) {
		return corner.INS;
	}
	else if(t.get(p00).gettype()!=tiletype.OFF && t.get(p01).gettype()!=tiletype.OFF && t.get(p10).gettype()!=tiletype.OFF && t.get(p20).gettype()!=tiletype.OFF && t.get(p21).gettype()!=tiletype.OFF) {
		return corner.CL;
	}
	else if(t.get(p01).gettype()!=tiletype.OFF && t.get(p02).gettype()!=tiletype.OFF && t.get(p12).gettype()!=tiletype.OFF && t.get(p22).gettype()!=tiletype.OFF && t.get(p21).gettype()!=tiletype.OFF) {
		return corner.CR;
	}
	else if (t.get(p10).gettype()!=tiletype.OFF && t.get(p00).gettype()!=tiletype.OFF && t.get(p01).gettype()!=tiletype.OFF && t.get(p02).gettype()!=tiletype.OFF && t.get(p12).gettype()!=tiletype.OFF) {
		return corner.CT;
	}
	else if(t.get(p10).gettype()!=tiletype.OFF && t.get(p20).gettype()!=tiletype.OFF && t.get(p21).gettype()!=tiletype.OFF && t.get(p22).gettype()!=tiletype.OFF && t.get(p12).gettype()!=tiletype.OFF) {
		return corner.CB;
	}
	else if(t.get(p20).gettype()!=tiletype.OFF && t.get(p21).gettype()!=tiletype.OFF && t.get(p10).gettype()!=tiletype.OFF) {
		 return corner.TR;
	 }
	 else if(t.get(p12).gettype()!=tiletype.OFF && t.get(p21).gettype()!=tiletype.OFF && t.get(p22).gettype()!=tiletype.OFF) {
		 return corner.TL;
	 }
	 else if(t.get(p01).gettype()!=tiletype.OFF && t.get(p12).gettype()!=tiletype.OFF && t.get(p02).gettype()!=tiletype.OFF) {
		 return corner.BL;
	 }
	 else if(t.get(p00).gettype()!=tiletype.OFF && t.get(p10).gettype()!=tiletype.OFF&&t.get(p01).gettype()!=tiletype.OFF) {
		 return corner.BR;
	 }
	 else if(t.get(p01).gettype()!=tiletype.OFF && t.get(p21).gettype()!=tiletype.OFF) {
		 return corner.CO;
	 }
	 else if(t.get(p10).gettype()!=tiletype.OFF && t.get(p12).gettype()!=tiletype.OFF) {
		 return corner.CV;
	 }
	 else if(t.get(p01).gettype()!=tiletype.OFF) {
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
