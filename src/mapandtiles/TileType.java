package mapandtiles;

/**
 * different types of tiles OFF: non walkable ON: walkable Heal: special tyle, 1
 * use fully heals the character Teleport: teleports the carachter to a random
 * location Trap: greatly damages the character Gemstone: contains a rare gem
 * Exit: exit of the floor LockedExit: exit of the floor, can only be used with
 * a key Key: pick up the key for a locked exit Powerstone: pick up to weaken
 * the boss.
 */

public enum TileType {
  
  OFF, ON, HEAL, TELEPORT, TRAP, GEMSTONE, EXIT, LOCKEDEXIT, KEY, POWERSTONE;
}
