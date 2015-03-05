/**
 * 2011-07-015		mrWiener		Rewrote the image loading to work with Applet. #67
 */

package core;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.ResourceLoader;

public class PowerUpSprite {
	private static final int ICONWIDTH = 40, ICONHEIGHT = 40;
	private SpriteSheet powerUpSprite;
	private Image spriteImage; 
	
	public PowerUpSprite() {
		try {
			spriteImage = new Image(ResourceLoader.getResourceAsStream("/media/images/powerup_spritesheet.png"), "powerup_spritesheet", false);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		powerUpSprite = new SpriteSheet(spriteImage, ICONWIDTH, ICONHEIGHT);
	}
	
	public Image getIcon(int color, int index) {
		return powerUpSprite.getSprite(color, index);
	}
}
