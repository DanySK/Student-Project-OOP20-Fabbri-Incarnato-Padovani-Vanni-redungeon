package game;

import java.awt.event.KeyAdapter;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.FloatControl;

import utilities.*;
import entity.*;

/**
 * Class extended from KeyAdapter, used for user input
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see entity.Player
 * @see java.awt.event.KeyEvent
 * @see java.awt.event.KeyAdapter
 * @see game.Handler
 * @see javax.sound.sampled.FloatControl
 */
public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	FloatControl punch_control;
	FloatControl bonk_control;
	double gain;
	float dB;
	int move=0;
	
	/**
	 * Constructor
	 * @param handler event controller
	 * @param punch	  player hit sound
	 * @param bonk	  enemy hit sound
	 */
	public KeyInput(Handler handler, FloatControl punch, FloatControl bonk) {
		this.handler=handler;
		
		punch_control = punch;
		
		bonk_control = bonk;
		
		gain = 0.5;   
		dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
	}
	/**
	 * Control events when user press a key
	 * @param key the key pressed
	 */
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode()==107)
		{
			if(gain<1.0)
			{
				gain+=0.1;
				dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
				this.punch_control.setValue(dB);
				this.bonk_control.setValue(dB);
			}
		}
		
		if(key.getKeyCode()==109)
		{
			if(gain>0.15)
			{
				gain-=0.1;
				dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
				this.punch_control.setValue(dB);
				this.bonk_control.setValue(dB);
			}
		}
		List<AABB> collisions = new ArrayList<AABB>();
		handler.object.stream().filter(x->x.getID() != ID.Floor).forEach( x -> collisions.add(((Entity) x).getBox()));
		for(int i=0;i<handler.object.size();i++) {
			GameObject tempobj=handler.object.get(i);
			
			if(tempobj.getID()==ID.Player) {
				//((Entity) tempobj).setAttacking(true);
				tempobj.input(key, collisions);
				move++;
			}
			
			if(key.getKeyCode()==KeyEvent.VK_A || key.getKeyCode()==KeyEvent.VK_S || key.getKeyCode()==KeyEvent.VK_D || key.getKeyCode()==KeyEvent.VK_W || key.getKeyCode()==KeyEvent.VK_J || key.getKeyCode()==KeyEvent.VK_K)
			{
				if(key.getKeyCode()!=KeyEvent.VK_J && key.getKeyCode()!=KeyEvent.VK_K)
				{
					if(tempobj.getID()==ID.Player) {
						((Player) tempobj).setAttacking(false);
					} 
				}
				
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
	
	/**
	 * Control when user release the pressed key
	 * @param e the event of the key pressed
	 */
	public void keyReleased(KeyEvent e) {
		int key=e.getKeyCode();
		for(int i=0; i<handler.object.size();i++) {
			GameObject tempobj=handler.object.get(i);
			if(tempobj.getID()==ID.Enemy || tempobj.getID()==ID.Player) {
				if(key==KeyEvent.VK_W) {
					tempobj.setvelY(0);
					((Entity) tempobj).setMovement(false);
					((Entity) tempobj).setAttacking(false);
				}
				if(key==KeyEvent.VK_A) {
					tempobj.setvelX(0);
					((Entity) tempobj).setMovement(false);
					((Entity) tempobj).setAttacking(false);
				}
				if(key==KeyEvent.VK_S) {
					tempobj.setvelY(0);
					((Entity) tempobj).setMovement(false);
					((Entity) tempobj).setAttacking(false);
				}
				if(key==KeyEvent.VK_D) {
					tempobj.setvelX(0);
					((Entity) tempobj).setMovement(false);
					((Entity) tempobj).setAttacking(false);
				}
				if(key==KeyEvent.VK_J) {
					tempobj.setvelX(0);
					((Entity) tempobj).setMovement(false);
					((Entity) tempobj).setAttacking(false);
				}
			}
		}
	}
	
	/**
	 * 
	 * @return the entity movement
	 */
	public int getMoves()
	{
		return this.move;
	}
	
	/**
	 * Set the entity movement
	 */
	public void setMoves()
	{
		this.move = 0;
	}
	
}
