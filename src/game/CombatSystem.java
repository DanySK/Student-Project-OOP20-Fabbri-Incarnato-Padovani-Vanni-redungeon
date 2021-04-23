package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entity.*;
import mapandtiles.tiletype;
import utilities.*;

/**
 * Class used for entity combat system
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see entity.Player
 * @see entity.Enemy
 * @see java.util.List
 * @see entity.Boss
 * @see utilities.AABB
 * @see java.awt.image.BufferedImage
 * @see javax.sound.sampled.AudioInputStream
 * 
 */
public class CombatSystem {
	
	private Player player;
	private Enemy enemy;
	private List<Enemy> enemies;
	private Boss boss;
	private AABB direction_box;
	private boolean collide;
	private BufferedImage punch_img;
	private BufferedImage flame_img;
	private AABB punch_box;
	private int dungeon_level;

	long timer;
	long lastime;
	
	AABB magic_boxes[];

	private Clip bone_sound;
	private AudioInputStream bone_audio;
	private FloatControl bone_volume;
	
	private Clip punch_sound;
	private AudioInputStream punch_audio;
	private FloatControl punch_volume;
	
	private Clip boss_sound;
	private AudioInputStream boss_audio;
	private FloatControl boss_volume;
	
	/**
	 * Constructor
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public CombatSystem(double effect) throws IOException, LineUnavailableException, UnsupportedAudioFileException
	{
		ResourceLoader resource = new ResourceLoader();
    	float dB;
		this.dungeon_level = 1; 
		enemies = new ArrayList<Enemy>();

		SpriteSheet sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("flame")));
		flame_img = sprite.grabImage(1, 1, 32, 32); 
		
		punch_img=ImageIO.read(resource.getStreamImage("punch"));
		
		bone_sound = AudioSystem.getClip();
		bone_audio= 
			    AudioSystem.getAudioInputStream(
			            new BufferedInputStream(
			                resource.getStreamAudio("bonk")
			            )
			         );
		bone_sound.open(bone_audio);
		bone_volume = (FloatControl) bone_sound.getControl(FloatControl.Type.MASTER_GAIN);
    	dB = (float) (Math.log(effect) / Math.log(10.0) * 20.0);
    	bone_volume.setValue(dB);
		
		punch_sound = AudioSystem.getClip();
		punch_audio= 
			    AudioSystem.getAudioInputStream(
			            new BufferedInputStream(
			                resource.getStreamAudio("punch")
			            )
			         );
		punch_sound.open(punch_audio);
		punch_volume = (FloatControl) punch_sound.getControl(FloatControl.Type.MASTER_GAIN);
    	dB = (float) (Math.log(effect) / Math.log(10.0) * 20.0);
    	punch_volume.setValue(dB);
    	
    	boss_sound = AudioSystem.getClip();
		boss_audio= 
			    AudioSystem.getAudioInputStream(
			            new BufferedInputStream(
			                resource.getStreamAudio("bossroar")
			            )
			         );
		boss_sound.open(boss_audio);
		boss_volume = (FloatControl) boss_sound.getControl(FloatControl.Type.MASTER_GAIN);
    	dB = (float) (Math.log(effect) / Math.log(10.0) * 20.0);
    	boss_volume.setValue(dB);
		
		timer=0;
		lastime = 0;
	}

	/**
	 * 
	 * @return enemy hot sound
	 */
	public Clip getBonk()
	{
		return bone_sound;
	}
	/**
	 * 
	 * @return player hit sound
	 */
	public Clip getPunch()
	{
		return punch_sound;
	}
	/**
	 * Add entity Player
	 * @param player
	 */
	public void addPlayer(Player player)
	{
		this.player = player;
	}
	/**
	 * Add entity enemy
	 * @param enemy
	 */
	public void addEnemy(Enemy enemy)
	{
		this.enemies.add(enemy);
	}
	public void removeEnemy(Enemy enemy)
	{
		this.enemies.remove(enemy);
	}
	/**
	 * Add entity boss
	 * @param boss
	 */
	public void addBoss(Boss boss)
	{
		this.boss = boss;
	}
	/**
	 * Update floor level when change floor
	 */
	public void setDungeonLevel()
	{
		this.dungeon_level++;
	}
	
