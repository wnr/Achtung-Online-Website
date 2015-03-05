/**
 * Help menu.
 * 
 * 2011-06-08		OMMatte			Created class. Fixed issue #65
 */

package states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.RoundedRectangle;

import core.Engine;
import core.State;

public class Help extends State {
	private static final RoundedRectangle RECBACK = new RoundedRectangle(700, 550, 70, 35, 5);
	private static final GradientFill RECFILLCOLOR = new GradientFill(45, 45, Color.darkGray, 46, 46, Color.darkGray);
	private static final GradientFill RECLINECOLOR = new GradientFill(45, 45, Color.yellow, 46, 46, Color.yellow);
	
	public Help(GameContainer gc, Engine engine) {
		super("Help", engine);
		

	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			buttonPressed(gc, gc.getInput().getMouseX(), gc.getInput().getMouseY());
		}
		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			engine.getStateManager().changeState("Menu", gc);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.fill(RECBACK, RECFILLCOLOR);
		g.draw(RECBACK, RECLINECOLOR);
		g.drawString("Back", RECBACK.getMinX() + 10, RECBACK.getMinY() + 8);
		g.drawString("Just don't drive in to shit with your worm (except powerups, they are awesome).", 30, 30);
	}
	
	private void buttonPressed(GameContainer gc, int x, int y) {
		if (RECBACK.contains(x, y)) {
			engine.getStateManager().changeState("Menu", gc);
		}
	}

}
