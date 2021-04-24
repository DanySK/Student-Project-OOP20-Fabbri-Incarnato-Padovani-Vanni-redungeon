package menu;

import entity.Player;
import game.GameObject;
import game.ID;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import utilities.AABB;
import utilities.CustomFontUtil;
import utilities.ResourceLoader;

public class Hud extends GameObject {

  /* this class need only the rendere, because is a graphics clas */
  private boolean hudDisplay = false;
  private BufferedImage hudMenu;
  private BufferedImage key;
  protected Player player;
  protected int dungeonLevel;
  
  public Hud(int x, int y, ID id, Player p) throws IOException {
    super(x, y, id);
    // TODO Auto-generated constructor stub
    ResourceLoader resource = new ResourceLoader();
    this.hudMenu = ImageIO.read(resource.getStreamImage("HUD"));
    this.key = ImageIO.read(resource.getStreamImage("key"));
    this.player = p;
    this.dungeonLevel = 1;
  }

  @Override
  public void render(Graphics2D g) {
    // TODO Auto-generated method stub
    if (this.hudDisplay) {
      g.setFont(new CustomFontUtil(true, 18).getCustomFont());
      g.drawImage(this.hudMenu, this.x, this.x, null);
      g.drawString(String.valueOf(this.dungeonLevel), 
          this.x + 60, this.y + 24);
      g.drawString("x " + String.valueOf(this.player.getInventory().getgems()), 
          this.x + 60, this.y + 54);
      g.drawString(String.valueOf(this.player.getAttack()), 
          this.x + 60, this.y + 115);
      g.drawString(String.valueOf(this.player.getDefence()), 
          this.x + 60, this.y + 148);
      g.drawString("x " + String.valueOf(this.player.getSpells()), 
          this.x + 60, this.y + 181);
      g.setFont(new CustomFontUtil(true, 12).getCustomFont());
      g.drawString(String.valueOf(this.player.getHp())
          + "/" 
          + String.valueOf(this.player.getMax_hp()), this.x + 40, this.y + 84);
      if (this.player.getInventory().hasKey()) {
        g.drawImage(key, this.x + 30, this.y + 185, null);
      }
    }
  }
  
  public boolean ishudDisplay() {
    return hudDisplay;
  }
  
  public void sethudDisplay(boolean hudDisplay) {
    this.hudDisplay = hudDisplay;
  }
  
  public void setdungeonLevel() {
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
  public void input(KeyEvent key, List<AABB> collisions) {
  // TODO Auto-generated method stub
  }

}
