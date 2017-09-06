package com.lagocp.sprites;

import com.lagocp.gameEngine.sprite.Sprite;
import com.lagocp.main.FroggerApp;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class represents a track on which Cars will be on.
 * 
 * @author Phillip
 *
 */
public class Track extends Sprite {
	private int limit; // The max number of cars allowed to be on this track

	public static final double WIDTH = FroggerApp.CANVAS_WIDTH;
	public static final double HEIGHT = Car.DIM_HEIGHT - 5;
	private static final Color COLOR = Color.GRAY;

	private Car[] cars;

	private double[] dashes = { 0, 30, 60, 90, 120, 150 };

	public Track(double x, double y, double width, double height, GraphicsContext gc, int limit) {
		super(x, y, width, height, gc);
		setLimit(limit);
		cars = new Car[getLimit()];
	}

	@Override
	public boolean didCollideWith(Sprite other) {
		return false;
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
		return false;
	}

	@Override
	public boolean didCollideWithRightWall(Canvas canvas) {
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.setFill(COLOR);
		gc.fillRect(getX(), getY(), WIDTH, HEIGHT);

		/*
		 * Drawing dashes damages performance 
		 * 
		 * gc.setStroke(Color.YELLOW); boolean on = true;
		 * gc.moveTo(getX(), getY() + (HEIGHT / 2)); for (double xx = getX(); xx <=
		 * FroggerApp.CANVAS_WIDTH; xx += 10) { if (on) gc.lineTo(xx, getY() + (HEIGHT /
		 * 2)); else gc.moveTo(xx, getY() + (HEIGHT / 2)); on = !on; } gc.stroke();
		 * 
		 * gc.setStroke(Color.BLACK);
		 */
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Car[] getCars() {
		return cars;
	}

	public void setCars(Car[] cars) {
		this.cars = cars;
	}

}
