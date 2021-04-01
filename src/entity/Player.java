package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import game.ID;
import mapandtiles.Floor;
import utilities.AABB;

public class Player extends Entity {

	private Floor playerFloor;
	boolean flag;
	BufferedImage img = null;{
		try {
			img = ImageIO.read(new File("data/zelda.png"));
		} catch(IOException e){ }
	}

	public Player(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence, Floor f) {
		super(x, y, id, level, hp, attack, magic_attack, defence);
		this.playerFloor = f;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		Point pred = new Point(x+velX,y+velY);

		x+=velX;
		y+=velY;
		box.setpos(new Point(x,y));
		velX=0;
		velY=0;
		
		if(this.getHp() < this.getMax_hp() ) {
			this.setHp(this.getHp()+1);
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.GREEN);
		
		if(this.getHp() > 0) {
			if(this.getHp()==this.getHp()) {
				g.fillRect((x-playerFloor.getOffsetX()*32), 
						(y-playerFloor.getOffsetY()*32), 
						30, 10);
			}
			else if ( this.getHp()/this.getHp() <= 2)
			{
				g.setColor(Color.orange);
				g.fillRect((x-playerFloor.getOffsetX())*32,
						(y-playerFloor.getOffsetY())*32-1, 
						(this.getHp()*30)/this.getHp(), 10);
			}
			else if (this.getHp()/this.getHp() <= 3)
			{
				g.setColor(Color.red);
				g.fillRect((x-playerFloor.getOffsetX())*32, 
						(y-playerFloor.getOffsetY())*32-1, 
						(this.getHp()*30)/this.getHp(), 10);
			}
		}
		
		g.drawImage(img,(x-playerFloor.getOffsetX())*32,
				(y-playerFloor.getOffsetY())*32,null);
        g.setColor(Color.black);
        g.draw(getBounds()); g.setColor(Color.BLACK);
       
	}

	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		
		int e = key.getKeyCode();
		
		switch (e) {
		case KeyEvent.VK_W:
			this.setvelY(-32);
			//this.setvelX(0);
			this.move();
			break;
			
		case KeyEvent.VK_A:
			this.setvelX(-32);
			//this.setvelY(0);
			this.move();
			break;
			
		case KeyEvent.VK_S:
			this.setvelY(32);
			//this.setvelX(0);
			this.move();
			break;
			
		case KeyEvent.VK_D:
			this.setvelX(32);
			//this.setvelY(0);
			this.move();
			break;
		}
		
	}

	public Rectangle getBounds()
	{
		return new Rectangle((x-playerFloor.getOffsetX())*32, 
				(y-playerFloor.getOffsetY())*32, 
				img.getWidth(null), 
				img.getHeight(null));
	}
	

}
