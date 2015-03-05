/**
 * Worm class
 * 
 * The class for handling a players worms. Each object of class Worm will represent
 * one worm.
 * 
 * 2011-06-23		OMMatte			Created class
 * 2011-06-23		OMMatte			Now works with wallhack+rectangle.
 * 2011-06-25		OMMatte			Added support for wallHack swithcing Colors.
 * 2011-07-05		OMMatte			Added Undead support and changed some logics.
 * 2011-07-09		OMMatte			MAJOR CHANGE: Now saves shapes and works with the new collition. A worm now Always has a mirrorWorm.
 * 2011-07-10		OMMatte			Changed incUndeadArea, the konstant RADIUSCHANGEKONSTANT should be tested, aswell as the REMOVEUNDEADKONSTANT.
 * 2011-07-11		OMMatte			Fixed blinking wallhack bugg and added some fixes to method wallHack().
 * 2011-07-12		OMMatte			Modified the class to work with the Twin-PU.
 * 2011-07-13		OMMatte			Fixed a bug with passing by the corners of a rec-map. Added 2 new variables, recMirDouJump and recMirDouJumpUndeadArea
 */
package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import table.BodyShape;

public class Worm {
	private static final int BLINKINGKONSTANT = 500;
	private static final int REMOVEUNDEADKONSTANT = 1;
	private static final int RADIUSCHANGEKONSTANT = 50;

	private Engine engine;
	private Player player;
	private Shape wormBody;
	private Shape wormHead;
	private Color wallHackColor;

	private int tileNumber;
	private double wallHackCounter;

	private boolean mainWorm;
	private boolean switchColor;
	private Worm mirrorWorm;
	private boolean active;
	private boolean alive;
	
	private boolean rectangleMirrorDoubleJump;
	private Set<BodyShape> recMirDouJumpUndeadArea;
	
	private double direction;

	private Set<BodyShape> undeadArea;
	private List<BodyShape> wormBodies;
	private Set<Worm> sharedUndeadAreaWorms;

	public Worm(Engine engine, Player player, float x, float y, double direction) {
		this.engine = engine;
		this.player = player;
		this.direction = direction;
		wormBody = new Circle(x, y, player.getWormRadius());
		wormHead = new Circle(x, y, player.getWormRadius()
				+ Player.getHeadIncrease());
		mainWorm = true;
		undeadArea = new HashSet<BodyShape>();
		wallHackCounter = 0;
		tileNumber = -1;
		active = true;
		mirrorWorm = new Worm(engine, player, this, 0, 0);
		wormBodies = new ArrayList<BodyShape>();
		this.wallHackColor = player.getColor();
		this.sharedUndeadAreaWorms = new HashSet<Worm>();
		alive = true;
		rectangleMirrorDoubleJump = false;
	}

	public Worm(Engine engine, Player player, Worm mirrorWorm, float x, float y) {
		this.engine = engine;
		this.player = player;
		wormBody = new Circle(0, 0, player.getWormRadius());
		wormHead = new Circle(0, 0, player.getWormRadius()
				+ Player.getHeadIncrease());
		this.mirrorWorm = mirrorWorm;
		mainWorm = false;
		wormBodies = new ArrayList<BodyShape>();

		this.undeadArea = new HashSet<BodyShape>();
		this.sharedUndeadAreaWorms = new HashSet<Worm>();

		wallHackCounter = 0;
		tileNumber = -1;
		active = false;
		player.addWorm(this);
		this.wallHackColor = player.getColor();
		alive = true;
		rectangleMirrorDoubleJump = false;

	}

	public void setPosition(float x, float y) {
		wormBody.setCenterX(x);
		wormBody.setCenterY(y);
		wormHead.setCenterX(x);
		wormHead.setCenterY(y);
	}

