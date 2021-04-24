package mapandtiles;

import entity.Entity;
import java.awt.Point;
import java.util.Map;
/**
 * An interface for the AbsFloor.
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 *
 */

public interface AbsFloor {
  void setTile(Point p);
  
  void placeEntity(Entity e);
  
  void moveCam(int x, int y);
  
  Map<Point, Tile> getMap();
  
  int getOffsetX();
  
  int getOffsetY();
  
  int getScreenw();
  
  int getScreenh();
  
  int getWidth();
  
  int getHeight();
  
}
