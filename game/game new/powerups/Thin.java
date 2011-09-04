package powerups;

import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Thin extends PowerUp {
	private static final String DESCRIPTION = "Thin";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Small";
	private static final int INDEX = 9;
	private static final int COLOR = PowerUpManager.GREEN;


	public Thin() {
		super();
		
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.spawnChance = SPAWNCHANCE;
		this.description= DESCRIPTION;
		this.name = NAME;
		this.affectSelf = true;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		if(!isActive())
			return;
	}

	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	
		this.player.modifyWormRadius((float) (-player.getRealWormRadius()/2));
	}
	
	public void deactivate() {
		super.deactivate();
		
		this.player.modifyWormRadius((float) (player.getRealWormRadius()));
	}


	
}