	public void update(GameContainer gc, int delta, double deltaDirection) {
		if (!active) {
			return;
		}
		updateRadius();

		wallHackBlink(delta);

		if (!mainWorm) {
			updateAsMirror();
		} else {

			direction += deltaDirection;
			moveBody(delta);
			moveHead(delta);
		}
		tileNumber = engine.getTableMap().getTileNumber(tileNumber,
				wormBody.getCenterX(), wormBody.getCenterY());

	}

	private void wallHackBlink(int delta) {

		if (!isMainWorm() && player.isWallHack()) {
			switchColor = mirrorWorm.isSwitchColor();
			setWallHackCounter(mirrorWorm.getWallHackCounter());
			setWallHackColor(mirrorWorm.getWallHackColor());
		}

		if (!player.isWallHack() && getWallHackCounter() != 0
				&& !player.getHeadColor().equals(getWallHackColor())) {
			setWallHackCounter(0);
			setWallHackColor(player.getHeadColor());
			switchColor = false;
		}

		if (player.isWallHack() && isMainWorm()) {
			if (getWallHackCounter() == 1) {
				setWallHackCounter(0);
			}

			float r1;
			float b1;
			float g1;
			float r2;
			float b2;
			float g2;

			if (!switchColor) {
				r1 = player.getHeadColor().r;
				b1 = player.getHeadColor().b;
				g1 = player.getHeadColor().g;
				r2 = player.getColor().r;
				b2 = player.getColor().b;
				g2 = player.getColor().g;
			} else {
				r2 = player.getHeadColor().r;
				b2 = player.getHeadColor().b;
				g2 = player.getHeadColor().g;
				r1 = player.getColor().r;
				b1 = player.getColor().b;
				g1 = player.getColor().g;
			}

			float rDif = r1 - r2;
			float bDif = b1 - b2;
			float gDif = g1 - g2;

			setWallHackCounter(getWallHackCounter() + (double) delta
					/ BLINKINGKONSTANT);

			if (getWallHackCounter() >= 1) {
				setWallHackCounter(1);
				if (!switchColor) {
					switchColor = true;
				} else {
					switchColor = false;
				}
			}

			float rDelta = (float) (rDif * getWallHackCounter());
			float bDelta = (float) (bDif * getWallHackCounter());
			float gDelta = (float) (gDif * getWallHackCounter());
			setWallHackColor(new Color(r1 - rDelta, g1 - gDelta, b1 - bDelta));
		}
	}

	private void updateAsMirror() {
		if (engine.getMapManager().getActiveMap().isCircle()) {
			updateAsMirrorCircle();
		} else {
			updateAsMirrorRec();
		}
	}

	private void updateAsMirrorCircle() {
		wormBody = setNewMirrorPos(mirrorWorm.getWormBody());
		direction = setMirrorDirection(mirrorWorm.getWormBody().getCenterX(),
				mirrorWorm.getWormBody().getCenterY(),
				mirrorWorm.getDirection());
	}

	private Shape setNewMirrorPos(Shape main) {
		if (engine.getMapManager().getActiveMap().isCircle()) {
			return setNewMirrorCirclePos(main);
		} else {
			return setNewMirrorRecPos(main);
		}
	}

	private Shape setNewMirrorCirclePos(Shape main) {
		float mirrorX = main.getCenterX();
		float mirrorY = main.getCenterY();
		Shape gameArea = engine.getMapManager().getActiveMap().getGameArea();
		float x1 = gameArea.getCenterX() - mirrorX;
		float y1 = gameArea.getCenterY() - mirrorY;
		float r1 = (float) Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
		float d = gameArea.getBoundingCircleRadius() * 2;
		Circle mirrorShape = new Circle(0, 0, main.getBoundingCircleRadius());
		mirrorShape.setCenterX(d * x1 / r1 - x1 + gameArea.getCenterX());
		mirrorShape.setCenterY(d * y1 / r1 - y1 + gameArea.getCenterY());
		return mirrorShape;
	}

