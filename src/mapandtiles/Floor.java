package mapandtiles;

import entity.Enemy;
import entity.Entity;
import game.GameObject;
import game.Id;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import utilities.AaBb;
import utilities.ResourceLoader;
import utilities.SpriteSheet;

/**
 * generate a Floor with level, width ,
 * height,screen width and screen height.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 */
public class Floor extends GameObject implements AbsFloor {
  private final Maputil util = new Maputil();
  private final int level;
  private int screenw;
  private int screenh;
  private final int width;
  private final int height;
  private final Map<Point, Tile> tilestate = new HashMap<>();
  private final int tilesize;
  private final List<Leaf> leaves = new ArrayList<>();
  private final List<List<Point>> rooms = new ArrayList<>();
  private static final int MAX_LEAF_SIZE = 24;
  private final Random rand = new Random();
  private final SpriteSheet sprite;
  private int offsetX;
  private int offsetY;

  /**
   * Costructor.
   *
   * @param l       level of this floor
   * @param w       width of the floor in pixels
   * @param h       height of the floor in pixels
   * @param screenw width of the window in pixels
   * @param screenh height of the screen in pixels
   * @throws IOException  If a function that handler
   *                      call doesn't read a file
   */
  public Floor(final int l, final int w, final int h, final int screenw, 
      final int screenh) throws IOException {
    super(w, h, Id.FLOOR);
    this.level = l;
    this.height = h;
    this.width = w;
    this.screenw = screenw;
    this.screenh = screenh;
    this.tilesize = 32;
    this.offsetX = 0;
    this.offsetY = 0;
    
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
    this.floorGenner(this.width, this.height);
  }

  /**
   * generate a pseudorandom floor using bsp algorithm.
   *
   * @param w width of the map in pixels
   * @param h height of the map in pixels
   */
  private void floorGenner(final int w, final int h) {
    roomsCreate();
    healCreate();
    trapCreate();
    teleportCreate();
    gemstoneCreate();
    exitCreate();
    for (int i = 0; i < w / tilesize; i++) {
      for (int j = 0; j < h / tilesize; j++) {
        if (!(tilestate.containsKey(new Point(i, j)))) {
          this.tilestate.put(new Point(i, j), new Tile(new Point(i, j), TileType.OFF, sprite));
        }
      }
    }
    for (int p = 1; p < width / tilesize - 1; p++) {
      for (int l = 1; l < height / tilesize - 1; l++) {
        if (this.tilestate.get(new Point(p, l)).gettype() == TileType.OFF) {
          choosetile(new Point(p, l));

        }
      }
    }

  }

  // use a BSP algorithm to create the rooms and halls through a vector of Leaf
  private void roomsCreate() {
    final Leaf root = new Leaf(1, 1, width / tilesize - 1, height / tilesize - 1, this.sprite);
    leaves.add(root);
    boolean didsplit = true;
    while (didsplit) {
      didsplit = false;
      for (int i = 0; i < leaves.size(); i++) {
        final Leaf l = leaves.get(i);
        if (l.leftChild == null && l.rightChild == null) { // if this Leaf is not already split...
          // if this Leaf is too big, or 75% chance...
          if (l.width > MAX_LEAF_SIZE || l.height > MAX_LEAF_SIZE || rand.nextFloat() > 0.25) {
            if (l.split()) { // split the Leaf!
              // if we did split, push the child leafs to the List so we can loop into them
              // next
              leaves.add(l.leftChild);
              leaves.add(l.rightChild);
              didsplit = true;
            }
          }
        }
      }
    }
    root.createRooms(this.tilestate, rooms);

  }

  /**
   * creates the exit of this floor in a pseudorandom position inside a room.
   */
  private void exitCreate() {
    if ((this.level + 1) % 5 == 0) {
      final Random r = new Random();
      final int a = r.nextInt(rooms.size());
      final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
      this.tilestate.replace(rpos, new Tile(rpos, TileType.LOCKEDEXIT, sprite));
      final int b = r.nextInt(rooms.size());
      final Point kpos = rooms.get(b).get(r.nextInt(rooms.get(b).size()));
      this.tilestate.replace(kpos, new Tile(kpos, TileType.KEY, sprite));

    } else {
      final Random r = new Random();
      final int a = r.nextInt(rooms.size());
      final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
      this.tilestate.replace(rpos, new Tile(rpos, TileType.EXIT, sprite));
    }

  }

