package mapandtiles;

import java.awt.Point;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import mapandtiles.tiletype;
import utilities.SpriteSheet;
import game.*;

public class Tile {
	private tiletype t;
	BufferedImage img;
	SpriteSheet sprite;
	
	public Tile(Point p,tiletype t) {
		this.t= t;
		 try 
			{
				sprite = new SpriteSheet(ImageIO.read(new File("data/tiles.png")));
				
			}	catch(IOException e) 
				{
				
				}
		Random rand = new Random();
		if(this.t==tiletype.ON) {
		int int_random = rand.nextInt(4);
		
		this.img = sprite.grabImage(int_random+1, 1, 32, 32);}
		if(this.t==tiletype.OFF) {
			int int_random=rand.nextInt(2);
			this.img = sprite.grabImage(int_random+1, 2, 32, 32);
		}
		if(this.t==tiletype.Exit) {
			this.img = sprite.grabImage(1, 3, 32, 32);
		}
		
		
	}
	public void setSprite(File f) {
		
	}
	public BufferedImage getImg() {return this.img;}
	public void settype(tiletype t) {
		this.t=t;
	}
	public tiletype gettype() {return this.t;}
}

