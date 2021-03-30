package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import utilities.*;
import game.*;

public class Enemy extends Entity{
	
	Player player_parameter;
	AABB box1;
	
	public Enemy(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence, Player player) throws IOException{
		super(x, y, id, level, hp, attack, magic_attack, defence);
		// TODO Auto-generated constructor stub
		this.player_parameter = player;
		this.img = ImageIO.read(new File("data/megaman.png"));
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		box.sumX(this.getvelX());
		box.sumY(this.getvelY());
		x+=velX;
		y+=velY;
		velX=0;
		velY=0;
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.GREEN);
		g.drawImage(img, x, y, null);
	}

	@Override
	public void input(KeyEvent key) {
		// TODO Auto-generated method stub
		box1 = new AABB(new Point(this.getBox().getX(), getBox().getY()), 32, 32);
		
		if(this.getY()<player_parameter.getY())
		{
			box1.sumY(32);	
			this.setvelY(32);
		}
		
		if(this.getY()>player_parameter.getY())
		{
			box1.sumY(-32);	
			this.setvelY(-32);
		}
		
		if(this.getX()<player_parameter.getX())
		{
			box1.sumX(32);	
			this.setvelX(32);
		}
		
		if(this.getX()>player_parameter.getX())
		{
			box1.sumX(-32);	
			this.setvelX(-32);
		}
		
		if(!(box1.collides(player_parameter.getBox())))
			this.move();
		else {
			
			try {
		         Clip clip = AudioSystem.getClip();
		         clip.open(AudioSystem.getAudioInputStream(new File("data/bonk.wav")));
		         clip.start();
		      } catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		}
	}

}
