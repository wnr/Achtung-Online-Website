package powerups;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Slow extends PowerUp {
	private static final String DESCRIPTION = "Slow";
	private static final int SPAWNCHANCE = 15;
	private static final double SPEED = 0.02;
	private static final String NAME = "SlowDown";
	private static final int INDEX = 12;
	private static final int COLOR = PowerUpManager.RED;

	public Slow() {
		super();
		
		this.amount = SPEED;
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.spawnChance = SPAWNCHANCE;
		this.description= DESCRIPTION;
		this.name = NAME;
		this.affectOthers = true;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		if(!isActive())
			return;	
	}

	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
		
		this.player.modifySpeed( - amount);
	}

	public void deactivate() {
		super.deactivate();
		
		player.modifySpeed(amount);

	}
}
