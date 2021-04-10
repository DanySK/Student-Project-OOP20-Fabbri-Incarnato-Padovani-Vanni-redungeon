package game;

public class Inventory {
	boolean key;
  public Inventory() {
	  this.key= false;
  }
	
	public void setKey(boolean b) {
		this.key=b;
	}
	public boolean hasKey() {return this.key;}
}
