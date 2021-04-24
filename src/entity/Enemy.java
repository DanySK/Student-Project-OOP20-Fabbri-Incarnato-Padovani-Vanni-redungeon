package entity;

import game.CombatSystem;
import game.Id;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mapandtiles.AbsFloor;
import mapandtiles.TileType;
import utilities.AABB;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;
import utilities.SpriteSheet;

public class Enemy extends Entity {

  private int column;
  private final Player playerParameter;
  private AABB box1;
  private boolean collide;
  private long timer;
  private long lastime;
  private final int expGuaranteed;

  /*
   * Constructor
   * 
   * @param x      horizontal position
   * @param y      vertical position
   * @param id     game.ID
   * @param combat Type of combat
   * @param level  Used for stats modifier
   * @param floor  Used for positioning
   * @param player Used for damage and statistics
   * 
   * @throws IOException
   * @throws LineUnavailableException
   * @throws UnsupportedAudioFileException
   */
  
  public Enemy(final int x, final int y, final Id id, final CombatSystem combat, 
      final int level, final AbsFloor floor, final Player player)
      throws IOException, LineUnavailableException, UnsupportedAudioFileException {
    super(x, y, id, combat, level, floor);
    // TODO Auto-generated constructor stub
    
    this.column = 0;

    final ResourceLoader resource = new ResourceLoader();

    hpBar = ImageIO.read(resource.getStreamImage("hpbar"));
    sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("enemy1")));
    this.setBox(new AABB(new Point(this.cordX, this.cordY), 1, 2));
    this.playerParameter = player;
    this.imgMatrix = new BufferedImage[4][3];
    for (int row = 0; row < 4; row++) {
      for (int column = 0; column < 3; column++) {
        imgMatrix[row][column] = getSprite().grabImage(column + 1, row + 1, 38, 66);
      }
    }

    this.setDirection(Direction.LEFT);

    setImg(imgMatrix[0][1]);

    lastime = System.currentTimeMillis();
    timer = 0;

    this.setMaxHp(100);
    this.expGuaranteed = 30 + (this.getLevel() * 10);
    this.augmStat();
  }

  @Override
  public void tick() {
    timer += System.currentTimeMillis() - lastime;
    // TODO Auto-generated method stub
    if (timer >= 1500) {
      switch (this.getDirection()) {
        case LEFT:
          setImg(imgMatrix[1][this.column]);
          break;
  
        case DOWN:
          setImg(imgMatrix[0][this.column]);
          break;
  
        case RIGHT:
          setImg(imgMatrix[2][this.column]);
          break;
  
        case UP:
          setImg(imgMatrix[3][this.column]);
          break;
          
        default:
          break;
      }

      if (this.column == 2) {
        this.column = 0;
      } else {
        this.column++;
      }

      lastime = System.currentTimeMillis();
      timer = 0;
    }
  }

  @Override
  public void move() {
    // TODO Auto-generated method stub

    cordX += velX;
    cordY += velY;
    box.setpos(new Point(cordX, cordY));
    velX = 0;
    velY = 0;
  }

  @Override
  public void render(final Graphics2D g) {
    // TODO Auto-generated method stub

    g.setColor(Color.green);

    // with a proportion the render function set the hp of the monster

    if (this.getHp() > 0) {
      if (this.getMaxHp() / this.getHp() < 2) {

        g.fillRect((cordX - getFloor().getOffsetX()) * 32, (cordY - getFloor().getOffsetY() - 1) * 32 - 11,
            (this.getHp() * 54) / this.getMaxHp(), 14);

        g.drawImage(getHpBar(), (cordX - getFloor().getOffsetX()) * 32 - 14, 
            (cordY - getFloor().getOffsetY() - 2) * 32 + 19, null);
      } else if (this.getMaxHp() / this.getHp() <= 4 && this.getMaxHp() / this.getHp() >= 2) {
        g.setColor(Color.orange);
        g.fillRect((cordX - getFloor().getOffsetX()) * 32, (cordY - getFloor().getOffsetY() - 1) * 32 - 11,
            (this.getHp() * 54) / this.getMaxHp(), 14);

        g.drawImage(getHpBar(), (cordX - getFloor().getOffsetX()) * 32 - 14, 
            (cordY - getFloor().getOffsetY() - 2) * 32 + 19, null);
      } else if (this.getMaxHp() / this.getHp() > 4) {
        g.setColor(Color.red);
        g.fillRect((cordX - getFloor().getOffsetX()) * 32, (cordY - getFloor().getOffsetY() - 1) * 32 - 11,
            (this.getHp() * 54) / this.getMaxHp(), 14);

        g.drawImage(getHpBar(), (cordX - getFloor().getOffsetX()) * 32 - 14, 
            (cordY - getFloor().getOffsetY() - 2) * 32 + 19, null);
      }
    }
    g.drawImage(getImg(), (cordX - getFloor().getOffsetX()) * 32, 
        (cordY - getFloor().getOffsetY() - 1) * 32, null);

    g.setColor(Color.black);
    String level;
    level = String.valueOf(this.getLevel());
    if (this.getLevel() < 10) {
      g.setFont(new CustomFontUtil(true, 12).getCustomFont());
      g.drawString(level, (cordX - getFloor().getOffsetX()) * 32 - 7, 
          (cordY - getFloor().getOffsetY() - 2) * 32 + 33);
    } else if (this.getLevel() >= 10) {
      g.setFont(new CustomFontUtil(true, 12).getCustomFont());
      g.drawString(level, (cordX - getFloor().getOffsetX()) * 32 - 11, 
          (cordY - getFloor().getOffsetY() - 2) * 32 + 32);
    }
  }

  @Override
  public void input(final KeyEvent key, final List<AABB> collisions) {
    // TODO Auto-generated method stub
    box1 = new AABB(new Point(this.getBox().getX(), getBox().getY()), 1, 2);
    collisions.remove(box);
    collide = false;

    // the enemy find the position of the player like it is in a cartesian system

    if (this.getY() < playerParameter.getY()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX, this.cordY + 1)).gettype() 
          == TileType.OFF)) {
        this.changeDirection(Direction.DOWN);
        box1.sumY(1);
        this.setvelY(1);
      }
    }

    if (this.getY() > playerParameter.getY()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX, this.cordY - 1)).gettype() 
          == TileType.OFF)) {
        this.changeDirection(Direction.UP);
        box1.sumY(-1);
        this.setvelY(-1);
      }
    }

    if (this.getX() < playerParameter.getX()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX + 1, this.cordY + velY)).gettype() 
          == TileType.OFF)) {
        this.changeDirection(Direction.RIGHT);
        box1.sumX(1);
        this.setvelX(1);
      }
    }

    if (this.getX() > playerParameter.getX()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX - 1, this.cordY + velY)).gettype() 
          == TileType.OFF)) {
        this.changeDirection(Direction.LEFT);
        box1.sumX(-1);
        this.setvelX(-1);
      }
    }

    // try if there are any other entity in the position where i'm going to go, if
    // not the enemy move
    collisions.forEach(x -> {
      if (box1.collides(x)) {
        collide = true;
      }
    });
    if (!collide) {
      this.move();
    } else {

      this.setvelX(0);
      this.setvelY(0);

      if (box1.collides(playerParameter.getBox())) {
        this.setAttacking(true);
        getCombat().enemyAttack(this);
      }

    }

    collisions.add(box);
  }

  public int getExpGuaranteed() {
    return expGuaranteed;
  }

  @Override
  public final void augmStat() {

    final Random rng = new Random();
    final int attack = this.playerParameter.getDefence() 
        + (this.getLevel() * 2 - rng.nextInt(this.getLevel()));

    this.setAttack(attack);
    this.setMaxHp(this.getMaxHp() + (this.getLevel() * 10));
    this.setHp(this.getMaxHp());
    this.setDefence((int) this.playerParameter.getAttack() / 2 + this.getLevel());
  }

  public Player getPlayerparameter() {
    return this.playerParameter;
  }
  // attacco nemico = difesa del player + ((level*2)-random(level))

}
