package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

import utilities.*;

/**
 * 
 * Class used to create main game elements
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see entity.Entity
 * @see java.awt.Graphics2
 * @see java.awt.event.KeyEvent
 * @see game.ID
 */
public abstract class GameObject {
	
	protected int x,y;
	protected ID id;
	protected int velX,velY;
	
	/**
	 * Constructor for GameObject
	 * @param x
	 * 		Horizontal position
	 * @param y
	 * 		Vertical position
	 * @param id
	 * 		Class ID
	 */
	public GameObject(int x,int y,ID id) {
		this.x=x;
		this.y=y;
		this.id=id;
	}
	
	/**
	 * Execute operation per CPU clock
	 */
	public abstract void tick();
	
	/*
	 * Update position
	 */
	public abstract void move();
	
	/**
	 * Generate the graphics elements
	 * @param g
	 */
	public abstract void render(Graphics2D g);
	
	/**
	 * Take input from user keyboard or update entity action after key press
	 * @param key
	 * 		The keyboard key(Es.: KeyEvent.VK_W)
	 * @param collisions
	 * 		Set the movement limit on the map
	 */
	public abstract void input(KeyEvent key, List<AABB> collisions);
	
	/**
	 * Set horizontal position
	 * @param x
	 */
    public void setX(int x) {
    	this.x=x;
    }
    
    /**
     * Set vertical position
     * @param y
     */
    public void setY(int y) {
    	this.y=y;
    }
    
    /**
     * Set the enum ID
     * @param id
     */
    public void setID(ID id) {
    	this.id=id;
    }
    
    /**
     * @return horizontal position
     */
    public int getX() {
    	return x;
    }
    
    /**
     * @return vertical position
     */
    public int getY() {
    	return y;
    }
    
    /**
     * 
     * @return the entity id
     */
    public ID getID() {
    	return id;
    }
    
    /**
     * Set the horizontal movement 
     * @param velX
     * 		Velocity on the x axis 
     */
    public void setvelX(int velX) {
    	this.velX=velX;
    }
    
    /**
     * Set the vertical movement
     * @param velY
     * 		Velocity on the y axis
     */
    public void setvelY(int velY) {
    	this.velY=velY;
    }
    
    /**
     * 
     * @return the horizontal movement speed
     */
    public int getvelX() {
    	return velX;
    }
    
    /**
     * 
     * @return the vertical movement speed
     */
    public int getvelY() {
    	return velY;
    }
}
