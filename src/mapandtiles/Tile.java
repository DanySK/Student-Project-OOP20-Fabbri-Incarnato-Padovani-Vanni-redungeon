package mapandtiles;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import game.*;

public class Tile {
	private tiletype t;
	BufferedImage img;
	
	public Tile(Point p,tiletype t) {
		this.t= t;
		
		
	}
	public void setSprite(File f) {
		
	}
	public BufferedImage getImg() {return this.img;}
	public void settype(tiletype t) {
		this.t=t;
	}
	public tiletype gettype() {return this.t;}
}

