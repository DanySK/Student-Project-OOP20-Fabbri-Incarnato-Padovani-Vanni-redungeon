package game;

import java.awt.Graphics2D;
import java.util.LinkedList;

public class Handler {
	
	LinkedList<GameObject> object= new LinkedList<GameObject>();


	public void tick() {
		for(int i=0;i<object.size();i++) {
			GameObject tempobj=object.get(i);
			tempobj.tick();
		}
		
	}
	
	public void render(Graphics2D g) {
		for(int i=0;i<object.size();i++) {
			GameObject tempobj=object.get(i);
			tempobj.render(g);
		}
	}
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}

}
