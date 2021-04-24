package utilities;

import java.awt.Point;

/**
 * axis-aligned bounding box, class for
 * the collisions.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see java.awt.Point
 *
 */
public class AABB {
  private Point pos;
  private final int width;
  private final int height;
  private final int size;

  /**
   * Constructor.
   *
   * @param p starting point of the box
   * @param w box width
   * @param h box height
   */
  public AABB(final Point p, final int w, final int h) {
    this.pos = p;
    this.width = w;
    this.height = h;
    this.size = Math.max(w, h);
  }

  /**
   * get the position of the box.
   *
   * @return position point
   */
  public Point getpos() {
    return pos;
  }

  /**
   * get the size of your box.
   *
   * @return box size
   */
  public int getsize() {
    return size;
  }

  /**
   * get the coordinate x.
   *
   * @return horizontal position
   */
  public int getX() {
    return pos.x;
  }

  /**
   * get the coordinate y.
   *
   * @return vertical position
   */
  public int getY() {
    return pos.y;
  }

  /**
   * Used in horizontal velocity movement.
   *
   * @param sum sum of actual pos.x
   */
  public void sumX(final int sum) {
    this.pos.x += sum;
  }

  /**
   * Used in vertical velocity movement.
   *
   * @param sum sum of actual pos.y
   */
  public void sumY(final int sum) {
    this.pos.y += sum;
  }

  /**
   * Set box position.
   *
   * @param p position point
   */
  public void setpos(final Point p) {
    this.pos = p;
  }

  /**
   * Used as control for hit on box.
   *
   * @param box the box of the object
   * @return true if collides, false if not
   */
  public boolean collides(final AABB box) {

    if (this.pos.x < box.pos.x + box.width && this.pos.x + this.width > box.pos.x 
        && this.pos.y < box.pos.y + box.height && this.pos.y + this.height > box.pos.y) {
      return true;
    }
    
    return false;


  }
}
