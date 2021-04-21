package menu;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import game.Game;
import utilities.*;


public class Menu extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8098037462564546327L;
	
	private Clip menu_sound;
	private AudioInputStream menu_audio;
	
	public static int width=800;
	public static int height=600;
	public static int mapwidth=2000;
	public static int mapheight=2000;
	
	public Menu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		
	final JFrame f =new JFrame("Re:dungeon");
	f.setSize(width,height);
	final JPanel menupanel = new JPanel();
	menupanel.setBackground(Color.BLACK);
	menupanel.setLayout(null);
	f.getContentPane().add(menupanel);
	f.setVisible(true);
	
	menu_sound = AudioSystem.getClip();
	menu_audio= AudioSystem.getAudioInputStream(new File("data/RansLoveTheme.wav"));
	menu_sound.open(menu_audio);
	
	menu_sound.loop(Clip.LOOP_CONTINUOUSLY);
	final JPanel Optionspanel= new JPanel();
	
	
	String[] resolution = {"800x600","1280x768","1440x900","1920x1080"};
	String[] difficulty = {"Facile","Normale","Difficile"};
	
	JComboBox<?> comboBox = new JComboBox<Object>(resolution);
	JComboBox<?> difficultyBox= new JComboBox<Object>(difficulty);
	
	ActionListener NewGame = (e)->{
		menu_sound.stop();
		try {
			new Game(width,height,mapwidth,mapheight);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		f.dispose();
	};
	
	ActionListener Options = (e)->{
		f.remove(menupanel);
		f.setContentPane(Optionspanel);		//apre le opzioni
		f.validate();
		f.repaint();
	};
	
	ActionListener Quit = (e)->{
		System.exit(0);
	};
	
	ActionListener Back = (e)->{
		f.remove(Optionspanel);
		f.setContentPane(menupanel);
		f.validate();
		f.repaint();
	};
	
	ActionListener res= (e)->{
		comboBox.getSelectedIndex();
		if(comboBox.getSelectedIndex()==0)
		{
			width=800;
			height=600;
			f.setSize(width,height);
		}
		else if(comboBox.getSelectedIndex()==1)
		{
			width=1280;
			height=768;
			f.setSize(width,height);
		}
		else if(comboBox.getSelectedIndex()==2)
		{
			width=1440;
			height=900;
			f.setSize(width,height);
		}
		else if(comboBox.getSelectedIndex()==3)
		{
			width=1920;
			height=1080;
			f.setSize(width,height);
		}
	};
	
	ActionListener diff= (e)->{
		difficultyBox.getSelectedIndex();
		if(difficultyBox.getSelectedIndex()==0)
		{
			mapwidth=1000;
			mapheight=1000;
		}
		else if(difficultyBox.getSelectedIndex()==1)
		{
			mapwidth=1500;
			mapheight=1500;
		}
		else if(difficultyBox.getSelectedIndex()==2)
		{
			mapwidth=2000;
			mapheight=2000;
		}
	};
	
	
	
	final JButton b1=new JButton ("Inizia Gioco") ;
	final JButton b2=new JButton ("Opzioni") ;
	final JButton b3=new JButton ("Esci");
	menupanel.add ( b1 );
	menupanel.add ( b2 );
	menupanel.add ( b3 );
	
	Dimension size = b1.getPreferredSize () ;
	b1.setFont(new CustomFontUtil(true, 18).getCustomFont());
	b1.setBounds ( width/2, height/5 , size.width, size.height );
	b1.setFocusable(false);
	b1.addActionListener(NewGame);
	
	size = b2.getPreferredSize () ;
	b2.setFont(new CustomFontUtil(true, 18).getCustomFont());
	b2 . setBounds (width/2 , height/5*2 , size.width*3, size.height *3) ;
	b2.setFocusable(false);
	b2.addActionListener(Options);
	
	size = b3.getPreferredSize () ;
	b3.setFont(new CustomFontUtil(true, 18).getCustomFont());
	b3.setBounds ( width/2, height/5*4 , size.width*2, size.height*2 );
	b3.setFocusable(false);
	b3.addActionListener(Quit);
	
	
	
	comboBox.setBounds(width/2, height/5, size.width*5/2, size.height);
	comboBox.setFont(new CustomFontUtil(true, 18).getCustomFont());
	comboBox.addActionListener(res);
	Optionspanel.add(comboBox);
	
	difficultyBox.setBounds(width/2, height/5*2, size.width*5/2, size.height);
	difficultyBox.setFont(new CustomFontUtil(true, 18).getCustomFont());
	difficultyBox.addActionListener(diff);
	Optionspanel.add(difficultyBox);
	
	
	
	
	Optionspanel.setLayout(null);
	final JButton b4=new JButton ("Indietro") ;
	b4.setBounds(width/2, height/2, size.width*2, size.height*2);
	b4.setFont(new CustomFontUtil(true, 18).getCustomFont());
	b4.setFocusable(false);
	b4.addActionListener(Back);
	Optionspanel.add(b4);
	
	final JButton musicB=new JButton ();
	musicB.setBounds(200, height/5*3, 25, 25);
	musicB.setFont(new CustomFontUtil(true, 18).getCustomFont());
	musicB.setFocusable(false);
	Optionspanel.add(musicB);
	
	final JButton effectB=new JButton ();
	effectB.setBounds(200, height/5*4, 25, 25);
	effectB.setFont(new CustomFontUtil(true, 18).getCustomFont());
	effectB.setFocusable(false);
	Optionspanel.add(effectB);
	
	
	
JSlider musicSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
	musicSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
	musicSlider.setMajorTickSpacing(10);
	musicSlider.setMinorTickSpacing(1);
	musicSlider.setPaintTicks(true);
	musicSlider.setPaintLabels(true);
	musicSlider.setBounds(300, height/5*3, size.width*7, size.height*2);
	Optionspanel.add(musicSlider);
	
JSlider effectSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
	effectSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
	effectSlider.setMajorTickSpacing(10);
	effectSlider.setMinorTickSpacing(1);
	effectSlider.setPaintTicks(true);
	effectSlider.setPaintLabels(true);
	effectSlider.setBounds(300, height/5*4, size.width*7, size.height*2);
	Optionspanel.add(effectSlider);
	
	
	
	
	
	
	
		
		
	}
	
	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		new Menu();
	}

}

