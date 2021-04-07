package entity;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import game.ID;
import mapandtiles.AbsFloor;

public class EnemyFactory  {
	
public Enemy normalEnemy(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence,AbsFloor floor, Player p)throws IOException, LineUnavailableException, UnsupportedAudioFileException {
	return new Enemy( x,  y,  id,  level,  hp,  attack,  magic_attack,  defence,  floor, p);
}
public Boss commonBoss(int x, int y, ID id, int level, int hp, int attack, int magic_attack, int defence,AbsFloor floor, Player p) throws IOException, LineUnavailableException, UnsupportedAudioFileException{
	return new Boss( x,  y,  id,  level,  hp,  attack,  magic_attack,defence, floor, p);
}

}
