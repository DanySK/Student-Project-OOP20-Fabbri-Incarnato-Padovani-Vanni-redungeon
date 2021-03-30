package game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;



public abstract class GameObject {
	
	protected int x,y;
	protected ID id;
	protected int velX,velY;
	
	public GameObject(int x,int y,ID id) {
		this.x=x;
		this.y=y;
		this.id=id;
	
	}
	public abstract void tick(); 
	public abstract void move();
	public abstract void render(Graphics2D g);
	public abstract void input(KeyEvent key);
	
    public void setX(int x) {
    	this.x=x;
    }
    public void setY(int y) {
    	this.y=y;
    }
    public void setID(ID id) {
    	this.id=id;
    }
    public int getX() {
    	return x;
    }
    public int getY() {
    	return y;
    }
    public ID getID() {
    	return id;
    }
    public void setvelX(int velX) {
    	this.velX=velX;
    }
    public void setvelY(int velY) {
    	this.velY=velY;
    }
    public int getvelX() {
    	return velX;
    }
    public int getvelY() {
    	return velY;
    }
}
