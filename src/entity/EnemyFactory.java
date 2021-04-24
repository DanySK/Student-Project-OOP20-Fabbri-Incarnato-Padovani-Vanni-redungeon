package entity;

import game.CombatSystem;
import game.Id;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import mapandtiles.AbsFloor;
import mapandtiles.BossFloor;


/**
 * Enemy generator.
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see entity.Enemy
 * @see entity.Boss
 *
 */
public class EnemyFactory {

  /*
   * Constructor for normal enemy
   * 
   * @param x      horizontal position
   * @param y      vertical position
   * @param id     game.ID
   * @param combat Type of combat
   * @param level  Used for stats modifier
   * @param floor  Used for positioning
   * @param player Used for damage and statistics
   * 
   * @throws IOException
   * @throws LineUnavailableException
   * @throws UnsupportedAudioFileException
   */
  public Enemy normalEnemy(final int x, final int y, final Id id, final CombatSystem combat, 
      final int level, final AbsFloor floor, final Player p)
      throws IOException, LineUnavailableException, UnsupportedAudioFileException {
    return new Enemy(x, y, id, combat, level, floor, p);
  }

  /*
   * Constructor for boss entity
   * 
   * @param x      horizontal position
   * @param y      vertical position
   * @param id     game.ID
   * @param combat Type of combat
   * @param level  Used for stats modifier
   * @param floor  Used for positioning
   * @param player Used for damage and statistics
   * 
   * @throws IOException
   * @throws LineUnavailableException
   * @throws UnsupportedAudioFileException
   */
  public Boss commonBoss(final int x, final int y, final Id id, final CombatSystem combat, 
      final int level, final BossFloor floor, final Player p)
      throws IOException, LineUnavailableException, UnsupportedAudioFileException {
    return new Boss(x, y, id, combat, level, floor, p);
  }

}
