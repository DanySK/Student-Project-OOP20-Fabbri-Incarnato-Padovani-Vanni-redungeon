package game;

import entity.Boss;
import entity.Enemy;
import entity.EnemyFactory;
import entity.Player;
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
import mapandtiles.AbsFloor;
import mapandtiles.BossFloor;
import mapandtiles.FloorFactory;
import menu.Difficulty;
import menu.Hud;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;

/*
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
public class Game extends Canvas implements Runnable {

  /*
   * 
   */
  private static final long serialVersionUID = -8647713295702872480L;

  private final int width;
  private final int height;
  private final int mapw;
  private final int maph;

  private final int maxEnemies;

  private Thread thread;

  private final BufferedImage gameOverBar;

  private final Clip clip;

  private boolean running;
  private AbsFloor floor;
  private final FloorFactory floorFactory = new FloorFactory();
  private final Player player;
  private final EnemyFactory enemyFactory;
  private int level = 1;
  private final Handler handler;
  private final CombatSystem combat;
  private final KeyInput keylistener;
  private final Hud hudMenu;
  
  /**
   * Constructor that generate the game object.
   *
   * @param width     screen width
   * @param height    screen height
   * @param mapwidth  map width
   * @param mapheight map height
   * 
   * @throws IOException    If a function that handler
   *                        call doesn't read a file
   *                        
   * @throws LineUnavailableException   If a function that handler
   *                                    call doens't open a line beacuse
   *                                    it's unavailable
   *                                    
   * @throws UnsupportedAudioFileException If an audio file isn't supported
   */
  public Game(final int width, final int height, final int mapwidth, final int mapheight, 
      final Difficulty difficulty, final double music, final double effect)
      throws IOException, LineUnavailableException, UnsupportedAudioFileException {
    
    this.running = false;


    AudioInputStream audio;
    
    final FloatControl clipVolume;
    
    final ResourceLoader resource = new ResourceLoader();

    gameOverBar = ImageIO.read(resource.getStreamImage("GameOverBar"));

    clip = AudioSystem.getClip();
    audio = AudioSystem.getAudioInputStream(
        new BufferedInputStream(resource.getStreamAudio("GameBGM")));
    clip.open(audio);
    clipVolume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    final float db = (float) (Math.log(music) / Math.log(10.0) * 20.0);
    clipVolume.setValue(db);

    this.width = width;
    this.height = height;
    this.mapw = mapwidth;
    this.maph = mapheight;

    switch (difficulty) {
      case EASY:
        this.maxEnemies = 15;
        break;
  
      case HARD:
        this.maxEnemies = 45;
        break;
  
      case NORMAL:
        this.maxEnemies = 30;
        break;
  
      default:
        this.maxEnemies = 30;
        break;

    }
    combat = new CombatSystem(effect);

    handler = new Handler();
    keylistener = new KeyInput(handler);
    this.addKeyListener(keylistener);
    new Window(width, height, "Re:Dungeon", this);
    this.floor = floorFactory.standardFloor(level, mapw, maph, width, height);
    handler.addObject((GameObject) floor);
    this.player = new Player(15, 15, Id.PLAYER, combat, 1, 200, 15, 50, 5, floor);
    combat.addPlayer(player);
    floor.placeEntity(player);
    handler.addObject(player);

    this.enemyFactory = new EnemyFactory();
    final Enemy enemy = enemyFactory.normalEnemy(0, 0, Id.ENEMY, combat, level, floor, player);

    for (int j = 0; j < level; j++) {
      floor.placeEntity(enemy);
      handler.addObject(enemy);
      combat.addEnemy(enemy);
    }

    hudMenu = new Hud(32, 32, Id.HUD, player);
    handler.addObject(hudMenu);
  }

  /**
   * Start game thread.
   */
  public synchronized void start() {

    clip.loop(Clip.LOOP_CONTINUOUSLY);

    thread = new Thread(this);
    thread.start();
    running = true;

  }

  /**
   * Stop game thread.
   * 
   */
  public synchronized void stop() {
    try {
      thread.join();
      running = false;
      clip.stop();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Runnable function for thread execution.
   */
  public void run() {
    long lastTime = System.nanoTime();
    final double numberofticks = 60.0;
    final double nanoseconds = 1_000_000_000 / numberofticks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    while (running) {
      final long now = System.nanoTime();
      delta += (now - lastTime) / nanoseconds;
      lastTime = now;
      while (delta >= 1) {
        try {
          tick();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException 
            | InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        delta = 0;
      }
      if (running) {
        this.render();
      }

      if (System.currentTimeMillis() - timer > 1000) {
        timer += 1000;

      }
    }

    stop();

  }

  /**
   * Game updater that update for every moves done.
   *
   * @throws IOException If a function that handler
   *                     call doesn't read a file
   *                  
   * @throws LineUnavailableException   If a function that handler
   *                                    call doens't open a line beacuse
   *                                    it's unavailable
   *                                    
   * @throws UnsupportedAudioFileException  If an audio file isn't supported
   * 
   * @throws InterruptedException   Thrown when a thread is waiting, sleeping, 
   *                                occupied, and the thread is interrupted, 
   *                                either before or during the activity
   */
  private void tick() throws IOException, LineUnavailableException, 
      UnsupportedAudioFileException, InterruptedException {

    handler.tick();
    if (handler.next) {
      handler.next = false;
      nextLevel();
    }

    if (keylistener.getMoves() >= 50 && level % 5 != 0 && handler.enemiesNumber <= maxEnemies) {
      keylistener.setMoves();
      final Enemy enemy = enemyFactory.normalEnemy(0, 0, Id.ENEMY, combat, level, floor, player);
      floor.placeEntity(enemy);
      handler.addObject(enemy);
      combat.addEnemy(enemy);
    }

  }

  /**
   * Generate graphics elements.
   */
  private void render() {
    Graphics2D graphics;
    
    final BufferStrategy bs = this.getBufferStrategy();
    if (bs == null) {
      this.createBufferStrategy(3);
      return;
    }
    graphics = (Graphics2D) bs.getDrawGraphics();

    graphics.setColor(Color.black);
    graphics.fillRect(0, 0, width, height);

    handler.render(graphics);
    this.combat.render(graphics);
    
    if (handler.dead) {
      graphics.drawImage(gameOverBar, width / 2 - 110, height / 2 - 70, null);

      graphics.setColor(Color.black);

      graphics.setFont(new CustomFontUtil(true, 50).getCustomFont());
      graphics.drawString("GAME OVER", width / 2 - 100, height / 2);
      graphics.drawString(handler.point + " POINTS", width / 2 - 100, height / 2 + 50);

    }
    graphics.dispose();
    bs.show();
  }

  /**
   * Used to generate new tile layer and boss floor layer.
   *
   *@throws IOException If a function that handler
   *                    call doesn't read a file
   *                  
   * @throws LineUnavailableException   If a function that handler
   *                                    call doens't open a line beacuse
   *                                    it's unavailable
   *                                    
   * @throws UnsupportedAudioFileException  If an audio file isn't supported
   * 
   */
  public void nextLevel() throws IOException, LineUnavailableException, 
      UnsupportedAudioFileException {

    hudMenu.setDungeonLevel();

    player.addExp(level * 10);

    for (int i = handler.object.size() - 1; i > 1; i--) {
      if (handler.object.get(i).getId() == Id.ENEMY) {
        combat.removeEnemy((Enemy) handler.object.get(i));
        handler.removeObject(handler.object.get(i));
      }
    }
    level++;
    if (level % 5 != 0) {
      this.floor = floorFactory.standardFloor(level, mapw, maph, width, height);
      handler.object.set(0, (GameObject) floor);
      player.setFloor(floor);
      floor.placeEntity(player);
      for (int j = 0; j < (level <= 15 ? level : 15); j++) {
        final Enemy enemy = enemyFactory.normalEnemy(0, 0, Id.ENEMY, combat, level, floor, player);
        handler.addObject(enemy);
        combat.addEnemy(enemy);
        floor.placeEntity(enemy);
      }
    } else if (level % 5 == 0) {
      final BossFloor bossf = floorFactory.bossFloor(level, mapw, maph, width, height);
      this.floor = bossf;
      handler.object.set(0, (GameObject) floor);
      player.setFloor(floor);
      floor.placeEntity(player);
      final Boss boss = enemyFactory.commonBoss(0, 0, Id.BOSS, combat, level, bossf, player);
      handler.addObject(boss);
      combat.addBoss(boss);
      floor.placeEntity(boss);

    }

    combat.setDungeonLevel();

  }

}
