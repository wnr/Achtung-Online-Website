/**
 * TronTurn class
 * 
 * 2011-06-17		OMMatte			Replaced change color with change shape (most changes in player class), also fixed small bug
 */

package powerups;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class TronTurn extends PowerUp {
	private static final String DESCRIPTION = "Tron-Turn";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Tron-Turn";
	private static final int INDEX = 10;
	private static final int COLOR = PowerUpManager.RED;

	public TronTurn() {
		super();

		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.spawnChance = SPAWNCHANCE;
		this.description = DESCRIPTION;
		this.name = NAME;
		this.affectOthers = true;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		if (!isActive())
			return;
	}

	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	
		if (!player.isTronTurn()) {
			player.setTronTurn(true);
		} else {
			player.setTronTurn(false);
		}
	}

	public void deactivate() {
		super.deactivate();

		if (!player.isTronTurn()) {
			player.setTronTurn(true);
		} else {
			player.setTronTurn(false);
		}
	}
}
