/**
 * Twin class
 * 
 * 2011-07-12		OMMatte			Created class
 */
package powerups;

import java.util.Iterator;

import org.newdawn.slick.GameContainer;

import table.BodyShape;

import core.Engine;
import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Twin extends PowerUp {
	private static final String DESCRIPTION = "Twin";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Twin";
	private static final int INDEX = 4;
	private static final int COLOR = PowerUpManager.GREEN;
	
	private Engine engine;
	

	public Twin(Engine engine) {
		super();
		this.engine = engine;
		
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
		if(!worm.isMainWorm()){
			super.activate(player, worm.getMirrorWorm());
		}else{
			super.activate(player, worm);
		}
		
		this.worm.setDirection(this.worm.getDirection()-Math.PI/4);
		Worm newWorm = new Worm(engine, player, this.worm.getWormBody().getCenterX(),this.worm.getWormBody().getCenterY(),this.worm.getDirection()+Math.PI/2);
		Iterator<BodyShape> undeadArea = this.worm.getUndeadArea().iterator();
		while(undeadArea.hasNext()){
			BodyShape undeadBody = undeadArea.next();
			newWorm.addUndeadArea(undeadBody, false);
			newWorm.getMirrorWorm().addUndeadArea(undeadBody, false);
		}
		undeadArea = this.worm.getMirrorWorm().getUndeadArea().iterator();
		while(undeadArea.hasNext()){
			BodyShape undeadBody = undeadArea.next();
			newWorm.addUndeadArea(undeadBody, false);
			newWorm.getMirrorWorm().addUndeadArea(undeadBody, false);
		}
		
		Iterator<Worm> otherSharedWorms = this.worm.getSharedUndeadAreaWorms().iterator();
		while(otherSharedWorms.hasNext()){
			Worm sharedWorm = otherSharedWorms.next();
			newWorm.addSharedUndeadAreaWorm(sharedWorm);
			newWorm.getMirrorWorm().addSharedUndeadAreaWorm(sharedWorm);
			sharedWorm.addSharedUndeadAreaWorm(newWorm);
			sharedWorm.getMirrorWorm().addSharedUndeadAreaWorm(newWorm);
		}
		
		newWorm.addSharedUndeadAreaWorm(this.worm);
		newWorm.getMirrorWorm().addSharedUndeadAreaWorm(this.worm);
		this.worm.addSharedUndeadAreaWorm(newWorm);
		this.worm.getMirrorWorm().addSharedUndeadAreaWorm(newWorm);
		
		
		player.addWorm(newWorm);
		
		deactivate();
	}
	
	public void deactivate() {
		
		super.deactivate();
		
	}
}

		
