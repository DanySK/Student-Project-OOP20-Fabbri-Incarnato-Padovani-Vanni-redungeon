package mapandtiles;

import java.io.IOException;

/**
 * A factory for create two typer of floow,
 * different each other.
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 * 
 *
 */
public class FloorFactory {

  public Floor standardFloor(final int l, final int w, final int h, final int screenw, 
      final int screenh) throws IOException {
    return new Floor(l, w, h, screenw, screenh);
  }

  public BossFloor bossFloor(final int l, final int w, final int h, final int screenw, 
      final int screenh) throws IOException {
    return new BossFloor(l, w, h, screenw, screenh);
  }

}
