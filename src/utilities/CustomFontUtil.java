package utilities;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class CustomFontUtil {

	Font CustomFont;
	
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
		CustomFont = new Font("data/font/pkmndpb.ftt", Font.PLAIN, fontSize);
	}

	private void setCustomFont(int fontSize) {
		CustomFont = new Font("data/font/pkmndp.ftt", Font.PLAIN, fontSize);
	}
	
}