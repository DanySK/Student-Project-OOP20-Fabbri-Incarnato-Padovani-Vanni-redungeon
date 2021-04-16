package utilities;

import java.awt.Font;

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
		CustomFont = new Font("data/font/pkmndpb.ftt", Font.BOLD, fontSize);
	}

	private void setCustomFont(int fontSize) {
		CustomFont = new Font("data/font/pkmndp.ftt", Font.PLAIN, fontSize);
	}
	
}