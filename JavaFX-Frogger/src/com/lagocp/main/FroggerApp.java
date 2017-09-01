package com.lagocp.main;

import java.util.HashSet;
import java.util.Set;

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
 * @author Phillip
 *
 */
public class FroggerApp extends Application {
	private static final double CANVAS_WIDTH = 800;
	private static final double CANVAS_HEIGHT = 800;
	
	private Frog frog;
	private static final double FROG_DIM = Frog.RENDER_DIM;
	private static final double FROG_SPAWN_X = CANVAS_WIDTH / 2 - (FROG_DIM / 2);
	private static final double FROG_SPAWN_Y = CANVAS_HEIGHT - FROG_DIM;
	
	private Set<String> pressedKeys = new HashSet<String>();
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);

		frog = new Frog("/com/lagocp/assets/frog.png", FROG_SPAWN_X, FROG_SPAWN_Y, 0, 0, gc);

		canvas.setFocusTraversable(true);
		canvas.setOnKeyPressed(new KeyPressHandler());
		canvas.setOnKeyReleased(new KeyReleasedHandler());

		new AnimationTimer() {

			@Override
			public void handle(long now) {

				gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

				frog.render(gc);
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
