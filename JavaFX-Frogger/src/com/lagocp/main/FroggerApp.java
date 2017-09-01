package com.lagocp.main;

import com.lagocp.sprites.Frog;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 * This is the main class where the application will run from.
 * @author Phillip
 *
 */
public class FroggerApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		Canvas canvas = new Canvas(800.0, 800.0);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		Frog frog = new Frog("/com/lagocp/assets/frog.png", 200, 200, 0, 0, gc);
		
		root.getChildren().add(canvas);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
