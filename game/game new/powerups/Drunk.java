/**
 * Drunk class
 * 
 * This class represent that PowerUp: Drunk
 * 
 * 2011-06-02		OMMatte			Is now more balanced with low fps.
 * 2011-06-16		mrWiener		Removed time calculations. See Issue #75
 * 2011-06-23		OMMatte			Modified to work with class Worm
 */

package powerups;

import java.util.Random;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Drunk extends PowerUp {
	private static final double MAXCHANGESPEED = 0.02;
	private static final String DESCRIPTION = "Drunk";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "SwitchKeys";
	private static final int INDEX = 1;
	private static final int COLOR = PowerUpManager.RED;

	private Random random;
	private double changedSpeed;

	public Drunk() {
		super();

		this.index = INDEX;
		this.colorIndex = COLOR;
		random = new Random();
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
		double degree = (double) (random.nextInt(31) - 15) / 100 / Math.sqrt(delta);
		double deltaDirection = (double) player.getSpeed() * degree * delta;
		double deltaSpeed = (double) (random.nextInt(101) - 50) / 15000;
		player.modifyDirection(deltaDirection);

		if (this.changedSpeed + deltaSpeed < MAXCHANGESPEED && this.changedSpeed + deltaSpeed > -MAXCHANGESPEED) {
			player.modifySpeed(deltaSpeed);
			this.changedSpeed += deltaSpeed;
		}
	}

	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	}

	public void deactivate() {
		super.deactivate();

		player.modifySpeed(-this.changedSpeed);
	}

}
