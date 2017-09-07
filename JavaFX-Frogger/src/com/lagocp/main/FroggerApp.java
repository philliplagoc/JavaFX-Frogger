package com.lagocp.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.lagocp.sprites.Car;
import com.lagocp.sprites.Frog;
import com.lagocp.sprites.Track;
import com.lagocp.ui.FroggerUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the main class where the application will run from.
 * 
 * @author Phillip
 *
 */
public class FroggerApp extends Application {
	public static final double CANVAS_WIDTH = 600;
	public static final double CANVAS_HEIGHT = 720;
	private Canvas canvas;
	private GraphicsContext gc;

	private Frog frog;
	private static final String FROG_FILE_NAME = "/com/lagocp/assets/frog.png";
	private static final double FROG_DIM_WIDTH = Frog.DIM_WIDTH;
	private static final double FROG_DIM_HEIGHT = Frog.DIM_HEIGHT;
	private static final double FROG_SPAWN_X = CANVAS_WIDTH / 2 - (FROG_DIM_WIDTH / 2);
	private static final double FROG_SPAWN_Y = CANVAS_HEIGHT - FROG_DIM_WIDTH;

	private ArrayList<Car> cars = new ArrayList<Car>();
	private static final String CAR_LEFT_NAME = "LeftFacing";
	private static final String CAR_RIGHT_NAME = "RightFacing";
	private static final String CAR_LEFT_FILE_NAME = "/com/lagocp/assets/car-facing-left.png";
	private static final String CAR_RIGHT_FILE_NAME = "/com/lagocp/assets/car-facing-right.png";
	private static final double CAR_DIM_HEIGHT = Car.DIM_HEIGHT;
	private static final double CAR_DIM_WIDTH = Car.DIM_WIDTH;

	private static final double ELAPSED_TIME_SPEED = 1;

	private Set<String> pressedKeys = new HashSet<String>();

	private FroggerUI froggerUI;

	private int level = 0;
	private static final double LEVEL_EDGE = 60; // Min y coordinate where cars cannot spawn
	private static final double SAFEZONE = 630; // Max y coordinate where cars cannot spawn

	// private ArrayList<Track> tracks = new ArrayList<Track>();
	private Track[] tracks;
	private int limit = 4;

	private boolean isGameOver = false;

	private KeyPressHandler keyPressHandler = new KeyPressHandler();
	private KeyReleasedHandler keyReleasedHandler = new KeyReleasedHandler();

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		// Set-up UI
		froggerUI = new FroggerUI(canvas);
		// froggerUI.initStats();
		// froggerUI.create();
		froggerUI.placeCanvas(root);

		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(keyPressHandler);
		canvas.setOnKeyReleased(keyReleasedHandler);

		spawn(gc);

