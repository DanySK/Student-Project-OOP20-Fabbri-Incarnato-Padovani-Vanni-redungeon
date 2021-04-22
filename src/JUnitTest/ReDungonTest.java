package JUnitTest;

import java.awt.Point;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entity.Boss;
import entity.Player;
import game.CombatSystem;
import game.ID;
import mapandtiles.BossFloor;
import mapandtiles.Floor;
import utilities.AABB;

public class ReDungonTest {

	private Boss boss;
	private BossFloor bFloor;
	private Floor floor;
	private CombatSystem combat;
	private Player player;
	private AABB box;
	
	static int level = 5;
	static int mapWidth = 1000;
	static int mapHeight = 1000;
	static int screenWidth = 100;
	static int screenHeight = 100;
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
				mapWidth, 
				mapHeight, 
				screenWidth, 
				screenHeight, 
				floor);
		boss = new Boss(
				this.posX, 
				this.posY, 
				ID.Boss, 
				this.combat, 
				this.level, 
				this.bFloor, 
				this.player);
	}
	
	@org.junit.Test
	public void PlayerTest() {

	}
	
	@org.junit.Test
	public void BossTest() {

	}
	
	@org.junit.Test
	public void MenuTest() {

	}
	
	@org.junit.Test
	public void EnemyTest() {

	}
	
	@org.junit.Test
	public void CombatSystemTest() {

	}
	
	
	
	
}
