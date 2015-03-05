package powerups;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import core.Player;
import core.PowerUp;
import core.PowerUpManager;
import core.Worm;

public class SwitchKeys extends PowerUp {
	private static final String DESCRIPTION = "Switch Keybindings";
	private static final int SPAWNCHANCE = 15;
	private static final String NAME = "SwitchKeys";
	private static final Color POWERUPCOLOR = Color.magenta;
	private static final int INDEX = 6;
	private static final int COLOR = PowerUpManager.RED;

	private Color headColor;

	public SwitchKeys() {
		super();
		this.index = INDEX;
		this.colorIndex = COLOR;
		this.powerUpIcon = PowerUpManager.POWERUPSPRITE.getIcon(index, colorIndex);
		headColor = Player.getHeadcolor();
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
	
		int tempLeft = player.getLeftKey();
		player.setLeftKey(player.getRightKey());
		player.setRightKey(tempLeft);
		if (!player.isKeySwitched()) {
			player.setKeySwitched(true);
			player.setHeadColor(POWERUPCOLOR);
		} else {
			player.setKeySwitched(false);
			player.setHeadColor(headColor);
		}

	}

	public void deactivate() {
		super.deactivate();

		int tempLeft = player.getLeftKey();
		player.setLeftKey(player.getRightKey());
		player.setRightKey(tempLeft);
		if (!player.isKeySwitched()) {
			player.setKeySwitched(true);
			player.setHeadColor(POWERUPCOLOR);
		} else {
			player.setKeySwitched(false);
			player.setHeadColor(headColor);
		}
	}
}
