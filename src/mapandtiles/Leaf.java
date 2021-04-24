package mapandtiles;

import java.awt.Point;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
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

  public int y, x, w, h; // the position and size of this Leaf

  public Leaf leftChild; // the Leaf's left child Leaf
  public Leaf rightChild; // the Leaf's right child Leaf
  public Rectangle room; // the room that is inside this Leaf
  private Vector<Rectangle> halls; // hallways to connect this Leaf to other Leafs
  private final Map<Point, Tile> tilestate = new HashMap<>();
  private final SpriteSheet s;

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
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.s = s;
    
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
    if (w > h && w / h >= 1.25) {
      splitH = false;
    } else if (h > w && h / w >= 1.25) {
      splitH = true;
    }


    final int max = (splitH ? h : w) - minLeafSize; // determine the maximum height or width
    if (max <= minLeafSize) {
      return false; // the area is too small to split any more...
    }


    final int split = (int) (Math.random() * (max - minLeafSize + 1) 
        + minLeafSize); // determine where we're going to split
                                                                                  

    // create our left and right children based on the direction of the split
    if (splitH) {
      leftChild = new Leaf(x, y, w, split, s);
      rightChild = new Leaf(x, y + split, w, h - split, s);
    } else {
      leftChild = new Leaf(x, y, split, h, s);
      rightChild = new Leaf(x + split, y, w - split, h, s);
    }
    return true; // split successful!
  }

  /**
   * this function generates all the rooms 
   * and hallways for this Leaf and all of 
   * its children.
   *
   * @param tilestate2 the final state of tiles
   * @param rv vector of points
   */
  public void createRooms(final Map<Point, Tile> tilestate2, final Vector<Vector<Point>> rv) {
    if (leftChild != null || rightChild != null) {
      // this leaf has been split, so go into the children leafs
      if (leftChild != null) {
        leftChild.createRooms(tilestate2, rv);
      }
      if (rightChild != null) {
        rightChild.createRooms(tilestate2, rv);
      }
      if (leftChild != null && rightChild != null) {
        createHall(leftChild.getRoom(), rightChild.getRoom(), tilestate2);
      }
    } else {
      // this Leaf is the ready to make a room
      Point roomSize;
      Point roomPos;
      // the room can be between 3 x 3 tiles to the size of the leaf - 2.
      roomSize = new Point((int) (Math.random() * (w - 2 - 3) + 3), 
          (int) (Math.random() * (h - 2 - 3) + 3));
      // place the room within the Leaf, but don't put it right
      // against the side of the Leaf (that would merge rooms together)
      roomPos = new Point((int) (Math.random() * (w - roomSize.x - 1 - 1) + 1),
          (int) (Math.random() * (h - roomSize.y - 1 - 1) + 1));
      room = new Rectangle(x + roomPos.x, y + roomPos.y, roomSize.x, roomSize.y);
      final Vector<Point> roomph = new Vector<Point>();
      for (int a = room.x; a < room.x + room.width; a++) {
        for (int b = room.y; b < room.y + room.height; b++) {
          tilestate2.put(new Point(a, b), new Tile(new Point(a, b), TileType.ON, s));
          roomph.add(new Point(a, b));
        }

      }
      rv.add(roomph);

    }
  }

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

  public void createHall(Rectangle l, Rectangle r, Map<Point, Tile> tilestate2) {
    // now we connect these two rooms together with hallways.
    // this looks pretty complicated, but it's just trying to figure out which point
    // is where and then either draw a straight line, or a pair of lines to make a
    // right-angle to connect them.
    // you could do some extra logic to make your halls more bendy, or do some more
    // advanced things if you wanted.

    halls = new Vector<Rectangle>();

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
              new Tile(new Point(halls.get(a).x + b, halls.get(a).y + d), TileType.ON, s));
        }
      }
    }

  }

  public Map<Point, Tile> getTilemap() {
    return this.tilestate;
  } 
  
  ;
}
