package menu;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import game.*;
import utilities.AABB;

public class HUD extends GameObject{

	/* this class need only the rendere, because is a graphics clas */
	private boolean hud_display = false;
	BufferedImage HUD_menu;
	
	
	public HUD(int x, int y, ID id) throws IOException {
		super(x, y, id);
		// TODO Auto-generated constructor stub
		HUD_menu = ImageIO.read(new File("data/HUD.png"));
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
        if(this.hud_display)
        {
        	g.drawImage(this.HUD_menu, this.x, this.x, null);
        }
	}
	public boolean isHud_display() {
		return hud_display;
	}

	public void setHud_display(boolean hud_display) {
		this.hud_display = hud_display;
	}
	
	
	
	
	

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void input(KeyEvent key, List<AABB> collisions) {
		// TODO Auto-generated method stub
		
	}

}
