package menu;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import entity.Player;
import game.*;
import utilities.AABB;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;

public class HUD extends GameObject{

	/* this class need only the rendere, because is a graphics clas */
	private boolean hud_display = false;
	private BufferedImage HUD_menu;
	private BufferedImage key;
	protected Player player;
	protected int dungeon_level;
	
	
	public HUD(int x, int y, Id id, Player p) throws IOException {
		super(x, y, id);
		// TODO Auto-generated constructor stub
		
 		ResourceLoader resource = new ResourceLoader();
 		
		this.HUD_menu = ImageIO.read(resource.getStreamImage("HUD"));
		this.key = ImageIO.read(resource.getStreamImage("key"));
		this.player = p;
		this.dungeon_level = 1;
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
        if(this.hud_display)
        {
        	g.setFont(new CustomFontUtil(true, 18).getCustomFont());
        	g.drawImage(this.HUD_menu, this.cordX, this.cordX, null);
        	
        	g.drawString(String.valueOf(this.dungeon_level), 
        			this.cordX+60, this.cordY+24);
        	
        	g.drawString("x " + String.valueOf(this.player.getInventory().getgems()), 
        			this.cordX+60, this.cordY+54);
        	
        	g.drawString(String.valueOf(this.player.getAttack()), 
        			this.cordX+60, this.cordY+115);
        	
        	g.drawString(String.valueOf(this.player.getDefence()), 
        			this.cordX+60, this.cordY+148);
        	
        	g.drawString("x " +String.valueOf(this.player.getSpells()), 
        			this.cordX+60, this.cordY+181);
        	
        	g.setFont(new CustomFontUtil(true, 12).getCustomFont());
        	
        	g.drawString(String.valueOf(this.player.getHp())
        			+"/"+
        			String.valueOf(this.player.getMaxHp()), 
        			this.cordX+40, this.cordY+84);
        	
        	if(this.player.getInventory().hasKey())
        	{
            	g.drawImage(key, 
            			this.cordX+30, this.cordY+185, 
            			null);
        	}
        }
	}
	public boolean isHud_display() {
		return hud_display;
	}

	public void setHud_display(boolean hud_display) {
		this.hud_display = hud_display;
	}
	
	public void setDungeonLevel()
	{
		this.dungeon_level++;
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
