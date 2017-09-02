package com.lagocp.sprites;

import java.util.Random;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Car extends Sprite {
	public static final double RENDER_DIM_WIDTH = 120;
	public static final double RENDER_DIM_HEIGHT = 60;
	private static final double MAX_CAR_SPEED = 8.0;
	private static final double MIN_CAR_SPEED = 3.7;
	
	private double carSpeed;
	private Random random;
	
	public Car(String imageFile, double x, double y, double width, double height, GraphicsContext gc) {
		super(imageFile, x, y, width, height, gc);
		random = new Random();
		this.carSpeed = genRandomInRange(MIN_CAR_SPEED, MAX_CAR_SPEED);
	}

	/**
	 * Generates a random double within the range that is inclusive of min and max.
	 * @param min The smallest number to generate.
	 * @param max The largest number to generate.
	 * @return The generated number.
	 */
	private double genRandomInRange(double min, double max) {
		double randomDouble = random.nextDouble();
		double result = min + (randomDouble * (max - min));
		return result;
	}
	@Override
	public boolean didCollideWith(Sprite other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getX(), getY(), RENDER_DIM_WIDTH, RENDER_DIM_HEIGHT);
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
		System.out.println(getCarSpeed());
	}

	public double getCarSpeed() {
		return carSpeed;
	}
	
	public void setCarSpeed(double carSpeed) {
		this.carSpeed = carSpeed;
	}
}
