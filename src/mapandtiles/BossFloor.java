package mapandtiles;

import entity.Entity;
import game.GameObject;
import game.Id;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import utilities.AaBb;
import utilities.ResourceLoader;
import utilities.SpriteSheet;

/**
 * The same as the Floor but this class create
 * a "standard" floor for the boss level.
 *
 * @author Francesco
 * @author Luigi
 * @author Francesco
 * @author Francesco
 *
 */
public class BossFloor extends GameObject implements AbsFloor {

  private final int level;
  private final Map<Point, Tile> tilestate = new HashMap<>();
  private final int tilesize;
  private final int screenw;
  private final int screenh;
  private final int width;
  private final int height;
  //private int offsetX=0;
  //private int offsetY=0;
  private final SpriteSheet sprite;
  private final int border;
  
  /**
  * create a new boss floor.
  *
  * @param l level of the floor
  *
  * @param w width of the floor in pixels
  *
  * @param h height of the floor in pixels
  *
  * @param screenw width of the screen in pixels
  * 
  * @param screenh height of the screen in pixels
  *
  */
  public BossFloor(final int l, final int w, final int h, final int screenw,
      final int screenh) throws IOException {
    super(w, h, Id.FLOOR);
    this.level = l;
    this.height = screenh;
    this.width = screenw;
    this.screenw = screenw;
    this.screenh = screenh;
    this.tilesize = 32;
    this.border = 2;
    final ResourceLoader resource = new ResourceLoader();
    int floorseed = 2;
    if (l > 5 && l <= 10) {
      floorseed = 1;
    } else if (l > 10 && l <= 15) { 
      floorseed = 3; 
    } else if (l > 15) {
      floorseed = 4;
    }
    sprite = new SpriteSheet(ImageIO.read(resource.getStreamImage("tiles" + floorseed)));
    bossfloorGenner(this.width, this.height);
    // TODO Auto-generated constructor stub
  }
  
  /**
     * generate the floor.
     *
     * @param w width of the floor
     *
     * @param h height of the floor
     *
     */

  private void bossfloorGenner(final int w, final int h) {
    for (int i = border; i < w / tilesize - border; i++) {
      for (int j = border + 1; j < h / tilesize - border; j++) {
        this.tilestate.put(new Point(i, j), new Tile(new Point(i, j), TileType.ON, sprite));
      }
    }
    for (int i = 0; i < width / tilesize; i++) {
      for (int j = 0; j < height / tilesize; j++) {
        if (!(tilestate.containsKey(new Point(i, j)))) {
          this.tilestate.put(new Point(i, j), new Tile(new Point(i, j), TileType.OFF, sprite));
        }
      }
    }
    for (int i = 0; i < 3; i++) {
      powerstoneCreate();
    }
  }
  /**
     * create the exit at a given point.
     *
     * @param p point of the exit
     *
     */
  
  public void exitCreate(final Point p) {
    this.tilestate.replace(p, new Tile(p, TileType.EXIT, sprite));
  }
  
  @Override
  public void tick() {
  // TODO Auto-generated method stub
  }

  @Override
  public void move() {
  // TODO Auto-generated method stub
  }
  
  public Map<Point, Tile> getMap() {
    return this.tilestate;
  }
    
    
    
  @Override
  public void render(final Graphics2D g) {
    g.setColor(Color.gray);
    for (int i = 0; i < width / tilesize; i++) {
      for (int j = 0; j < height / tilesize; j++) {
        g.drawImage(tilestate.get(new Point(i, j)).getImg(), i * tilesize, j * tilesize, null);
      }
    }

    // TODO Auto-generated method stub
  }
  
  /**
     * replace the tile at point p with an ON tile.
     */
  
  public void setTile(final Point p) {
    tilestate.replace(p, new Tile(p, TileType.ON, sprite));
  }
  
  /**
     * places the boss' attacks in random valid positions.
     *
     * @param flame a flame to be placed
     *
     */
  
  public void placeFlames(final AaBb flame) {
    final int randx = (int) (Math.random() * (this.width / 32 - 4) + 2);
    final int randy = (int) (Math.random() * (this.height / 32 - 4) + 2);
    if (!new Point(randx, randy)
        .equals(new Point(screenw / (tilesize * 2), screenh / tilesize - 10))) {
      flame.setpos(new Point(randx, randy));
    } else {
      placeFlames(flame);
    }
  }
  
  /**
    * creates stones that help defeat the boss.
    */
  
  public void powerstoneCreate() {
    final int randx = (int) (Math.random() * (this.width / 32 - 5) + 3);
    final int randy = (int) (Math.random() * (this.height / 32 - 5) + 3);
    if (!new Point(randx, randy)
        .equals(new Point(screenw / (tilesize * 2), screenh / tilesize - 10))
        || tilestate.get(new Point(randx, randy)).gettype() != TileType.POWERSTONE) {
      this.tilestate.replace(new Point(randx, randy), new Tile(
          new Point(randx, randy), TileType.POWERSTONE, sprite));
    } else {
      powerstoneCreate();
    }
  }
  
  /**
     * places entity to their designated position.
     *
     * @param e an entity, usually the boss and the player
     *
     */
  
  public void placeEntity(final Entity e) {
    if (e.getId() == Id.PLAYER) {
      e.setX(screenw / (tilesize * 2));
      e.setY(screenh / tilesize - 10);
      e.setBox(new AaBb(new Point(screenw / (tilesize  * 2), screenh / tilesize - 10), 1, 2)); 
    }
    if (e.getId() == Id.BOSS) {
      e.setX(screenw / (tilesize * 2) - 1);
      e.setY(5);
      e.setBox(new AaBb(new Point(screenw / (tilesize * 2) - 1, 5), 6, 4));
    }
  }
  
  public void moveCam(final int x, final int y) {}

  @Override
  public void input(final KeyEvent key, final List<AaBb> collisions) {
  // TODO Auto-generated method stub
  }
  
  public int getLevel() {
    return this.level;
  }
  
  @Override
 public int getOffsetX() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getOffsetY() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getScreenw() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getScreenh() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getWidth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

}
