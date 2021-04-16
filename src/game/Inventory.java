package game;

public class Inventory {
	boolean key;
	int power_stone;
	
  public Inventory() {
	  this.key= false;
	  this.power_stone = 0;
  }
	
	public void gotKey() {
		this.key=true;
	}
	
	public void increasePowerStone()
	{
		this.power_stone++;
	}
	
	public int getPowerStone()
	{
		return this.power_stone;
	}
	
	public boolean hasKey() {return this.key;}
	
	public void clearInventory()
	{
		this.key = false;
		this.power_stone = 0;
	}
}
