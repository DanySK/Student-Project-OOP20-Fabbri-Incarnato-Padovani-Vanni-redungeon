package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.*;

public class Enemy extends Entity{

	Player player_parameter;
	
	public Enemy(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence, Player player) {
		super(x, y, id, level, hp, attack, magic_attack, defence);
		// TODO Auto-generated constructor stub
		this.player_parameter = player;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		x+=velX;
		y+=velY;
		velX=0;
		velY=0;
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.GREEN);
		g.drawRect(x, y, 32, 32);
	}

	@Override
	public void input(KeyEvent key) {
		// TODO Auto-generated method stub
		if(this.getY()<player_parameter.getY())
		{
				this.setvelY(1);
		}
		
		if(this.getY()>player_parameter.getY())
		{
				this.setvelY(-1);
		}
		
		if(this.getX()<player_parameter.getX())
		{
				this.setvelX(1);
		}
		
		if(this.getX()>player_parameter.getX())
		{
				this.setvelX(-1);
		}
	}

}
