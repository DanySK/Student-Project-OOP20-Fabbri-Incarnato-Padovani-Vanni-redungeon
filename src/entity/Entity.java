package entity;

import java.awt.image.BufferedImage;

import game.*;
import utilities.*;
import mapandtiles.*;

/*
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

public abstract class Entity extends GameObject {

  protected SpriteSheet sprite;
  protected BufferedImage[][] imgMatrix;
  private BufferedImage img;
  protected BufferedImage hpBar;
  private final CombatSystem combat;
  protected AABB box;
  private int level;
  private int hp;
  private int maxHp;
  private int attack;
  private int magicAttack;
  private int defence;
  private Attribute attribute;

  private Direction direction;
  private boolean movement; // false the entity steady, true is moving
  private boolean attacking;

  protected AbsFloor floor;

  /*
   * Entity constructor
   * 
   * @param x      Horizontal starting position
   * 
   * @param y      Vertical starting position
   * 
   * @param id     Id for the entity taken from ID enum class
   * 
   * @param combat Combat system for the entity
   * 
   * @param level  Entity level for stats augmentation
   * 
   * @param floor  Used to take level and position
   * 
   * @param player Used as variable to calculate damage and give experience to
   *               player
   * 
   */
  public Entity(final int x, final int y, final Id id, final CombatSystem combat, 
      final int level, final AbsFloor floor) {
    super(x, y, id);
    this.sprite = null;
    this.imgMatrix = null;
    this.hpBar = null;
    this.setLevel(level);
    this.floor = floor;
    this.combat = combat;
  }

  /*
   * Change entity sprite on different direction
   * 
   * @param direction
   */
  public void changeDirection(final Direction direction) {
    this.setDirection(direction);

    switch (direction) {
      case DOWN:
        setImg(imgMatrix[0][1]);
        break;
  
      case LEFT:
        setImg(imgMatrix[1][1]);
        break;
  
      case RIGHT:
        setImg(imgMatrix[2][1]);
        break;
  
      case UP:
        setImg(imgMatrix[3][1]);
        break;
      
      default:
        break;
    }
  }

  /*
   * @return boolean
   */
  public boolean isDead() {

    if (this.hp <= 0) {
      this.setAttacking(false);
      return true;
    } else {
      return false;
    }
      

  }

  /*
   * Change entity stats on level up(for player) or level change(for enemy and
   * boss)
   */
  public abstract void augmStat();

  /*
   * @return level
   */
  public int getLevel() {
    return level;
  }

  /*
   * @return health point
   */
  public int getHp() {
    return hp;
  }

  /*
   * @return attack
   */
  public int getAttack() {
    return attack;
  }

  /*
   * @return magic attack
   */
  public int getMagicAttack() {
    return magicAttack;
  }

  /*
   * @return defense
   */
  public int getDefence() {
    return defence;
  }

  /*
   * @return magic attack type
   */
  public Attribute getAttribute() {
    return attribute;
  }

  /*
   * @return direction
   */
  public Direction getDirection() {
    return direction;
  }

  /*
   * @return collision box
   */
  public AABB getBox() {
    return box;
  }

  /*
   * @return max health point
   */
  public int getMaxHp() {
    return maxHp;
  }

  /*
   * @return
   */
  public AbsFloor getFloor() {
    return floor;
  }

  /*
   * @return boolean when entity is moving or not
   */
  public boolean isMoving() {
    return movement;
  }

  /*
   * @return boolean when entity is attacking or not
   */
  public boolean isAttacking() {
    return attacking;
  }

  /*
   * Set entity level
   * 
   * @param level
   */
  public void setLevel(final int level) {
    this.level = level;
  }

  /*
   * Set entity health point
   * 
   * @param hp
   */
  public void setHp(final int hp) {
    this.hp = hp;
  }

  /*
   * Set entity attack damage
   * 
   * @param attack
   */
  public void setAttack(final int attack) {
    this.attack = attack;
  }

  /*
   * Set entity magic attack damage
   * 
   * @param magic_attack
   */
  public void setMagicAttack(final int magicAttack) {
    this.magicAttack = magicAttack;
  }

  /*
   * Set entity defense
   * 
   * @param defence
   */
  public void setDefence(final int defence) {
    this.defence = defence;
  }

  /*
   * Set entity collision box
   * 
   * @param box
   */
  public void setBox(final AABB box) {
    this.box = box;
  }

  /*
   * Set entity maximum health point
   * 
   * @param max_hp
   */
  public void setMaxHp(final int maxHp) {
    this.maxHp = maxHp;
  }

  /*
   * Set entity movement direction
   * 
   * @param direction
   */
  public void setDirection(final Direction direction) {
    this.direction = direction;
  }

  /*
   * Set entity magic attack element
   * 
   * @param attribute
   */
  public void setAttribute(final Attribute attribute) {
    this.attribute = attribute;
  }

  /*
   * Set entity movement
   * 
   * @param movement
   */
  public void setMovement(final boolean movement) {
    this.movement = movement;
  }

  /*
   * Set attack state on entity
   * 
   * @param attacking
   */
  public void setAttacking(final boolean attacking) {
    this.attacking = attacking;
  }

  public BufferedImage getHpBar() {
    return hpBar;
  }

  public SpriteSheet getSprite() {
    return sprite;
  }

  public BufferedImage getImg() {
    return img;
  }

  public void setImg(final BufferedImage img) {
    this.img = img;
  }

  public CombatSystem getCombat() {
    return combat;
  }
}
