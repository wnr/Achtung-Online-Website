package powerups;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Circle;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Invisible extends PowerUp {
	private static final String DESCRIPTION = "Super Jump";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Invisible";
	private static final int INDEX = 5;
	private static final int COLOR = PowerUpManager.GREEN;

	public Invisible() {
		super();
		
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.spawnChance = SPAWNCHANCE;
		this.description= DESCRIPTION;
		this.name = NAME;
		this.affectSelf = true;
	}
	
	public Invisible(double time, int x, int y) {
		super();
		
		this.time = time;
		powerUpShape = new Circle(x, y, RADIUS);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);

		if(!isActive())
			return;
		
		if(!player.isInvisible()){
			player.setVisibility(false);
			}
	}

	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	
		
	}
	
	public void deactivate() {
		super.deactivate();
		
		player.setVisibility(true);
	}
}
