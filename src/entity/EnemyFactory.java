package entity;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.*;
import mapandtiles.AbsFloor;

public class EnemyFactory  {
	
	public Enemy normalEnemy(int x, int y, ID id, CombatSystem combat, int level, int hp, int attack, int magic_attack, int defence,AbsFloor floor, Player p)throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		return new Enemy( x,  y,  id, combat,  level,  hp,  attack,  magic_attack,  defence,  floor, p);
	}
	
	public Boss commonBoss(int x, int y, ID id, CombatSystem combat, int level, int hp, int attack, int magic_attack, int defence,AbsFloor floor, Player p) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
		return new Boss( x,  y,  id, combat,  level,  hp,  attack,  magic_attack,defence, floor, p);
	}

}
