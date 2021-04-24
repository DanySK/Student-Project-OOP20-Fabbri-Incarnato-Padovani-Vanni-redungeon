package entity;

import game.CombatSystem;
import game.Id;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mapandtiles.BossFloor;
import mapandtiles.TileType;
import utilities.AABB;
import utilities.ResourceLoader;
import utilities.SpriteSheet;

/*
 * Extended from Entity, is a particular type of Enemy with some different
 * function
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see game.CombatSystem
 *
 */
public class Boss extends Entity {

  private int column;
  private final Player playerParameter;
  private AABB box1;
  private boolean collide;
  private long timer;
  private long lastime;
  private final int hpBarx;
  private final int hpBary;

  private final int expGuaranteed;

  private BufferedImage[][] flameImgMatrix;
  private BufferedImage flameImg;

  /*
   * Extended from Entity is an Enemy with particular statistic
   */
  private final List<AABB> flames = new ArrayList<>();

  /*
   * Constructor
   * 
   * @param x horizontal position
   * 
   * @param y vertical position
   * 
   * @param id game.ID
   * 
   * @param combat Type of combat
   * 
   * @param level Used for stats modifier
   * 
   * @param floor Used for positioning
   * 
   * @param player Used for damage and statistics
   * 
   * @throws IOException
   * 
   * @throws LineUnavailableException
   * 
   * @throws UnsupportedAudioFileException
   */
  public Boss(final int x, final int y, final Id id, final CombatSystem combat, 
      final int level, final BossFloor floor, final Player player)
      throws IOException, LineUnavailableException, UnsupportedAudioFileException {
    super(x, y, id, combat, level, floor);
    // TODO Auto-generated constructor stub

    this.column = 0;

    SpriteSheet sprite2;
    final ResourceLoader resource = new ResourceLoader();

    hpBar = ImageIO.read(resource.getStreamImage("bosshpbar"));
    hpBarx = x - getFloor().getOffsetX();
    hpBary = y - getFloor().getOffsetY();

    final Random rng = new Random();

    switch (rng.nextInt(3)) {
      case 0:
        sprite2 = new SpriteSheet(ImageIO.read(resource.getStreamImage("flame")));
        sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("boss1")));
        this.setAttribute(Attribute.FIRE);
        break;

