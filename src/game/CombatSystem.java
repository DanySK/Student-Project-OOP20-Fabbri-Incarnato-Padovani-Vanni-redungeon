package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entity.*;
import utilities.*;

public class CombatSystem {
	
	private Graphics2D g;
	private Player player;
	private Enemy enemy;
	private List<Enemy> enemies;
	private AABB direction_box;
	private boolean collide;
	private BufferedImage punch_img;
	private BufferedImage bone_img;
	private AABB punch_box;
	private AABB bone_box;

	private Clip bone_sound;
	private AudioInputStream bone_audio;
	
	private Clip punch_sound;
	private AudioInputStream punch_audio;
	
	public CombatSystem() throws IOException, LineUnavailableException, UnsupportedAudioFileException
	{
		enemies = new ArrayList<Enemy>();
		punch_img=ImageIO.read(new File("data/punch.png"));

		bone_sound = AudioSystem.getClip();
		bone_audio= AudioSystem.getAudioInputStream(new File("data/bonk.wav"));
		bone_sound.open(bone_audio);
		
		punch_sound = AudioSystem.getClip();
		punch_audio= AudioSystem.getAudioInputStream(new File("data/punch.wav"));
		punch_sound.open(punch_audio);
	}

	public Clip getBonk()
	{
		return bone_sound;
	}
	public Clip getPunch()
	{
		return punch_sound;
	}
	
	public void addPlayer(Player player)
	{
		this.player = player;
	}
	
	public void addEnemy(Enemy enemy)
	{
		this.enemies.add(enemy);
	}
	public void removeEnemy(Enemy enemy)
	{
		this.enemies.remove(enemy);
	}
	public void render(Graphics2D g)
	{
		try {
			
			if(this.player.isAttacking()) {
				punch_sound.loop(1);
				g.drawImage(punch_img, (this.punch_box.getX()-player.getFloor().getOffsetX())*32, (this.punch_box.getY()-player.getFloor().getOffsetY())*32, null);
			   }
			if(this.enemy.isAttacking()) {
				//g.drawImage(bone_img, (this.punch_box.getX()-player.getFloor().getOffsetX())*32, (this.punch_box.getY()-player.getFloor().getOffsetY())*32, null);
			   }
			if(!this.player.isAttacking()) {
				punch_sound.stop();
			   }
		}
		catch(Exception e)
		{}
	}
	
	public void PlayerAttack()
	{
		collide = false;
		
		switch(this.player.getDirection())
		{
			case Down:
				direction_box = new AABB(new Point(player.getX(), player.getY()+1), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				this.damagePlayer(collide);
				
				punch_box = new AABB(new Point(player.getX(), player.getY()+1), 1, 2);
				
				break;
				
			case Left:
				direction_box = new AABB(new Point(player.getX()-1, player.getY()), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				this.damagePlayer(collide);
				
				punch_box = new AABB(new Point(player.getX()-1, player.getY()-1), 1, 2);
				
				break;
				
			case Right:
				direction_box = new AABB(new Point(player.getX()+1, player.getY()), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				this.damagePlayer(collide);
				
				punch_box = new AABB(new Point(player.getX()+1, player.getY()-1), 1, 2);
				
				break;
				
			case Up:
				direction_box = new AABB(new Point(player.getX(), player.getY()-1), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				this.damagePlayer(collide);
				
				punch_box = new AABB(new Point(player.getX(), player.getY()-2), 1, 2);
				
				break;
		}
	}
	
	private void damagePlayer(boolean collide)
	{
		if(collide)
		{
			enemy.setHp(enemy.getHp()-(player.getAttack()-enemy.getDefence()));
			if(enemy.isDead())
			{
				this.player.addExp(this.enemy.getExpGaranted());
				this.removeEnemy(enemy);
			}
		}
	}
	
	public void EnemyAttack(Enemy enemy)
	{
		collide = false;
		
		switch(enemy.getDirection())
		{
			case Down:
				direction_box = new AABB(new Point(enemy.getX(), enemy.getY()+1), 1, 2);

				if(direction_box.collides(player.getBox()))
				{
					if(enemy.getAttack()-player.getDefence()>0)
						player.setHp(player.getHp()-(enemy.getAttack()-player.getDefence()));

					bone_sound.loop(1);
				}
				
				break;
				
			case Left:
				direction_box = new AABB(new Point(enemy.getX()-1, enemy.getY()), 1, 2);

				if(direction_box.collides(player.getBox()))
				{
					if(enemy.getAttack()-player.getDefence()>0)
						player.setHp(player.getHp()-(enemy.getAttack()-player.getDefence()));
					
					bone_sound.loop(1);
				}
				
				break;
				
			case Right:
				direction_box = new AABB(new Point(enemy.getX()+1, enemy.getY()), 1, 2);

				if(direction_box.collides(player.getBox()))
				{
					if(enemy.getAttack()-player.getDefence()>0)
						player.setHp(player.getHp()-(enemy.getAttack()-player.getDefence()));
					
					bone_sound.loop(1);
				}
				
				break;
				
			case Up:
				direction_box = new AABB(new Point(enemy.getX(), enemy.getY()-1), 1, 2);

				if(direction_box.collides(player.getBox()))
				{
					if(enemy.getAttack()-player.getDefence()>0)
						player.setHp(player.getHp()-(enemy.getAttack()-player.getDefence()));
					
					bone_sound.loop(1);
				}
				
				break;
				
		}
	}
	
	public void flamesAttack()
	{
		player.setHp(player.getHp()-((int) (player.getMax_hp()*20/100)));
	}
}
