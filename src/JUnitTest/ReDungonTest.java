package JUnitTest;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entity.*;
import game.*;
import mapandtiles.*;
import menu.Difficulty;
import menu.Menu;
import utilities.AABB;

public class ReDungonTest {

	public Boss boss;
	public BossFloor bFloor;
	public Floor floor;
	public CombatSystem combat;
	public Player player;
	public AABB box;
	public Menu menu;
	public Game game;
	public Enemy enemy;
	public KeyEvent key;
	public Handler handler;
	List<AABB> collisions;
	Robot robot;
	
	static int level = 1;
	public static int mapWidth=2000;
	public static int mapHeight=2000;
	static int screenWidth = 960;
	static int screenHeight = 720;
	static int posX=0;
	static int posY=0;
	
	@org.junit.Before
	public void initAll() throws IOException, LineUnavailableException, UnsupportedAudioFileException, AWTException {
		box=new AABB(new Point(), mapWidth, mapHeight);
		floor = new Floor(level, mapWidth, mapHeight, screenWidth, screenHeight);
		
		robot = new Robot();
		
		collisions = new ArrayList<AABB>();
		
		bFloor = new BossFloor(level, mapWidth, mapHeight, screenWidth, screenHeight);
		combat = new CombatSystem(0);
		
		player = new Player(
				0, 
				0, 
				ID.Player, 
				combat, 
				1, 
				100, 
				10, 
				10, 
				10, 
				floor);
		
		collisions.add(player.getBox());

		boss = new Boss(
				32, 
				32, 
				ID.Boss, 
				combat, 
				level, 
				bFloor, 
				player);
		
		enemy=new Enemy(
				32, 
				32, 
				ID.Enemy, 
				combat, 
				level, 
				floor, 
				player);

		floor.placeEntity(player);

		floor.placeEntity(enemy);

		floor.placeEntity(boss);
		
		combat.addBoss(boss);
		combat.addEnemy(enemy);
		combat.addPlayer(player);
		
		game = new Game(screenWidth,screenHeight,mapWidth,mapHeight, Difficulty.Easy, 0.0, 0.0);
		key = new KeyEvent(game, KeyEvent.KEY_PRESSED, 
				System.currentTimeMillis(), 0,  
				KeyEvent.VK_1,'0');
	}
	
	@org.junit.Test
	public void BossTest() {
		
		/*the boss exp increase ten by ten*/
		assertEquals(70, boss.getExpGuaranteed());
		boss.setLevel(2);
		boss.setExpGuaranteed(60); //by costructor it always have 30 + (leve*10)
		assertEquals(80, boss.getExpGuaranteed());
		
		enemy.setLevel(1);
		/*boss by costructor has specif stats depending on floor and player level 
		 * at the start of the bossfloor it has the same defence as the attack
		 * of the player
		 * */
		assertEquals(510, boss.getMax_hp());
		assertEquals(player.getAttack(),
				boss.getDefence());
		boss.setMax_hp(500);
		boss.setLevel(2);
		boss.augmStat();
		assertEquals(520, boss.getMax_hp());
		assertEquals(player.getAttack(),
				boss.getDefence());
	}
	
	@org.junit.Test
	public void EnemyTest() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		
		/*the enemy exp increase ten by ten getting to next levels*/
		assertEquals(40, enemy.getExpGuaranteed());
		enemy.setLevel(2);
		enemy.setExpGuaranteed(30); //by costructor it always have 30 + (leve*10)
		assertEquals(50, enemy.getExpGuaranteed());
		

		enemy.setLevel(1);
		/*enemy by costructor has specif stats depending on floor and player level */
		assertEquals(110, enemy.getMax_hp());
		assertEquals((player.getDefence()/2)+enemy.getLevel(),
				enemy.getDefence());
		enemy.setMax_hp(100);
		enemy.setLevel(2);
		enemy.augmStat();
		assertEquals(120, enemy.getMax_hp());
		assertEquals((player.getDefence()/2)+enemy.getLevel(),
				enemy.getDefence());
		
		
		enemy.setHp(0);
		assertTrue(enemy.isDead());
	}
	
	@org.junit.Test
	public void PlayerTest(){
		
		assertFalse(player.isDead());
		
		player.addExp(60); //level up beacuse the player need 50 exp
		assertEquals(2, player.getLevel());
		player.addExp(50); //the player's experience need 75 for the next level
		/*now the player has 60 to 75 exp*/
		assertNotEquals(3, player.getLevel());
		
		/* the player level up and the stats increase*/
		assertTrue(player.getMax_hp()>100);
		assertTrue(player.getAttack()>10);
		assertTrue(player.getMagic_attack()>10);
		assertTrue(player.getDefence()>10);
		
		/*try to simulate the keys pressed on the
		 * keyboard
		 */
		
		/*movement*/
		key.setKeyCode(KeyEvent.VK_W);
		player.input(key, collisions);
		assertEquals(Direction.Up, player.getDirection());
		key.setKeyCode(KeyEvent.VK_A);
		player.input(key, collisions);
		assertEquals(Direction.Left, player.getDirection());
		key.setKeyCode(KeyEvent.VK_S);
		player.input(key, collisions);
		assertEquals(Direction.Down, player.getDirection());
		key.setKeyCode(KeyEvent.VK_D);
		player.input(key, collisions);
		assertEquals(Direction.Right, player.getDirection());
		/*now the player done 4 moves*/
		
		/*attack*/
		key.setKeyCode(KeyEvent.VK_J);
		player.input(key, collisions);
		assertTrue(player.isAttacking());
		key.setKeyCode(KeyEvent.VK_K);
		player.input(key, collisions);
		assertTrue(player.isMagicAttacking());
		/*the player only have one magic attack at the start
		 * of the game, used one it remains 0 magic attack
		 */
		assertEquals(0, player.getSpells());
		
		player.setHp(0);
		assertTrue(player.isDead());
	}
	
	@org.junit.Test
	public void CombatSystemTest() {
		combat.damagePlayer("enemy", true);
		
	}
	
	
	
	
}
