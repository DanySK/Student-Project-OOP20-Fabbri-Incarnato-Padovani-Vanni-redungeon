package mapandtiles;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import utilities.AABB;
import utilities.SpriteSheet;

/**
 * this class put the right images on
 * the various type of tiles.
 *
 * @author Francesco
 * @author Luis
 * @author Leroy
 * @author Matteo
 *
 */
public class Tile {
  private final AABB box;
  private TileType tiles;
  private BufferedImage img;

  /**
   * create a new tile.
   *
   * @param p the position of the tile
   * @param t the type of tile
   * @param s the spritesheet of this floor to pick up tile's image
   */
  public Tile(final Point p, final TileType t, final SpriteSheet s) {
    final SpriteSheet sprite;
    this.box = new AABB(p, 1, 1);
    this.tiles = t;
    sprite = s;
    final Random rand = new Random();
    if (this.tiles == TileType.ON) {
      final int intRandom = rand.nextInt(4);

      this.img = sprite.grabImage(intRandom + 1, 1, 32, 32);
    }
    if (this.tiles == TileType.OFF) {
      final int intRandom = rand.nextInt(2);
      this.img = sprite.grabImage(intRandom + 1, 2, 32, 32);
    }
    if (this.tiles == TileType.EXIT) {
      this.img = sprite.grabImage(1, 3, 32, 32);
    }
    if (this.tiles == TileType.HEAL) {
      this.img = sprite.grabImage(1, 4, 32, 32);
    }
    if (this.tiles == TileType.LOCKEDEXIT) {
      this.img = sprite.grabImage(2, 3, 32, 32);
    }
    if (this.tiles == TileType.KEY) {
      this.img = sprite.grabImage(3, 4, 32, 32);
    }
    if (this.tiles == TileType.POWERSTONE) {
      this.img = sprite.grabImage(4, 4, 32, 32);
    }
    if (this.tiles == TileType.TELEPORT) {
      this.img = sprite.grabImage(4, 4, 32, 32);
    }
    if (this.tiles == TileType.TRAP) {
      this.img = sprite.grabImage(4, 4, 32, 32);
    }
    if (this.tiles == TileType.GEMSTONE) {
      this.img = sprite.grabImage(4, 4, 32, 32);
    }

  }

  /**
   * set this tile's sprite.
   *
   * @param im the sprite
   */
  public void setImg(final BufferedImage im) {
    this.img = im;
  }

  public AABB getbox() {
    return this.box;
  }

  public void setSprite(final File f) {

  }

  public BufferedImage getImg() {
    return this.img;
  }

  public void settype(final TileType t) {
    this.tiles = t;
  }

  public TileType gettype() {
    return this.tiles;
  }
}