	/**
	 * Generate player attack sprites
	 * @param g Graphics2D
	 */
	public void render(Graphics2D g)
	{

		timer += System.currentTimeMillis() - lastime;
		try {
			
			if(this.player.isAttacking()) {
				g.drawImage(punch_img, (this.punch_box.getX()-player.getFloor().getOffsetX())*32, (this.punch_box.getY()-player.getFloor().getOffsetY())*32, null);
			   }
			
			if(this.player.isMagicAttacking())
			{
				if(timer<=500)
				{
					for(AABB box : this.magic_boxes)
					{
						g.drawImage(flame_img, (box.getX()-player.getFloor().getOffsetX())*32, (box.getY()-player.getFloor().getOffsetY()-1)*32, null);
					}

				}
				else
				{
					this.player.setMagicAttacking(false);
				}
			}
			else if(!this.player.isMagicAttacking())
			{
				timer = 0;
			}
		}
		catch(Exception e)
		{}

		lastime = System.currentTimeMillis();
	}
	
	/**
	 * Used when player make an attack
	 */
	public void playerAttack()
	{
		collide = false;
		
		switch(this.player.getDirection())
		{
			case Down:
				direction_box = new AABB(new Point(player.getX(), player.getY()+1), 1, 2);
				
				if(!this.enemies.isEmpty())
				{
					enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
					this.damagePlayer("enemy",collide);
				}
				else if(this.dungeon_level%5 == 0 && boss!=null)
				{
					if(direction_box.collides(this.boss.getBox()))
					{
						this.collide = true;
						this.damagePlayer("boss",collide);
					}
				}
				
				punch_box = new AABB(new Point(player.getX(), player.getY()+1), 1, 2);
				
				break;
				
			case Left:
				direction_box = new AABB(new Point(player.getX()-1, player.getY()), 1, 2);
				
				if(!this.enemies.isEmpty())
				{
					enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
					this.damagePlayer("enemy",collide);
				}
				else if(this.dungeon_level%5 == 0 && boss!=null)
				{
					if(direction_box.collides(this.boss.getBox()))
					{
						this.collide = true;
						this.damagePlayer("boss",collide);
					}
				}
				
				punch_box = new AABB(new Point(player.getX()-1, player.getY()-1), 1, 2);
				
				break;
				
			case Right:
				direction_box = new AABB(new Point(player.getX()+1, player.getY()), 1, 2);
				
				if(!this.enemies.isEmpty())
				{
					enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
					this.damagePlayer("enemy",collide);
				}
				else if(this.dungeon_level%5 == 0 && boss!=null)
				{
					if(direction_box.collides(this.boss.getBox()))
					{
						this.collide = true;
						this.damagePlayer("boss",collide);
					}
				}
				
				punch_box = new AABB(new Point(player.getX()+1, player.getY()-1), 1, 2);
				
				break;
				
			case Up:
				direction_box = new AABB(new Point(player.getX(), player.getY()-1), 1, 2);
				
				if(!this.enemies.isEmpty())
				{
					enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
					this.damagePlayer("enemy",collide);
				}
				else if(this.dungeon_level%5 == 0 && boss!=null)
				{
					if(direction_box.collides(this.boss.getBox()))
					{
						this.collide = true;
						this.damagePlayer("boss",collide);
					}
				}
				
				punch_box = new AABB(new Point(player.getX(), player.getY()-2), 1, 2);
				
				break;
		}
	}
	
	/**
	 * Used when player take damage from enemy/boss
	 * @param type		type of enemy that damage the player
	 * @param collide	boolean that control if the player box collides with entity attack 
	 */
	private void damagePlayer(String type,boolean collide)
	{
		if(!(this.player.getFloor().getMap().get(direction_box.getpos()).gettype()==tiletype.OFF))
		{
			if(type=="boss")
			{
				if(!boss.isDead())
				{
					if(collide)
					{
						boss.setHp(boss.getHp()-(player.getAttack()-boss.getDefence()));
						
						if(!punch_sound.isRunning())
						{
							punch_sound.loop(1);
						}
						
						if(boss.isDead())
						{
							this.player.addExp(this.boss.getExpGuaranteed());
							boss.getBossFloor().exitCreate(boss.getBox().getpos());
						}
					}
				}
			}
			else if(type=="enemy")
			{
				if(collide)
				{
					enemy.setHp(enemy.getHp()-(player.getAttack()-enemy.getDefence()));
					
					if(!punch_sound.isRunning())
					{
						punch_sound.loop(1);
					}
					
					if(enemy.isDead())
					{
						this.player.addExp(this.enemy.getExpGuaranteed());
						this.removeEnemy(enemy);
					}
				}
			}
		}
	}
	
