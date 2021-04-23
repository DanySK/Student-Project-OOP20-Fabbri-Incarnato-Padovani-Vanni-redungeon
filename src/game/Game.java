package game;

import java.awt.Canvas; 

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import mapandtiles.*;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;
import entity.*;
import menu.Difficulty;
import menu.HUD;

/**
 * 
 * Principal class that generate floor and entity and manage the game flow
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see java.awt.Canvas
 * @see java.lang.Runnable
 * @see java.lang.Thread
 * @see java.awt.Graphics2D
 * @see mapandtiles.AbsFloor
 * @see entity.Player
 * @see entity.EnemyFactory
 * @see game.Handler
 */
public class Game extends Canvas implements Runnable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647713295702872480L;
	
	public int WIDTH =1280, HEIGHT=WIDTH/12*9;
	public int MAPW=3600,MAPH=HEIGHT;
	
	private final int MAX_ENEMIES;
	
	private Thread thread;
	
	private BufferedImage gameover_bar;
	
	private Clip clip;
	private AudioInputStream audio;
	private FloatControl clip_volume;
	
	private Graphics2D g;
	private boolean running= false;
	private AbsFloor f;
	private FloorFactory ff =new FloorFactory();
	private Player p;
	private EnemyFactory ef;
	private int level=1;
	private Handler handler;
	private CombatSystem combat;
	private KeyInput keylistener;
	private HUD hud_menu;

	//private Clip clip;
	//private AudioInputStream audio;
	/**
	 * Constructor that generate the game object
	 * @param width		screen width
	 * @param height	screen height
	 * @param mapwidth	map width
	 * @param mapheight	map height
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public Game(int width, int height, int mapwidth, int mapheight, Difficulty difficulty, double music, double effect) throws IOException, LineUnavailableException, UnsupportedAudioFileException {

 		ResourceLoader resource = new ResourceLoader();
 		
		gameover_bar = ImageIO.read(resource.getStreamImage("GameOverBar"));
		
		clip = AudioSystem.getClip();
		audio= 
			    AudioSystem.getAudioInputStream(
			            new BufferedInputStream(
			                resource.getStreamAudio("GameBGM")
			            )
			         );
		clip.open(audio);
		clip_volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    	float dB = (float) (Math.log(music) / Math.log(10.0) * 20.0);
    	clip_volume.setValue(dB);
    	
		this.WIDTH=width;
		this.HEIGHT=height;
		this.MAPW=mapwidth;
		this.MAPH=mapheight;
		
		switch(difficulty)
		{
		case Easy:
			this.MAX_ENEMIES=15;
			break;
			
		case Hard:
			this.MAX_ENEMIES=45;
			break;
			
		case Normal:
			this.MAX_ENEMIES=30;
			break;
			
		default:
			this.MAX_ENEMIES=30;
			break;
		
		}
		
		handler=new Handler();
		combat = new CombatSystem(effect);
		keylistener = new KeyInput(handler);
		this.addKeyListener(keylistener);
		new Window(WIDTH,HEIGHT,"Re:Dungeon",this);
		this.f= ff.standardFloor(level,MAPW,MAPH,WIDTH,HEIGHT);
		handler.addObject((GameObject)f);
		this.p=new Player(15, 15, ID.Player, combat, 1, 200, 15, 50, 5,f);
		combat.addPlayer(p);
		f.placeEntity(p);
		handler.addObject(p);
		
		
		this.ef=new EnemyFactory();
		Enemy enemy=ef.normalEnemy(0, 0, ID.Enemy, combat, level, f, p);
		
		for(int j=0;j<level;j++) {
			f.placeEntity(enemy);
			handler.addObject(enemy);
			combat.addEnemy(enemy);
		}
		
		hud_menu=new HUD(32, 32, ID.HUD, p);
		handler.addObject(hud_menu);
	}
	
	/**
	 * Start game thread
	 */
	public synchronized void start() {
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		thread= new Thread(this);
		thread.start();
		running = true;
		
	}
	
	/**
	 * Stop game thread
	 */
	public synchronized void stop() {
		try {
			thread.join();
			running=false;
			clip.stop();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Runnable function for thread execution
	 */
	public void run() {
		long lastTime= System.nanoTime();
		double numberofticks=60.0;
		double nanoseconds= 1000000000/numberofticks;
		double delta=0;
		long timer= System.currentTimeMillis();
		while(running) {
			long now= System.nanoTime();
			delta+=(now-lastTime)/nanoseconds;
			lastTime=now;
			while(delta>=1) {
				try {
					tick();
				} catch (IOException | LineUnavailableException | UnsupportedAudioFileException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				delta=0;
			}
			if(running) {
				render();
			}
			
			if(System.currentTimeMillis()-timer>1000) {
				timer+=1000;
			
			}
		}
		
		stop();
		
	}
	
	/**
	 * Game updater that update for every moves done
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 * @throws InterruptedException
	 */
	private void tick() throws IOException, LineUnavailableException, UnsupportedAudioFileException, InterruptedException {
		
		handler.tick();
		if(handler.next) {
			handler.next=false;
			nextLevel();
		}
		
		if(keylistener.getMoves()>=50 && level%5!=0 && handler.enemies_number<=MAX_ENEMIES)
		{
			keylistener.setMoves();
			Enemy enemy=ef.normalEnemy(0, 0, ID.Enemy, combat, level, f, p);
			f.placeEntity(enemy);
			handler.addObject(enemy);
			combat.addEnemy(enemy);
		}
		
	}
	
	/**
	 * Generate graphics elements
	 */
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
		if(handler.dead) 
		{
			g.drawImage(gameover_bar, WIDTH/2-110, HEIGHT/2-70, null);
			
			g.setColor(Color.black);
			
			g.setFont(new CustomFontUtil(true, 50).getCustomFont());
			g.drawString("GAME OVER" , WIDTH/2-100,HEIGHT/2);
			g.drawString(String.valueOf(handler.point)+ " POINTS" , WIDTH/2-100,HEIGHT/2+50);
				
		}
		g.dispose();
		bs.show();
	}
	
	/**
	 * 
	 * Used to generate new tile layer and boss floor layer
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 */
	public void nextLevel() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		
		hud_menu.setDungeonLevel();
		
		p.addExp(level*10);
		
		for(int i=handler.object.size()-1;i>1;i--) {
			if(handler.object.get(i).getID()==ID.Enemy)
			{
				combat.removeEnemy((Enemy) handler.object.get(i));
				handler.removeObject(handler.object.get(i));
			}
		}
		level++;
		if(level%5 !=0) {
			this.f= ff.standardFloor(level,MAPW,MAPH,WIDTH,HEIGHT);
			handler.object.set(0, (GameObject) f);
			p.setFloor(f);
			f.placeEntity(p);
			for(int j=0;j<(level<=15 ? level:15);j++) {
				Enemy enemy=ef.normalEnemy(0, 0, ID.Enemy, combat, level, f, p);
				handler.addObject(enemy);
				combat.addEnemy(enemy);
				f.placeEntity(enemy);
			}
		}
		else if(level%5==0) {
			BossFloor bossf= ff.bossFloor(level,MAPW,MAPH,WIDTH,HEIGHT);
			this.f = bossf;
			handler.object.set(0, (GameObject) f);
			p.setFloor(f);
			f.placeEntity(p);
			Boss boss=ef.commonBoss(0, 0, ID.Boss, combat, level, bossf, p);
			handler.addObject(boss);
			combat.addBoss(boss);
			f.placeEntity(boss);
			
		}
		
		combat.setDungeonLevel();
		
	}
	

}
