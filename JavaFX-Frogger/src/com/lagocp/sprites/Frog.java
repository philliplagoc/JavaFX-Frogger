package com.lagocp.sprites;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents a Frog in Frogger.
 * A frog will have an image, and can move (jump) in
 * a direction by UNIT spaces.
 * @author Phillip
 *
 */
public class Frog extends Sprite {
	private static final double UNIT = 50;
	public static final double RENDER_DIM = 70;
	
	public Frog(String imageFile, double x, double y, double width, double height, GraphicsContext gc) {
		super(imageFile, x, y, width, height, gc);
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		return false;
	}

	@Override
	public boolean didCollideWithWalls(Canvas canvas) {
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getX(), getY(), RENDER_DIM, RENDER_DIM);
	}
	
	/**
	 * Moves the frog one unit upwards.
	 */
	public void moveUp() {
		this.setY(this.getY() - UNIT);
	}
	
	/**
	 * Moves the frog one unit left.
	 */
	public void moveLeft() {
		this.setX(this.getX() - UNIT);
	}
	
	/**
	 * Moves the frog one unit right.
	 */
	public void moveRight() {
		this.setX(this.getX() + UNIT);
	}
	
	/**
	 * Moves the unit one unit down.
	 */
	public void moveDown() {
		this.setY(this.getY() + UNIT);
	}

}
