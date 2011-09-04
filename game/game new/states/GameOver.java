package states;

/**
 * 2011-06-02		mrWiener		Removed the reset score call. This is done each time a new game is loaded. Added escape key.
 * 2011-06-19		mrWiener		Displaying right color of winner.
 */

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import core.Engine;
import core.State;

public class GameOver extends State {

	public GameOver(GameContainer gc, Engine engine) {
		super("GameOver", engine);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if(gc.getInput().isKeyPressed(57) || gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			engine.getStateManager().changeState("Menu", gc);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setColor(engine.getPlayerManager().getWinner().getColor());
		g.drawString("This color won!", 30, 10);
		g.drawString("Press space to get to the main menu!", 50, 30);
	}
}
