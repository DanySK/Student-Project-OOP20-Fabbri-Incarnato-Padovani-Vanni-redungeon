package game;

import entity.Boss;
import entity.Enemy;
import entity.Player;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mapandtiles.TileType;
import utilities.AaBb;
import utilities.ResourceLoader;
import utilities.SpriteSheet;


/**
 * Class used for entity combat system.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see entity.Player
 * @see entity.Enemy
 * @see java.util.List
 * @see entity.Boss
 * @see utilities.AaBb
 * @see java.awt.image.BufferedImage
 * @see javax.sound.sampled.AudioInputStream
 * 
 */
public class CombatSystem {

  private Player player;
  private Enemy enemy;
  private List<Enemy> enemies;
  private Boss boss;
  private AaBb directionBox;
  private boolean collide;
  private final BufferedImage punchImg;
  private final BufferedImage flameImg;
  private AaBb punchBox;
  private int dungeonLevel;

  private long timer;
  private long lastime;
  private final String enemyString;
  private final String bossString;

  private AaBb[] magicBoxes;

  private final Clip boneSound;

  private final Clip punchSound;

  private final Clip bossSound;

  /**
   * Constructor.
   *
   * @throws IOException If a function that handler
   *                     call doesn't read a file
   *                     
   * @throws LineUnavailableException   If a function that handler
   *                                    call doens't open a line beacuse
   *                                    it's unavailable
   *                                    
   * @throws UnsupportedAudioFileException  If an audio file isn't supported
   */
  public CombatSystem(final double effect) throws IOException, LineUnavailableException, 
      UnsupportedAudioFileException {

    enemyString = "enemy";
    bossString = "boss";
    
    final AudioInputStream boneAudio;
    final FloatControl boneVolume;
    final AudioInputStream punchAudio;
    final FloatControl punchVolume;
    final AudioInputStream bossAudio;
    final FloatControl bossVolume;
    
    final ResourceLoader resource = new ResourceLoader();
    final float dB;
    dB = (float) (Math.log(effect) / Math.log(10.0) * 20.0);
    
    this.dungeonLevel = 1;
    enemies = new ArrayList<>();

    final SpriteSheet sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("flame")));
    flameImg = sprite.grabImage(1, 1, 32, 32);

    punchImg = ImageIO.read(resource.getStreamImage("punch"));

    boneSound = AudioSystem.getClip();
    boneAudio = AudioSystem.getAudioInputStream(
        new BufferedInputStream(resource.getStreamAudio("bonk")));
    boneSound.open(boneAudio);
    boneVolume = (FloatControl) boneSound.getControl(FloatControl.Type.MASTER_GAIN);
    boneVolume.setValue(dB);

    punchSound = AudioSystem.getClip();
    punchAudio = AudioSystem.getAudioInputStream(
        new BufferedInputStream(resource.getStreamAudio("punch")));
    punchSound.open(punchAudio);
    punchVolume = (FloatControl) punchSound.getControl(FloatControl.Type.MASTER_GAIN);
    punchVolume.setValue(dB);

    bossSound = AudioSystem.getClip();
    bossAudio = AudioSystem.getAudioInputStream(
        new BufferedInputStream(resource.getStreamAudio("bossroar")));
    bossSound.open(bossAudio);
    bossVolume = (FloatControl) bossSound.getControl(FloatControl.Type.MASTER_GAIN);
    bossVolume.setValue(dB);

    timer = 0;
    lastime = 0;
  }

  /**
   * The audio for enemy hit.
   *
   * @return enemy hit sound
   */
  public Clip getBonk() {
    return boneSound;
  }

  /**
   * The audio for player hit.
   *
   * @return player hit sound
   */
  public Clip getPunch() {
    return punchSound;
  }

  /**
   * Add entity Player.
   *
   * @param player this need for all player parameter
   */
  public void addPlayer(final Player player) {
    this.player = player;
  }

  /**
   * Add entity enemy.
   *
   * @param enemy this need for all enemy parameter
   */
  public void addEnemy(final Enemy enemy) {
    this.enemies.add(enemy);
  }

  public void removeEnemy(final Enemy enemy) {
    this.enemies.remove(enemy);
  }

  /**
   * Add entity boss.
   *
   * @param boss this need for all boss parameter
   */
  public void addBoss(final Boss boss) {
    this.boss = boss;
  }

  /**
   * Update floor level when change floor.
   */
  public void setDungeonLevel() {
    this.dungeonLevel++;
  }

  /**
   * Generate player attack sprites.
   *
   * @param g Graphics2D
   */
  public void render(final Graphics2D g) {

    timer += System.currentTimeMillis() - lastime;
    if (this.player != null) {
      if (this.player.isAttacking()) {
        g.drawImage(punchImg, (this.punchBox.getX() - player.getFloor().getOffsetX()) * 32,
            (this.punchBox.getY() - player.getFloor().getOffsetY()) * 32, null);
      }

      if (this.player.isMagicAttacking()) {
        if (timer <= 500) {
          for (final AaBb box : this.magicBoxes) {
            g.drawImage(flameImg, (box.getX() - player.getFloor().getOffsetX()) * 32,
                (box.getY() - player.getFloor().getOffsetY() - 1) * 32, null);
          }

        } else {
          this.player.setMagicAttacking(false);
        }
      } else if (!this.player.isMagicAttacking()) {
        timer = 0;
      }
    }

    lastime = System.currentTimeMillis();
  }

  /**
   * Used when player make an attack.
   */
  public void playerAttack() {
    collide = false;

    switch (this.player.getDirection()) {
      case DOWN:
        directionBox = new AaBb(new Point(player.getX(), player.getY() + 1), 1, 2);
  
        if (!this.enemies.isEmpty()) {
          enemies.forEach(x -> {
            if (directionBox.collides(x.getBox())) {
              collide = true;
              enemy = x;
            }
          });
          this.damagePlayer(enemyString, collide);
        } else if (this.dungeonLevel % 5 == 0 && boss != null) {
          if (directionBox.collides(this.boss.getBox())) {
            this.collide = true;
            this.damagePlayer(bossString, collide);
          }
        }
  
        punchBox = new AaBb(new Point(player.getX(), player.getY() + 1), 1, 2);
  
        break;
  
      case LEFT:
        directionBox = new AaBb(new Point(player.getX() - 1, player.getY()), 1, 2);
  
        if (!this.enemies.isEmpty()) {
          enemies.forEach(x -> {
            if (directionBox.collides(x.getBox())) {
              collide = true;
              enemy = x;
            }
          });
          this.damagePlayer(enemyString, collide);
        } else if (this.dungeonLevel % 5 == 0 && boss != null) {
          if (directionBox.collides(this.boss.getBox())) {
            this.collide = true;
            this.damagePlayer(bossString, collide);
          }
        }
  
        punchBox = new AaBb(new Point(player.getX() - 1, player.getY() - 1), 1, 2);
  
        break;
  
      case RIGHT:
        directionBox = new AaBb(new Point(player.getX() + 1, player.getY()), 1, 2);
  
        if (!this.enemies.isEmpty()) {
          enemies.forEach(x -> {
            if (directionBox.collides(x.getBox())) {
              collide = true;
              enemy = x;
            }
          });
          this.damagePlayer(enemyString, collide);
        } else if (this.dungeonLevel % 5 == 0 && boss != null) {
          if (directionBox.collides(this.boss.getBox())) {
            this.collide = true;
            this.damagePlayer(bossString, collide);
          }
        }
  
        punchBox = new AaBb(new Point(player.getX() + 1, player.getY() - 1), 1, 2);
  
        break;
  
      case UP:
        directionBox = new AaBb(new Point(player.getX(), player.getY() - 1), 1, 2);
  
        if (!this.enemies.isEmpty()) {
          enemies.forEach(x -> {
            if (directionBox.collides(x.getBox())) {
              collide = true;
              enemy = x;
            }
          });
          this.damagePlayer(enemyString, collide);
        } else if (this.dungeonLevel % 5 == 0 && boss != null) {
          if (directionBox.collides(this.boss.getBox())) {
            this.collide = true;
            this.damagePlayer(bossString, collide);
          }
        }
  
        punchBox = new AaBb(new Point(player.getX(), player.getY() - 2), 1, 2);
  
        break;
      
      default:
        break;
    }
  }

  /**
   * Used when player take damage from enemy/boss.
   *
   * @param type    type of enemy that damage the player
   * @param collide boolean that control if the player box collides with entity
   *                attack
   */
  public void damagePlayer(final String type, final boolean collide) {
    if (this.player.getFloor().getMap().get(directionBox.getpos()).gettype() 
        != TileType.OFF) {
      if (type.equals(bossString)) {
        if (!boss.isDead()) {
          if (collide) {
            boss.setHp(boss.getHp() - (player.getAttack() - boss.getDefence()));

            if (!punchSound.isRunning()) {
              punchSound.loop(1);
            }

            if (boss.isDead()) {
              this.player.addExp(this.boss.getExpGuaranteed());
              boss.getBossFloor().exitCreate(boss.getBox().getpos());
            }
          }
        }
      } else if (type.equals(enemyString)) {
        if (collide) {
          enemy.setHp(enemy.getHp() - (player.getAttack() - enemy.getDefence()));

          if (!punchSound.isRunning()) {
            punchSound.loop(1);
          }

          if (enemy.isDead()) {
            this.player.addExp(this.enemy.getExpGuaranteed());
            this.removeEnemy(enemy);
          }
        }
      }
    }
  }

  /**
   * Used when the player use the magic attack.
   */
  public void playerMagicAttack() {
    this.player.setSpells();
    final AaBb[] magicBoxes = { 
        new AaBb(new Point(this.player.getX() - 1, this.player.getY() - 1), 1, 1),
        new AaBb(new Point(this.player.getX(), this.player.getY() - 1), 1, 1),
        new AaBb(new Point(this.player.getX() + 1, this.player.getY() - 1), 1, 1),
        new AaBb(new Point(this.player.getX() - 1, this.player.getY()), 1, 1),
        new AaBb(new Point(this.player.getX() + 1, this.player.getY()), 1, 1),
        new AaBb(new Point(this.player.getX() + 1, this.player.getY() + 1), 1, 1),
        new AaBb(new Point(this.player.getX() - 1, this.player.getY() + 1), 1, 1),
        new AaBb(new Point(this.player.getX() - 1, this.player.getY() + 2), 1, 1),
        new AaBb(new Point(this.player.getX(), this.player.getY() + 2), 1, 1),
        new AaBb(new Point(this.player.getX() + 1, this.player.getY() + 2), 1, 1) };

    this.magicBoxes = magicBoxes;

    if (this.dungeonLevel % 5 != 0 && !(this.enemies.isEmpty())) {
      for (final AaBb box : magicBoxes) {
        this.enemies.forEach(x -> {

          if (box.collides(x.getBox())) {
            collide = true;
            if (collide) {
              this.magicDamage(x);
            }
          }

        });

      }

      this.enemies = removethedead(enemies);

    } else if (this.dungeonLevel % 5 == 0 && !this.boss.isDead()) {
      this.magicDamageBoss();
    }

  }

  /**
   * Used when player kill enemy with agic attack.
   *
   * @param e Enemy
   */
  public void magicDamage(final Enemy e) {
    this.enemy = e;
    enemy.setHp(enemy.getHp() - this.player.getMagicAttack());

    if (enemy.isDead()) {
      this.player.addExp(enemy.getExpGuaranteed());
    }
  }

  /**
   * Used in boss floor for the moving boss magic attack.
   */
  public void magicDamageBoss() {
    switch (this.player.getInventory().getPowerStone()) {
      case 1:
        boss.setHp(boss.getHp() - (this.player.getMagicAttack() / 2));
        break;
  
      case 2:
        boss.setHp((int) ((boss.getHp() - (this.player.getMagicAttack()) / 1.5)));
        break;
  
      case 3:
        boss.setHp(boss.getHp() - this.player.getMagicAttack());
        break;
      
      default:
        break;

    }

    if (boss.isDead()) {
      this.player.addExp(this.boss.getExpGuaranteed());
      boss.getBossFloor().exitCreate(boss.getBox().getpos());

    }
  }

  /**
   * Attack of the normal enemy.
   *
   * @param enemy attack from enemy to player
   */
  public void enemyAttack(final Enemy enemy) {
    if (enemy.getAttack() - player.getDefence() > 0) {
      player.setHp(player.getHp() - (enemy.getAttack() - player.getDefence()));
    }

    boneSound.loop(1);
  }

  /**
   * Attack of the boss.
   *
   * @param boss attack from boss to player
   */
  public void bossAttack(final Boss boss) {
    if (boss.getAttack() - player.getDefence() > 0) {
      player.setHp(player.getHp() - (boss.getAttack() - player.getDefence()));
    }

    bossSound.loop(1);
  }

  /**
   * Boss flame attack damage output.
   */
  public void flamesAttack() {
    player.setHp(player.getHp() - ((int) (player.getMaxHp() * 20 / 100)));
  }

  /**
   * Decrease boss stats when player take power stone.
   */
  public void lowerBossStats() {
    switch (player.getInventory().getPowerStone()) {
      case 0:
        break;
  
      case 1:
        this.boss.setAttack(this.boss.getAttack() / 2);
        this.boss.setDefence(this.boss.getDefence() / 2);
        break;
  
      case 2:
        this.boss.setAttack(this.boss.getAttack() / 2);
        this.boss.setDefence(this.boss.getDefence() / 2);
        break;
  
      case 3:
        this.boss.setAttack(this.boss.getAttack() / 2);
        this.boss.setDefence(0);
        break;
      
      default:
        break;
    }
  }

  private List<Enemy> removethedead(final List<Enemy> enemies) {
    return enemies.stream().filter(x -> !x.isDead()).collect(Collectors.toList());
  }
}
