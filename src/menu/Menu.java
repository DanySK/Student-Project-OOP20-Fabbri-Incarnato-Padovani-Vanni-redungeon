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
import utilities.*;

public class Menu extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = -8098037462564546327L;

  private ResourceLoader resource;

  private Clip menuSound;
  private AudioInputStream menuAudio;
  private FloatControl menuVolume;
  private double musicGain = 0.5;
  private double effectGain = 0.5;

  private Clip testSound;
  private AudioInputStream testAudio;
  private FloatControl testVolume;

  private Difficulty difficulty = Difficulty.Easy;

  public static int width = 960;
  public static int height = 760;
  public static int mapwidth = 2000;
  public static int mapheight = 2000;

  final ImageIcon backGroundImage;
  final ImageIcon optionsImage;
  final ImageIcon tutorialImage;

  public Menu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

    resource = new ResourceLoader();

    backGroundImage = new ImageIcon();
    backGroundImage.setImage(ImageIO.read(resource.getStreamImage("GameBackground1920x1080")));

    optionsImage = new ImageIcon();

    tutorialImage = new ImageIcon();

    final JFrame f = new JFrame("Re:dungeon");
    f.setSize(width, height);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        menuSound.stop();
        System.exit(0);
      }
    });

    final ImagePanel menupanel = new ImagePanel(backgroundImageResizer(
        width, height, backGroundImage));
    menupanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3));
    menupanel.setLayout(new GridLayout(10, 1, 10, 5));

    menupanel.setBackground(Color.BLACK);

    final ImagePanel TutorialPanel = new ImagePanel(backgroundImageResizer(
        width, height, backGroundImage));
    TutorialPanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3));
    TutorialPanel.setLayout(new GridLayout(10, 1, 10, 5));

    f.add(menupanel);
    f.setResizable(false);
    f.setVisible(true);

    menuSound = AudioSystem.getClip();
    menuAudio = AudioSystem.getAudioInputStream(new BufferedInputStream(
        resource.getStreamAudio("BeneaththeMask")));
    menuSound.open(menuAudio);

    menuSound.loop(Clip.LOOP_CONTINUOUSLY);

    testSound = AudioSystem.getClip();
    testAudio = AudioSystem.getAudioInputStream(new BufferedInputStream(
        resource.getStreamAudio("bonk")));
    testSound.open(testAudio);

    final ImagePanel Optionspanel = new ImagePanel(backgroundImageResizer(
        width, height, backGroundImage));
    Optionspanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
        (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
    //Optionspanel.setLayout(new GridLayout(10, 1, 10, 5));

    final JButton b1 = new JButton("Inizia Gioco");
    final JButton b2 = new JButton("Opzioni");
    final JButton b3 = new JButton("Esci");
    final JButton b4 = new JButton("Indietro");
    final JButton b6 = new JButton("Tutorial");
    final JButton b5 = new JButton("Indietro");

    String[] resolution = { "960x760", "1280x720", "1440x900", "1600x900", "1920x1080" };
    String[] difficultySelection = { "Facile", "Normale", "Difficile" };

    JComboBox<?> comboBox = new JComboBox<Object>(resolution);
    JComboBox<?> difficultyBox = new JComboBox<Object>(difficultySelection);
    JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    JSlider effectSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
    final JButton effectB = new JButton("Suono");
    final JButton mus = new JButton("Volume Musica");

    final ActionListener NewGame = (e) -> {
      menuSound.stop();
      try {
        new Game(width, height, mapwidth, mapheight, getDifficulty(), musicGain, effectGain);
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

    final ActionListener Tutorial = (e) -> {
      f.remove(menupanel);
      f.setContentPane(TutorialPanel); // apre il tutorial
      f.validate();
      f.repaint();
    };

    final ActionListener Options = (e) -> {
      f.remove(menupanel);
      f.setContentPane(Optionspanel); // apre le opzioni
      f.validate();
      f.repaint();
    };

    final ActionListener Quit = (e) -> {
      menuSound.stop();
      System.exit(0);
    };

    final ActionListener Back = (e) -> {
      f.remove(Optionspanel);
      f.setContentPane(menupanel);
      f.validate();
      f.repaint();
    };

    final ActionListener Back2 = (e) -> {
      f.remove(TutorialPanel);
      f.setContentPane(menupanel);
      f.validate();
      f.repaint();
    };

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
        Optionspanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
        Optionspanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2,
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
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
        Optionspanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        Optionspanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
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
        Optionspanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        Optionspanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
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
        Optionspanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3));
        Optionspanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4, 
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
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
        Optionspanel.setBorder(new EmptyBorder((int) f.getSize().getHeight() / 5, 
            (int) f.getSize().getWidth() / 3, (int) f.getSize().getHeight() / 5,
            (int) f.getSize().getWidth() / 3));
        Optionspanel.setImage(backgroundImageResizer(width, height, backGroundImage));
        comboBox.setBounds((int) f.getSize().getWidth() / 4, 
            (int) f.getSize().getHeight() / 5, 150, 50);
        difficultyBox.setBounds((int) f.getSize().getWidth() / 2,
            (int) f.getSize().getHeight() / 5, 150, 50);
        b4.setBounds((int) f.getSize().getWidth() / 4,
            (int) f.getSize().getHeight() * 4 / 5, 150, 50);
        effectB.setBounds(150, height * 2 / 3, 100, 25);
        effectSlider.setBounds(300, (int) f.getSize().getHeight() * 2 / 3, 900, 50);
        musicSlider.setBounds(300, (int) f.getSize().getHeight() / 2, 900, 50);
        mus.setBounds(150, (int) f.getSize().getHeight() / 2, 150, 25);
      }
    };

    final ActionListener diff = (e) -> {
      difficultyBox.getSelectedIndex();
      if (difficultyBox.getSelectedIndex() == 0) {
        setDifficulty(Difficulty.Easy);
        mapwidth = 2000;
        mapheight = 2000;
      } else if (difficultyBox.getSelectedIndex() == 1) {
        setDifficulty(Difficulty.Normal);
        mapwidth = 2560;
        mapheight = 2560;
      } else if (difficultyBox.getSelectedIndex() == 2) {
        setDifficulty(Difficulty.Hard);
        mapwidth = 3200;
        mapheight = 3200;
      }
    };

    final ActionListener test = (e) -> {
      if (!testSound.isRunning()) {
        testSound.loop(1);
      }
    };

    menupanel.add(b1);
    menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
    menupanel.add(b2);
    menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
    menupanel.add(b6);
    menupanel.add(Box.createRigidArea(new Dimension(20, 0)));
    menupanel.add(b3);

    Dimension size = b1.getPreferredSize();
    b1.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b1.setForeground(Color.WHITE);
    b1.setBackground(Color.BLACK);
    b1.setFocusable(false);
    b1.addActionListener(NewGame);

    size = b2.getPreferredSize();
    b2.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b2.setForeground(Color.WHITE);
    b2.setBackground(Color.BLACK);
    b2.setFocusable(false);
    b2.addActionListener(Options);

    size = b3.getPreferredSize();
    b3.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b3.setForeground(Color.WHITE);
    b3.setBackground(Color.BLACK);
    b3.setFocusable(false);
    b3.addActionListener(Quit);

    size = b6.getPreferredSize();
    b6.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b6.setForeground(Color.WHITE);
    b6.setBackground(Color.BLACK);
    b6.setFocusable(false);
    b6.addActionListener(Tutorial);

    comboBox.setBounds((int) f.getSize().getWidth() / 4,
        (int) f.getSize().getHeight() / 5, 150, 50);
    comboBox.setFont(new CustomFontUtil(true, 18).getCustomFont());
    comboBox.setForeground(Color.WHITE);
    comboBox.setBackground(Color.BLACK);
    comboBox.addActionListener(res);
    Optionspanel.add(comboBox);

    difficultyBox.setBounds((int) f.getSize().getWidth() / 2, 
        (int) f.getSize().getHeight() / 5, 150, 50);
    difficultyBox.setFont(new CustomFontUtil(true, 18).getCustomFont());
    difficultyBox.setForeground(Color.WHITE);
    difficultyBox.setBackground(Color.BLACK);
    difficultyBox.addActionListener(diff);
    Optionspanel.add(difficultyBox);
    Optionspanel.setLayout(null);

    b4.setBounds((int) f.getSize().getWidth() / 4, (int) f.getSize().getHeight() * 4 / 5, 150, 50);
    b4.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b4.setForeground(Color.WHITE);
    b4.setBackground(Color.BLACK);
    b4.setFocusable(false);
    b4.addActionListener(Back);
    Optionspanel.add(b4);

    effectB.setBounds(150, height * 2 / 3, 100, 25);
    effectB.setFont(new CustomFontUtil(true, 18).getCustomFont());
    effectB.setForeground(Color.WHITE);
    effectB.setBackground(Color.BLACK);
    effectB.setFocusable(false);
    effectB.addActionListener(test);
    Optionspanel.add(effectB);

    mus.setBounds(150, height / 2, 150, 25);
    mus.setFont(new CustomFontUtil(true, 18).getCustomFont());
    mus.setForeground(Color.WHITE);
    mus.setContentAreaFilled(false);
    mus.setBorderPainted(false);
    mus.setFocusable(false);
    Optionspanel.add(mus);

    musicSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
    musicSlider.setMajorTickSpacing(10);
    musicSlider.setMinorTickSpacing(10);
    musicSlider.setPaintTicks(true);
    musicSlider.setPaintLabels(true);
    musicSlider.setOpaque(false);
    musicSlider.setForeground(Color.WHITE);
    musicSlider.setBackground(Color.RED);
    musicSlider.setBounds(300, height / 2, size.width * 7, size.height * 2);
    musicSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        musicGain = musicSlider.getValue() * 0.01;
        float db = (float) (Math.log(musicGain) / Math.log(10.0) * 20.0);
        menuVolume = (FloatControl) menuSound.getControl(FloatControl.Type.MASTER_GAIN);
        menuVolume.setValue(db);
      }
    });
    Optionspanel.add(musicSlider);

    effectSlider.setFont(new CustomFontUtil(true, 18).getCustomFont());
    effectSlider.setMajorTickSpacing(10);
    effectSlider.setMinorTickSpacing(10);
    effectSlider.setPaintTicks(true);
    effectSlider.setPaintLabels(true);
    effectSlider.setOpaque(false);
    effectSlider.setForeground(Color.WHITE);
    effectSlider.setBounds(300, height * 2 / 3, 500, 50);
    effectSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        effectGain = effectSlider.getValue() * 0.01;
        float db = (float) (Math.log(effectGain) / Math.log(10.0) * 20.0);
        testVolume = (FloatControl) testSound.getControl(FloatControl.Type.MASTER_GAIN);
        testVolume.setValue(db);
      }
    });
    Optionspanel.add(effectSlider);

    size = b5.getPreferredSize();
    b5.setFont(new CustomFontUtil(true, 18).getCustomFont());
    b5.setFocusable(false);
    b5.setForeground(Color.WHITE);
    b5.setBackground(Color.BLACK);
    b5.addActionListener(Back2);
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    TutorialPanel.add(b5);

  }

  public static void main(String[] args) throws IOException, 
      LineUnavailableException, UnsupportedAudioFileException {
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
   * @param width     new image width
   * @param height    new image height
   * @param imageIcon the give image
   * @return a reized image
   */
  private Image backgroundImageResizer(int width, int height, ImageIcon imageIcon) {
    Image preResizedImage = imageIcon.getImage().getScaledInstance(
        width, height, java.awt.Image.SCALE_SMOOTH);
    ImageIcon resizedImage = new ImageIcon(preResizedImage);
    return resizedImage.getImage();

  }

}
