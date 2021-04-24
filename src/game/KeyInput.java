package game;

import entity.Entity;
import entity.Player;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import menu.Hud;
import utilities.AaBb;

/**
 * Class extended from KeyAdapter, used for user input.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see entity.Player
 * @see java.awt.event.KeyEvent
 * @see java.awt.event.KeyAdapter
 * @see game.Handler
 * @see javax.sound.sampled.FloatControl
 */
public class KeyInput extends KeyAdapter {

  private final Handler handler;
  private int moves;

  /**
   * Constructor.
   *
   * @param handler event controller
   */
  public KeyInput(final Handler handler) {
    this.handler = handler;
    this.moves = 0;
  }

  /**
   * Control events when user press a key.
   *
   * @param key the key pressed
   */
  public void keyPressed(final KeyEvent key) {

    final List<AaBb> collisions = new ArrayList<>();
    handler.object.stream().filter(x -> x.getId() != Id.FLOOR && x.getId() != Id.HUD)
        .forEach(x -> collisions.add(((Entity) x).getBox()));
    for (int i = 0; i < handler.object.size(); i++) {
      final GameObject tempobj = handler.object.get(i);

      if (tempobj.getId() == Id.PLAYER) {
        // ((Entity) tempobj).setAttacking(true);
        tempobj.input(key, collisions);
        if (key.getKeyCode() == KeyEvent.VK_A || key.getKeyCode() == KeyEvent.VK_S 
            || key.getKeyCode() == KeyEvent.VK_D || key.getKeyCode() == KeyEvent.VK_W) {
          this.moves++;
        }


      }

      if (key.getKeyCode() == KeyEvent.VK_A || key.getKeyCode() == KeyEvent.VK_S 
          || key.getKeyCode() == KeyEvent.VK_D || key.getKeyCode() == KeyEvent.VK_W 
          || key.getKeyCode() == KeyEvent.VK_J || key.getKeyCode() == KeyEvent.VK_K) {
        if (key.getKeyCode() != KeyEvent.VK_J && key.getKeyCode() != KeyEvent.VK_K) {
          if (tempobj.getId() == Id.PLAYER) {
            ((Player) tempobj).setAttacking(false);
          }
        }

        if (tempobj.getId() == Id.ENEMY) {
          tempobj.input(key, collisions);
        }
        if (tempobj.getId() == Id.BOSS) {
          tempobj.input(key, collisions);
        }
      }

      if (tempobj.getId() == Id.HUD && key.getKeyCode() == KeyEvent.VK_Q) {
        ((Hud) tempobj).setHudDisplay(true);
      }
    }
    collisions.clear();
  }

  /**
   * Control when user release the pressed key.
   *
   * @param e the event of the key pressed
   */
  public void keyReleased(final KeyEvent e) {
    final int key = e.getKeyCode();
    for (int i = 0; i < handler.object.size(); i++) {
      final GameObject tempobj = handler.object.get(i);
      if (tempobj.getId() == Id.ENEMY || tempobj.getId() == Id.PLAYER) {
        if (key == KeyEvent.VK_W) {
          tempobj.setvelY(0);
          ((Entity) tempobj).setMovement(false);
          ((Entity) tempobj).setAttacking(false);
        }
        if (key == KeyEvent.VK_A) {
          tempobj.setvelX(0);
          ((Entity) tempobj).setMovement(false);
          ((Entity) tempobj).setAttacking(false);
        }
        if (key == KeyEvent.VK_S) {
          tempobj.setvelY(0);
          ((Entity) tempobj).setMovement(false);
          ((Entity) tempobj).setAttacking(false);
        }
        if (key == KeyEvent.VK_D) {
          tempobj.setvelX(0);
          ((Entity) tempobj).setMovement(false);
          ((Entity) tempobj).setAttacking(false);
        }
        if (key == KeyEvent.VK_J) {
          tempobj.setvelX(0);
          ((Entity) tempobj).setMovement(false);
          ((Entity) tempobj).setAttacking(false);
        }
      }

      if (key == KeyEvent.VK_Q && tempobj.getId() == Id.HUD) {
        ((Hud) tempobj).setHudDisplay(false);
      }
    }
  }

  /**
   * How many steps the player done.
   *
   * @return the entity movement
   */
  public int getMoves() {
    return this.moves;
  }

  /**
   * Set the entity movement.
   */
  public void setMoves() {
    this.moves = 0;
  }

}
