package game;

import java.awt.Canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import mapandtiles.*;
import entity.*;

//nabi

public class Game extends Canvas implements Runnable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647713295702872480L;
	
	public static final int WIDTH =1080, HEIGHT=WIDTH/12*9;
	public static final int MAPW=3600,MAPH=HEIGHT;
	
	private Thread thread;
	
	private Graphics2D g;
	private boolean running= false;
	private AbsFloor f;
	private Player p;
	private EnemyFactory ef;
	private int level=1;
	private Handler handler;
	private CombatSystem combat;
	private Clip clip;
	private AudioInputStream audio;

	public Game() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		
		clip = AudioSystem.getClip();
		audio= AudioSystem.getAudioInputStream(new File("data/cavalcata.wav"));
		clip.open(audio);
		
		handler=new Handler();
		combat = new CombatSystem();
		this.addKeyListener(new KeyInput(handler));
		new Window(WIDTH,HEIGHT,"Re:Dungeon",this);
		this.f= new Floor(level,MAPW,MAPH,WIDTH,HEIGHT);
		handler.addObject(f);
		this.p=new Player(15, 15, ID.Player, combat, 1, 200, 15, 10, 5,f);
		combat.addPlayer(p);
		f.placeEntity(p);
		handler.addObject(p);
		
		this.ef=new EnemyFactory();
		
		for(int j=0;j<level;j++) {
			Enemy enemy=ef.normalEnemy(0, 0, ID.Enemy, combat, level, 100, 7, 20, 8, f, p);
			f.placeEntity(enemy);
			handler.addObject(enemy);
			combat.addEnemy(enemy);
		}
	}
	
	public synchronized void start() {
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		thread= new Thread(this);
		thread.start();
		running = true;
		
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running=false;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		long lastTime= System.nanoTime();
		double numberofticks=60.0;
		double nanoseconds= 1000000000/numberofticks;
		double delta=0;
		long timer= System.currentTimeMillis();
		int frames=0;
		while(running) {
			long now= System.nanoTime();
			delta+=(now-lastTime)/nanoseconds;
			lastTime=now;
			while(delta>=1) {
				try {
					tick();
				} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delta=0;
			}
			if(running) {
				render();
			}
			frames++;
			
			if(System.currentTimeMillis()-timer>1000) {
				timer+=1000;
				frames=0;
			}
			
		}
		stop();
		
	}
	
	private void tick() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		handler.tick();
		if(handler.next) {
			handler.next=false;
			nextLevel();
		}
		
	}
	
	private void render() {
		BufferStrategy bs= this.getBufferStrategy();
		if(bs==null) {
			this.createBufferStrategy(3);
			return;
		}
		g= (Graphics2D) bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		combat.render(g);
		
		g.dispose();
		bs.show();
	}
	public void nextLevel() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		
		p.addExp(level*10);
		
		for(int i=handler.object.size()-1;i>1;i--) {
			if(handler.object.get(i).getID()==ID.Enemy)
				combat.removeEnemy((Enemy) handler.object.get(i));
			
			handler.removeObject(handler.object.get(i));
		}
		level++;
		if(level%5 !=0) {
			this.f= new Floor(level,MAPW,MAPH,WIDTH,HEIGHT);
			handler.object.set(0, (GameObject) f);
			for(int j=0;j<level;j++) {
				Enemy enemy=ef.normalEnemy(0, 0, ID.Enemy, combat, level, 100, 7, 20, 8, f, p);
				handler.addObject(enemy);
				combat.addEnemy(enemy);
				f.placeEntity(enemy);
			}
		}
		else if(level%5==0) {
			this.f= new BossFloor(level,MAPW,MAPH,WIDTH,HEIGHT);
			handler.object.set(0, (GameObject) f);
			Boss boss=ef.commonBoss(0, 0, ID.Boss, combat, level, 100, 30, 20, 10, f, p);
			handler.addObject(boss);
			f.placeEntity(boss);
			
		}
		
		p.setFloor(f);
		f.placeEntity(p);
	}
	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		new Game();
	}

}
