package game;
/**
 * 
 * Class that control if player take key or the boss power stone
 * 
 * @author Francesco Padovani
 * @author Luigi Incarnato
 * @author Leroy Fabbri
 * @author Matteo Vanni
 * 
 * @see entity.Player
 */
public class Inventory {
	boolean key;
	int power_stone;
	int gemstones;

	/**
	 * Constructor
	 */
	public Inventory() {
		this.gemstones=0;
		this.key= false;
		this.power_stone = 0;
  	}
	
	/**
	 * Used for control on pre-boss floor
	 */
	public void gotKey() {
		this.key=true;
	}
	/**
	 * Used as counter for power stone in boss floor
	 */
	public void increasePowerStone()
	{
		this.power_stone++;
	}
	
	/**
	 * 
	 * @return the amount of power stone taken
	 */
	public int getPowerStone()
	{
		return this.power_stone;
	}
	/**
	 * 
	 * @return true if player taken the key
	 */
	public boolean hasKey() {return this.key;}
	
	/**
	 * 
	 * @return the amount of gems taken
	 */
	public int getgems() {return this.gemstones;}
	/**
	 * Add gems to player inventory when taken
	 * @param i the number of gem taken
	 */
	public void addGems(int i) {
		this.gemstones+=i;
	}

	/**
	 * Reset inventory
	 */
	public void clearInventory()
	{
		
		this.key = false;
		this.power_stone = 0;
	}
}
