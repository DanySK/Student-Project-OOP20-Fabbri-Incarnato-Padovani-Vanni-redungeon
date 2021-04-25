package menu;

import game.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;

/**
 * Menu for select the varoisu option
 * before creating the game.
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 *
 */
public class Menu extends JFrame {

  private static final long serialVersionUID = -8098037462564546327L;


  private final Clip menuSound;
  private double musicGain = 0.5;
  private double effectGain = 0.5;

  private final Clip testSound;

  private Difficulty difficulty = Difficulty.EASY;

  public int width = 960;
  public int height = 760;
  public int mapwidth = 2000;
  public int mapheight = 2000;

  private final ImageIcon backGroundImage;
  protected final ImageIcon tutorialImage;

  /**
   * Costructor.
   *
   * @throws IOException    If a function that handler
   *                        call doesn't read a file
   *                        
   * @throws LineUnavailableException   If a function that handler
   *                                    call doens't open a line beacuse
   *                                    it's unavailable
   *                                    
   * @throws UnsupportedAudioFileException If an audio file isn't supported
   */
  
  public Menu() throws IOException, UnsupportedAudioFileException, LineUnavailableException {

    final ResourceLoader resource;
    
    resource = new ResourceLoader();

    backGroundImage = new ImageIcon();
    backGroundImage.setImage(ImageIO.read(resource.getStreamImage("GameBackground1920x1080")));

    tutorialImage = new ImageIcon();
    tutorialImage.setImage(ImageIO.read(resource.getStreamImage("tutorial")));
    
    
    //menu JFrame creation
    final JFrame f = new JFrame("Re:dungeon");
    f.setSize(width, height);
    
    f.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(final WindowEvent e) {
        menuSound.stop();
      }
    });
    
    final ImagePanel menupanel = new ImagePanel(backgroundImageResizer(
        width, height, backGroundImage));
    menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3));
    menupanel.setLayout(new GridLayout(10, 1, 10, 5));

    final ImagePanel tutorialPanel = new ImagePanel(backgroundImageResizer(
        width, height, tutorialImage));
    tutorialPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3));

    f.add(menupanel);
    f.setResizable(false);
    f.setVisible(true);

    final AudioInputStream menuAudio;

    menuSound = AudioSystem.getClip();
    menuAudio = AudioSystem.getAudioInputStream(new BufferedInputStream(
        resource.getStreamAudio("BeneaththeMask")));
    menuSound.open(menuAudio);

    menuSound.loop(Clip.LOOP_CONTINUOUSLY);
    
    final AudioInputStream testAudio;
    testSound = AudioSystem.getClip();
    testAudio = AudioSystem.getAudioInputStream(new BufferedInputStream(
        resource.getStreamAudio("bonk")));
    testSound.open(testAudio);

    final ImagePanel optionPanel = new ImagePanel(backgroundImageResizer(
        width, height, backGroundImage));
    optionPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));

    //initializing buttons
    final JButton b1 = new JButton("Inizia Gioco");
    final JButton b2 = new JButton("Opzioni");
    final JButton b3 = new JButton("Esci");
    final JButton b4 = new JButton("Indietro");
    final JButton b6 = new JButton("Tutorial");
    final JButton b5 = new JButton("Indietro");

    final String[] resolution = { "960x760", "1280x720", "1440x900", "1600x900", "1920x1080" };
    final String[] difficultySelection = { "Facile", "Normale", "Difficile" };

    final JComboBox<?> comboBox = new JComboBox<>(resolution);
    final JComboBox<?> difficultyBox = new JComboBox<>(difficultySelection);
    final JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    final JSlider effectSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    final JButton effectB = new JButton("Suono");
    final JButton mus = new JButton("Volume Musica");

    final ActionListener newGame = (e) -> {
      try {
        menuAudio.close();
        testAudio.close();
      } catch (IOException e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
      }
      menuSound.stop();
      try {
        new Game(width, height, mapwidth, mapheight, getDifficulty(), musicGain, effectGain);
      } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      } 
      f.dispose();
    };

    //Buttons ActionListeners
    final ActionListener tutorial = (e) -> {
      f.remove(menupanel);
      f.setContentPane(tutorialPanel); // apre il tutorial
      f.validate();
      f.repaint();
    };

    final ActionListener options = (e) -> {
      f.remove(menupanel);
      f.setContentPane(optionPanel); // apre le opzioni
      f.validate();
      f.repaint();
    };

    final ActionListener quit = (e) -> {
      menuSound.stop();
      System.exit(0);
    };

    final ActionListener back = (e) -> {
      f.remove(optionPanel);
      f.setContentPane(menupanel);
      f.validate();
      f.repaint();
    };

    final ActionListener back2 = (e) -> {
      f.remove(tutorialPanel);
      f.setContentPane(menupanel);
      f.validate();
      f.repaint();
    };

    //ActionListener for the window size, moves images and buttons according to size
    final ActionListener res = (e) -> {
      comboBox.getSelectedIndex();
      if (comboBox.getSelectedIndex() == 0) {
        width = 960;
        height = 760;
        f.setSize(width, height);
        menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
        menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        optionPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
        optionPanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        tutorialPanel.setImage(backgroundImageResizer(width, height, tutorialImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2,
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
        b5.setBounds(100, 600, 150, 50);
        effectB.setBounds(150, (int) f.getSize().getHeight() * 2 / 3, 100, 25);
        effectSlider.setBounds(300, (int) f.getSize().getHeight() * 2 / 3, 500, 50);
        musicSlider.setBounds(300, (int) f.getSize().getHeight() / 2, 500, 50);
        mus.setBounds(150, (int) f.getSize().getHeight() / 2, 150, 25);
      } else if (comboBox.getSelectedIndex() == 1) {
        width = 1280;
        height = 720;
        f.setSize(width, height);
        menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
        menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        optionPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        optionPanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        tutorialPanel.setImage(backgroundImageResizer(width, height, tutorialImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
        b5.setBounds(100, 550, 150, 50);
        effectB.setBounds(150, height * 2 / 3, 100, 25);
        effectSlider.setBounds(300, (int) f.getSize().getHeight() * 2 / 3, 625, 50);
        musicSlider.setBounds(300, (int) f.getSize().getHeight() / 2, 625, 50);
        mus.setBounds(150, (int) f.getSize().getHeight() / 2, 150, 25);
      } else if (comboBox.getSelectedIndex() == 2) {
        width = 1440;
        height = 900;
        f.setSize(width, height);
        menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        optionPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        optionPanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        tutorialPanel.setImage(backgroundImageResizer(width, height, tutorialImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
        b5.setBounds(100, 750, 150, 50);
        effectB.setBounds(150, height * 2 / 3, 100, 25);
        effectSlider.setBounds(300, (int) f.getSize().getHeight() * 2 / 3, 750, 50);
        musicSlider.setBounds(300, (int) f.getSize().getHeight() / 2, 750, 50);
        mus.setBounds(150, (int) f.getSize().getHeight() / 2, 150, 25);
      } else if (comboBox.getSelectedIndex() == 3) {
        width = 1600;
        height = 900;
        f.setSize(width, height);
        menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        optionPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        optionPanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        tutorialPanel.setImage(backgroundImageResizer(width, height, tutorialImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4, 
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
        b5.setBounds(100, 750, 150, 50);
        effectB.setBounds(150, height * 2 / 3, 100, 25);
        effectSlider.setBounds(300, (int) f.getSize().getHeight() * 2 / 3, 750, 50);
        musicSlider.setBounds(300, (int) f.getSize().getHeight() / 2, 750, 50);
        mus.setBounds(150, (int) f.getSize().getHeight() / 2, 150, 25);
      } else if (comboBox.getSelectedIndex() == 4) {
        width = 1920;
        height = 1080;
        f.setSize(width, height);
        menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        menupanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        optionPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
        optionPanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        tutorialPanel.setImage(backgroundImageResizer(width, height, tutorialImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2,
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
        b5.setBounds(100, 900, 150, 50);
        effectB.setBounds(150, height * 2 / 3, 100, 25);
        effectSlider.setBounds(300, (int) f.getSize().getHeight() * 2 / 3, 900, 50);
        musicSlider.setBounds(300, (int) f.getSize().getHeight() / 2, 900, 50);
        mus.setBounds(150, (int) f.getSize().getHeight() / 2, 150, 25);
      }
    };
    
    //ActionListener for the difficulty
    final ActionListener diff = (e) -> {
      difficultyBox.getSelectedIndex();
      if (difficultyBox.getSelectedIndex() == 0) {
        setDifficulty(Difficulty.EASY);
        mapwidth = 2000;
        mapheight = 2000;
      } else if (difficultyBox.getSelectedIndex() == 1) {
        setDifficulty(Difficulty.NORMAL);
        mapwidth = 2560;
        mapheight = 2560;
      } else if (difficultyBox.getSelectedIndex() == 2) {
        setDifficulty(Difficulty.HARD);
        mapwidth = 3200;
        mapheight = 3200;
      }
    };

    final ActionListener test = (e) -> {
      if (!testSound.isRunning()) {
        testSound.loop(1);
      }
    };

    //adding buttons in the main menu panel
    menupanel.add(b1);
    menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
    menupanel.add(b2);
    menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
    menupanel.add(b6);
    menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
    menupanel.add(b3);

    //setting the buttons
    b1.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b1.setForeground(Color.WHITE);
    b1.setBackground(Color.BLACK);
    b1.setFocusable(false);
    b1.addActionListener(newGame);


    b2.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b2.setForeground(Color.WHITE);
    b2.setBackground(Color.BLACK);
    b2.setFocusable(false);
    b2.addActionListener(options);


    b3.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b3.setForeground(Color.WHITE);
    b3.setBackground(Color.BLACK);
    b3.setFocusable(false);
    b3.addActionListener(quit);

   
    b6.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b6.setForeground(Color.WHITE);
    b6.setBackground(Color.BLACK);
    b6.setFocusable(false);
    b6.addActionListener(tutorial);
    
    //setting comboBoxes
    comboBox.setBounds((int) f.getSize().getWidth() / 4,
        (int) f.getSize().getHeight() / 5, 150, 50);
    comboBox.setFont(new CustomFontUtil(true, 18).getCustomFont());
    comboBox.setForeground(Color.WHITE);
    comboBox.setBackground(Color.BLACK);
    comboBox.addActionListener(res);
    optionPanel.add(comboBox);

    difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
        (int) f.getSize().getHeight() / 5, 150, 50);
    difficultyBox.setFont(new CustomFontUtil(true, 18).getCustomFont());
    difficultyBox.setForeground(Color.WHITE);
    difficultyBox.setBackground(Color.BLACK);
    difficultyBox.addActionListener(diff);
    optionPanel.add(difficultyBox);
    optionPanel.setLayout(null);

    b4.setBounds((int) f.getSize().getWidth() / 4, (int) f.getSize().getHeight() * 4 / 5, 150, 50);
    b4.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b4.setForeground(Color.WHITE);
    b4.setBackground(Color.BLACK);
    b4.setFocusable(false);
    b4.addActionListener(back);
    optionPanel.add(b4);

    effectB.setBounds(150, height * 2 / 3, 100, 25);
    effectB.setFont(new CustomFontUtil(true, 18).getCustomFont());
    effectB.setForeground(Color.WHITE);
    effectB.setBackground(Color.BLACK);
    effectB.setFocusable(false);
    effectB.addActionListener(test);
    optionPanel.add(effectB);

    mus.setBounds(150, height / 2, 150, 25);
    mus.setFont(new CustomFontUtil(true, 18).getCustomFont());
    mus.setForeground(Color.WHITE);
    mus.setContentAreaFilled(false);
    mus.setBorderPainted(false);
    mus.setFocusable(false);
    optionPanel.add(mus);
    
    //setting Sliders
    musicSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
    musicSlider.setMajorTickSpacing(10);
    musicSlider.setMinorTickSpacing(10);
    musicSlider.setPaintTicks(true);
    musicSlider.setPaintLabels(true);
    musicSlider.setOpaque(false);
    musicSlider.setForeground(Color.WHITE);
    musicSlider.setBackground(Color.RED);
    musicSlider.setBounds(300, height / 2, 500, 50);
    musicSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        final FloatControl menuVolume;
        musicGain = musicSlider.getValue() * 0.01;
        final float db = (float) (Math.log(musicGain) / Math.log(10.0) * 20.0);
        menuVolume = (FloatControl) menuSound.getControl(FloatControl.Type.MASTER_GAIN);
        menuVolume.setValue(db);
      }
    });
    optionPanel.add(musicSlider);

    effectSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
    effectSlider.setMajorTickSpacing(10);
    effectSlider.setMinorTickSpacing(10);
    effectSlider.setPaintTicks(true);
    effectSlider.setPaintLabels(true);
    effectSlider.setOpaque(false);
    effectSlider.setForeground(Color.WHITE);
    effectSlider.setBounds(300, height * 2 / 3, 500, 50);
    effectSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        final FloatControl testVolume;
        effectGain = effectSlider.getValue() * 0.01;
        final float db = (float) (Math.log(effectGain) / Math.log(10.0) * 20.0);
        testVolume = (FloatControl) testSound.getControl(FloatControl.Type.MASTER_GAIN);
        testVolume.setValue(db);
      }
    });
    optionPanel.add(effectSlider);

    b5.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b5.setFocusable(false);
    b5.setForeground(Color.WHITE);
    b5.setBackground(Color.BLACK);
    b5.addActionListener(back2);
    b5.setBounds(100, 600, 150, 50);
    tutorialPanel.add(b5);

  }

  public static void main(final String[] args) throws IOException, 
      LineUnavailableException, UnsupportedAudioFileException {
    new Menu();
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(final Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Resize the given ImageIcon and convert it into an Image istance.
   *
   * @param width     new image width
   * @param height    new image height
   * @param imageIcon the give image
   * @return a reized image
   */
  private Image backgroundImageResizer(final int width, final int height,
      final ImageIcon imageIcon) {
    final Image preResizedImage = imageIcon.getImage().getScaledInstance(
        width, height, Image.SCALE_SMOOTH);
    final ImageIcon resizedImage = new ImageIcon(preResizedImage);
    return resizedImage.getImage();

  }

}
