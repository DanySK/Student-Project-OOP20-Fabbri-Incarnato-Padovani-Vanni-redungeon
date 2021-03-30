package entity;

import game.*;

public abstract class Entity extends GameObject{

	private int level;
	private int hp;
	private int attack;
	private int magic_attack;
	private int defence;
	private Attribute attribute;
	
	public Entity(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence) {
		
		super(x, y, id);
		this.setLevel(level);
		this.setHp(hp);
		this.setAttack(attack);
		this.setMagic_Attack(magic_attack);
		this.setDefence(defence);
		
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
}
