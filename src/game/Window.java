package game;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Class extended from Canvas, is the game window 
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see java.awt.Canvas
 * @see javax.swing.JFrame
 * @see java.awt.Dimension
 * @see game.Game
 */
public class Window extends Canvas {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -4180120783538854905L;
	/**
	 * Constructor
	 * @param width 	Window width
	 * @param height	Window height
	 * @param title		Window title
	 * @param game		Used to generate the game on the window
	 */
	public Window(int width,int height,String title, Game game) {
	JFrame frame = new JFrame(title);
	
	frame.setPreferredSize(new Dimension(width,height));
	frame.setMaximumSize(new Dimension(width,height));
	frame.setMinimumSize(new Dimension(width,height));
	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.add(game);
	frame.setVisible(true);
	game.start();
	}
}
