package powerups;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class SlowTurn extends PowerUp {
	private static final String DESCRIPTION = "Slow Turn";
	private static final int SPAWNCHANCE = 15;
	private static final double TURNRATIO = 0.006;
	private static final String NAME = "Invisible";
	private static final int INDEX = 11;
	private static final int COLOR = PowerUpManager.RED;

	public SlowTurn() {
		super();
		
		this.amount = TURNRATIO;
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
	
		this.player.setDegree(player.getDegree() - amount);
	}

	public void deactivate() {
		super.deactivate();
		
		player.setDegree(player.getDegree() + amount);

	}
}
