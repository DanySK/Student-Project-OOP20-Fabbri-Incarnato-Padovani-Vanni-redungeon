package entity;

import java.awt.image.BufferedImage;

import game.*;
import utilities.*;
import mapandtiles.*;
/** 
 * Class that extend GameObject and add function to manage game entity
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see entity.Player
 * @see entity.Enemy
 * @see entity.Boss
 *
 */
public abstract class Entity extends GameObject{

	SpriteSheet sprite;
	BufferedImage[][] img_matrix; 
	BufferedImage img;
	BufferedImage hp_bar;
	CombatSystem combat;
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
	private boolean attacking;

	private AbsFloor floor;

	
	/**
	 * Entity constructor
	 * 
	 * @param x 
	 * 		Horizontal starting position
	 * 
	 * @param y
	 * 		Vertical starting position
	 * 
	 * @param id
	 * 		Id for the entity taken from ID enum class
	 * 
	 * @param combat
	 * 		Combat system for the entity
	 * 
	 * @param level
	 * 		Entity level for stats augmentation
	 * 
	 * @param floor
	 * 		Used to take level and position
	 * 
	 * @param player 
	 * 		Used as variable to calculate damage and give experience to player
	 * 
	 */
	public Entity(int x, int y, ID id, CombatSystem combat, int level, AbsFloor floor) {
		super(x, y, id);
		this.setLevel(level);
		this.setFloor(floor);
		this.combat = combat;
	}
	
	/**
	 * Change entity sprite on different direction
	 * @param direction
	 */
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
	
	/**
	 * Used for 
	 * @return boolean
	 */
	public boolean isDead() 
	{
		
		if(this.hp <= 0)
		{
			this.setAttacking(false);
			return true;
		}
		
		else
			return false;
		
	}
	
	/**
	 * Change entity stats on level up(for player) or level change(for enemy and boss)
	 */
	public abstract void augmStat();
	
	
	/**
	 * @return level
	 */
	public int getLevel()
	{
		return level;
	}
	/**
	 * @return health point
	 */
	public int getHp()
	{
		return hp;
	}
	/**
	 * @return attack
	 */
	public int getAttack()
	{
		return attack;
	}
	/**
	 * @return magic attack
	 */
	public int getMagic_attack()
	{
		return magic_attack;
	}
	/**
	 * @return defense
	 */
	public int getDefence()
	{
		return defence;
	}
	/**
	 * @return magic attack type
	 */
	public Attribute getAttribute()
	{
		return attribute;
	}
	/**
	 * @return direction
	 */
	public Direction getDirection() 
	{
		return direction;
	}
	/**
	 * @return collision box
	 */
	public AABB getBox() 
	{
		return box;
	}
	/**
	 * @return max health point
	 */
	public int getMax_hp()
	{
		return max_hp;
	}
	/**
	 * @return
	 */
	public AbsFloor getFloor() {
		return floor;
	}
	/**
	 * @return boolean when entity is moving or not
	 */
	public boolean isMoving() {
		return movement;
	}
	/**
	 * @return boolean when entity is attacking or not
	 */
	public boolean isAttacking() {
		return attacking;
	}


	
	/**
	 * Set entity level
	 * @param level
	 */
	public void setLevel(int level)
	{
		this.level = level;
	}
	/**
	 * Set entity health point
	 * @param hp
	 */
	public void setHp(int hp)
	{
		this.hp = hp;
	}
	/**
	 * Set entity attack damage
	 * @param attack
	 */
	public void setAttack(int attack)
	{
		this.attack = attack;
	}
	/**
	 * Set entity magic attack damage
	 * @param magic_attack
	 */
	public void setMagic_Attack(int magic_attack)
	{
		this.magic_attack = magic_attack;
	}
	/**
	 * Set entity defense
	 * @param defence
	 */
	public void setDefence(int defence)
	{
		this.defence = defence;
	}
	/**
	 * Set entity collision box
	 * @param box
	 */
	public void setBox(AABB box) 
	{
		this.box = box;
	}
	/**
	 * Set entity maximum health point
	 * @param max_hp
	 */
	public void setMax_hp(int max_hp) 
	{
		this.max_hp = max_hp;
	}
	/**
	 * Set entity floor 
	 * @param floor
	 */
	public void setFloor(AbsFloor floor) {
		this.floor = floor;
	}
	/**
	 * Set entity movement direction
	 * @param direction
	 */
	public void setDirection(Direction direction) 
	{
		this.direction = direction;
	}
	/**
	 * Set entity magic attack element
	 * @param attribute
	 */
	public void setAttribute(Attribute attribute)
	{
		this.attribute = attribute;
	}
	/**
	 * Set entity movement
	 * @param movement
	 */
	public void setMovement(boolean movement) 
	{
		this.movement = movement;
	}
	/**
	 * Set attack state on entity
	 * @param attacking
	 */
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
}
