package powerups;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Thick extends PowerUp {
	private static final String DESCRIPTION = "Thick";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Fat";
	private static final int INDEX = 8;
	private static final int COLOR = PowerUpManager.RED;
	

	public Thick() {
		super();
		
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
	
		this.player.modifyWormRadius((float) (player.getRealWormRadius()));
	}

	public void deactivate() {
		super.deactivate();
		
		this.player.modifyWormRadius((float) (-player.getRealWormRadius()/2));
	}
}

		
