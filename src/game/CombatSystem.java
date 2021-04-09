package game;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import entity.*;
import utilities.*;

public class CombatSystem {
	
	private Player player;
	private Enemy enemy;
	private List<Enemy> enemies;
	private AABB direction_box;
	private boolean collide;
	
	public CombatSystem(){enemies = new ArrayList<Enemy>();}
	
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
	
	public void PlayerAttack()
	{
		collide = false;
		
		switch(this.player.getDirection())
		{
			case Down:
				direction_box = new AABB(new Point(player.getX(), player.getY()+1), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				
				if(collide)
				{
					enemy.setHp(enemy.getHp()-(player.getAttack()-enemy.getDefence()));
					if(enemy.isDead())
					{
						this.removeEnemy(enemy);
					}
				}
				
				break;
				
			case Left:
				direction_box = new AABB(new Point(player.getX()-1, player.getY()), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				
				if(collide)
				{
					enemy.setHp(enemy.getHp()-(player.getAttack()-enemy.getDefence()));
					if(enemy.isDead())
					{
						enemies.remove(enemy);
					}
				}
				
				break;
				
			case Right:
				direction_box = new AABB(new Point(player.getX()+1, player.getY()), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				
				if(collide)
				{
					enemy.setHp(enemy.getHp()-(player.getAttack()-enemy.getDefence()));
					if(enemy.isDead())
					{
						enemies.remove(enemy);
					}
				}
				
				break;
				
			case Up:
				direction_box = new AABB(new Point(player.getX(), player.getY()-1), 1, 2);
				
				enemies.forEach(x -> {if(direction_box.collides(x.getBox())) {collide = true; enemy = x;} });
				
				if(collide)
				{
					enemy.setHp(enemy.getHp()-(player.getAttack()-enemy.getDefence()));
					if(enemy.isDead())
					{
						enemies.remove(enemy);
					}
				}
				
				break;
		}
	}
}
