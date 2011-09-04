/**
 * 2011-07-09		OMMatte			Created Class.
 */
package table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Shape;

import core.Engine;
import core.Map;

public class TableMap {
	private static final int AMOUNTOFTILES = 40;

	private Engine engine;
	private ArrayList<Tile> tiles;
	private int[] closeEight;

	public TableMap(Engine engine) {
		this.engine = engine;
		tiles = new ArrayList<Tile>();
		closeEight = new int[8];
		closeEight[0] = -1;
		closeEight[1] = 1;
		closeEight[2] = -AMOUNTOFTILES + 1;
		closeEight[3] = -AMOUNTOFTILES;
		closeEight[4] = -AMOUNTOFTILES - 1;
		closeEight[5] = AMOUNTOFTILES - 1;
		closeEight[6] = AMOUNTOFTILES;
		closeEight[7] = AMOUNTOFTILES + 1;

	}

	public void reset() {
		Iterator<Tile> tiles = this.tiles.iterator();
		while (tiles.hasNext()) {
			Tile tile = tiles.next();
			tile.reset();
		}
	}

	public void load(GameContainer gc) {
		Map map = engine.getMapManager().getActiveMap();
		float x = map.getGameArea().getWidth() / AMOUNTOFTILES;
		float y = map.getGameArea().getHeight() / AMOUNTOFTILES;

		for (int i = 0; i < AMOUNTOFTILES; i++) {

			for (int s = 0; s < AMOUNTOFTILES; s++) {
				Tile tile = new Tile(map.getGameArea().getX() + s * x, map
						.getGameArea().getY() + i * y, x, y);
				tiles.add(tile);
			}
		}

	}

	public void addBodyShape(BodyShape bodyShape, int tileNumber,
			boolean[] checkedTiles) {
		if (tileNumber == -1) {
			return;
		}
		if (checkedTiles[tileNumber]) {
			return;
		}
		Shape wormBody = bodyShape.getWormBody();
		if (!tiles.get(tileNumber).getTileShape().intersects(wormBody)) {
			return;
		} else {
			tiles.get(tileNumber).addBodyShape(bodyShape);
			checkedTiles[tileNumber] = true;
		}

		for (int i = 0; i < closeEight.length; i++) {
			if (tileNumber + closeEight[i] < 0
					|| tileNumber + closeEight[i] > tiles.size() - 1) {
				continue;
			}
			if (tiles.get(tileNumber + closeEight[i]).getTileShape()
					.intersects(wormBody)) {

				if (checkedTiles[tileNumber + closeEight[i]]) {
					continue;
				}

				addBodyShape(bodyShape, tileNumber + closeEight[i],
						checkedTiles);

			}
		}
	}

	public int getTileNumber(int currentNr, float x, float y) {
		if (currentNr != -1) {
			if (tiles.get(currentNr).getTileShape().contains(x, y)) {
				return currentNr;
			} else {
				for (int i = 0; i < closeEight.length; i++) {
					if (currentNr + closeEight[i] < 0
							|| currentNr + closeEight[i] > tiles.size() - 1) {
						continue;
					}
					if (tiles.get(currentNr + closeEight[i]).getTileShape()
							.contains(x, y)) {
						return currentNr + closeEight[i];
					}
				}
			}
		}
		float mapStartX = engine.getMapManager().getActiveMap().getGameArea()
				.getX();
		float mapStartY = engine.getMapManager().getActiveMap().getGameArea()
				.getY();
		float tileLeangth = engine.getMapManager().getActiveMap().getGameArea()
				.getWidth()
				/ AMOUNTOFTILES;
		int tileX = -1;
		int tileY = -1;
		for (int i = 0; i < AMOUNTOFTILES; i++) {
			if (x < mapStartX + tileLeangth * i) {
				tileX = i;
				break;
			}

		}
		if (tileX == -1) {
			tileX = AMOUNTOFTILES - 1;
		}
		for (int i = 0; i < AMOUNTOFTILES; i++) {
			if (y < mapStartY + tileLeangth * i) {
				tileY = i;
				break;
			}

		}
		if (tileY == -1) {
			tileY = AMOUNTOFTILES - 1;
		}
		return tileX + tileY * AMOUNTOFTILES;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public ArrayList<LinkedList<BodyShape>> getTouchedBodyShapes(
			Shape wormBody, int tileNumber, boolean[] checkedTiles,
			ArrayList<LinkedList<BodyShape>> returnArray) {
		if (tileNumber == -1) {
			return returnArray;
		}
		if (checkedTiles[tileNumber]) {
			return returnArray;
		}
		if (!tiles.get(tileNumber).getTileShape().intersects(wormBody)) {
			return returnArray;
		} else {
			returnArray.add(tiles.get(tileNumber).getWormBodies());
			checkedTiles[tileNumber] = true;
		}

		for (int i = 0; i < closeEight.length; i++) {
			if (tileNumber + closeEight[i] < 0
					|| tileNumber + closeEight[i] > tiles.size() - 1) {
				continue;
			}
			if (tiles.get(tileNumber + closeEight[i]).getTileShape()
					.intersects(wormBody)) {

				if (checkedTiles[tileNumber + closeEight[i]]) {
					continue;
				}

				getTouchedBodyShapes(wormBody, tileNumber + closeEight[i],
						checkedTiles, returnArray);

			}
		}
		return returnArray;
	}

}
