/**
 * 2011-07-09		OMMatte			Created Class.
 */
package table;

import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Tile {
	private Shape tileShape;

	private LinkedList<BodyShape> wormBodies;

	public Tile(float x, float y, float width, float height) {
		this.tileShape = new Rectangle(x, y, width, height);
		wormBodies = new LinkedList<BodyShape>();
	}
	
	public void addBodyShape(BodyShape bodyShape) {
		wormBodies.add(bodyShape);
	}

	public Shape getTileShape() {
		return tileShape;
	}

	public LinkedList<BodyShape> getWormBodies() {
		return wormBodies;
	}

	public void reset() {
		wormBodies.clear();
	}

}
