package utilities;

import java.awt.image.BufferedImage;

/**
 * create a spreadsheet that can be divided
 * in columns and rows to get an accurate portion
 * of the image 
 *
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see java.awt.Image.BufferedImage
 */
public class SpriteSheet {

  private final BufferedImage image;

  /**
   * Set the sprite sheet image.
   *
   * @param image for "setting" the image for the sprite
   */
  public SpriteSheet(final BufferedImage image) {
    this.image = image;
  }

  /**
   * Work only for sprite sheet.
   *
   * @param col    horizontal image tile position
   * @param row    vertical image tile position
   * @param width  width of the image
   * @param height height of the image
   * @return the selected tile from the sheet
   */
  public BufferedImage grabImage(final int col, final int row, final int width, final int height) {
    return image.getSubimage(col * width - width, 
        row * height - height, width, height);
  }
}
