/**
 * Standard map class
 * 
 * The standard map.
 * 
 * 2011-06-07		OMMatte			Changed 4 rectangles to 1 rectangle (gameArea).
 * 2011-07-015		mrWiener		Rewrote the image loading to work with Applet. #67
 */
package maps;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

import core.Map;

public class Standard extends Map {
	private static final int EDGESIZE = 10;

	public Standard(int width, int height) {
		super(width, height);
		this.id = STANDARD;
		this.setGameArea(false, width, height, EDGESIZE);
		try {
			mapImage = new Image(ResourceLoader.getResourceAsStream("media/images/maps/square.png"), "square", false);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}
	
}
