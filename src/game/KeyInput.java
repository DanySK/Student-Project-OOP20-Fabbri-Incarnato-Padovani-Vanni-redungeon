package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import game.*;
import utilities.*;
import entity.*;


public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	public KeyInput(Handler handler) {
		this.handler=handler;
	}
	
	public void keyPressed(KeyEvent key) {
		List<AABB> collisions = new ArrayList<AABB>();
		handler.object.stream().filter(x->x.id != ID.Floor).forEach( x -> collisions.add(((Entity) x).getBox()));
		for(int i=0;i<handler.object.size();i++) {
			GameObject tempobj=handler.object.get(i);
			
			if(tempobj.getID()==ID.Player) {
				tempobj.input(key, collisions);
			}

			if(tempobj.getID()==ID.Enemy) {
				tempobj.input(key, collisions);
			}
		}
	}
	
	public void keyReleased() {
		
	}
}