	private void updateAsMirrorRec() {
		wormBody = setNewMirrorRecPos(mirrorWorm.getWormBody());
		wormHead.setCenterX(wormBody.getCenterX());
		wormHead.setCenterY(wormBody.getCenterY());
		direction = mirrorWorm.getDirection();
	}

	@SuppressWarnings("unchecked")
	private Shape setNewMirrorRecPos(Shape main) {
		Shape gameArea = engine.getMapManager().getActiveMap().getGameArea();
		float x1 = main.getCenterX();
		float y1 = main.getCenterY();
		float w = gameArea.getWidth();
		float h = gameArea.getHeight();
		@SuppressWarnings("static-access")
		Circle tempHead = new Circle(0, 0, main.getBoundingCircleRadius()
				+ player.getHeadIncrease());
		tempHead.setCenterX(x1);
		tempHead.setCenterY(y1);
		Circle mirrorShape = new Circle(0, 0, main.getBoundingCircleRadius());
		mirrorShape.setCenterX(x1);
		mirrorShape.setCenterY(y1);
		int isDoubleJump = 0;

		if (tempHead.getMinX() < gameArea.getMinX()) {
			mirrorShape.setCenterX(x1 + w);
			isDoubleJump+=1;
		}
		if (tempHead.getMaxX() > gameArea.getMaxX()) {
			mirrorShape.setCenterX(x1 - w);
			isDoubleJump+=1;
		}
		if (tempHead.getMinY() < gameArea.getMinY()) {
			mirrorShape.setCenterY(y1 + h);
			isDoubleJump+=1;
		}
		if (tempHead.getMaxY() > gameArea.getMaxY()) {
			mirrorShape.setCenterY(y1 - h);
			isDoubleJump+=1;
		}
		if(isDoubleJump == 2 && !rectangleMirrorDoubleJump){
			HashSet<BodyShape> undeadArea = (HashSet<BodyShape>) this.undeadArea;
			recMirDouJumpUndeadArea = (Set<BodyShape>) undeadArea.clone();
			rectangleMirrorDoubleJump = true;
		}
		if(isDoubleJump == 1 && mirrorWorm.isRectangleMirrorDoubleJump()){
			Iterator<BodyShape> undeadBodies = mirrorWorm.getRecMirDouJumpUndeadArea().iterator();
			while(undeadBodies.hasNext()){
				BodyShape undeadBody = undeadBodies.next();
				addUndeadArea(undeadBody, true);
			}
			mirrorWorm.setRectangleMirrorDoubleJump(false);
		}
		return mirrorShape;
	}

	/**
	 * Method for moving the body of the worm.
	 * 
	 * @param delta
	 *            The delta-time.
	 */
	private void moveBody(int delta) {
		wormBody.setCenterX((float) (wormBody.getCenterX() + (Math
				.cos(direction) * player.getSpeed() * delta)));
		wormBody.setCenterY((float) (wormBody.getCenterY() + (Math
				.sin(direction) * player.getSpeed() * delta)));
	}

	/**
	 * Method for moving the head of the worm.
	 * 
	 * @param delta
	 *            The delta-time.
	 */
	private void moveHead(int delta) {
		wormHead.setCenterX((float) (wormHead.getCenterX() + (Math
				.cos(direction) * player.getSpeed() * delta)));
		wormHead.setCenterY((float) (wormHead.getCenterY() + (Math
				.sin(direction) * player.getSpeed() * delta)));
	}

	public void renderBody(GameContainer gc, Graphics g) {
		if (!active) {
			return;
		}
		
		if (!player.isInvisible() && mainWorm) {
			g.fill(wormBody, new GradientFill(45, 45, player.getColor(), 50,
					50, player.getColor()));
		}

		if (!player.isInvisible() && !mainWorm) {
			g.fill(wormBody, new GradientFill(45, 45, player.getColor(), 50,
					50, player.getColor()));
		}

	}

