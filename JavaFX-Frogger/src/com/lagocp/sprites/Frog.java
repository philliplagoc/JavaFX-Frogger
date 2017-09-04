package com.lagocp.sprites;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This class represents a Frog in Frogger. A frog will have an image, and can
 * move (jump) in a direction by UNIT spaces.
 * 
 * @author Phillip
 *
 */
public class Frog extends Sprite {
	private static final double UNIT = 30;
	public static final double DIM_WIDTH = 60;
	public static final double DIM_HEIGHT = 60;

	public Frog(String imageFile, double x, double y, double width, double height, GraphicsContext gc) {
		super(imageFile, x, y, width, height, gc);
		Image scaled = this.scaleImage(getImage(), DIM_WIDTH, DIM_HEIGHT, true);
		setImage(scaled);
		setWidth(scaled.getWidth());
		setHalfWidth(getWidth() / 2);
		setHeight(scaled.getHeight());
		setHalfHeight(getHeight() / 2);
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getX(), getY());
		// Also going to draw lines that coincide with boundaries
		gc.strokeLine(getX(), getY(), getX() + getWidth(), getY()); // Top
		gc.strokeLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight()); // Bot
		gc.strokeLine(getX(), getY(), getX(), getY() + getHeight()); // Left
		gc.strokeLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight()); // Right
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

	@Override
	public boolean didCollideWithTopWall(Canvas canvas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean didCollideWithBotWall(Canvas canvas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean didCollideWithLeftWall(Canvas canvas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean didCollideWithRightWall(Canvas canvas) {
		// TODO Auto-generated method stub
		return false;
	}

}
