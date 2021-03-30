package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.ID;

public class Player extends Entity {

	public Player(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence) {
		super(x, y, id, level, hp, attack, magic_attack, defence);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		x+=velX;
		y+=velY;
		velX=0;
		velY=0;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.drawRect(x, y, 32, 32);
	}

	@Override
	public void input(KeyEvent key) {
		int e = key.getKeyCode();
		
		switch (e) {
		case KeyEvent.VK_W:
			this.setvelY(-1);
			//this.setvelX(0);
			this.move();
			break;
			
		case KeyEvent.VK_A:
			this.setvelX(-1);
			//this.setvelY(0);
			this.move();
			break;
			
		case KeyEvent.VK_S:
			this.setvelY(1);
			//this.setvelX(0);
			this.move();
			break;
			
		case KeyEvent.VK_D:
			this.setvelX(1);
			//this.setvelY(0);
			this.move();
			break;
		}
	}

}
