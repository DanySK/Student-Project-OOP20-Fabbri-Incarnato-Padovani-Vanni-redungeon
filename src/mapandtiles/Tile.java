package mapandtiles;

import java.awt.Point;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import utilities.*;

public class Tile {
	private AABB box;
	private tiletype t;
	BufferedImage img;
	SpriteSheet sprite;
	
	public Tile(Point p,tiletype t,SpriteSheet s) {
		this.box=new AABB(p,1,1);
		this.t= t;
		this.sprite=s;
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
		if(this.t==tiletype.Heal) {
			this.img = sprite.grabImage(1, 4, 32, 32);
		}
		if(this.t==tiletype.LockedExit) {
			this.img=sprite.grabImage(2, 3, 32, 32);
		}
		if(this.t==tiletype.Key) {
			this.img=sprite.grabImage(3, 4, 32, 32);
		}
		if(this.t==tiletype.Powerstone) {
			this.img=sprite.grabImage(4, 4, 32, 32);
		}
		if(this.t==tiletype.Teleport){
			this.img=sprite.grabImage(4, 4, 32, 32);
		}
		if(this.t==tiletype.Trap){
			this.img=sprite.grabImage(4, 4, 32, 32);
		}
		if(this.t==tiletype.Gemstone){
			this.img=sprite.grabImage(4, 4, 32, 32);
		}
		
	}
	public void setImg(BufferedImage im) {this.img=im;}
	public AABB getbox() {return this.box;}
	public void setSprite(File f) {
		
	}
	public BufferedImage getImg() {return this.img;}
	public void settype(tiletype t) {
		this.t=t;
	}
	public tiletype gettype() {return this.t;}
}