  // create a special heal tile
  private void healCreate() {
    final Random r = new Random();
    final int a = r.nextInt(rooms.size());
    final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
    if (tilestate.get(rpos).gettype() != TileType.EXIT) {
      this.tilestate.replace(rpos, new Tile(rpos, TileType.HEAL, sprite));
    }


  }

  /**
   * create a teleport tile.
   */
  private void teleportCreate() {
    final Random r = new Random();
    final int a = r.nextInt(rooms.size());
    final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
    if (tilestate.get(rpos).gettype() != TileType.EXIT) {
      this.tilestate.replace(rpos, new Tile(rpos, TileType.TELEPORT, sprite));
    }


  }

  /**
   * create a trap that damages the player.
   */
  private void trapCreate() {
    for (int i = 0; i <= this.width / 1000; i++) {
      final Random r = new Random();
      final int a = r.nextInt(rooms.size());
      final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
      if (tilestate.get(rpos).gettype() != TileType.EXIT) {
        this.tilestate.replace(rpos, new Tile(rpos, TileType.TRAP, sprite));
      }

    }
  }

  /**
   * creates a rare gem to pick up.
   */
  private void gemstoneCreate() {
    for (int i = 0; i <= this.width / 1000; i++) {
      final Random r = new Random();
      final int a = r.nextInt(rooms.size());
      final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
      if (tilestate.get(rpos).gettype() != TileType.EXIT) {
        this.tilestate.replace(rpos, new Tile(rpos, TileType.GEMSTONE, sprite));
      }

    }
  }

  /**
   * places the entity in a random available position. checks that no enemy gets
   * moved on top of the player and moves the camera focus on the player when it
   * moves it.
   *
   * @param e an entity to be placed
   */
  public void placeEntity(final Entity e) {
    final Random r = new Random();
    final int a = r.nextInt(rooms.size());
    final Point rpos = rooms.get(a).get(r.nextInt(rooms.get(a).size()));
    e.setX(rpos.x);
    e.setY(rpos.y);
    e.setBox(new AaBb(rpos, 1, 2));
    if (e.getId() == Id.ENEMY) {
      final Enemy tempen = (Enemy) e;
      if (tempen.getBox().collides(tempen.getPlayerparameter().getBox())) {
        placeEntity(e);
      }
    }
    if (e.getId() == Id.PLAYER) {
      if (this.tilestate.get(new Point(e.getX(), e.getY())).gettype() != TileType.ON) {
        placeEntity(e);
      }
      this.setCamera(e);
    }

  }

  /**
   * moves the camera on the entity so the view results centered if possible.
   *
   * @param e an entity, usually the player
   */
  public void setCamera(final Entity e) {
    if (e.getX() - screenw / (tilesize * 2) > 0 
        && e.getX() + screenw / (tilesize * 2) < width / tilesize) {
      this.offsetX = e.getX() - screenw / 64;
    } else if (e.getX() - screenw / (tilesize * 2) <= 0) {
      this.offsetX = 0;
    } else if (e.getX() + screenw / (tilesize * 2) >= width / tilesize) {
      this.offsetX = width / tilesize - screenw / tilesize;
    }
    if (e.getY() - screenh / (tilesize * 2) > 0 
        && e.getY() + screenh / (tilesize * 2) < height / tilesize) {
      this.offsetY = e.getY() - screenh / 64;
    } else if (e.getY() - screenh / (tilesize * 2) <= 0) {
      this.offsetY = 0;
    } else if (e.getY() + screenh / (tilesize * 2) >= height / tilesize) {
      this.offsetY = height / tilesize - screenh / tilesize;
    }

  }

  @Override
  public void render(final Graphics2D g) {
    g.setColor(Color.gray);
    for (int i = 0; i < screenw / tilesize; i++) {
      for (int j = 0; j < screenh / tilesize; j++) {
        if (tilestate.containsKey(new Point(i + offsetX, j + offsetY))) {
          g.drawImage(tilestate.get(new Point(i + offsetX, j + offsetY))
              .getImg(), i * tilesize, j * tilesize, null);
        } else {
          g.setColor(Color.black);
          g.fillRect(i, j, 32, 32);
        }

      }
    }

  }

  public int getOffsetX() {
    return this.offsetX;
  }