	/**
	 * Method for rendering the worms head.
	 * 
	 * @param gc
	 *            The container for the game.
	 * @param g
	 *            The graphics for the game.
	 * @throws SlickException
	 */
	public void renderHead(GameContainer gc, Graphics g) {
		if (!active) {
			if (!alive) {
				g.fill(wormHead, new GradientFill(45, 45,
						player.getHeadColor(), 50, 50, player.getHeadColor()));
				g.draw(wormHead, new GradientFill(45, 45,
						player.getHeadColor(), 50, 50, player.getHeadColor()));
			}
			return;
		}
		if (!player.isWallHack()) {
			g.fill(wormHead, new GradientFill(45, 45, player.getHeadColor(),
					50, 50, player.getHeadColor()));
			g.draw(wormHead, new GradientFill(45, 45, player.getHeadColor(),
					50, 50, player.getHeadColor()));
		} else {
			g.fill(wormHead, new GradientFill(45, 45, getWallHackColor(), 50,
					50, getWallHackColor()));
			g.draw(wormHead, new GradientFill(45, 45, getWallHackColor(), 50,
					50, getWallHackColor()));
		}
	}

	public void wallHack() {
		if (!mirrorWorm.isActive()) {
			mirrorWorm.setActive(true);
			mirrorWorm.setMainWorm(false);
			mirrorWorm.updateAsMirror();
			this.mainWorm = true;
		}
	}

	public void removeMirror() {
		mirrorWorm.setActive(false);
	}

	public void removeMain() {

		mirrorWorm.setNormalWorm(wormBody.getCenterX(), wormBody.getCenterY(),
				direction);

		this.mainWorm = false;
		this.active = false;
	}

	private void setNormalWorm(float x, float y, double direction) {
		this.direction = setMirrorDirection(x, y, direction);
		mainWorm = true;
		active = true;
	}

	private double setMirrorDirection(float x, float y, double direction) {
		if (!engine.getMapManager().getActiveMap().isCircle()) {
			return direction;
		}
		double oldDir = direction;
		double newDir = 0;
		float mirrorX = x;
		float mirrorY = y;
		Shape gameArea = engine.getMapManager().getActiveMap().getGameArea();
		float x1 = mirrorX - gameArea.getCenterX();
		float y1 = mirrorY - gameArea.getCenterY();
		float r1 = (float) Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
		float dir1 = (float) Math.acos(x1 / r1);

		if (y1 < 0) {
			dir1 += (Math.PI - dir1) * 2;
		}

		while (oldDir < 0) {
			oldDir += (2 * Math.PI);
		}
		while (oldDir > 2 * Math.PI) {
			oldDir -= (2 * Math.PI);
		}

		newDir = oldDir + (dir1 - oldDir) * 2;
		return newDir;
	}

