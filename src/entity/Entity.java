package entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import game.*;
import utilities.*;
import mapandtiles.*;

public abstract class Entity extends GameObject{

	SpriteSheet sprite;
	BufferedImage[][] img; 
	protected AABB box;
	private int level;
	private int hp;
	private int max_hp;
	private int attack;
	private int magic_attack;
	private int defence;
	private Attribute attribute;
	private Floor floor;
	
	
	public Entity(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence, Floor floor) {
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
	
	public boolean isDead() {
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
	public AABB getBox() {
		return box;
	}	
	public int getMax_hp() {
		return max_hp;
	}
	public Floor getFloor() {
		return floor;
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
	public void setBox(AABB box) {
		this.box = box;
	}
	public void setMax_hp(int max_hp) {
		this.max_hp = max_hp;
	}
	public void setFloor(Floor floor) {
		this.floor = floor;
	}
}
