package menu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import utilities.*;
//import game.Window;


public class Menu{

	
	public static int width=1366;
	public static int height=768;
	
	public Menu() {

		
	final JFrame f =new JFrame("Re:dungeon");
	f.setSize(width,height);
	final JPanel menupanel = new JPanel();
	menupanel.setBackground(Color.BLACK);
	menupanel.setLayout(null);
	f.getContentPane().add(menupanel);
	
	
	final JPanel Optionspanel= new JPanel();
	
	
	String[] resolution = {"800x600","1280x768","1440x900","1920x1080"};
	
	JComboBox comboBox = new JComboBox(resolution);
	
	ActionListener NewGame = (e)->{
		//inizia nuovo gioco
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
	b2 . setBounds (55 , 40 , size.width*3, size.height *3) ;
	b2.setFocusable(false);
	b2.addActionListener(Options);
	
	size = b3.getPreferredSize () ;
	b3.setFont(new CustomFontUtil(true, 18).getCustomFont());
	b3.setBounds ( width/2, height/5*4 , size.width, size.height );
	b3.setFocusable(false);
	b3.addActionListener(Quit);
	
	
	
	comboBox.setBounds(100, 100, 100, 100);
	comboBox.addActionListener(res);
	Optionspanel.add(comboBox);
	
	
	
	
	
	
	Optionspanel.setLayout(null);
	final JButton b4=new JButton ("Indietro") ;
	b4.setFocusable(false);
	b4.addActionListener(Back);
	Optionspanel.add(b4);
	
	
	
	
	
	
	
		
		
	}

}

