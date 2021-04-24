package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import mapandtiles.*;
import game.*;
import utilities.AABB;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;
import utilities.SpriteSheet;

/**
 * Extended from Entity is the player controlled to the user with keyboard
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see entity.Entity
 * @see game.GameObject
 * @see game.CombatSystem
 */
public class Player extends Entity {
  
  private boolean flag;
  private long timer;
  private long lastime;
  private int column;
  private final Inventory inventory;
  private int experience;
  private int maxExperience = 50;
  private int maxSpell = 1;
  private int spellRemain;
  private boolean magicAttacking;

  /*
   * 
   * Constructor
   * 
   * @param x            Horizontal starting position
   * 
   * @param y            Vertical starting position
   * 
   * @param id           Id for the entity taken from ID enum class
   * 
   * @param combat       Combat system for the entity
   * 
   * @param level        Entity level for stats augmentation
   * 
   * @param hp           Health point
   * 
   * @param attack       Attack damage
   * 
   * @param magic_attack Magic attack damage
   * 
   * @param defence      Player defense
   * 
   * @param floor        Used to take level and position
   * 
   * @throws IOException
   */
  public Player(final int x, final int y, final Id id, final CombatSystem combat, final int level, 
      final int hp, final int attack, final int magicAttack, final int defence, 
      final AbsFloor floor) throws IOException {
    super(x, y, id, combat, level, floor);
    this.setHp(hp);
    this.setMaxHp(hp);
    this.setAttack(attack);
    this.setMagicAttack(magicAttack);
    this.setDefence(defence);
    this.setAttribute(Attribute.FIRE);
    this.inventory = new Inventory();

    this.column = 0;
    this.timer = 0;

    final ResourceLoader resource = new ResourceLoader();

    this.setAttacking(false);
    hpBar = ImageIO.read(resource.getStreamImage("hpbar"));

    sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("player")));
    this.setBox(new AABB(new Point(this.cordX, this.cordY), 1, 2));
    this.imgMatrix = new BufferedImage[4][3];

    spellRemain = maxSpell;

    for (int row = 0; row < 4; row++) {
      for (int column = 0; column < 3; column++) {
        imgMatrix[row][column] = getSprite().grabImage(column + 1, row + 1, 34, 60);
      }
    }

    // this.setMaxExp();
    this.setDirection(Direction.LEFT);

    setImg(imgMatrix[0][1]);

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
    final Point pred = new Point(cordX + velX, cordY + velY);

    if (new AABB(pred, 1, 1).collides(
        this.getFloor().getMap().get(new Point(this.box.getpos().x + velX, 
            this.box.getpos().y + velY)).getbox()) 
        && this.getFloor().getMap().get(new Point(this.cordX + velX, this.cordY + velY)).gettype() 
        == TileType.OFF) {

      velX = 0;
      velY = 0;
    } else {
      if (this.getHp() < this.getMaxHp()) {
        this.setHp(this.getHp() + this.getLevel());
      }
      int opvelx; 
      int opvely;
      if (this.getX() - this.getFloor().getScreenw() / (32 * 2) > 0
          && this.getX() + this.getFloor()
          .getScreenw() / (32 * 2) < this.getFloor().getWidth() / 32) {
        opvelx = this.velX;
      } else {
        opvelx = 0;
      }
      if (this.getY() - this.getFloor().getScreenh() / (32 * 2) > 0
          && this.getY() + this.getFloor()
          .getScreenh() / (32 * 2) < this.getFloor().getHeight() / 32) {
        opvely = this.velY;
      } else {
        opvely = 0;
      }
      this.getFloor().moveCam(opvelx, opvely);
      cordX += velX;
      cordY += velY;
      box.setpos(new Point(cordX, cordY));
      if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.HEAL) {
        this.setHp(this.getMaxHp());
        this.getFloor().setTile(this.getBox().getpos());

        velX = 0;
        velY = 0;
      }
      if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.KEY) {
        this.inventory.gotKey();
        this.getFloor().setTile(this.getBox().getpos());

        velX = 0;
        velY = 0;
      }

      if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.POWERSTONE) {
        this.inventory.increasePowerStone();
        this.inventory.addGems(1);
        this.getFloor().setTile(this.getBox().getpos());
        this.getCombat().lowerBossStats();

        velX = 0;
        velY = 0;
      }
      if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.TELEPORT) {
        this.getFloor().placeEntity(this);
        velX = 0;
        velY = 0;
      }
      if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.TRAP) {
        this.setHp(this.getHp() - this.getHp() / 2);

        velX = 0;
        velY = 0;
      }
      if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.GEMSTONE) {
        this.inventory.addGems(1);
        this.getFloor().setTile(this.getBox().getpos());

        velX = 0;
        velY = 0;
      }
    }
  }

  @Override
  public void render(final Graphics2D g) {

    // Experience bar
    g.setColor(Color.blue);
    g.fillRect((cordX - getFloor().getOffsetX()) * 32, (cordY - getFloor().getOffsetY() + 1) * 32,
        (this.getActualExp() * 32) / this.getMaxExp(), 8);

    g.drawImage(null, (cordX - getFloor().getOffsetX()) * 32, 
        (cordY - getFloor().getOffsetY() - 1) * 32, null);

    g.setColor(Color.GREEN);

    if (this.getHp() > 0) {
      if (this.getMaxHp() / this.getHp() < 2) {

        g.fillRect((cordX - getFloor().getOffsetX()) * 32, 
            (cordY - getFloor().getOffsetY() - 1) * 32 - 11,
            (this.getHp() * 54) / this.getMaxHp(), 14);

        g.drawImage(getHpBar(), (cordX - getFloor().getOffsetX()) * 32 - 14, 
            (cordY - getFloor().getOffsetY() - 2) * 32 + 19,
            null);

      } else if (this.getMaxHp() / this.getHp() <= 4 && this.getMaxHp() / this.getHp() >= 2) {
        g.setColor(Color.orange);
        g.fillRect((cordX - getFloor().getOffsetX()) * 32, 
            (cordY - getFloor().getOffsetY() - 1) * 32 - 11,
            (this.getHp() * 54) / this.getMaxHp(), 14);

        g.drawImage(getHpBar(), (cordX - getFloor().getOffsetX()) * 32 - 14, 
            (cordY - getFloor().getOffsetY() - 2) * 32 + 19,
            null);
      } else if (this.getMaxHp() / this.getHp() > 4) {
        g.setColor(Color.red);
        g.fillRect((cordX - getFloor().getOffsetX()) * 32, 
            (cordY - getFloor().getOffsetY() - 1) * 32 - 11,
            (this.getHp() * 54) / this.getMaxHp(), 14);

        g.drawImage(getHpBar(), (cordX - getFloor().getOffsetX()) * 32 - 14, 
            (cordY - getFloor().getOffsetY() - 2) * 32 + 19,
            null);
      }
    }

    g.drawImage(getImg(), (cordX - getFloor().getOffsetX()) * 32, 
        (cordY - getFloor().getOffsetY() - 1) * 32, null);
    g.setColor(Color.black);
    // g.draw(getBounds()); g.setColor(Color.BLACK);

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

    final int e = key.getKeyCode();
    collisions.remove(box);
    flag = false;
    AABB box1;

    switch (e) {
      case KeyEvent.VK_W:
        this.changeDirection(Direction.UP);
        box1 = new AABB(new Point(box.getX(), box.getY() - 1), 1, 2);
        collisions.forEach(x -> {
          if (box1.collides(x)) {
            flag = true;
          }
        });
        if (!flag) {
          this.changeDirection(Direction.UP);
          this.setvelY(-1);
          // this.setvelX(0);
          this.move();
        }
        break;
      case KeyEvent.VK_A:
        this.changeDirection(Direction.LEFT);
        box1 = new AABB(new Point(box.getX() - 1, box.getY()), 1, 2);
        collisions.forEach(x -> {
          if (box1.collides(x)) {
            flag = true;
          }
        });
        if (!flag) {
          this.changeDirection(Direction.LEFT);
          this.setvelX(-1);
          // this.setvelY(0);
          this.move();
        }
        break;
  
      case KeyEvent.VK_S:
        this.changeDirection(Direction.DOWN);
        box1 = new AABB(new Point(box.getX(), box.getY() + 1), 1, 2);
        collisions.forEach(x -> {
          if (box1.collides(x)) {
            flag = true;
          }
        });
        if (!flag) {
          this.changeDirection(Direction.DOWN);
          this.setvelY(1);
          // this.setvelX(0);
          this.move();
        }
        break;
  
      case KeyEvent.VK_D:
        this.changeDirection(Direction.RIGHT);
        box1 = new AABB(new Point(box.getX() + 1, box.getY()), 1, 2);
        collisions.forEach(x -> {
          if (box1.collides(x)) {
            flag = true;
          }
        });
        if (!flag) {
          this.changeDirection(Direction.RIGHT);
          this.setvelX(1);
          // this.setvelY(0);
          this.move();
        }
        break;
  
      case KeyEvent.VK_LEFT:
        this.changeDirection(Direction.LEFT);
        break;
  
      case KeyEvent.VK_UP:
        this.changeDirection(Direction.UP);
        break;
  
      case KeyEvent.VK_DOWN:
        this.changeDirection(Direction.DOWN);
        break;
  
      case KeyEvent.VK_RIGHT:
        this.changeDirection(Direction.RIGHT);
        break;
  
      case KeyEvent.VK_J:
        this.setAttacking(true);
        getCombat().playerAttack();
        /* attack case */
        break;
  
      case KeyEvent.VK_K:
        if (this.spellRemain != 0) {
          this.setMagicAttacking(true);
          getCombat().playerMagicAttack();
        }
        // magic attack
        break;
        
      default:
        break;
    }

    collisions.add(box);
  }

  /*
   * Used to reset life and make control on experience gained from killed enemy
   */
  private void levelUp() {
    this.setLevel(this.getLevel() + 1);

    final int expOverflow = this.getActualExp() - this.getMaxExp();
    if (expOverflow > 0) {
      this.setExperience(expOverflow);
    } else {
      this.setExperience(0);
    }

    this.setMaxExp();
    // this.setMaxExp();

    /* stats augm. */
    this.augmStat();

  }

  /*
   * Used as setter for experience and control for level up
   * @param additionalExp
   */
  public void addExp(final int additionalExp) {
    this.experience += additionalExp;
    if (this.experience >= this.maxExperience) {
      this.levelUp();
    }
  }

  /*
   * Set player max experience
   */
  private void setMaxExp() {
    final int newMaxExp = this.maxExperience / 2;
    this.maxExperience = this.maxExperience + newMaxExp;
  }

  /*
   * 
   * @return the actual experience gained
   */
  private int getActualExp() {
    return this.experience;
  }

  /*
   * 
   * @return the max experience of the player
   */
  private int getMaxExp() {
    return this.maxExperience;
  }

  /*
   * Set the actual experience, used also for experience overflow with player
   * level up
   * 
   * @param exp
   */
  private void setExperience(final int exp) {
    this.experience = exp;
  }

  /*
   * Used as control for player floor change
   * 
   * @return boolean
   */
  public boolean isOut() {
    if (this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.EXIT
        || this.getFloor().getMap().get(this.box.getpos()).gettype() == TileType.LOCKEDEXIT
            && this.inventory.hasKey()) {
      this.inventory.clearInventory();
      this.spellRemain = this.maxSpell;
      return true;
    } else {
      return false;
    }
  }

  /*
   * 
   * @return the player inventory
   */
  public Inventory getInventory() {
    return this.inventory;
  }

  /*
   * Reduce the player spell usage when used
   */
  public void setSpells() {
    this.spellRemain--;
  }

  public int getSpells() {
    return this.spellRemain;
  }

  /*
   * 
   * @return a control if the player is attacking with the magic attack
   */
  public boolean isMagicAttacking() {
    return this.magicAttacking;
  }

  /*
   * Used for magic attack control
   * 
   * @param bool
   */
  public void setMagicAttacking(final boolean bool) {
    this.magicAttacking = bool;
  }

  @Override
  public void augmStat() {
    final Random rng = new Random();
    final int minRange = 3;
    final int maxRange = 6;
    final int range = maxRange - minRange + 1;

    this.setAttack(this.getAttack() + (rng.nextInt(range) + minRange));

    this.setDefence(this.getDefence() + (rng.nextInt(range) + minRange) - 2);

    this.setMagicAttack(this.getMagicAttack() + (rng.nextInt(range) + minRange));

    this.setMaxHp(this.getMaxHp() + ((rng.nextInt(range) + minRange) * 10));

    this.setHp(this.getMaxHp());

    if (this.getLevel() % 5 == 0) {
      this.maxSpell++;
    }
  }

  public void setFloor(final AbsFloor floor) {
    // TODO Auto-generated method stub
    this.floor = floor;
  }

}
