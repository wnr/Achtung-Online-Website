/**
 * 2011-07-09		OMMatte			Created Class.
 */
package table;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Shape;

import core.Player;

public class BodyShape {
	private Shape wormBody;
	private boolean active;
	private Player player;

	public BodyShape(Player player, Shape wormBody) {
		this.wormBody = wormBody;
		this.player = player;
		active = true;
	}

	public void render(GameContainer gc, Graphics g) {
		g.fill(wormBody, new GradientFill(45, 45, player.getColor(), 50, 50,
				player.getColor()));
	}

	public boolean isActive() {
		return active;
	}

	public Shape getWormBody() {
		return wormBody;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Player getPlayer() {
		return player;
	}

}
