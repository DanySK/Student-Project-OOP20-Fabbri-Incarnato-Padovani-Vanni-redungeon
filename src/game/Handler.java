package game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import entity.Boss;
import entity.Enemy;
import entity.Player;


/**
 * Class that manage game events.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see java.util.LinkedList
 * @see game.GameObject
 * @see game.Id
 * @see entity.Entity
 * @see entity.Player
 * @see entity.Enemy
 * @see entity.Boss
 * @see java.awt.Graphics2D
 */
public class Handler {

  protected final List<GameObject> object = new ArrayList<>();
  public boolean next = false;
  public boolean dead = false;
  public int point = 0;
  public int enemiesNumber = 0;

  /**
   * Make control on game events
   */
  public void tick() {
    for (int i = 0; i < object.size(); i++) {
      final GameObject tempobj = object.get(i);
      tempobj.tick();
      if (tempobj.getId() == Id.PLAYER) {
        final Player obj = (Player) tempobj;
        if (obj.isOut()) {
          next = true;
        }
        if (obj.isDead()) {
          this.dead = true;
          point = point + (obj.getInventory().getgems() * 500);
          this.removeObject(obj);
        }
      }

      if (tempobj.getId() == Id.ENEMY) {
        final Enemy obj = (Enemy) tempobj;
        if (obj.isDead()) {
          point += 10;
          this.removeObject(obj);
        }
      }

      if (tempobj.getId() == Id.BOSS) {
        final Boss obj = (Boss) tempobj;
        if (obj.isDead()) {
          point += 100;
          this.removeObject(obj);
        }
      }
    }

  }

  /**
   * render all the graphic.
   *
   * @param g graphics that draw all elements
   */
  public void render(final Graphics2D g) {
    for (int i = 0; i < object.size(); i++) {
      final GameObject tempobj = object.get(i);
      tempobj.render(g);
    }
  }

  /**
   * add a GameObject.
   *
   * @param object GameObject
   */
  public void addObject(final GameObject object) {
    if (object.getId() == Id.ENEMY) {
      enemiesNumber++;
      this.object.add(2, object);
    } else {
      this.object.add(object);
    }

  }

  /**
   * remove a GameObject.
   *
   * @param object GameObject
   */
  public void removeObject(final GameObject object) {
    if (object.getId() == Id.ENEMY) {
      enemiesNumber--;
    }
    this.object.remove(object);
  }

}
