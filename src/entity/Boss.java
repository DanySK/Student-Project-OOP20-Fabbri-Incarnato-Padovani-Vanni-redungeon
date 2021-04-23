package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import utilities.*;
import game.*;
import mapandtiles.*;

/**
 * Extended from Entity, is a particular type of Enemy with some different function
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see game.CombatSystem
 *
 */
public class Boss extends Entity{
	
	int column=0;
	Player player_parameter;
	AABB box1;
	boolean collide;
	Clip clip;
	AudioInputStream audio;
	long timer;
	long lastime;
	int hp_barx;
	int hp_bary;

	private int expGuaranteed;
	
	int flames_number;
	BufferedImage flame_img_matrix[][];
	BufferedImage flame_img;
	SpriteSheet sprite2;
	
	/**
	 * Extended from Entity is an Enemy with particular statistic
	 */
	List<AABB> flames = new ArrayList<AABB>();
	
	/**
	 * Constructor
	 * 
	 * @param x 		horizontal position
	 * @param y 		vertical position
	 * @param id 	 	game.ID
	 * @param combat	Type of combat
	 * @param level		Used for stats modifier
	 * @param floor		Used for positioning
	 * @param player	Used for damage and statistics
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public Boss(int x, int y, ID id, CombatSystem combat, int level, BossFloor floor, Player player) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
		super(x, y, id, combat, level, floor);
		// TODO Auto-generated constructor stub
		hp_bar = ImageIO.read(new File("data/bosshpbar.png"));
		hp_barx = x-getFloor().getOffsetX();
		hp_bary = y-getFloor().getOffsetY();

		Random rng = new Random();
		
		switch(rng.nextInt(3))
		{
			case 0:
				this.
				sprite2 = new SpriteSheet(ImageIO.read(new File("data/flame.png")));
				sprite = new SpriteSheet(ImageIO.read(new File("data/boss1.png")));
				this.setAttribute(Attribute.Fire);
				break;
				
			case 1:
				sprite2 = new SpriteSheet(ImageIO.read(new File("data/waternado.png")));
				sprite = new SpriteSheet(ImageIO.read(new File("data/boss3.png")));
				this.setAttribute(Attribute.Water);
				break;
				
			case 2:
				sprite2 = new SpriteSheet(ImageIO.read(new File("data/leafnado.png")));
				sprite = new SpriteSheet(ImageIO.read(new File("data/boss2.png")));
				this.setAttribute(Attribute.Grass);
				break;
		}
		
		this.flame_img_matrix = new BufferedImage[1][3];
		for(int column=0; column<3; column++)
		{
			flame_img_matrix[0][column] = sprite2.grabImage(column+1, 1, 32, 32); 
		}
		
		this.setBox(new AABB(new Point(this.x, this.y), 6, 4));
		this.player_parameter = player;
		this.img_matrix = new BufferedImage[4][3];
		for(int row=0; row<4; row++)
		{
			for(int column=0; column<3; column++)
			{
				img_matrix[row][column] = sprite.grabImage(column+1, row+1, 192, 128); 
			}
		}
		
		flames_number = rng.nextInt(7)+10;
		
		for(int i=0; i<flames_number; i++)
		{
			AABB flame = new AABB(new Point(0,0), 1, 1);
			floor.placeFlames(flame);
			flames.add(flame);
		}
		
		
		clip = AudioSystem.getClip();
		audio= AudioSystem.getAudioInputStream(new File("data/bonk.wav"));
		clip.open(audio);
		
		this.setDirection(Direction.Down);
		
		img = img_matrix[0][1];

		lastime = System.currentTimeMillis();
		timer = 0;
		
		this.setMax_hp(500);
		this.setExpGuaranteed(30);
		this.augmStat();
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
					img = img_matrix [3][this.column];
					break;
					
				case Down:
					img = img_matrix [2][this.column];
					break;
					
				case Right:
					img = img_matrix [1][this.column];
					break;
					
				case Up:
					img = img_matrix [0][this.column];
					break;
			}
			
			flame_img = flame_img_matrix[0][column];
			
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
		// TODO Auto-generated method stub
		
		x+=velX;
		y+=velY;
		box.setpos(new Point(x,y));
		velX=0;
		velY=0;
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.green);
		
		//with a proportion the render function set the hp of the monster
		
		if(this.getHp() > 0) {
			if(this.getMax_hp()/this.getHp() < 2) {
				
				g.fillRect(hp_barx*32+48, 
						hp_bary*32+10, 
						(this.getHp()*906)/this.getMax_hp(), 44);
				
			}
			else if ( this.getMax_hp()/this.getHp() <= 4 && this.getMax_hp()/this.getHp() >= 2)
			{		
				g.setColor(Color.orange);
				g.fillRect(hp_barx*32+48, 
						hp_bary*32+10, 
						(this.getHp()*906)/this.getMax_hp(), 44);
			}
			else if (this.getMax_hp()/this.getHp() > 4)
			{
				g.setColor(Color.red);
				g.fillRect(hp_barx*32+48, 
						hp_bary*32+10, 
						(this.getHp()*906)/this.getMax_hp(), 44);
			}
		}
		
		for(AABB x : this.flames)
		{
			g.drawImage(flame_img,(x.getX())*32,
					(x.getY())*32,null);
		}

		g.drawImage(hp_bar,hp_barx,
				hp_barx,null);
		
		g.drawImage(img,(x-getFloor().getOffsetX())*32,
				(y-getFloor().getOffsetY()-1)*32,null);
	}
	

	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		// TODO Auto-generated method stub
		box1 = new AABB(new Point(this.getBox().getX(), getBox().getY()), 6, 4);
		collisions.remove(box);
		collide = false;
		
		/**the enemy finds the position of the player like it is in a cartesian system*/
		
