package powerups;


import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class FastTurn extends PowerUp {
	private static final String DESCRIPTION = "Fast Turn";
	private static final int SPAWNCHANCE = 15;
	private static final double TURNRATIO = 0.006;
	private static final String NAME = "FastTurn";
	private static final int INDEX = 3;
	private static final int COLOR = PowerUpManager.GREEN;
	

	public FastTurn() {
		super();
		
		this.amount = TURNRATIO;
//		powerUpShape = new Circle(x,y,RADIUS);
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.time=TIME;
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
	
		player.setDegree(player.getDegree() + amount);
	}

	public void deactivate() {
		super.deactivate();
		
		player.setDegree(player.getDegree() -amount);
	}


	
}
