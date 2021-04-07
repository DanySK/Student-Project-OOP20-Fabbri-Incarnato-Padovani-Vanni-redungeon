package mapandtiles;

public class FloorFactory {
	
	
   public Floor standardFloor(int l,int w,int h,int screenw,int screenh) {
	   return new Floor(l,w,h,screenw,screenh);
   }
   public BossFloor bossFloor(int l,int w,int h,int screenw,int screenh) {
	   return new BossFloor(l,w,h,screenw,screenh);
   }

}
