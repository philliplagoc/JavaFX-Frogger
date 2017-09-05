package com.lagocp.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.lagocp.sprites.Car;
import com.lagocp.sprites.Frog;
import com.lagocp.ui.FroggerUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * This is the main class where the application will run from.
 * 
 * NOTE: Everything moves in units of 3, and all scaling should be done to
 * factors of 3. Don't make collisions <= or >=; instead, make collisions
 * strictly < or >.
 * 
 * @author Phillip
 *
 */
public class FroggerApp extends Application {
	private static final double CANVAS_WIDTH = 540;
	private static final double CANVAS_HEIGHT = 720;

	private Frog frog;
	private static final String FROG_FILE_NAME = "/com/lagocp/assets/frog.png";
	private static final double FROG_DIM_WIDTH = Frog.DIM_WIDTH;
	private static final double FROG_SPAWN_X = CANVAS_WIDTH / 2 - (FROG_DIM_WIDTH / 2);
	private static final double FROG_SPAWN_Y = CANVAS_HEIGHT - FROG_DIM_WIDTH;

	private ArrayList<Car> cars = new ArrayList<Car>();
	private static final String CAR_LEFT_NAME = "LeftFacing";
	private static final String CAR_RIGHT_NAME = "RightFacing";
	private static final String CAR_LEFT_FILE_NAME = "/com/lagocp/assets/car-facing-left.png";
	private static final String CAR_RIGHT_FILE_NAME = "/com/lagocp/assets/car-facing-right.png";
	private static final double CAR_DIM_HEIGHT = Car.DIM_HEIGHT;

	private static final double ELAPSED_TIME_SPEED = 1;

	private Set<String> pressedKeys = new HashSet<String>();

	private FroggerUI froggerUI;

	private int level = 0;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Set-up UI
		froggerUI = new FroggerUI(canvas);
		froggerUI.initStats();
		froggerUI.create();
		froggerUI.placeCanvas(root);

		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(new KeyPressHandler());
		canvas.setOnKeyReleased(new KeyReleasedHandler());
		
		spawn(gc);

		new AnimationTimer() {

			@Override
			public void handle(long now) {

				/*
				 * froggerUI.updateUI(frog, car);
				 * 
				 * if(car.didCollideWithLeftWall(canvas)) { car.setX(CANVAS_WIDTH); }
				 * 
				 * if(car2.didCollideWithRightWall(canvas)) { car2.setX(0 - car2.getWidth()); }
				 * car.moveLeft(); car2.moveRight();
				 * 
				 * car.update(ELAPSED_TIME_SPEED);
				 * 
				 * frog.update(ELAPSED_TIME_SPEED);
				 * 
				 * car2.update(ELAPSED_TIME_SPEED);
				 * 
				 * gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				 * 
				 * frog.render(gc);
				 * 
				 * car.render(gc);
				 * 
				 * car2.render(gc);
				 */
				
				frog.update(ELAPSED_TIME_SPEED);
				
				for(int i = 0; i < cars.size(); i++) {
					Car car = cars.get(i);
					
					if(car.didCollideWithLeftWall(canvas)) {
						car.setX(CANVAS_WIDTH);
					} else if(car.didCollideWithRightWall(canvas)) {
						car.setX(0 - car.getWidth());
					}
					
					car.update(ELAPSED_TIME_SPEED);
				}
				
				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				
				frog.render(gc);
				
				for(int i = 0; i < cars.size(); i++) {
					cars.get(i).render(gc);
				}
			}

		}.start();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Spawns all necessary sprites.
	 * @param gc The GraphicsContext to draw with.
	 */
	private void spawn(GraphicsContext gc) {
		// Spawning frog
		frog = new Frog(FROG_FILE_NAME, FROG_SPAWN_X, FROG_SPAWN_Y, 0, 0, gc);
		
		double ySpawnInc = 0;
		
		Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			double ySpawn = genRandomInRange(0, CANVAS_HEIGHT - FROG_SPAWN_Y - CAR_DIM_HEIGHT);
			Car car = (rand.nextBoolean() ? spawnLeftCar(gc, ySpawn + ySpawnInc) : spawnRightCar(gc, ySpawn + ySpawnInc));
			cars.add(car);
			ySpawnInc += CAR_DIM_HEIGHT;
		}
	}

	/**
	 * Spawns a left car.
	 * 
	 * @param gc
	 *            The GraphicsContext to draw with.
	 */
	private Car spawnLeftCar(GraphicsContext gc, double ySpawn) {
		Car car = new Car(CAR_LEFT_FILE_NAME, CANVAS_WIDTH - (2 *  CAR_DIM_HEIGHT), ySpawn, 0, 0, gc, CAR_LEFT_NAME);
		car.moveLeft();
		return car;
	}

	/**
	 * Spawns a right car.
	 * 
	 * @param gc
	 *            The GraphicsContext to draw with.
	 */
	private Car spawnRightCar(GraphicsContext gc, double ySpawn) {
		Car car = new Car(CAR_RIGHT_FILE_NAME, 0, ySpawn, 0, 0, gc, CAR_RIGHT_NAME);
		car.moveRight();
		return car;
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
		Random random = new Random();
		double randomDouble = random.nextDouble();
		double result = min + (randomDouble * (max - min));
		return result;
	}
	
	/**
	 * This class handles the frog's movement from the keyboard. The user cannot
	 * move diagonally, and can only move once a key is released.
	 * 
	 * @author Phillip
	 *
	 */
	private class KeyPressHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			switch (event.getCode()) {
			case W:
				if (!pressedKeys.contains(event.getCode().toString()) && pressedKeys.size() == 0) {
					frog.moveUp();
					pressedKeys.add(event.getCode().toString());
				}
				break;
			case A:
				if (!pressedKeys.contains(event.getCode().toString()) && pressedKeys.size() == 0) {
					frog.moveLeft();
					pressedKeys.add(event.getCode().toString());
				}
				break;
			case S:
				if (!pressedKeys.contains(event.getCode().toString()) && pressedKeys.size() == 0) {
					frog.moveDown();
					pressedKeys.add(event.getCode().toString());
				}
				break;
			case D:
				if (!pressedKeys.contains(event.getCode().toString()) && pressedKeys.size() == 0) {
					frog.moveRight();
					pressedKeys.add(event.getCode().toString());
				}
				break;
			}
		}

	}

	/**
	 * This class handles what happens when the user stops pressing a button.
	 * Through using a set, it disables user from being able to hold a movement key
	 * and move the frog indefinitely.
	 * 
	 * @author Phillip
	 *
	 */
	private class KeyReleasedHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			pressedKeys.remove(event.getCode().toString());
		}

	}

}
