package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	public KeyInput(Handler handler) {
		this.handler=handler;
	}
	
	public void keyPressed(KeyEvent key) {
		for(int i=0;i<handler.object.size();i++) {
			GameObject tempobj=handler.object.get(i);
			
			if(tempobj.getID()==ID.Player) {
				tempobj.input(key);
			}

			if(tempobj.getID()==ID.Enemy) {
				tempobj.input(key);
			}
		}
	}
	
	public void keyReleased() {
		
	}
}
