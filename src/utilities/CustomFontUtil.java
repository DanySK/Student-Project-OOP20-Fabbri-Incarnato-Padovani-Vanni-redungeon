package utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class CustomFontUtil {

	private Font CustomFont;
	
	public CustomFontUtil(boolean bold, int size) {
		if(bold) {
			this.setCustomFontBold(size);
		} else {
			this.setCustomFont(size);
		}
	}

	public Font getCustomFont() {
		return CustomFont;
	}

	private void setCustomFontBold(int fontSize) {
		try {
			CustomFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/pkmndpb.ttf")).deriveFont(Font.BOLD, fontSize);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		//new Font("data/fonts/pkmndpb.ttf", Font.PLAIN, fontSize);
	}

	private void setCustomFont(int fontSize) {
		try {
			CustomFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/pkmndp.ttf")).deriveFont(Font.PLAIN, fontSize);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		//new Font("data/fonts/pkmndp.ttf", Font.PLAIN, fontSize);
	}
	
}