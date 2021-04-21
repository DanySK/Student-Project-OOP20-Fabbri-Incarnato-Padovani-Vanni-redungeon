package utilities;

import java.awt.image.BufferedImage;
/**
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *	
 * @see java.awt.Image.BufferedImage
 */
public class SpriteSheet {

	private BufferedImage image;
	/**
	 * Set the sprite sheet image
	 * @param image
	 */
	public SpriteSheet(BufferedImage image)
	{
		this.image = image;
	}
	
	/**
	 * Work only for sprite sheet
	 * @param col horizontal image tile position 
	 * @param row vertical image tile position
	 * @param width
	 * @param height
	 * @return the selected tile from the sheet
	 */
	public BufferedImage grabImage(int col, int row, int width, int height)
	{
		BufferedImage img = image.getSubimage((col*width)-width, (row*height)-height, width, height);
		return img;
	}
}
