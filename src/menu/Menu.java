package menu;

import java.awt.*;
import javax.swing.*;

//import game.Window;


public class Menu{
	/**
	 * 
	 */
	
	public static int width=1366;
	public static int height=768;
	
	public Menu() {
		
	//new game.Window(width,height,"Re:Dungeon");
	final JFrame f =new JFrame("Re:dungeon");
	f.setSize(width,height);
	final JPanel panel = new JPanel();
	panel.setBackground(Color.BLACK);
	panel.setLayout(null);
	f.getContentPane().add(panel);
	
	final JButton b1=new JButton ("Inizia Gioco") ;
	final JButton b2=new JButton ("Opzioni") ;
	final JButton b3=new JButton ("Esci");
	panel.add ( b1 );
	panel.add ( b2 );
	panel.add ( b3 );
	
	Dimension size = b1.getPreferredSize () ;
	b1.setBounds ( width/2, height/5 , size.width, size.height );
	size = b2.getPreferredSize () ;
	b2 . setBounds (55 , 40 , size.width*3, size.height *3) ;
	
	
	
		
		
	}

	
	
}


class OptionsMenu{
	
	
	
}

class GameMenu{
	
}



