package game;

import java.util.List;

import entity.*;

public class CombatSystem {
	
	private Player player;
	private List<Enemy> enemies;
	
	public CombatSystem(Player player, List<Enemy> enemies)
	{
		this.player = player;
		this.enemies = enemies;
	}
	
	public void PlayerAttack()
	{
		Enemy enemy;
		
		switch(this.player.getDirection())
		{
			case Down:
				
				break;
				
			case Left:
				break;
				
			case Right:
				break;
				
			case Up:
				break;
		}
	}
}
