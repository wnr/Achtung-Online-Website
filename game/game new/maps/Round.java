/**
 * Round map class
 * 
 * The standard round map.
 * 
 * 2011-06-07		OMMatte			Created and modified to work with several maps. #17
 * 2011-07-015		mrWiener		Rewrote the image loading to work with Applet. #67
 */

package maps;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;

import core.Map;

public class Round extends Map {
	private static final int EDGESIZE = 10;


	public Round(int radius) {
		super(radius*2, radius*2);
		this.id = ROUND;
		this.setGameArea(true, radius, radius, EDGESIZE);
		try {
			mapImage = new Image(ResourceLoader.getResourceAsStream("media/images/maps/circle.png"), "circle", false);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}
	
	
}
