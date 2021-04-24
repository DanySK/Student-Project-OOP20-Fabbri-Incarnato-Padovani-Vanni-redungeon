package menu;

import entity.Player;
import game.GameObject;
import game.Id;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import utilities.AaBb;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;

/**
 *  hud need to watch stats and inventory.
 *
 * @author Francesco
 * @author Luigi
 * @author Leroy
 * @author Matteo
 */

public class Hud extends GameObject {

  /* this class need only the rendere, because is a graphics clas */
  private boolean hudDisplay;
  private final BufferedImage hudMenu;
  private final BufferedImage key;
  protected final Player player;
  protected int dungeonLevel;

  /**
   * Costructor.
   *
   * @param x coordinate 
   * @param y coordinate
   * @param id "identity"
   * @param p player
   * @throws IOException  If a function that handler
   *                      call doesn't read a file
   */
  public Hud(final int x, final int y, final Id id, final Player p) 
      throws IOException {
    super(x, y, id);
    // TODO Auto-generated constructor stub
    this.hudDisplay = false;

    final ResourceLoader resource = new ResourceLoader();

    this.hudMenu = ImageIO.read(resource.getStreamImage("HUD"));
    this.key = ImageIO.read(resource.getStreamImage("key"));
    this.player = p;
    this.dungeonLevel = 1;
  }

  @Override
  public void render(final Graphics2D g) {
    // TODO Auto-generated method stub

    if (this.hudDisplay) {
      g.setFont(new CustomFontUtil(true, 18).getCustomFont());
      g.drawImage(this.hudMenu, this.cordX, this.cordX, null);

      g.drawString(String.valueOf(this.dungeonLevel), this.cordX + 60, this.cordY + 24);

      g.drawString("x " + this.player.getInventory().getgems(), this.cordX + 60, this.cordY + 54);

      g.drawString(String.valueOf(this.player.getAttack()), this.cordX + 60, this.cordY + 115);

      g.drawString(String.valueOf(this.player.getDefence()), this.cordX + 60, this.cordY + 148);

      g.drawString("x " + this.player.getSpells(), this.cordX + 60, this.cordY + 181);

      g.setFont(new CustomFontUtil(true, 12).getCustomFont());

      g.drawString(this.player.getHp() + "/" + this.player.getMaxHp(),
          this.cordX + 40, this.cordY + 84);

      if (this.player.getInventory().hasKey()) {
        g.drawImage(key, this.cordX + 30, this.cordY + 185, null);
      }
    }
  }

  public boolean isHudDisplay() {
    return hudDisplay;
  }

  public void setHudDisplay(final boolean hudDisplay) {
    this.hudDisplay = hudDisplay;
  }

  public void setDungeonLevel() {
    this.dungeonLevel++;
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
  public void input(final KeyEvent key, final List<AaBb> collisions) {
    // TODO Auto-generated method stub

  }

}
