package com.lagocp.sprites;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.geometry.Bounds;
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
	public static final double DIM_HEIGHT = 54;
	
	private static final double RENDER_WIDTH = 40;
	private static final double RENDER_HEIGHT = 55;

	private double xHitbox;
	private double yHitbox;
	private double widthHitbox;
	private double heightHitbox;

	private static final double XHITBOX_OFFSET = 10;
	private static final double YHITBOX_OFFSET = 5;
	private static final double WIDTH_HITBOX_OFFSET = 20;
	private static final double HEIGHT_HITBOX_OFFSET = 15;

	public Frog(String imageFile, double x, double y, double width, double height, GraphicsContext gc) {
		super(imageFile, x, y, width, height, gc);

		Image scaled = this.scaleImage(getImage(), DIM_WIDTH, DIM_HEIGHT, true);

		setWidth(scaled.getWidth());
		setHalfWidth(getWidth() / 2);
		setHeight(scaled.getHeight());
		setHalfHeight(getHeight() / 2);

		createHitbox(getX() + XHITBOX_OFFSET, getY() + YHITBOX_OFFSET, getWidth() - WIDTH_HITBOX_OFFSET,
				getHeight() - HEIGHT_HITBOX_OFFSET);
	}

	/**
	 * Creates a hit box for the Sprite.
	 * 
	 * @param x
	 *            The upper-left x coordinate
	 * @param y
	 *            The upper-left y coordinate
	 * @param width
	 *            The width of the hit box
	 * @param height
	 *            The height of the hit box
	 */
	private void createHitbox(double x, double y, double width, double height) {
		setXHitbox(x);
		setYHitbox(y);
		setWidthHitbox(width);
		setHeightHitbox(height);
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		if (other instanceof Car) {
			double carX = ((Car) other).getXHitbox();
			double carY = ((Car) other).getYHitbox();
			double carHeight = ((Car) other).getHeightHitbox();
			double carWidth = ((Car) other).getWidthHitbox();

			double x = this.getXHitbox();
			double y = this.getYHitbox();
			double height = this.getHeightHitbox();
			double width = this.getWidthHitbox();

			boolean xCond1 = x + width >= carX;
			boolean xCond2 = x + width <= carX + carWidth;
			boolean xCond3 = x >= carX;
			boolean xCond4 = x <= carX + carWidth;
			boolean collidedX = (xCond1 && xCond2) || (xCond3 && xCond4);
			// System.out.println("X: " + collidedX);
			boolean yCond1 = y + height >= carY;
			boolean yCond2 = y + height <= carY + carHeight;
			boolean yCond3 = y >= carY;
			boolean yCond4 = y <= carY + carHeight;
			boolean collidedY = (yCond1 && yCond2) || (yCond3 && yCond4);
			// System.out.println("Y: " + collidedY);

			// System.out.println("Frog: " +(collidedX && collidedY));

			return collidedX && collidedY;
		}
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getX(), getY(), RENDER_WIDTH, RENDER_HEIGHT);
		// Also going to draw lines that coincide with boundaries
//		gc.strokeLine(getXHitbox(), getYHitbox(), getXHitbox() + getWidthHitbox(), getYHitbox()); // Top
//		gc.strokeLine(getXHitbox(), getYHitbox() + getHeightHitbox(), getXHitbox() + getWidthHitbox(),
//				getYHitbox() + getHeightHitbox()); // Bot
//		gc.strokeLine(getXHitbox(), getYHitbox(), getXHitbox(), getYHitbox() + getHeightHitbox()); // Left
//		gc.strokeLine(getXHitbox() + getWidthHitbox(), getYHitbox(), getXHitbox() + getWidthHitbox(),
//				getYHitbox() + getHeightHitbox()); // Right
	}

	@Override
	public void update(double time) {
		this.x += this.getvX() * time;
		this.y += this.getvY() * time;

		this.setCenterX(this.getX() + this.getHalfWidth());
		this.setCenterY(this.getY() + this.getHalfHeight());

		setXHitbox(getX() + XHITBOX_OFFSET);
		setYHitbox(getY() + YHITBOX_OFFSET);
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
		Bounds bounds = canvas.getBoundsInLocal();
		
		return getYHitbox() <= bounds.getMinY();
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

	public void setXHitbox(double x) {
		xHitbox = x;
	}

	public void setYHitbox(double y) {
		yHitbox = y;
	}

	public void setWidthHitbox(double width) {
		widthHitbox = width;
	}

	public void setHeightHitbox(double height) {
		heightHitbox = height;
	}

	public double getXHitbox() {
		return xHitbox;
	}

	public double getYHitbox() {
		return yHitbox;
	}

	public double getWidthHitbox() {
		return widthHitbox;
	}

	public double getHeightHitbox() {
		return heightHitbox;
	}

}
