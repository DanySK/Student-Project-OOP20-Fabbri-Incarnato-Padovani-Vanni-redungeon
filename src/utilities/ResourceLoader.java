package utilities;

import java.io.InputStream;

/**
 * this class create an inputstream for all
 * the file inside the resource folder.
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 *
 */

public class ResourceLoader {

  public InputStream getStreamAudio(final String name) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return classLoader.getResourceAsStream(name + ".wav");
  }

  public InputStream getStreamImage(final String name) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return classLoader.getResourceAsStream(name + ".png");
  }

  public InputStream getStreamFont(final String name) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return classLoader.getResourceAsStream("fonts/" + name + ".ttf");
  }
}
