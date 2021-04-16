package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import mapandtiles.*;
import game.*;
import utilities.AABB;
import utilities.CustomFontUtil;
import utilities.SpriteSheet;



public class Player extends Entity {
	boolean flag;
	long timer;
	long lastime;
	int column = 0;
    Inventory inventory;
	int experience;
	int maxExperience=50;
	/*CustomFontUtil customFont = new CustomFontUtil(true, 15);
	Font fonts=this.customFont.getCustomFont();*/
	
	public Player(int x, int y, ID id, CombatSystem combat, int level, int hp, int attack, int magic_attack, int defence, AbsFloor floor) throws IOException {
		super(x, y, id, combat, level, floor);
		this.setHp(hp);
		this.setMax_hp(hp);
		this.setAttack(attack);
		this.setMagic_Attack(magic_attack);
		this.setDefence(defence);
         this.inventory= new Inventory();
         setAttacking(false);
		hp_bar = ImageIO.read(new File("data/hpbar.png"));
		sprite = new SpriteSheet(ImageIO.read(new File("data/player.png")));
		this.setBox(new AABB(new Point(this.x, this.y), 1, 2));
		this.img_matrix = new BufferedImage[4][3];
		for(int row=0; row<4; row++)
		{
			for(int column=0; column<3; column++)
			{
				img_matrix[row][column] = sprite.grabImage(column+1, row+1, 34, 60); 
			}
		}

		//this.setMaxExp();
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
		else
		{	
			if(this.getHp() < this.getMax_hp() ) {
				this.setHp(this.getHp()+this.getLevel());
			}			
			int opvelx,opvely;
			if(this.getX()-this.getFloor().getScreenw()/(32*2) >0 && this.getX()+this.getFloor().getScreenw()/(32*2)<this.getFloor().getWidth()/32) {
				opvelx=this.velX;
			}
			else {opvelx=0;}
			if(this.getY()-this.getFloor().getScreenh()/(32*2) >0 && this.getY()+this.getFloor().getScreenh()/(32*2)<this.getFloor().getHeight()/32) {
				opvely=this.velY;
			}
			else {opvely=0;}
			this.getFloor().moveCam(opvelx,opvely);
			x+=velX;
			y+=velY;
			box.setpos(new Point(x,y));
			if(this.getFloor().getMap().get(this.box.getpos()).gettype()==tiletype.Heal) {
				this.setHp(this.getMax_hp());
				this.getFloor().setTile(this.getBox().getpos());

				velX=0;
				velY=0;
			}
			if(this.getFloor().getMap().get(this.box.getpos()).gettype()==tiletype.Key) {
				this.inventory.setKey(true);
				this.getFloor().setTile(this.getBox().getpos());

				velX=0;
				velY=0;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		
		// Experience bar
		g.setColor(Color.blue);
		g.fillRect((x-getFloor().getOffsetX())*32,
				(y-getFloor().getOffsetY()+1)*32,
				(this.getActualExp()*54)/this.getMaxExp(), 8);
		
		g.drawImage(null,(x-getFloor().getOffsetX())*32,
				(y-getFloor().getOffsetY()-1)*32,null);
		
		
		
		g.setColor(Color.GREEN);
		
		if(this.getHp() > 0) {
			if(this.getMax_hp()/this.getHp() < 2) {
				
				g.fillRect((x-getFloor().getOffsetX())*32,
						(y-getFloor().getOffsetY()-1)*32-11, 
						(this.getHp()*54)/this.getMax_hp(), 14);
				
				g.drawImage(hp_bar,(x-getFloor().getOffsetX())*32-14,
						(y-getFloor().getOffsetY()-2)*32+19,null);
				
				
			}
			else if ( this.getMax_hp()/this.getHp() <= 4 && this.getMax_hp()/this.getHp() >= 2)
			{		
				g.setColor(Color.orange);
				g.fillRect((x-getFloor().getOffsetX())*32,
						(y-getFloor().getOffsetY()-1)*32-11, 
						(this.getHp()*54)/this.getMax_hp(), 14);
				
				g.drawImage(hp_bar,(x-getFloor().getOffsetX())*32-14,
						(y-getFloor().getOffsetY()-2)*32+19,null);
			}
			else if (this.getMax_hp()/this.getHp() > 4)
			{
				g.setColor(Color.red);
				g.fillRect((x-getFloor().getOffsetX())*32, 
						(y-getFloor().getOffsetY()-1)*32-11, 
						(this.getHp()*54)/this.getMax_hp(), 14);
				
				g.drawImage(hp_bar,(x-getFloor().getOffsetX())*32-14,
						(y-getFloor().getOffsetY()-2)*32+19,null);
			}
		}
		
		g.drawImage(img,(x-getFloor().getOffsetX())*32,
				(y-getFloor().getOffsetY()-1)*32,null);
        g.setColor(Color.black);
        //g.draw(getBounds()); g.setColor(Color.BLACK);
        
        String level;
        if(this.getLevel()<10) {
        	level="0"+this.getLevel();
        }
        else {
        	level=""+this.getLevel();
        }
        
        g.drawString(level, (x-getFloor().getOffsetX())*32-11, 
				(y-getFloor().getOffsetY()-2)*32+32);
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
					
 			case KeyEvent.VK_LEFT:
 				this.changeDirection(Direction.Left);
				break;
 					
 			case KeyEvent.VK_UP:
 				this.changeDirection(Direction.Up);
 				break;
 					
 			case KeyEvent.VK_DOWN:
 				this.changeDirection(Direction.Down);
 				break;
 					
 			case KeyEvent.VK_RIGHT:
 				this.changeDirection(Direction.Right);
 				break;
 				     
			case KeyEvent.VK_J:
				this.setAttacking(true);
				combat.PlayerAttack();
				/*attack case*/
				break;
				
			case KeyEvent.VK_K:
					
				//magic attack
				break;
		}
			
		collisions.add(box);
	}
	
	private void levelUp(){
		this.setLevel(this.getLevel()+1);
		
		int expOverflow=this.getActualExp()-this.getMaxExp();
		if(expOverflow > 0) {
			this.setExperience(expOverflow);
		}
		else {
			this.setExperience(0);
		}
		
		this.setMaxExp();
		//this.setMaxExp();
		
		/*stats augm.*/
		
		Random rng = new Random();
		int minRange=3;
		int maxRange=6;
		int range=maxRange-minRange+1;
		
		this.setAttack(this.getAttack() + (rng.nextInt(range) + minRange) );
		
		this.setDefence(this.getDefence() + (rng.nextInt(range) + minRange) );
		
		this.setMagic_Attack(this.getMagic_attack() + (rng.nextInt(range) + minRange) );
		
		this.setMax_hp(this.getMax_hp() + ((rng.nextInt(range) + minRange)*10) );
		
		this.setHp(this.getMax_hp());
	}
	
	public void addExp(int additionalExp) {
		this.experience+=additionalExp;
		if(this.experience >= this.maxExperience) {
			this.levelUp();
		}
	}
	
	private void setMaxExp() {
		int newMaxExp=this.maxExperience/2;
		this.maxExperience=this.maxExperience+newMaxExp;
	}
	
	private int getActualExp() {
		return this.experience;
	}
	private int getMaxExp() {
		return this.maxExperience;
	}
	
	private void setExperience(int exp) {
		this.experience = exp;
	}
	public boolean isOut() {if(this.getFloor().getMap().get(this.box.getpos()).gettype()==tiletype.Exit || (this.getFloor().getMap().get(this.box.getpos()).gettype()==tiletype.LockedExit &&
			this.inventory.hasKey())) {
		this.inventory.setKey(false);
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
