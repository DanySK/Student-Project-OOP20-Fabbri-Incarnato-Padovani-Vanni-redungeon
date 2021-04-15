package menu;

import java.awt.*;

import game.Window;


public class Menu extends Canvas{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5685652752362930028L;
	
	public static int width=1366;
	public static int height=768;
	
	public Menu() {
		
	//new game.Window(width,height,"Re:Dungeon");
	Menu1 m= new Menu1();
		
		
	}

	
	
}

class Menu1 extends Canvas  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2436650669201065499L;

	public void paint(Graphics g)  {
		
		g.setColor(Color.black);
		g.drawString("Re:Dungeon", Menu.width/2, Menu.height/5);
		
	}
}