      case 1:
        sprite2 = new SpriteSheet(ImageIO.read(resource.getStreamImage("waternado")));
        sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("boss3")));
        this.setAttribute(Attribute.WATER);
        break;
  
      case 2:
        sprite2 = new SpriteSheet(ImageIO.read(resource.getStreamImage("leafnado")));
        sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("boss2")));
        this.setAttribute(Attribute.GRASS);
        break;
  
      default:
        sprite2 = new SpriteSheet(ImageIO.read(resource.getStreamImage("leafnado")));
        break;
    }

    this.flameImgMatrix = new BufferedImage[1][3];
    for (int column = 0; column < 3; column++) {
      flameImgMatrix[0][column] = sprite2.grabImage(column + 1, 1, 32, 32);
    }

    this.setBox(new AABB(new Point(this.cordX, this.cordY), 6, 4));
    this.playerParameter = player;
    this.imgMatrix = new BufferedImage[4][3];
    for (int row = 0; row < 4; row++) {
      for (int column = 0; column < 3; column++) {
        imgMatrix[row][column] = getSprite().grabImage(column + 1, row + 1, 192, 128);
      }
    }

    final int flameCounter = rng.nextInt(15) + 10;

    for (int i = 0; i < flameCounter; i++) {
      final AABB flame = new AABB(new Point(0, 0), 1, 1);
      floor.placeFlames(flame);
      flames.add(flame);
    }

    this.setDirection(Direction.DOWN);

    setImg(imgMatrix[0][1]);

    lastime = System.currentTimeMillis();
    timer = 0;

    this.setMaxHp(500);
    this.expGuaranteed = 60 + (this.getLevel() * 10);
    this.augmStat();
  }

  @Override
  public void tick() {
    timer += System.currentTimeMillis() - lastime;
    // TODO Auto-generated method stub
    if (timer >= 1500) {
      switch (this.getDirection()) {
        case LEFT:
          setImg(imgMatrix[3][this.column]);
          break;
  
        case DOWN:
          setImg(imgMatrix[2][this.column]);
          break;
  
        case RIGHT:
          setImg(imgMatrix[1][this.column]);
          break;
  
        case UP:
          setImg(imgMatrix[0][this.column]);
          break;
        
        default:
          break;
      }

      flameImg = flameImgMatrix[0][column];

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

        g.fillRect(hpBarx * 32 + 48, hpBary * 32 + 10, (this.getHp() * 906) / this.getMaxHp(), 44);

      } else if (this.getMaxHp() / this.getHp() <= 4 && this.getMaxHp() / this.getHp() >= 2) {
        g.setColor(Color.orange);
        g.fillRect(hpBarx * 32 + 48, hpBary * 32 + 10, (this.getHp() * 906) / this.getMaxHp(), 44);
      } else if (this.getMaxHp() / this.getHp() > 4) {
        g.setColor(Color.red);
        g.fillRect(hpBarx * 32 + 48, hpBary * 32 + 10, (this.getHp() * 906) / this.getMaxHp(), 44);
      }
    }

    for (final AABB x : this.flames) {
      g.drawImage(flameImg, (x.getX()) * 32, (x.getY()) * 32, null);
    }

    g.drawImage(getHpBar(), hpBarx, hpBarx, null);

    g.drawImage(getImg(), (cordX - getFloor().getOffsetX()) * 32, 
        (cordY - getFloor().getOffsetY() - 1) * 32, null);
  }
  
  @Override
  public void input(final KeyEvent key, final List<AABB> collisions) {
    // TODO Auto-generated method stub
    box1 = new AABB(new Point(this.getBox().getX(), getBox().getY()), 6, 4);
    collisions.remove(box);
    collide = false;

    /*
     * the enemy finds the position of the player like it is in a cartesian system
     */

    if (this.getY() + 3 < playerParameter.getY()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX, this.cordY + 1)).gettype() 
          == TileType.OFF)) {
        this.setDirection(Direction.DOWN);
        box1.sumY(1);
        this.setvelY(1);
      }
    }

    if (this.getY() > playerParameter.getY()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX, this.cordY - 1)).gettype() 
          == TileType.OFF)) {
        this.setDirection(Direction.UP);
        box1.sumY(-1);
        this.setvelY(-1);
      }
    }

    if (this.getX() + 5 < playerParameter.getX()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX + 1, this.cordY + velY)).gettype() 
          == TileType.OFF)) {
        this.setDirection(Direction.RIGHT);
        box1.sumX(1);
        this.setvelX(1);
      }
    }

    if (this.getX() > playerParameter.getX()) {
      if (!(this.getFloor().getMap().get(new Point(this.cordX - 1, this.cordY + velY)).gettype() 
          == TileType.OFF)) {
        this.setDirection(Direction.LEFT);
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
      if (box1.collides(playerParameter.getBox())) {
        this.getCombat().bossAttack(this);
      }

    }

    this.moveFlames();

    collisions.add(box);
  }

  /*
   * Boss special attack that moves around the map
   */
  public void moveFlames() {
    final Random rand = new Random();
    for (final AABB i : this.flames) {
      boolean flag = true;

      while (flag) {
        switch (rand.nextInt(8)) {
          // the south tile
          case 0:
            flag = this.flamesCollide(i, 0, -1);
            break;
  
          // the south-east tile
          case 1:
            flag = this.flamesCollide(i, +1, -1);
            break;
  
          // the east tile
          case 2:
            flag = this.flamesCollide(i, +1, 0);
            break;
  
          // the north-east tile
          case 3:
            flag = this.flamesCollide(i, +1, +1);
            break;
  
          // the north tile
          case 4:
            flag = this.flamesCollide(i, 0, +1);
            break;
  
          // the north-west tile
          case 5:
            flag = this.flamesCollide(i, -1, +1);
            break;
  
          // the west tile
          case 6:
            flag = this.flamesCollide(i, -1, 0);
            break;
  
          // the south-west tile
          case 7:
            flag = this.flamesCollide(i, -1, -1);
            break;
          
          default:
            break;

        }
      }

    }
  }

  /*
   * Collision for the flames so they never go out of the map or block with walls
   * 
   * @param box Collision box
   * @param a   horizontal modifiers
   * @param b   vertical modifiers
   * @return boolean
   */
  public boolean flamesCollide(final AABB box, final int a, final int b) {
    if (!(this.getFloor().getMap().get(new Point(box.getX() + a, box.getY() + b)).gettype() 
        == TileType.OFF)) {

      if (new AABB(new Point(box.getX() + a, box.getY() + b + 1), 1, 1)
          .collides(playerParameter.getBox())) {
        this.getCombat().flamesAttack();
      }

      this.flames.get(this.flames.indexOf(box)).setpos(new Point(box.getX() + a, box.getY() + b));

      return false;

    }

    return true;
  }

  /*
   * 
   * @return the experience given to player on kill
   */
  public int getExpGuaranteed() {
    return expGuaranteed;
  }

  /*
   * Set the experience given to player on kill
   * 
   * @param expGuaranteed experience modifier
   */

  /*
   * Used to get the boss floor
   * 
   * @return the boss floor to generate
   */
  public BossFloor getBossFloor() {
    return (BossFloor) this.getFloor();
  }

  @Override
  public final void augmStat() {

    final Random rng = new Random();
    int attack = (this.playerParameter.getDefence()
        + (this.getLevel() * 2 - rng.nextInt(this.getLevel()))) * 2;

    this.setAttack(attack);
    this.setMaxHp(this.getMaxHp() + (this.getLevel() * 10));
    this.setHp(this.getMaxHp());
    this.setDefence(playerParameter.getAttack());
  }

}