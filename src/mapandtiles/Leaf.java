package mapandtiles;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utilities.SpriteSheet;

/**
 * Leaf is the class that manage the creation
 * of rooms and halls, it are put inside a "tree structure".
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 *
 */

public class Leaf {

  private final int minLeafSize;

  //the position and size of this Leaf
  public int coordY;
  public int coordX;
  public int width;
  public int height;

  public Leaf leftChild; // the Leaf's left child Leaf
  public Leaf rightChild; // the Leaf's right child Leaf
  public Rectangle room; // the room that is inside this Leaf
  private final Map<Point, Tile> tilestate = new HashMap<>();
  private final SpriteSheet sprite;

  /**
   * the costructor of Leaf, inizialize
   * the leaf.
   *
   * @param x coordinate x
   * @param y cordinate y
   * @param w width
   * @param h height
   * @param s sprisheet for image
   */
  public Leaf(final int x, final int y, final int w, final int h, final SpriteSheet s) {
    this.coordX = x;
    this.coordY = y;
    this.width = w;
    this.height = h;
    this.sprite = s;
    
    this.minLeafSize = 9;

  }

  /**
   * begin splitting the leaf into two children.
   *
   * @return true if possible, flase if not
   */
  public boolean split() {
    if (leftChild != null || rightChild != null) {
      return false; // we're already split! Abort!
    }


    // determine direction of split
    // if the width is >25% larger than height, we split vertically
    // if the height is >25% larger than the width, we split horizontally
    // otherwise we split randomly
    boolean splitH = Math.random() > 0.5;
    if (width > height && width / height >= 1.25) {
      splitH = false;
    } else if (height > width && height / width >= 1.25) {
      splitH = true;
    }

    // determine the maximum height or width
    final int max = (splitH ? height : width) - minLeafSize; 
    if (max <= minLeafSize) {
      return false; // the area is too small to split any more...
    }


    final int split = (int) (Math.random() * (max - minLeafSize + 1) 
        + minLeafSize); // determine where we're going to split
                                                                                  

    // create our left and right children based on the direction of the split
    if (splitH) {
      leftChild = new Leaf(coordX, coordY, width, split, sprite);
      rightChild = new Leaf(coordX, coordY + split, width, height - split, sprite);
    } else {
      leftChild = new Leaf(coordX, coordY, split, height, sprite);
      rightChild = new Leaf(coordX + split, coordY, width - split, height, sprite);
    }
    return true; // split successful!
  }

  /**
   * this function generates all the rooms 
   * and hallways for this Leaf and all of 
   * its children.
   *
   * @param tilestate2 the final state of tiles
   * @param rooms vector of points
   */
  public void createRooms(final Map<Point, Tile> tilestate2, final List<List<Point>> rooms) {
    if (leftChild != null || rightChild != null) {
      // this leaf has been split, so go into the children leafs
      if (leftChild != null) {
        leftChild.createRooms(tilestate2, rooms);
      }
      if (rightChild != null) {
        rightChild.createRooms(tilestate2, rooms);
      }
      if (leftChild != null && rightChild != null) {
        createHall(leftChild.getRoom(), rightChild.getRoom(), tilestate2);
      }
    } else {
      // this Leaf is the ready to make a room
      Point roomSize;
      Point roomPos;
      // the room can be between 3 x 3 tiles to the size of the leaf - 2.
      roomSize = new Point((int) (Math.random() * (width - 2 - 3) + 3), 
          (int) (Math.random() * (height - 2 - 3) + 3));
      // place the room within the Leaf, but don't put it right
      // against the side of the Leaf (that would merge rooms together)
      roomPos = new Point((int) (Math.random() * (width - roomSize.x - 1 - 1) + 1),
          (int) (Math.random() * (height - roomSize.y - 1 - 1) + 1));
      room = new Rectangle(coordX + roomPos.x, coordY + roomPos.y, roomSize.x, roomSize.y);
      final List<Point> roomph = new ArrayList<>();
      for (int a = room.x; a < room.x + room.width; a++) {
        for (int b = room.y; b < room.y + room.height; b++) {
          tilestate2.put(new Point(a, b), new Tile(new Point(a, b), TileType.ON, sprite));
          roomph.add(new Point(a, b));
        }

      }
      rooms.add(roomph);

    }
  }
  
