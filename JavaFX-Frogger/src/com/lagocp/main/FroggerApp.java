package com.lagocp.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.lagocp.sprites.Car;
import com.lagocp.sprites.Frog;

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
 * NOTE:
 * Everything moves in units of 3, and all scaling should be done to factors of 3.
 * Don't make collisions <= or >=; instead, make collisions strictly < or >.
 * 
 * @author Phillip
 *
 */
public class FroggerApp extends Application {
	private static final double CANVAS_WIDTH = 540;
	private static final double CANVAS_HEIGHT = 720;
	
	private Frog frog;
	private static final double FROG_DIM = Frog.RENDER_DIM;
	private static final double FROG_SPAWN_X = CANVAS_WIDTH / 2 - (FROG_DIM / 2);
	private static final double FROG_SPAWN_Y = CANVAS_HEIGHT - FROG_DIM;
	
	// Will help in determining which car collided with the frog
	private ArrayList<Car> cars = new ArrayList<Car>();
	private static final double CAR_DIM_HEIGHT = Car.RENDER_DIM_HEIGHT;
	private static final double ELAPSED_TIME_SPEED = 1;
	
	private Set<String> pressedKeys = new HashSet<String>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);

		// Testing drawing sprites
		frog = new Frog("/com/lagocp/assets/frog.png", FROG_SPAWN_X, FROG_SPAWN_Y, 0, 0, gc);
		Car car = new Car("/com/lagocp/assets/car-facing-left.png", CANVAS_WIDTH / 2 - (CAR_DIM_HEIGHT / 2), CANVAS_HEIGHT - CAR_DIM_HEIGHT, 0, 0, gc);
		Car car2 = new Car("/com/lagocp/assets/car-facing-right.png", CANVAS_WIDTH / 2 - (CAR_DIM_HEIGHT / 2), CANVAS_HEIGHT - (2 * CAR_DIM_HEIGHT) - FROG_DIM, 0, 0, gc);
		
		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(new KeyPressHandler());
		canvas.setOnKeyReleased(new KeyReleasedHandler());

		new AnimationTimer() {

			@Override
			public void handle(long now) {
				
				if(car.didCollideWithLeftWall(canvas)) {
					car.setX(CANVAS_WIDTH);
				}
				car.moveLeft();
				
				car.update(ELAPSED_TIME_SPEED);

				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

				frog.render(gc);
				
				car.render(gc);
				
				car2.render(gc);
			}

		}.start();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This class handles the frog's movement from the keyboard.
	 * The user cannot move diagonally, and can only move once
	 * a key is released.
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