		new AnimationTimer() {

			@Override
			public void handle(long now) {
				// froggerUI.updateUI(frog, cars.get(0));
				// while (!isGameOver) {
				if (frog.didCollideWithTopWall(canvas)) {
					// System.out.println("Hit top!");
					froggerUI.increaseLevel();
				}

				frog.update(ELAPSED_TIME_SPEED);

				for (int i = 0; i < cars.size(); i++) {
					Car car = cars.get(i);

					if (car.didCollideWithLeftWall(canvas)) {
						car.setX(CANVAS_WIDTH);
					} else if (car.didCollideWithRightWall(canvas)) {
						car.setX(0 - car.getWidth());
					}

					if ((car.didCollideWith(frog) || frog.didCollideWith(car)) && !isGameOver) {
						froggerUI.createGameOver();
						isGameOver = true;
					}

					car.update(ELAPSED_TIME_SPEED);
				}

				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

				for (int i = 0; i < tracks.length; i++) {
					Track track = tracks[i];
					if (track != null)
						track.render(gc);
				}

				frog.render(gc);

				for (int i = 0; i < cars.size(); i++) {
					cars.get(i).render(gc);
				}
				// }

			}

		}.start();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Spawns all necessary sprites.
	 * 
	 * @param gc
	 *            The GraphicsContext to draw with.
	 */
	private void spawn(GraphicsContext gc) {
		// Spawning frog
		frog = new Frog(FROG_FILE_NAME, FROG_SPAWN_X, FROG_SPAWN_Y, 0, 0, gc);

		// Get possible spawns for a car
		double[] xSpawns = getSpawns(CAR_DIM_WIDTH, 0, CANVAS_WIDTH - CAR_DIM_WIDTH);
		double[] ySpawns = getSpawns(CAR_DIM_HEIGHT, LEVEL_EDGE, SAFEZONE);

		// Array is for determining where tracks get spawned
		boolean[] trackSpawnedHere = new boolean[ySpawns.length];
		tracks = new Track[ySpawns.length];

		// Start spawning cars
		for (int i = 0; i < 10; i++) {
			Random r = new Random();

			// Get a random xSpawn and ySpawn
			int xSpawnInd = getRandomIndex(xSpawns);
			double xSpawn = xSpawns[xSpawnInd];
			int ySpawnInd = getRandomIndex(ySpawns);
			double ySpawn = ySpawns[ySpawnInd];

			Car car = (r.nextBoolean() ? spawnLeftCar(gc, xSpawn, ySpawn) : spawnRightCar(gc, xSpawn, ySpawn));

			// Spawning tracks
			if (!trackSpawnedHere[ySpawnInd]) {
				Track track = new Track(0, ySpawn + 7.5, Track.WIDTH, Track.HEIGHT, gc, limit);
				track.addCar(car);

				tracks[ySpawnInd] = track;
				trackSpawnedHere[ySpawnInd] = true;
			} else {
				Track track = tracks[ySpawnInd];
				Car trackCar = track.getCars().get(0);

				// Make cars go in same direction
				if (!(car.getName().equals(trackCar.getName()))) {
					car = (trackCar.getName().equals(CAR_LEFT_NAME) ? spawnLeftCar(gc, xSpawn, ySpawn)
							: spawnRightCar(gc, xSpawn, ySpawn));
				}

				// Reposition car if in same spawn as trackCar
				if (car.didCollideWith(trackCar)) {
					car.setX(trackCar.getX() + CAR_DIM_WIDTH);
				}

				car.setvX(trackCar.getvX());
			}

			cars.add(car);
		}
	}

	/**
	 * Gets a random index from the inputted array.
	 * 
	 * @param array
	 *            The array to get a random index from.
	 * @return A random index from the array.
	 */
	private int getRandomIndex(double[] array) {
		Random random = new Random();
		int ind = random.nextInt(array.length);
		return ind;
	}

	/**
	 * Returns an array that will hold the potential spawns of a Car.
	 * 
	 * @param increase
	 *            The value by which to increase each spawn
	 * @param min
	 *            The minimum value for which all spawn points will be greater
	 * @param max
	 *            The maximum value for which all spawn points will be less than
	 * @return An array containing all possible spawn points.
	 */
	private double[] getSpawns(double increase, double min, double max) {
		double[] spawns = new double[(int) ((max - min) / increase)];
		double ySpawn = min;

		for (int i = 0; i < spawns.length; i++) {
			spawns[i] = ySpawn;
			ySpawn += increase;
		}

		return spawns;
	}

	/**
	 * Spawns a left car.
	 * 
	 * @param gc
	 *            The GraphicsContext to draw with.
	 */
	private Car spawnLeftCar(GraphicsContext gc, double xSpawn, double ySpawn) {
		Car car = new Car(CAR_LEFT_FILE_NAME, xSpawn, ySpawn, 0, 0, gc, CAR_LEFT_NAME);
		car.moveLeft();
		return car;
	}

	/**
	 * Spawns a right car.
	 * 
	 * @param gc
	 *            The GraphicsContext to draw with.
	 */
	private Car spawnRightCar(GraphicsContext gc, double xSpawn, double ySpawn) {
		Car car = new Car(CAR_RIGHT_FILE_NAME, xSpawn, ySpawn, 0, 0, gc, CAR_RIGHT_NAME);
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
			if (!isGameOver) {
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
			} else { // Restarting the level
				switch(event.getCode()) {
				case SPACE:
					gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
					
					tracks = null;
					cars = new ArrayList<Car>();
					frog = null;
					
					froggerUI.removeGameOver();
					
					spawn(gc);

					isGameOver = false;
					break;
				}
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
