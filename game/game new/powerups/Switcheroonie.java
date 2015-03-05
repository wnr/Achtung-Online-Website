/**
 * Switcharoonie class
 * 
 * 2011-06-11		OMMatte			Created class
 * 2011-06-23		OMMatte			Modified to work with Worm-class
 * 2011-07-09		OMMatte			Small change to work with new UndeadArea.
 */
package powerups;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Circle;

import core.Engine;
import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class Switcheroonie extends PowerUp {
	private static final String DESCRIPTION = "Switch All Player Positions";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "Switcharoonie";
	private static final int INDEX = 7;
	private static final int COLOR = PowerUpManager.BLUE;

	private Engine engine;
	private Random random;
	private List<Player> oldPlayerPos;
	private List<Player> newPlayerPos;

	public Switcheroonie(Engine engine) {
		super();

		this.engine = engine;
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index,
				colorIndex);
		this.spawnChance = SPAWNCHANCE;
		this.description = DESCRIPTION;
		this.name = NAME;
		oldPlayerPos = new LinkedList<Player>();
		newPlayerPos = new LinkedList<Player>();
		random = new Random();
		this.affectSelf = true;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		if (!isActive())
			return;
	}

	@SuppressWarnings("unchecked")
	public void activate(Player player, Worm worm) {
		super.activate(player, worm);
	
		boolean samePos = true;
		while (samePos) {
			oldPlayerPos.clear();
			newPlayerPos.clear();
			Iterator<Player> it = engine.getPlayerManager().getAlivePlayers()
					.iterator();
			while (it.hasNext()) {
				Player p = it.next();
				oldPlayerPos.add(p);
				newPlayerPos.add(null);
			}
			for (int i = 0; i < oldPlayerPos.size(); i++) {
				boolean taken = true;
				while (taken) {
					int s = random.nextInt(oldPlayerPos.size());
					if (newPlayerPos.get(s) != null) {
						taken = true;
					} else {
						taken = false;
						newPlayerPos.set(s, oldPlayerPos.get(i));
					}
				}
			}
			samePos = false;
			for (int i = 0; i < oldPlayerPos.size(); i++) {
				if (oldPlayerPos.get(i).equals(newPlayerPos.get(i))) {
					samePos = true;
				}
			}
		}

		LinkedList<LinkedList<Worm>> newWormList = new LinkedList<LinkedList<Worm>>();
		for (int i = 0; i < oldPlayerPos.size(); i++) {
			LinkedList<Worm> worms = new LinkedList<Worm>();
			Player newPlayer = newPlayerPos.get(i);
			worms = (LinkedList<Worm>) ((LinkedList<Worm>) newPlayer.getWorms())
					.clone();
			newWormList.add(worms);
		}

		for (int i = 0; i < oldPlayerPos.size(); i++) {
			Player oldPlayer = oldPlayerPos.get(i);
			oldPlayer.setWorms(newWormList.get(i));
			Iterator<Worm> worms = oldPlayer.getWorms().iterator();
			while (worms.hasNext()) {

				worm = worms.next();
				if (oldPlayer.getWormRadius() > worm.getPlayer()
						.getWormRadius()) {
					worm.incUndeadArea(new Circle(worm.getWormBody()
							.getCenterX(), worm.getWormBody().getCenterY(),
							oldPlayer.getWormRadius()));
				}

				worm.setPlayer(oldPlayer);

			}
		}
	}

	public void deactivate() {
		super.deactivate();
	}

}
