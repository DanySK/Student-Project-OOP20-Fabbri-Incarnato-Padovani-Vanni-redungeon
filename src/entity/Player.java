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

import mapandtiles.*;
import game.ID;
import mapandtiles.Floor;
import utilities.AABB;
import utilities.SpriteSheet;

public class Player extends Entity {

	boolean flag;
	long timer;
	long lastime;
	int column = 0;

	public Player(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence, AbsFloor floor) throws IOException {
		super(x, y, id, level, hp, attack, magic_attack, defence, floor);
		sprite = new SpriteSheet(ImageIO.read(new File("data/player.png")));
		this.img_matrix = new BufferedImage[4][3];
		for(int row=0; row<4; row++)
		{
			for(int column=0; column<3; column++)
			{
				img_matrix[row][column] = sprite.grabImage(column+1, row+1, 34, 60); 
			}
		}

		
		this.setDirection(Direction.Left);
		
		img = img_matrix[0][1];
	}

	@Override
	public void tick() {
		timer += System.currentTimeMillis() - lastime;
		// TODO Auto-generated method stub
		if(timer>=1500)
		{
			switch(this.getDirection())
			{
				case Left:
					img = img_matrix [1][this.column];
					break;
					
				case Down:
					img = img_matrix [0][this.column];
					break;
					
				case Right:
					img = img_matrix [2][this.column];
					break;
					
				case Up:
					img = img_matrix [3][this.column];
					break;
			}
			
			if(this.column==2)
				this.column=0;
			else
				this.column++;
			
			lastime = System.currentTimeMillis();
			timer = 0;
		}
	}

	@Override
	public void move() {
		Point pred = new Point(x+velX,y+velY);
							
		if(new AABB(pred,1,1).collides(
				this.getFloor().getMap().get(
						new Point(
								(this.box.getpos().x+velX),
								(this.box.getpos().y+velY)
								)
						).getbox()
				) && this.getFloor().getMap().get(
						new Point(
								(this.x+velX),
								(this.y+velY)
								)
						).gettype()==tiletype.OFF) {
			
			velX=0;
			velY=0;
		}
		this.getFloor().moveCam(this.velX,this.velY);
		x+=velX;
		y+=velY;
		box.setpos(new Point(x,y));
		if(this.getFloor().getMap().get(this.box.getpos()).gettype()==tiletype.Heal) {
			this.setHp(this.getMax_hp());
			this.getFloor().setTile(this.getBox().getpos());
		}
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
				g.fillRect((x-getFloor().getOffsetX())*32, 
						(y-getFloor().getOffsetY())*32, 
						30, 10);
			}
			else if ( this.getHp()/this.getHp() <= 2)
			{
				g.setColor(Color.orange);
				g.fillRect((x-getFloor().getOffsetX())*32,
						(y-getFloor().getOffsetY())*32-1, 
						(this.getHp()*30)/this.getHp(), 10);
			}
			else if (this.getHp()/this.getHp() <= 3)
			{
				g.setColor(Color.red);
				g.fillRect((x-getFloor().getOffsetX())*32, 
						(y-getFloor().getOffsetY())*32-1, 
						(this.getHp()*30)/this.getHp(), 10);
			}
		}
		
		g.drawImage(img,(x-getFloor().getOffsetX())*32,
				(y-getFloor().getOffsetY()-1)*32,null);
        g.setColor(Color.black);
        //g.draw(getBounds()); g.setColor(Color.BLACK);
       
	}

	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		
		int e = key.getKeyCode();
		collisions.remove(box);
		flag=false;
		AABB box1;
		
		if(e==KeyEvent.VK_D && e==KeyEvent.VK_S)
		{
			System.out.println("son qua dentro coglione");
		}
		
			switch (e)
			{
				case KeyEvent.VK_W:
					box1 = new AABB(new Point(box.getX(), box.getY()-1), 1, 2);
					collisions.forEach(x -> {if(box1.collides(x)) {flag=true;}});	
					if(!flag)
					{
						this.changeDirection(Direction.Up);
						this.setvelY(-1);
						//this.setvelX(0);
						this.move();
					}
					break;
					
				case KeyEvent.VK_A:
					box1 = new AABB(new Point(box.getX()-1, box.getY()), 1, 2);
					collisions.forEach(x -> {if(box1.collides(x)) {flag=true;}});	
					if(!flag)
					{
						this.changeDirection(Direction.Left);
						this.setvelX(-1);
						//this.setvelY(0);
						this.move();
					}
					break;
					
				case KeyEvent.VK_S:
					box1 = new AABB(new Point(box.getX(), box.getY()+1), 1, 2);
					collisions.forEach(x -> {if(box1.collides(x)) {flag=true;}});	
					if(!flag)
					{
						this.changeDirection(Direction.Down);
						this.setvelY(1);
						//this.setvelX(0);
						this.move();
					}
					break;
					
				case KeyEvent.VK_D:
					box1 = new AABB(new Point(box.getX()+1, box.getY()), 1, 2);
					collisions.forEach(x -> {if(box1.collides(x)) {flag=true;}});	
					if(!flag)
					{
						this.changeDirection(Direction.Right);
						this.setvelX(1);
						//this.setvelY(0);
						this.move();
					}
					break;
					
			}
			
		collisions.add(box);
	}
	public boolean isOut() {if(this.getFloor().getMap().get(this.box.getpos()).gettype()==tiletype.Exit) {
		return true;
		}
	else return false;
	}

	/*public Rectangle getBounds()
	{
		return new Rectangle((x-getFloor().getOffsetX())*32, 
				(y-getFloor().getOffsetY())*32, 
				img.getWidth(null), 
				img.getHeight(null));
	}*/
	

}