  /**
   * create a rectangle that represents
   * the room created.
   *
   * @return the room
   */

  public Rectangle getRoom() {
    // iterate all the way through these leafs to find a room, if one exists.
    if (room != null) {
      return room;
    } else {
      Rectangle leftRoom = null;
      Rectangle rightRoom = null;
      if (leftChild != null) {
        leftRoom = leftChild.getRoom();
      }
      if (rightChild != null) {
        rightRoom = rightChild.getRoom();
      }
      if (leftRoom == null && rightRoom == null) {
        return null;
      } else if (rightRoom == null) {
        return leftRoom;
      } else if (leftRoom == null) {
        return rightRoom;
      } else if (Math.random() > 0.5) {
        return leftRoom;
      } else {
        return rightRoom;
      }

    }
  }
  
  /**
   * creation of all the halls that conect
   * the rooms.
   *
   * @param l left
   * @param r right
   * @param tilestate2 mappa dei tiles
   */

  public void createHall(final Rectangle l, final Rectangle r, final Map<Point, Tile> tilestate2) {
    // now we connect these two rooms together with hallways.
    // this looks pretty complicated, but it's just trying to figure out which point
    // is where and then either draw a straight line, or a pair of lines to make a
    // right-angle to connect them.
    // you could do some extra logic to make your halls more bendy, or do some more
    // advanced things if you wanted.

    List<Rectangle> halls; // hallways to connect this Leaf to other Leafs
    halls = new ArrayList<>();

    final Point point1 = new Point((int) Math.random() * (l.width - l.x - 2) + l.x + 1,
        (int) Math.random() * (l.height - l.y - 2) + l.y + 1);
    final Point point2 = new Point((int) Math.random() * (r.width - r.x - 2) + r.x + 1,
        (int) Math.random() * (r.height - r.y - 2) + r.y + 1);

    final int w = point2.x - point1.x;
    final int h = point2.y - point1.y;

    if (w < 0) {
      if (h < 0) {
        if (Math.random() < 0.5) {
          halls.add(new Rectangle(point2.x, point1.y, Math.abs(w) + 1, 1));
          halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h) + 1));
        } else {
          halls.add(new Rectangle(point2.x, point2.y, Math.abs(w) + 1, 1));
          halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(h) + 1));
        }
      } else if (h > 0) {
        if (Math.random() < 0.5) {
          halls.add(new Rectangle(point2.x, point1.y, Math.abs(w) + 1, 1));
          halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(h) + 1));
        } else {
          halls.add(new Rectangle(point2.x, point2.y, Math.abs(w) + 1, 1));
          halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h) + 1));
        }
      } else {
        halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
      }

    } else if (w > 0) {
      if (h < 0) {
        if (Math.random() < 0.5) {
          halls.add(new Rectangle(point1.x, point2.y, Math.abs(w) + 1, 1));
          halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(h) + 1));
        } else {
          halls.add(new Rectangle(point1.x, point1.y, Math.abs(w) + 1, 1));
          halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h) + 1));
        }
      } else if (h > 0) {
        if (Math.random() < 0.5) {
          halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
          halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(h)));
        } else {
          halls.add(new Rectangle(point1.x, point2.y, Math.abs(w), 1));
          halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
        }

      } else {
        halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
      }
    } else {
      if (h < 0) {
        halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h) + 1));
      } else if (h > 0) {
        halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
      }
    }
    for (int a = 0; a < halls.size(); a++) {
      for (int b = 0; b < halls.get(a).width; b++) {
        for (int d = 0; d < halls.get(a).height; d++) {
          tilestate2.put(new Point(halls.get(a).x + b, halls.get(a).y + d),
              new Tile(new Point(halls.get(a).x + b, halls.get(a).y + d), TileType.ON, sprite));
        }
      }
    }

  }

  public Map<Point, Tile> getTilemap() {
    return this.tilestate;
  } 
  
  ;
}
