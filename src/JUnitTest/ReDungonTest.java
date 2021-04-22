package JUnitTest;

import java.awt.Point;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entity.Boss;
import entity.Enemy;
import entity.Player;
import game.CombatSystem;
import game.ID;
import mapandtiles.BossFloor;
import mapandtiles.Floor;
import menu.Menu;
import utilities.AABB;

public class ReDungonTest {

	private Boss boss;
	private BossFloor bFloor;
	private Floor floor;
	private CombatSystem combat;
	private Player player;
	private AABB box;
	private Menu menu;
	private Enemy enemy;
	
	static int level = 5;
	static int mapWidth = 1000;
	static int mapHeight = 1000;
	static int screenWidth = 50;
	static int screenHeight = 50;
	static int posX=0;
	static int posY=0;
	
	@org.junit.Before
	public void initAll() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		box=new AABB(new Point(), mapWidth, mapHeight);
		floor = new Floor(level, mapWidth, mapHeight, screenWidth, screenHeight);
		bFloor = new BossFloor(level, mapWidth, mapHeight, screenWidth, screenHeight);
		combat = new CombatSystem(0);
		
		player = new Player(
				this.floor.getX(), 
				this.floor.getY(), 
				ID.Player, 
				combat, 
				level, 
				//Used the map and screen size as stats for test
				mapWidth, 
				mapHeight, 
				screenWidth, 
				screenHeight, 
				floor);

		boss = new Boss(
				posX+10, 
				posY+10, 
				ID.Boss, 
				combat, 
				level, 
				bFloor, 
				player);
		
		enemy=new Enemy(
				posX+20, 
				posY+20, 
				ID.Enemy, 
				combat, 
				level, 
				bFloor, 
				player);
	}
	
	@org.junit.Test
	public void PlayerTest() {
		player.addExp(1000);
		player.augmStat();
		player.isAttacking();
		player.isDead();
		player.isMagicAttacking();
		player.isMoving();
		//player.move();
	}
	
	@org.junit.Test
	public void BossTest() {

	}
	
	@org.junit.Test
	public void MenuTest() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		menu=new Menu();
	}
	
	@org.junit.Test
	public void EnemyTest() {
		enemy.isAttacking();
		enemy.setMovement(true);
		enemy.isMoving();
		enemy.move();
	}
	
	@org.junit.Test
	public void CombatSystemTest() {

	}
	
	
	
	
}
