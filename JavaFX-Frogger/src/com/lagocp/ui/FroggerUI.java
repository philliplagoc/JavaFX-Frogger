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

	private HBox levelPane;

	private static final double HBOX_SPACING = 10;

	public FroggerUI(Canvas canvas) {
		super(canvas);
		base = new StackPane();

		levelPane = new HBox(HBOX_SPACING);
	}

	@Override
	public void create() {
		// Set alignment
		StackPane.setAlignment(stats, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(levelPane, Pos.TOP_CENTER);
	}

	@Override
	public void placeCanvas(Group root) {
		StackPane.setAlignment(getCanvas(), Pos.CENTER);

		base.getChildren().addAll(stats, levelPane, getCanvas());

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

}
