package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;
import utilities.AaBb;

/**
 * Class used to create main game elements.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see entity.Entity
 * @see java.awt.Graphics2
 * @see java.awt.event.KeyEvent
 * @see game.Id
 */
public abstract class GameObject {

  protected int cordX;
  protected int cordY;
  protected Id id;
  protected int velX; 
  protected int velY;

  /**
   * Constructor for GameObject.
   *
   * @param x  Horizontal position
   * @param y  Vertical position
   * @param id Class ID
   */
  public GameObject(final int x, final int y, final Id id) {
    this.cordX = x;
    this.cordY = y;
    this.id = id;
  }

  /**
   * Execute operation per CPU clock.
   */
  public abstract void tick();

  /*
   * Update position.
   */
  public abstract void move();

  /**
   * Generate the graphics elements.
   *
   * @param g the grapichs that "draw" the elements
   */
  public abstract void render(Graphics2D g);

  /**
   * Take input from user keyboard or update entity action after key press.
   *
   * @param key        The keyboard key(Es.: KeyEvent.VK_W)
   * @param collisions Set the movement limit on the map
   */
  public abstract void input(KeyEvent key, List<AaBb> collisions);

  /**
   * Set horizontal position.
   *
   * @param x the coordinate x
   */
  public void setX(final int x) {
    this.cordX = x;
  }

  /**
   * Set vertical position.
   *
   * @param y the coordinate y
   */
  public void setY(final int y) {
    this.cordY = y;
  }

  /**
   * Set the enum ID.
   *
   * @param id the ID for the type of entity, floor, HUD and item
   */
  public void setId(final Id id) {
    this.id = id;
  }

  /**
   * return the cordinate x.
   *
   * @return horizontal position
   */
  public int getX() {
    return cordX;
  }

  /**
   * return the cordinate y.
   *
   * @return vertical position
   */
  public int getY() {
    return cordY;
  }

  /**
   * return the "identity" of the object.
   *
   * @return the entity id
   */
  public Id getId() {
    return id;
  }

  /**
   * Set the horizontal movement.
   *
   * @param velX Velocity on the x axis
   */
  public void setvelX(final int velX) {
    this.velX = velX;
  }

  /**
   * Set the vertical movement.
   *
   * @param velY Velocity on the y axis
   */
  public void setvelY(final int velY) {
    this.velY = velY;
  }

  /**
   * Set the horizontal movement speed.
   *
   * @return the horizontal movement speed
   */
  public int getvelX() {
    return velX;
  }

  /**
   * Set the horizontal movement speed.
   *
   * @return the vertical movement speed
   */
  public int getvelY() {
    return velY;
  }
}
