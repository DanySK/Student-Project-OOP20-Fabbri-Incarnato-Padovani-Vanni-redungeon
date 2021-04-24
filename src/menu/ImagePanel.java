package menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

class ImagePanel extends JPanel {

  /**
   * Jpanel with the possibility of a
   * background image.
   */
  private static final long serialVersionUID = 7441642089495026890L;
  private Image img;

  public ImagePanel(final String img) {
    this(new ImageIcon(img).getImage());
  }

  public ImagePanel(final Image img) {
    this.img = img;
    final Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }

  public void paintComponent(final Graphics g) {
    g.drawImage(img, 0, 0, null);
  }

  public void setImage(final Image img) {
    this.img = img;
  }

}
