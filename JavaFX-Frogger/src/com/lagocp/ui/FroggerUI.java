package com.lagocp.ui;

import com.lagocp.gameEngine.sprite.Sprite;
import com.lagocp.gameEngine.ui.UI;
import com.lagocp.sprites.Car;
import com.lagocp.sprites.Frog;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class derives from the UI and will provide the basic UI elements for
 * Frogger, including the current level and statistics of each Sprite.
 * 
 * @author Phillip
 *
 */
public class FroggerUI extends UI {
	private StackPane base;

	private BorderPane stats;
	private HBox statsPane;
	private Label froggerStats;
	private Label carStats;

	// private HBox levelPane;

	// private static final double HBOX_SPACING = 10;

	private BorderPane uiPane;

	private int level;
	private Label levelLabel;

	private StackPane gameOverPane;

	public FroggerUI(Canvas canvas) {
		super(canvas);
		base = new StackPane();

		// levelPane = new HBox(HBOX_SPACING);
		uiPane = new BorderPane(canvas);
		level = 1;
		levelLabel = new Label("LEVEL: " + level);

		gameOverPane = new StackPane();
		gameOverPane.setStyle("-fx-background-color: transparent;");
		// gameOverPane.getChildren().add(new Label("Game Over!"));
	}

	@Override
	public void create() {
		// Set alignment
		// StackPane.setAlignment(levelPane, Pos.BOTTOM_CENTER);
	}

	@Override
	public void placeCanvas(Group root) {
		// StackPane.setAlignment(levelPane, Pos.BOTTOM_CENTER);
		// levelPane.getChildren().add(new Label("levelPane"));

		BorderPane.setAlignment(levelLabel, Pos.CENTER);
		uiPane.setTop(levelLabel);

		StackPane.setAlignment(getCanvas(), Pos.CENTER);
		StackPane.setAlignment(gameOverPane, Pos.CENTER);
		base.getChildren().addAll(getCanvas(), uiPane, gameOverPane);

		root.getChildren().add(base);
	}

	@Override
	public void updateUI(Sprite... sprites) {
		for (Sprite sprite : sprites) {
			if (sprite instanceof Frog) {
				froggerStats.setText("Frog: (" + sprite.getX() + ", " + sprite.getY() + ")\n" + "Center: ("
						+ sprite.getCenterX() + ", " + sprite.getCenterY() + ")\n" + "vX: " + sprite.getvX() + " - vY: "
						+ sprite.getvY() + "\nWidth: " + sprite.getWidth() + "\nHeight: " + sprite.getHeight()
						+ "\nHalfWidth: " + sprite.getHalfWidth() + "\nHalfHeight: " + sprite.getHalfHeight());
			} else if (sprite instanceof Car) {
				carStats.setText("Car: (" + sprite.getX() + ", " + sprite.getY() + ")\n" + "Center: ("
						+ sprite.getCenterX() + ", " + sprite.getCenterY() + ")\n" + "vX: " + sprite.getvX() + " - vY: "
						+ sprite.getvY() + "\nWidth: " + sprite.getWidth() + "\nHeight: " + sprite.getHeight()
						+ "\nHalfWidth: " + sprite.getHalfWidth() + "\nHalfHeight: " + sprite.getHalfHeight());
			}
		}
	}

	@Override
	public void initStats() {
		// Initialize UI elements relevant to stats
		stats = new BorderPane();
		statsPane = new HBox();
		froggerStats = new Label();
		carStats = new Label();

		// Set alignment
		froggerStats.setAlignment(Pos.CENTER);
		carStats.setAlignment(Pos.CENTER);

		// Adding children
		statsPane.getChildren().addAll(froggerStats, carStats);
		stats.setTop(statsPane);
	}

	/**
	 * Increases the level in the UI.
	 */
	public void increaseLevel() {
		level++;
		levelLabel.setText("LEVEL: " + level);
	}

	/**
	 * Creates the game over screen.
	 */
	public void createGameOver() {
		Text gameOverText = new Text("GAME OVER!\nPress SPACE to restart...");
		gameOverText.setTextAlignment(TextAlignment.CENTER);
		StackPane.setAlignment(gameOverText, Pos.CENTER);
		gameOverPane.getChildren().add(gameOverText);
		gameOverText.setFont(Font.font("Impact", FontWeight.BOLD, 60));
		// Make gameOverPane and base transparent
		gameOverPane.setStyle("-fx-background-color: rgba(255, 102, 129, 0.73);");
		base.setStyle("-fx-background-color: rgba(255, 102, 129, 0.73);");
	}
	
	/**
	 * Removes the game over screen.
	 */
	public void removeGameOver() {
		gameOverPane.getChildren().remove(0);
		gameOverPane.setStyle("-fx-background-color: transparent;");
		base.setStyle("-fx-background-color: transparent;");
	}
	

}
