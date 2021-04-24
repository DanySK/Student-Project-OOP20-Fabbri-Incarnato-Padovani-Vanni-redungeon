package mapandtiles;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * set the corner in the map of all
 * the tiles.
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 *
 */
public class Maputil {
  /**
   * takes a point in input, checks the adjacent tiles to determine the right
   * sprite.
   *
   * @param tilestate the map to check
   * @param p         the position of the tile we have to determine
   * @return an enum used to assign the right sprite
   */
  public Corner cornercheck(final Map<Point, Tile> tilestate, final Point p) {
    final List<Point> neartiles = new ArrayList<>();
    final Point p00 = new Point(p.x - 1, p.y - 1);
    neartiles.add(p00);
    final Point p01 = new Point(p.x, p.y - 1);
    neartiles.add(p01);
    final Point p02 = new Point(p.x + 1, p.y - 1);
    neartiles.add(p02);
    final Point p10 = new Point(p.x - 1, p.y);
    neartiles.add(p10);
    final Point p12 = new Point(p.x + 1, p.y);
    neartiles.add(p12);
    final Point p20 = new Point(p.x - 1, p.y + 1);
    neartiles.add(p20);
    final Point p21 = new Point(p.x, p.y + 1);
    neartiles.add(p21);
    final  Point p22 = new Point(p.x + 1, p.y + 1);
    neartiles.add(p22);
    if (neartiles.stream().allMatch(e -> tilestate.get(e).gettype() != TileType.OFF)) {
      return Corner.INS;
    } else if (tilestate.get(p00).gettype() != TileType.OFF 
        && tilestate.get(p01).gettype() != TileType.OFF
        && tilestate.get(p10).gettype() != TileType.OFF 
        && tilestate.get(p20).gettype() != TileType.OFF
        && tilestate.get(p21).gettype() != TileType.OFF) {
      return Corner.CL;
    } else if (tilestate.get(p01).gettype() != TileType.OFF 
        && tilestate.get(p02).gettype() != TileType.OFF
        && tilestate.get(p12).gettype() != TileType.OFF 
        && tilestate.get(p22).gettype() != TileType.OFF
        && tilestate.get(p21).gettype() != TileType.OFF) {
      return Corner.CR;
    } else if (tilestate.get(p10).gettype() != TileType.OFF 
        && tilestate.get(p00).gettype() != TileType.OFF
        && tilestate.get(p01).gettype() != TileType.OFF 
        && tilestate.get(p02).gettype() != TileType.OFF
        && tilestate.get(p12).gettype() != TileType.OFF) {
      return Corner.CT;
    } else if (tilestate.get(p10).gettype() != TileType.OFF 
        && tilestate.get(p20).gettype() != TileType.OFF
        && tilestate.get(p21).gettype() != TileType.OFF 
        && tilestate.get(p22).gettype() != TileType.OFF
        && tilestate.get(p12).gettype() != TileType.OFF) {
      return Corner.CB;
    } else if (tilestate.get(p20).gettype() != TileType.OFF 
        && tilestate.get(p21).gettype() != TileType.OFF
        && tilestate.get(p10).gettype() != TileType.OFF) {
      return Corner.TR;
    } else if (tilestate.get(p12).gettype() != TileType.OFF 
        && tilestate.get(p21).gettype() != TileType.OFF
        && tilestate.get(p22).gettype() != TileType.OFF) {
      return Corner.TL;
    } else if (tilestate.get(p01).gettype() != TileType.OFF 
        && tilestate.get(p12).gettype() != TileType.OFF
        && tilestate.get(p02).gettype() != TileType.OFF) {
      return Corner.BL;
    } else if (tilestate.get(p00).gettype() != TileType.OFF 
        && tilestate.get(p10).gettype() != TileType.OFF
        && tilestate.get(p01).gettype() != TileType.OFF) {
      return Corner.BR;
    } else if (tilestate.get(p01).gettype() != TileType.OFF 
        && tilestate.get(p21).gettype() != TileType.OFF) {
      return Corner.CO;
    } else if (tilestate.get(p10).gettype() != TileType.OFF 
        && tilestate.get(p12).gettype() != TileType.OFF) {
      return Corner.CV;
    } else if (tilestate.get(p01).gettype() != TileType.OFF) {
      return Corner.S;
    } else if (tilestate.get(p10).gettype() != TileType.OFF) {
      return Corner.E;
    } else if (tilestate.get(p12).gettype() != TileType.OFF) {
      return Corner.W;
    } else if (tilestate.get(p21).gettype() != TileType.OFF) {
      return Corner.N;
    } else if (tilestate.get(p20).gettype() != TileType.OFF) {
      return Corner.NE;
    } else if (tilestate.get(p02).gettype() != TileType.OFF) {
      return Corner.SW;
    } else if (tilestate.get(p22).gettype() != TileType.OFF) {
      return Corner.NW;
    } else if (tilestate.get(p00).gettype() != TileType.OFF) {
      return Corner.SE;
    } else {
      return null;
    }

  }
}
