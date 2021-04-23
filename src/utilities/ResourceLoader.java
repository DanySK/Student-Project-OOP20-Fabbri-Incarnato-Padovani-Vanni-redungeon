package utilities;

import java.io.InputStream;

public class ResourceLoader {
	
	public ResourceLoader() {}

	public InputStream getStreamAudio(String name)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(name+".wav");
		return input;
	}
	
	public InputStream getStreamImage(String name)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(name+".png");
		return input;
	}
	
	public InputStream getStreamFont(String name)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("fonts/"+name+".ttf");
		return input;
	}
}
