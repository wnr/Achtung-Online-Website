/**
 * WallHack class
 * 
 * 2011-06-23		OMMatte			Created class
 */
package powerups;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Wallhack extends PowerUp {
	private static final String DESCRIPTION = "Wallhack";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Wallhack";
	private static final int INDEX = 13;
	private static final int COLOR = PowerUpManager.GREEN;

	public Wallhack() {
		super();

		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.description = DESCRIPTION;
		this.spawnChance = SPAWNCHANCE;
		this.name = NAME;
		this.affectSelf = true;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		if (!isActive())
			return;
		if (!player.isWallHack()) {
			player.setWallHack(true);
		}
	}

	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	}

	public void deactivate() {
		super.deactivate();

		player.setWallHack(false);
	}
}
