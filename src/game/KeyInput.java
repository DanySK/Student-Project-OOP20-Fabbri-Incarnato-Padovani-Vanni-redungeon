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
		handler.object.stream().filter(x->x.getID() != ID.Floor).forEach( x -> collisions.add(((Entity) x).getBox()));
		for(int i=0;i<handler.object.size();i++) {
			GameObject tempobj=handler.object.get(i);
			
			if(tempobj.getID()==ID.Player) {
				tempobj.input(key, collisions);
			}
			
			if(key.getKeyCode()==KeyEvent.VK_A || key.getKeyCode()==KeyEvent.VK_S || key.getKeyCode()==KeyEvent.VK_D || key.getKeyCode()==KeyEvent.VK_W)
			{
				if(tempobj.getID()==ID.Enemy) {
					tempobj.input(key, collisions);
				} 
				if(tempobj.getID()==ID.Boss) {
					tempobj.input(key, collisions);
				}
			}
		}
		collisions.clear();
	}
	
	public void keyReleased(KeyEvent e) {
		int key=e.getKeyCode();
		for(int i=0; i<handler.object.size();i++) {
			GameObject tempobj=handler.object.get(i);
			if(tempobj.getID()==ID.Enemy || tempobj.getID()==ID.Player) {
				if(key==KeyEvent.VK_W) {
					tempobj.setvelY(0);
					((Entity) tempobj).setMovement(false);
				}
				if(key==KeyEvent.VK_A) {
					tempobj.setvelX(0);
					((Entity) tempobj).setMovement(false);
				}
				if(key==KeyEvent.VK_S) {
					tempobj.setvelY(0);
					((Entity) tempobj).setMovement(false);
				}
				if(key==KeyEvent.VK_D) {
					tempobj.setvelX(0);
					((Entity) tempobj).setMovement(false);
				}
			}
		}
	}
}
