package utilities;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class CustomFontUtil {

	Font CustomFont;

	public Font getCustomFont() {
		return CustomFont;
	}

	public void setCustomFontBold(int fontSize) {
		CustomFont = new Font("data/font/pkmndpb.ftt", Font.PLAIN, fontSize);
	}

	public void setCustomFont(int fontSize) {
		CustomFont = new Font("data/font/pkmndp.ftt", Font.PLAIN, fontSize);
	}
	
}