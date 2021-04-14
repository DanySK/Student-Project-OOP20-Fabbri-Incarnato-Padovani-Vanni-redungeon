package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.*;
import utilities.*;

public class CombatSystem {
	
	private Graphics2D g;
	private Player player;
	private Enemy enemy;
	private List<Enemy> enemies;
	private AABB direction_box;
	private boolean collide;
	private BufferedImage img;
	private AABB punch_box;
	private AABB bone_box;
	
	public CombatSystem() throws IOException
	{
		enemies = new ArrayList<Enemy>();
		img=ImageIO.read(new File("data/punch.png"));
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
				g.drawImage(img, (this.punch_box.getX()-player.getFloor().getOffsetX())*32, (this.punch_box.getY()-player.getFloor().getOffsetY())*32, null);
			   }
		}
		catch(Exception e)
		{
			
		}
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
				
				this.damageEnemy(direction_box.collides(player.getBox()));
				
				break;
				
			case Left:
				direction_box = new AABB(new Point(enemy.getX()-1, enemy.getY()), 1, 2);
				
				this.damageEnemy(direction_box.collides(player.getBox()));
				
				break;
				
			case Right:
				direction_box = new AABB(new Point(enemy.getX()+1, enemy.getY()), 1, 2);
				
				this.damageEnemy(direction_box.collides(player.getBox()));
				
				break;
				
			case Up:
				direction_box = new AABB(new Point(enemy.getX(), enemy.getY()-1), 1, 2);
				
				this.damageEnemy(direction_box.collides(player.getBox()));
				
				break;
				
		}
	}
	
	private void damageEnemy(boolean collide)
	{
		if(collide)
		{
			player.setHp(player.getHp()-(enemy.getAttack()-player.getDefence()));
		}
	}
}
