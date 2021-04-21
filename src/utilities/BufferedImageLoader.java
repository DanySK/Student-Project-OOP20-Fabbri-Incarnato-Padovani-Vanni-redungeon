package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Class for program images
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see java.awt.Image.BufferedImage
 * @see java.lang.String
 * @see javax.imageio.ImageIO
 */
public class BufferedImageLoader {

	private BufferedImage image;
	/**
	 * Load the chose image
	 * @param path The image path
	 * @return the chosen image
	 */
	public BufferedImage loadImage(String path)
	{
		try
		{
			image = ImageIO.read(getClass().getResource(path));
		} 	catch (IOException e)
			{
				e.printStackTrace();
			}
		
		return image;
	}
}
