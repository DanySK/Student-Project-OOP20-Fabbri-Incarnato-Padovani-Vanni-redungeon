package utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 * Class for personalized fonts
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 *
 * @see java.awt.Font
 *
 */
public class CustomFontUtil {

	private Font CustomFont;
	
	/**
	 * Constructor for fonts decision
	 * @param bold control for bold or plain font
	 * @param size font size
	 */
	public CustomFontUtil(boolean bold, int size) {
		if(bold) {
			this.setCustomFontBold(size);
		} else {
			this.setCustomFont(size);
		}
	}

	/**
	 * 
	 * @return the custom font setted
	 */
	public Font getCustomFont() {
		return CustomFont;
	}

	/**
	 * Set bold custom font
	 * @param fontSize
	 */
	private void setCustomFontBold(int fontSize) {
		try {
			CustomFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/pkmndpb.ttf")).deriveFont(Font.PLAIN, fontSize);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		//new Font("data/fonts/pkmndpb.ttf", Font.PLAIN, fontSize);
	}

	/**
	 * Set plain custom font
	 * @param fontSize
	 */
	private void setCustomFont(int fontSize) {
		try {
			CustomFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/pkmndp.ttf")).deriveFont(Font.PLAIN, fontSize);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		//new Font("data/fonts/pkmndp.ttf", Font.PLAIN, fontSize);
	}
	
}