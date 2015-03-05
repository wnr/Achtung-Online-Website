/**
 * 2011-06-16		mrWiener		Fixed time update. See Issue #75
 * 2011-07-09		OMMatte			Modified to work with object-oriented collition.
 */

package powerups;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;

import core.Engine;
import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Clear extends PowerUp {
	private static final String DESCRIPTION = "Clears The Map";
	private static final int SPAWNCHANCE = 7;
	private static final String NAME = "Clear";
	private static final int INDEX = 0;
	private static final int COLOR = PowerUpManager.BLUE;
	
	private Engine engine;

	public Clear(Engine engine) {
		super();
		
		this.engine = engine;
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		this.spawnChance = SPAWNCHANCE;
		this.description= DESCRIPTION;
		this.name = NAME;
		this.affectSelf = true;
		this.time = 0;
	}

	@Override
	public void update(GameContainer gc, int delta){
		super.update(gc, delta);
		
		engine.getStateManager().getGame().resetWormBodyImage();
		engine.getTableMap().reset();
		Iterator<Player> playersIt = engine.getPlayerManager().getPlayersIterator();
		while(playersIt.hasNext()){
			Player player = playersIt.next();
			player.resetAllBodies();
		}
	}
	
	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	}
	
	public void deactivate(){
		super.deactivate();
	}
}