		if(this.getY()+3<player_parameter.getY())
		{
			if(!(this.getFloor().getMap().get(new Point((this.x),(this.y+1))).gettype()==tiletype.OFF))
			{
				this.setDirection(Direction.Down);
				box1.sumY(1);	
				this.setvelY(1);
			}
		}
		
		if(this.getY()>player_parameter.getY())
		{
			if(!(this.getFloor().getMap().get(new Point((this.x),(this.y-1))).gettype()==tiletype.OFF))
			{
				this.setDirection(Direction.Up);
				box1.sumY(-1);	
				this.setvelY(-1);
			}
		}
		
		if(this.getX()+5<player_parameter.getX())
		{
			if(!(this.getFloor().getMap().get(new Point((this.x+1),(this.y+velY))).gettype()==tiletype.OFF))
			{
				this.setDirection(Direction.Right);
				box1.sumX(1);	
				this.setvelX(1);
			}
		}
		
		if(this.getX()>player_parameter.getX())
		{
			if(!(this.getFloor().getMap().get(new Point((this.x-1),(this.y+velY))).gettype()==tiletype.OFF))
			{	
				this.setDirection(Direction.Left);
				box1.sumX(-1);	
				this.setvelX(-1);
			}
		}
		
		//try if there are any other entity in the position where i'm going to go, if not the enemy move
		collisions.forEach(x -> {if(box1.collides(x)) {collide=true;}});
		if(!collide)
			this.move();
		
		else {
				if(box1.collides(player_parameter.getBox()))
				{
					this.combat.bossAttack(this);
					clip.loop(1);
				}
				      
		}
		
		this.moveFlames();
		
		collisions.add(box);
	}
	
	
	/**
	 * Boss special attack that moves around the map
	 */
	public void moveFlames()
	{
		Random rand = new Random();
		for(AABB i : this.flames)
		{
			boolean flag=true;
			
			while(flag)
			{
				switch(rand.nextInt(8))
				{
					//the south tile
					case 0:
						flag = this.flamesCollide(i, 0, -1);
						break;
						
					//the south-east tile
					case 1:
						flag = this.flamesCollide(i, +1, -1);
						break;
						
					//the east tile
					case 2:
						flag = this.flamesCollide(i, +1, 0);
						break;
						
					//the north-east tile	
					case 3:
						flag = this.flamesCollide(i, +1, +1);
						break;
						
					//the north tile	
					case 4:
						flag = this.flamesCollide(i, 0, +1);
						break;
						
					//the north-west tile	
					case 5:
						flag = this.flamesCollide(i, -1, +1);
						break;
						
					//the west tile	
					case 6:
						flag = this.flamesCollide(i, -1, 0);
						break;
						
					//the south-west tile
					case 7 :
						flag = this.flamesCollide(i, -1, -1);
						break;
						
				}
			}
			
		}
	}
	
	/**
	 * Collision for the flames so they never go out of the map or block with walls
	 * @param box	Collision box 
	 * @param a		horizontal modifiers
	 * @param b		vertical modifiers
	 * @return boolean
	 */
	public boolean flamesCollide(AABB box, int a, int b)
	{
		if(!(this.getFloor().getMap().get(new Point((box.getX()+a),(box.getY()+b))).gettype()==tiletype.OFF))
		{
				
				if((new AABB(new Point(box.getX()+a,box.getY()+b+1),1,1)).collides(player_parameter.getBox()))
				{
					this.combat.flamesAttack();
				}

				
				this.flames.get(this.flames.indexOf(box)).setpos(new Point(box.getX()+a, box.getY()+b));
				
				return false;
			
		}
		
		return true;
	}
	

	/**
	 * 
	 * @return the experience given to player on kill
	 */
	public int getExpGuaranteed() {
		return expGuaranteed;
	}
	
	/**
	 * Set the experience given to player on kill
	 * 
	 * @param expGuaranteed	experience modifier
	 */
	public void setExpGuaranteed(int expGuaranteed) {/*is dead true -> player_stat.setExp(expgranted)*/
		this.expGuaranteed=expGuaranteed+(this.getLevel()*10);
	}
	
	/**
	 * Used to get the boss floor
	 * @return the boss floor to generate
	 */
	public BossFloor getBossFloor() {return (BossFloor) this.getFloor();}
	

	@Override
	public void augmStat() {

		Random rng = new Random();
		int attack = ( this.player_parameter.getDefence() + ((this.getLevel()*2)-rng.nextInt(this.getLevel())) )*2;
		
		this.setAttack(attack);
		this.setMax_hp(this.getMax_hp()   + ( this.getLevel()*10 ) );
		this.setHp(this.getMax_hp());
		this.setDefence( player_parameter.getAttack() );
	}

}