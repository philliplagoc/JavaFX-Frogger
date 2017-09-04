package com.lagocp.sprites;

import java.util.Random;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car extends Sprite {
	public static final double DIM_WIDTH = 120;
	public static final double DIM_HEIGHT = 60;
	private double maxCarSpeed = 8.0;
	private double minCarSpeed = 3.7;

	private double carSpeed;
	private Random random;

	private double xHitbox;
	private double yHitbox;
	private double widthHitbox;
	private double heightHitbox;

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

	public Car(String imageFile, double x, double y, double width, double height, GraphicsContext gc) {
		super(imageFile, x, y, width, height, gc);
		random = new Random();
		this.carSpeed = genRandomInRange(minCarSpeed, maxCarSpeed);

		Image scaled = this.scaleImage(getImage(), DIM_WIDTH, DIM_HEIGHT, true);
		// setImage(scaled);

		setWidth(scaled.getWidth());
		setHalfWidth(getWidth() / 2);
		setHeight(scaled.getHeight() - 10);
		setHalfHeight(getHeight() / 2);

		createHitbox(getX() + 10, getY() + 15, getWidth() - 20, getHeight() - 20);
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

	/**
	 * Generates a random double within the range that is inclusive of min and max.
	 * 
	 * @param min
	 *            The smallest number to generate.
	 * @param max
	 *            The largest number to generate.
	 * @return The generated number.
	 */
	private double genRandomInRange(double min, double max) {
		double randomDouble = random.nextDouble();
		double result = min + (randomDouble * (max - min));
		return result;
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		if (other instanceof Frog) {
			double frogX = ((Frog) other).getXHitbox();
			double frogY = ((Frog) other).getYHitbox();
			double frogHeight = ((Frog) other).getHeightHitbox();
			double frogWidth = ((Frog) other).getWidthHitbox();

			double x = this.getXHitbox();
			double y = this.getYHitbox();
			double height = this.getHeightHitbox();
			double width = this.getWidthHitbox();

			boolean xCond1 = x + width >= frogX;
			boolean xCond2 = x + width <= frogX + frogWidth;
			boolean xCond3 = x >= frogX;
			boolean xCond4 = x <= frogX + frogWidth;
			boolean collidedX = (xCond1 && xCond2) || (xCond3 && xCond4);
			// System.out.println("X: " + collidedX);
			boolean yCond1 = y + height >= frogY;
			boolean yCond2 = y + height <= frogY + frogHeight;
			boolean yCond3 = y >= frogY;
			boolean yCond4 = y <= frogY + frogHeight;
			boolean collidedY = (yCond1 && yCond2) || (yCond3 && yCond4);
			// System.out.println("Y: " + collidedY);

			// System.out.println("Car: " + (collidedX && collidedY));

			return collidedX && collidedY;
		}
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getX(), getY(), DIM_WIDTH, DIM_HEIGHT);
		// Drawing boundaries
		gc.strokeLine(getXHitbox(), getYHitbox(), getXHitbox() + getWidthHitbox(), getYHitbox()); // Top
		gc.strokeLine(getXHitbox(), getYHitbox() + getHeightHitbox(), getXHitbox() + getWidthHitbox(),
				getYHitbox() + getHeightHitbox()); // Bot
		gc.strokeLine(getXHitbox(), getYHitbox(), getXHitbox(), getYHitbox() + getHeightHitbox()); // Left
		gc.strokeLine(getXHitbox() + getWidthHitbox(), getYHitbox(), getXHitbox() + getWidthHitbox(),
				getYHitbox() + getHeightHitbox()); // Right
		// Try using drawStrokeRect?
	}

	@Override
	public boolean didCollideWithTopWall(Canvas canvas) {
		return false;
	}

	@Override
	public boolean didCollideWithBotWall(Canvas canvas) {
		return false;
	}

	@Override
	public boolean didCollideWithLeftWall(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();
		return getX() + getWidth() < bounds.getMinX();
	}

	@Override
	public boolean didCollideWithRightWall(Canvas canvas) {
		Bounds bounds = canvas.getBoundsInLocal();
		return getX() > bounds.getMaxX();
	}

	/**
	 * Moves the car left.
	 */
	public void moveLeft() {
		this.setvX(getCarSpeed() * -1);
	}

	public double getCarSpeed() {
		return carSpeed;
	}

	public void setCarSpeed(double carSpeed) {
		this.carSpeed = carSpeed;
	}
}
