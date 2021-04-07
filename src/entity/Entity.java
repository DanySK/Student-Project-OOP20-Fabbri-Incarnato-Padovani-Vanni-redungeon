package entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import game.*;
import utilities.*;
import mapandtiles.*;

public abstract class Entity extends GameObject{

	SpriteSheet sprite;
	BufferedImage[][] img_matrix; 
	BufferedImage img;
	protected AABB box;
	private int level;
	private int hp;
	private int max_hp;
	private int attack;
	private int magic_attack;
	private int defence;
	private Attribute attribute;

	private Direction direction;
	private boolean movement; //false the entity steady, true is moving 

	private AbsFloor floor;

	
	
	public Entity(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence, AbsFloor floor) {
		super(x, y, id);
		this.setLevel(level);
		this.setHp(hp);
		this.setMax_hp(hp);
		this.setAttack(attack);
		this.setMagic_Attack(magic_attack);
		this.setDefence(defence);
		this.setFloor(floor);
		this.setBox(new AABB(new Point(this.x, this.y), 1, 2));
	}
	
	public void changeDirection(Direction direction)
	{
		this.setDirection(direction);
		
		switch(direction)
		{
			case Down:
				img = img_matrix[0][1];
				break;
				
			case Left:
				img = img_matrix[1][1];
				break;
				
			case Right:
				img = img_matrix[2][1];
				break;
				
			case Up:
				img = img_matrix[3][1];
				break;
		}
	}
	
	
	public boolean isDead() 
	{
		
		if(this.hp <= 0)
			return true;
		
		else
			return false;
		
	}
	
	
	public int getLevel()
	{
		return level;
	}
	public int getHp()
	{
		return hp;
	}
	public int getAttack()
	{
		return attack;
	}
	public int getMagic_attack()
	{
		return magic_attack;
	}
	public int getDefence()
	{
		return defence;
	}
	public Attribute getAttribute()
	{
		return attribute;
	}
	public Direction getDirection() 
	{
		return direction;
	}
	public AABB getBox() 
	{
		return box;
	}	
	public int getMax_hp()
	{
		return max_hp;
	}

	public AbsFloor getFloor() {
		return floor;
	}
	public boolean isMoving() {
		return movement;
	}


	
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	public void setHp(int hp)
	{
		this.hp = hp;
	}
	public void setAttack(int attack)
	{
		this.attack = attack;
	}
	public void setMagic_Attack(int magic_attack)
	{
		this.magic_attack = magic_attack;
	}
	public void setDefence(int defence)
	{
		this.defence = defence;
	}
	public void setBox(AABB box) 
	{
		this.box = box;
	}
	public void setMax_hp(int max_hp) 
	{
		this.max_hp = max_hp;
	}

	public void setFloor(AbsFloor floor) {
		this.floor = floor;
	}
	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}
	public void setMovement(boolean movement) 
	{
		this.movement = movement;
	}
}