	/**
	 * Used when the player use the magic attack 
	 */
	public void playerMagicAttack()
	{
		this.player.setSpells();
		AABB magic_boxes[] = 
			{
					new AABB(new Point(this.player.getX()-1,this.player.getY()-1),1,1),
					new AABB(new Point(this.player.getX(),this.player.getY()-1),1,1),
					new AABB(new Point(this.player.getX()+1,this.player.getY()-1),1,1),
					new AABB(new Point(this.player.getX()-1,this.player.getY()),1,1),
					new AABB(new Point(this.player.getX()+1,this.player.getY()),1,1),
					new AABB(new Point(this.player.getX()+1,this.player.getY()+1),1,1),
					new AABB(new Point(this.player.getX()-1,this.player.getY()+1),1,1),
					new AABB(new Point(this.player.getX()-1,this.player.getY()+2),1,1),
					new AABB(new Point(this.player.getX(),this.player.getY()+2),1,1),
					new AABB(new Point(this.player.getX()+1,this.player.getY()+2),1,1)	
			};
		
		this.magic_boxes = magic_boxes;
		
		if(this.dungeon_level%5 != 0 && !(this.enemies.isEmpty()))
		{
			for(AABB box : magic_boxes)
			{
				this.enemies.forEach(x -> {

					if(box.collides(x.getBox())) 
					{
						collide = true; 
						if(collide)
							this.magicDamage(x);
					} 
					
					});
				
			}
			
			this.enemies= removethedead(enemies);
			
		}
		else if(this.dungeon_level%5 == 0 && !this.boss.isDead())
			this.magicDamageBoss();
		
	}
	
	/**
	 * Used when player kill enemy with agic attack
	 * @param e Enemy
	 */
	public void magicDamage(Enemy e)
	{
		this.enemy=e;
		enemy.setHp(enemy.getHp()-this.player.getMagic_attack());
		
		if(enemy.isDead())
		{
			this.player.addExp(enemy.getExpGuaranteed());
		}
	}
	
	/**
	 * Used in boss floor for the moving boss magic attack
	 */
	public void magicDamageBoss()
	{
		switch(this.player.getInventory().getPowerStone())
		{
			case 1:
				boss.setHp((boss.getHp()-(this.player.getMagic_attack()/2)));
				break;
				
			case 2:
				boss.setHp((int) ((boss.getHp()-(this.player.getMagic_attack())/1.5)));
				break;
				
			case 3:
				boss.setHp(boss.getHp()-this.player.getMagic_attack());
				break;
				
		}
		
		if(boss.isDead())
		{
			this.player.addExp(this.boss.getExpGuaranteed());
			boss.getBossFloor().exitCreate(boss.getBox().getpos());
			
		}
	}
	
	/**
	 * Attack of the normal enemy
	 * @param enemy
	 */
	public void enemyAttack(Enemy enemy)
	{
		if(enemy.getAttack()-player.getDefence()>0)
			player.setHp(player.getHp()-(enemy.getAttack()-player.getDefence()));

		bone_sound.loop(1);
	}
	
	/**
	 * Attack of the boss
	 * @param boss
	 */
	public void bossAttack(Boss boss)
	{
		if(boss.getAttack()-player.getDefence()>0)
			player.setHp(player.getHp()-(boss.getAttack()-player.getDefence()));

		boss_sound.loop(1);
	}
	
	/**
	 * Boss flame attack damage output
	 */
	public void flamesAttack()
	{
		player.setHp(player.getHp()-((int) (player.getMax_hp()*20/100)));
	}
	/**
	 * Decrease boss stats when player take power stone
	 */
	public void lowerBossStats()
	{
		switch(player.getInventory().getPowerStone())
		{
			case 0:
				break;
				
			case 1:
				this.boss.setAttack(this.boss.getAttack()/2);
				this.boss.setDefence(this.boss.getDefence()/2);
				break;
				
			case 2:
				this.boss.setAttack(this.boss.getAttack()/2);
				this.boss.setDefence(this.boss.getDefence()/2);
				break;
				
			case 3:
				this.boss.setAttack(this.boss.getAttack()/2);
				this.boss.setDefence(0);
				break;
		}
	}
	private List<Enemy> removethedead( List<Enemy> enemies){
		return enemies.stream().filter(x-> x.isDead()==false).collect(Collectors.toList());
	}
}
