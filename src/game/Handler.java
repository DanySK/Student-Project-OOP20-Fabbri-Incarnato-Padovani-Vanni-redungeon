package game;

import java.awt.Graphics2D;
import java.util.LinkedList;

import entity.*;

/**
 * 
 * Class that manage game events
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see java.util.LinkedList
 * @see game.GameObject
 * @see game.ID
 * @see entity.Entity
 * @see entity.Player
 * @see entity.Enemy
 * @see entity.Boss
 * @see java.awt.Graphics2D
 */
public class Handler {
	
	LinkedList<GameObject> object= new LinkedList<GameObject>();
	public boolean next= false;
    public boolean dead=false;
    public int point=0;
    public int enemies_number=0;
    /**
     * Make control on game events 
     */
	public void tick() {
		for(int i=0;i<object.size();i++) {
			GameObject tempobj=object.get(i);
			tempobj.tick();
			if(tempobj.getID()==ID.Player) {
				Player obj= (Player) tempobj;
				if(obj.isOut()) {
					next = true;}
				if(obj.isDead())
				{
					this.dead=true;
					point=point+(obj.getInventory().getgems()*500);
					this.removeObject(obj);
				}
			}
			
			if(tempobj.getID()==ID.Enemy) {
				Enemy obj= (Enemy) tempobj;
				if(obj.isDead())
				{
					point+=10;
					this.removeObject(obj);
				}
			}
			
			if(tempobj.getID()==ID.Boss) {
				Boss obj= (Boss) tempobj;
				if(obj.isDead())
				{
					point+=100;
					this.removeObject(obj);
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param g
	 */
	public void render(Graphics2D g) {
		for(int i=0;i<object.size();i++) {
			GameObject tempobj=object.get(i);
			tempobj.render(g);
		}
	}
	
	/**
	 * 
	 * @param object GameObject
	 */
	public void addObject(GameObject object) {
		if(object.getID()==ID.Enemy)
		{
			enemies_number++;
			this.object.add(2,object);
		}
		else
			this.object.add(object);
	}
	/**
	 * 
	 * @param object GameObject
	 */
	public void removeObject(GameObject object) {
		if(object.getID()==ID.Enemy)
		{
			enemies_number--;
		}
		this.object.remove(object);
	}
	

}
