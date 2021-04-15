package utilities;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class CustomFontUtil {

	private static Font font;

	public static Font getFont() {
		return font;
	}

	public static void setFontBold(int fontSize) {
		try {
        	CustomFontUtil.font=Font.createFont( Font.TRUETYPE_FONT,
                new FileInputStream(new File("data/fonts/pkmndpb.ttf")) );
        } catch(Exception e) {
            System.out.println(e);
        }
        CustomFontUtil.font=font.deriveFont(Font.PLAIN, fontSize);
	}
	
	public static void setFontNormal(int fontSize) {
        
        try {
        	CustomFontUtil.font=Font.createFont( Font.TRUETYPE_FONT,
                new FileInputStream(new File("data/fonts/pkmndp.ttf")) );
        } catch(Exception e) {
            System.out.println(e);
        }
        CustomFontUtil.font=font.deriveFont(Font.PLAIN, fontSize);
	}
}