  public int getOffsetY() {
    return this.offsetY;
  }

  public void setOffsetX(final int x) {
    this.offsetX = x;
  }

  public void setOffsetY(final int y) {
    this.offsetY = y;
  }

  public int getScreenw() {
    return this.screenw;
  }

  public int getScreenh() {
    return this.screenh;
  }

  public void setScreenw(final int x) {
    this.screenw = x;
  }

  public void setScreenh(final int y) {
    this.screenh = y;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public Map<Point, Tile> getMap() {
    return this.tilestate;
  }

  /**
   * updates the offset of the map view.
   */
  public void moveCam(final int x, final int y) {
    // TODO Auto-generated method stub
    this.velX = x;
    this.velY = y;
    if (offsetX + velX < 0 || offsetX + velX > width / tilesize - screenw / tilesize) {
      velX = 0;
    }
    if (offsetY + velY < 0 || offsetY + velY > height / tilesize - screenh / tilesize) {
      velY = 0;
    }
    this.setOffsetX(offsetX += velX);
    this.setOffsetY(offsetY += velY);
    velX = 0;
    velY = 0;

  }

  /**
   * sets a tile to ON (called after picking up objects).
   *
   * @param p the position of the tile
   */
  public void setTile(final Point p) {
    tilestate.replace(p, new Tile(p, TileType.ON, sprite));
  }

  /**
   * used when creating a floor to choose the right image for a given tile.
   *
   * @param p the point of the tile on the map
   */
  private void choosetile(final Point p) {
    final Corner corner = util.cornercheck(tilestate, p);
    if (corner == Corner.INS) {
      tilestate.get(p).setImg(sprite.grabImage(3, 9, 32, 32));
    }
    if (corner == Corner.CR) {
      tilestate.get(p).setImg(sprite.grabImage(2, 8, 32, 32));
    }
    if (corner == Corner.CL) {
      tilestate.get(p).setImg(sprite.grabImage(1, 8, 32, 32));
    }
    if (corner == Corner.CT) {
      tilestate.get(p).setImg(sprite.grabImage(4, 8, 32, 32));
    }
    if (corner == Corner.CB) {
      tilestate.get(p).setImg(sprite.grabImage(3, 8, 32, 32));
    }
    if (corner == Corner.CO) {
      tilestate.get(p).setImg(sprite.grabImage(2, 9, 32, 32));
    }
    if (corner == Corner.CV) {
      tilestate.get(p).setImg(sprite.grabImage(1, 9, 32, 32));
    }
    if (corner == Corner.BL) {
      tilestate.get(p).setImg(sprite.grabImage(4, 7, 32, 32));
    }
    if (corner == Corner.BR) {
      tilestate.get(p).setImg(sprite.grabImage(3, 7, 32, 32));
    }
    if (corner == Corner.TL) {
      tilestate.get(p).setImg(sprite.grabImage(2, 7, 32, 32));
    }
    if (corner == Corner.TR) {
      tilestate.get(p).setImg(sprite.grabImage(1, 7, 32, 32));
    }
    if (corner == Corner.E) {
      tilestate.get(p).setImg(sprite.grabImage(4, 5, 32, 32));
    }
    if (corner == Corner.N) {
      tilestate.get(p).setImg(sprite.grabImage(2, 5, 32, 32));
    }
    if (corner == Corner.S) {
      tilestate.get(p).setImg(sprite.grabImage(1, 5, 32, 32));
    }
    if (corner == Corner.W) {
      tilestate.get(p).setImg(sprite.grabImage(3, 5, 32, 32));
    }
    if (corner == Corner.SW) {
      tilestate.get(p).setImg(sprite.grabImage(3, 6, 32, 32));
    }
    if (corner == Corner.NW) {
      tilestate.get(p).setImg(sprite.grabImage(1, 6, 32, 32));
    }
    if (corner == Corner.SE) {
      tilestate.get(p).setImg(sprite.grabImage(4, 6, 32, 32));
    }
    if (corner == Corner.NE) {
      tilestate.get(p).setImg(sprite.grabImage(2, 6, 32, 32));
    }

  }

  @Override
  public void input(final KeyEvent key, final List<AaBb> collisions) {

  }

  @Override
  public void move() {
    // TODO Auto-generated method stub

  }

  @Override
  public void tick() {
    // TODO Auto-generated method stub

  }

}
