package menu;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.Game;
import utilities.*;


public class Menu extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8098037462564546327L;
	
	private Clip menu_sound;
	private AudioInputStream menu_audio;
	private FloatControl menu_volume;
	private double music_gain = 0.5;
	private double effect_gain = 0.5;
	
	private Clip test_sound;
	private AudioInputStream test_audio;
	private FloatControl test_volume;
	
	private Difficulty difficulty = Difficulty.Easy;
	
	public static int width=800;
	public static int height=600;
	public static int mapwidth=2000;
	public static int mapheight=2000;

	final ImageIcon backGroundImage = new ImageIcon("data/GameBackground1920x1080.png");
	
	public Menu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		
	final JFrame f =new JFrame("Re:dungeon");
	f.setSize(width,height);
	f.addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent e){
        		menu_sound.stop();
                System.exit(0);
        }
    });
	
	final ImagePanel menupanel = new ImagePanel(backgroundImageResizer(width, height, backGroundImage));
	menupanel.setBorder(new EmptyBorder((int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3,(int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3));
	menupanel.setLayout(new GridLayout(10, 1, 10, 5));
	
	menupanel.setBackground(Color.BLACK);
	
	

	f.add(menupanel);
	//f.getContentPane().add(menupanel);
	f.setResizable(false);
	f.setVisible(true);
	
	
	menu_sound = AudioSystem.getClip();
	menu_audio= AudioSystem.getAudioInputStream(new File("data/BeneaththeMask.wav"));
	menu_sound.open(menu_audio);
	
	menu_sound.loop(Clip.LOOP_CONTINUOUSLY);
	
	test_sound = AudioSystem.getClip();
	test_audio= AudioSystem.getAudioInputStream(new File("data/bonk.wav"));
	test_sound.open(test_audio);
	
	final JPanel Optionspanel= new JPanel();
	
	
	String[] resolution = {"800x600","1280x720","1440x900", "1600x900", "1920x1080"};
	String[] difficulty_selection = {"Facile","Normale","Difficile"};
	
	JComboBox<?> comboBox = new JComboBox<Object>(resolution);
	JComboBox<?> difficultyBox= new JComboBox<Object>(difficulty_selection);
	JSlider musicSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
	JSlider effectSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
	final JButton effectB=new JButton ();
	
	ActionListener NewGame = (e)->{
		menu_sound.stop();
		try {
			new Game(width,height,mapwidth,mapheight, getDifficulty(), music_gain, effect_gain);
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
		menu_sound.stop();
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
			menupanel.setBorder(new EmptyBorder((int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3,(int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3));
			menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
		}
		else if(comboBox.getSelectedIndex()==1)
		{
			width=1280;
			height=720;
			f.setSize(width,height);
			menupanel.setBorder(new EmptyBorder((int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3,(int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3));
			menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
		}
		else if(comboBox.getSelectedIndex()==2)
		{
			width=1440;
			height=900;
			f.setSize(width,height);
			menupanel.setBorder(new EmptyBorder((int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3,(int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3));
			menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
		}

		else if(comboBox.getSelectedIndex()==3)
		{
			width=1600;
			height=900;
			f.setSize(width,height);
			menupanel.setBorder(new EmptyBorder((int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3,(int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3));
			menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
		}
		else if(comboBox.getSelectedIndex()==4)
		{
			width=1920;
			height=1080;
			f.setSize(width,height);
			menupanel.setBorder(new EmptyBorder((int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3,(int)f.getSize().getHeight()/5,(int)f.getSize().getWidth()/3));
			menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
		}
	};
	
	ActionListener diff= (e)->{
		difficultyBox.getSelectedIndex();
		if(difficultyBox.getSelectedIndex()==0)
		{
			setDifficulty(Difficulty.Easy);
			mapwidth=1600;
			mapheight=1600;
		}
		else if(difficultyBox.getSelectedIndex()==1)
		{
			setDifficulty(Difficulty.Normal);
			mapwidth=2000;
			mapheight=2000;
		}
		else if(difficultyBox.getSelectedIndex()==2)
		{
			setDifficulty(Difficulty.Hard);
			mapwidth=3200;
			mapheight=3200;
		}
	};
	
	ActionListener test = (e)->{
		if(!test_sound.isRunning())
			test_sound.loop(1);
	};
	
	
	
	final JButton b1=new JButton ("Inizia Gioco") ;
	final JButton b2=new JButton ("Opzioni") ;
	final JButton b3=new JButton ("Esci");
	menupanel.add ( b1 );
	menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
	menupanel.add ( b2 );
	menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
	menupanel.add ( b3 );

	
	Dimension size = b1.getPreferredSize () ;
	b1.setFont(new CustomFontUtil(true, 18).getCustomFont());
	b1.setFocusable(false);
	b1.addActionListener(NewGame);
	
	size = b2.getPreferredSize () ;
	b2.setFont(new CustomFontUtil(true, 18).getCustomFont());
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
	
	effectB.setBounds(200, height/5*4, 25, 25);
	effectB.setFont(new CustomFontUtil(true, 18).getCustomFont());
	effectB.setFocusable(false);
	effectB.addActionListener(test);
	Optionspanel.add(effectB);
	
	
	musicSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
	musicSlider.setMajorTickSpacing(10);
	musicSlider.setMinorTickSpacing(10);
	musicSlider.setPaintTicks(true);
	musicSlider.setPaintLabels(true);
	musicSlider.setBounds(300, height/5*3, size.width*7, size.height*2);
	musicSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
        		music_gain = musicSlider.getValue()*0.01;
        		float dB = (float) (Math.log(music_gain) / Math.log(10.0) * 20.0);
        		menu_volume = (FloatControl) menu_sound.getControl(FloatControl.Type.MASTER_GAIN);
        		menu_volume.setValue(dB);
            }
    });
	Optionspanel.add(musicSlider);
		
		
	effectSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
	effectSlider.setMajorTickSpacing(10);
	effectSlider.setMinorTickSpacing(10);
	effectSlider.setPaintTicks(true);
	effectSlider.setPaintLabels(true);
	effectSlider.setBounds(300, height/5*4, size.width*7, size.height*2);
	effectSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
        	effect_gain = effectSlider.getValue()*0.01;
        	float dB = (float) (Math.log(effect_gain) / Math.log(10.0) * 20.0);
    		test_volume = (FloatControl) test_sound.getControl(FloatControl.Type.MASTER_GAIN);
    		test_volume.setValue(dB);
        }
	});
	Optionspanel.add(effectSlider);
	
	
	
		
	}
	
	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		new Menu();
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	/**
	 * Resize the given ImageIcon and convert it into an Image istance
	 * 
	 * @param width		new image width
	 * @param height	new image height
	 * @param imageIcon	the give image
	 * @return a reized image
	 */
	private Image backgroundImageResizer(int width, int height, ImageIcon imageIcon) {
		Image preResizedImage= imageIcon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		ImageIcon resizedImage=new ImageIcon(preResizedImage);
		return resizedImage.getImage();
		
	}

}