	public void incUndeadArea(Shape wormShape) {
		boolean finnished = false;
		Shape outsideShape = null;
		for (int i = 0; !finnished; i++) {
			if (wormBodies.size() - 1 - i < 0) {
				return;
			}
			BodyShape bodyShape = wormBodies.get(wormBodies.size() - 1 - i);
			if (bodyShape.getWormBody().intersects(wormShape)) {
				undeadArea.add(bodyShape);
			} else {
				finnished = true;

				for (int s = i + 1; s < (i + wormShape
						.getBoundingCircleRadius() * RADIUSCHANGEKONSTANT); s++) {
					if (wormBodies.size() - 1 - s < 0) {
						return;
					}
					bodyShape = wormBodies.get(wormBodies.size() - 1 - s);
					if (bodyShape.getWormBody().intersects(wormShape)) {
						undeadArea.add(bodyShape);
					}
				}
				outsideShape = wormBodies.get(
						(int) (wormBodies.size() - 1 - (i + wormShape
								.getBoundingCircleRadius()
								* RADIUSCHANGEKONSTANT))).getWormBody();
			}
		}
		ArrayList<LinkedList<BodyShape>> bodyList = engine.getTableMap()
				.getTouchedBodyShapes(outsideShape, tileNumber,
						new boolean[engine.getTableMap().getTiles().size()],
						new ArrayList<LinkedList<BodyShape>>());

		Iterator<LinkedList<BodyShape>> bodyListIt = bodyList.iterator();
		while (bodyListIt.hasNext()) {
			LinkedList<BodyShape> bodies = bodyListIt.next();
			Iterator<BodyShape> bodiesIt = bodies.iterator();
			while (bodiesIt.hasNext()) {
				BodyShape oldBody = bodiesIt.next();
				if (undeadArea.contains(oldBody)) {
					continue;
				}
				if (outsideShape.intersects(oldBody.getWormBody())) {
					undeadArea.add(oldBody);
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public void updateRadius() {
		wormBody = new Circle(wormBody.getCenterX(), wormBody.getCenterY(),
				player.getWormRadius());
		if (!player.isTronTurn()) {
			wormHead = new Circle(wormBody.getCenterX(), wormBody.getCenterY(),
					player.getWormRadius() + player.getHeadIncrease());

		} else {
			wormHead = new Rectangle(0, 0, player.getWormRadius() * 2
					+ player.getHeadIncrease(), player.getWormRadius() * 2
					+ player.getHeadIncrease());
			wormHead.setCenterX(wormBody.getCenterX());
			wormHead.setCenterY(wormBody.getCenterY());
		}
	}

	public void resetAllBodies() {
		undeadArea.clear();
		wormBodies.clear();
	}

	public Shape getWormBody() {
		return wormBody;
	}

	public void setWormBody(Shape wormBody) {
		this.wormBody = wormBody;
	}

	public Shape getWormHead() {
		return wormHead;
	}

	public void setWormHead(Shape wormHead) {
		this.wormHead = wormHead;
	}

	public double getDirection() {
		return direction;
	}

	public Worm getMirrorWorm() {
		return mirrorWorm;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public Player getPlayer() {
		return player;
	}

	public List<BodyShape> getWormBodies() {
		return wormBodies;
	}

	public void addWormBodies(BodyShape wormBody) {
		this.wormBodies.add(wormBody);
	}

	public boolean isMainWorm() {
		return mainWorm;
	}

	public boolean hasMirrorWorm() {
		if (mirrorWorm == null) {
			return false;
		}
		return true;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Set<BodyShape> getUndeadArea() {
		return undeadArea;
	}

	public void addUndeadArea(BodyShape undeadArea, boolean addToShared) {
		this.undeadArea.add(undeadArea);
		if (addToShared) {
			Iterator<Worm> sharedWorms = sharedUndeadAreaWorms.iterator();
			while (sharedWorms.hasNext()) {
				Worm sharedWorm = sharedWorms.next();
				sharedWorm.addUndeadArea(undeadArea, false);
				sharedWorm.mirrorWorm.addUndeadArea(undeadArea, false);
			}
		}
	}

	public void checkUndeadArea() {
		Iterator<BodyShape> undeadShapes = undeadArea.iterator();
		while (undeadShapes.hasNext()) {
			Shape undeadShape = undeadShapes.next().getWormBody();
			if (!undeadShape.intersects(wormBody)) {
				if (Math.sqrt(Math.pow(
						undeadShape.getCenterX() - wormBody.getCenterX(), 2)
						+ Math.pow(
								undeadShape.getCenterY()
										- wormBody.getCenterY(), 2))
						- wormBody.getBoundingCircleRadius() > undeadShape
						.getBoundingCircleRadius() + REMOVEUNDEADKONSTANT) {
					undeadShapes.remove();
				}
			}
		}

	}

	public void checkSharedUndeadWorms() {
		if (!this.mainWorm) {
			return;
		}
		Iterator<Worm> sharedWorms = sharedUndeadAreaWorms.iterator();
		while (sharedWorms.hasNext()) {
			Worm sharedWorm = sharedWorms.next();
			if (!wormBody.intersects(sharedWorm.getWormBody())
					&& !wormBody.intersects(sharedWorm.getMirrorWorm()
							.getWormBody())) {
				if (Math.sqrt(Math.pow(sharedWorm.getWormBody().getCenterX()
						- wormBody.getCenterX(), 2)
						+ Math.pow(sharedWorm.getWormBody().getCenterY()
								- wormBody.getCenterY(), 2))
						- wormBody.getBoundingCircleRadius() > sharedWorm
						.getWormBody().getBoundingCircleRadius()
						+ REMOVEUNDEADKONSTANT) {
					if (Math.sqrt(Math.pow(sharedWorm.getMirrorWorm()
							.getWormBody().getCenterX()
							- wormBody.getCenterX(), 2)
							+ Math.pow(sharedWorm.getMirrorWorm().getWormBody()
									.getCenterY()
									- wormBody.getCenterY(), 2))
							- wormBody.getBoundingCircleRadius() > sharedWorm
							.getMirrorWorm().getWormBody()
							.getBoundingCircleRadius()
							+ REMOVEUNDEADKONSTANT) {
						sharedWorms.remove();
					}
				}
			}
			// Iterator<BodyShape> mainUndeadWorms =
			// sharedWorm.getUndeadArea().iterator();
			// while(mainUndeadWorms.hasNext()){
			// BodyShape undeadBody = mainUndeadWorms.next();
			// if(wormBody.intersects(undeadBody.getWormBody())){
			// continue start;
			// }
			// }
			// Iterator<BodyShape> mirrorUndeadWorms =
			// sharedWorm.getMirrorWorm().getUndeadArea().iterator();
			// while(mirrorUndeadWorms.hasNext()){
			// BodyShape undeadBody = mirrorUndeadWorms.next();
			// if(wormBody.intersects(undeadBody.getWormBody())){
			// continue start;
			// }
			// }
			// if(!wormBody.intersects(sharedWorm.getWormBody()) &&
			// !wormBody.intersects(sharedWorm.getMirrorWorm().getWormBody())) {
			// sharedWorm.removeSharedUndeadAreaWorm(this);
			// sharedWorms.remove();
			// }
			//
			// }

		}
	}

	public void removeSharedUndeadAreaWorm(Worm worm) {
		sharedUndeadAreaWorms.remove(worm);
		sharedUndeadAreaWorms.remove(worm.mirrorWorm);
	}

	public int getTileNumber() {
		return tileNumber;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getWallHackCounter() {
		return wallHackCounter;
	}

	public void setWallHackCounter(double wallHackCounter) {
		this.wallHackCounter = wallHackCounter;
	}

	public void setMainWorm(boolean mainWorm) {
		this.mainWorm = mainWorm;
	}

	public boolean isSwitchColor() {
		return switchColor;
	}

	public void setSwitchColor(boolean switchColor) {
		this.switchColor = switchColor;
	}

	public Color getWallHackColor() {
		return wallHackColor;
	}

	public void setWallHackColor(Color wallHackColor) {
		this.wallHackColor = wallHackColor;
	}

	public Set<Worm> getSharedUndeadAreaWorms() {
		return sharedUndeadAreaWorms;
	}

	public void addSharedUndeadAreaWorm(Worm worm) {
		this.sharedUndeadAreaWorms.add(worm);
	}

	public void die() {
		active = false;
		alive = false;
		mirrorWorm.setActive(false);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setUndeadArea(Set<BodyShape> undeadArea) {
		this.undeadArea = undeadArea;
	}

	public boolean isRectangleMirrorDoubleJump() {
		return rectangleMirrorDoubleJump;
	}

	public void setRectangleMirrorDoubleJump(boolean rectangleMirrorDoubleJump) {
		this.rectangleMirrorDoubleJump = rectangleMirrorDoubleJump;
	}

	public Set<BodyShape> getRecMirDouJumpUndeadArea() {
		return recMirDouJumpUndeadArea;
	}
}
