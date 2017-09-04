package com.lagocp.sprites;

import java.util.Random;

import com.lagocp.gameEngine.sprite.Sprite;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Car extends Sprite {
	public static final double DIM_WIDTH = 120;
	public static final double DIM_HEIGHT = 60;
	private double maxCarSpeed = 8.0;
	private double minCarSpeed = 3.7;

	private double carSpeed;
	private Random random;
	
	private static final int TOLERANCE_THRESHOLD = 0xFF;
	
	public Car(String imageFile, double x, double y, double width, double height, GraphicsContext gc) {
		super(imageFile, x, y, width, height, gc);
		random = new Random();
		this.carSpeed = genRandomInRange(minCarSpeed, maxCarSpeed);
		
		Image scaled = this.scaleImage(getImage(), DIM_WIDTH, DIM_HEIGHT, true);
		//setImage(scaled);
		
		setWidth(scaled.getWidth());
		setHalfWidth(getWidth() / 2);
		setHeight(scaled.getHeight());
		setHalfHeight(getHeight() / 2);
		
	}
	
	/**
	 * Makes the desired image background transparent.
	 * @param inputImage
	 * @return
	 */
	 private Image makeTransparent(Image inputImage) {
	        int W = (int) inputImage.getWidth();
	        int H = (int) inputImage.getHeight();
	        WritableImage outputImage = new WritableImage(W, H);
	        PixelReader reader = inputImage.getPixelReader();
	        PixelWriter writer = outputImage.getPixelWriter();
	        for (int y = 0; y < H; y++) {
	            for (int x = 0; x < W; x++) {
	                int argb = reader.getArgb(x, y);

	                int r = (argb >> 16) & 0xFF;
	                int g = (argb >> 8) & 0xFF;
	                int b = argb & 0xFF;

	                if (r >= TOLERANCE_THRESHOLD 
	                        && g >= TOLERANCE_THRESHOLD 
	                        && b >= TOLERANCE_THRESHOLD) {
	                    argb &= 0x00FFFFFF;
	                }

	                writer.setArgb(x, y, argb);
	            }
	        }

	        return outputImage;
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
			double frogX = other.getX();
			double frogY = other.getY();
			double frogHeight = other.getHeight();
			double frogWidth = other.getWidth();

			double x = this.getX();
			double y = this.getY();
			double height = this.getHeight();
			double width = this.getWidth();

			boolean xCond1 = x + width >= frogX;
			boolean xCond2 = x + width <= frogX + frogWidth;
			boolean xCond3 = x >= frogX;
			boolean xCond4 = x <= frogX + frogWidth;
			boolean collidedX = (xCond1 && xCond2) || (xCond3 && xCond4);
//			System.out.println("X: " + collidedX);
			boolean yCond1 = y + height >= frogY;
			boolean yCond2 = y + height <= frogY + frogHeight;
			boolean yCond3 = y >= frogY;
			boolean yCond4 = y <= frogY + frogHeight;
			boolean collidedY = (yCond1 && yCond2) || (yCond3 && yCond4);
//			System.out.println("Y: " + collidedY);
			
//			System.out.println(collidedX && collidedY);
			
			return collidedX && collidedY;
		}
		return false;
	}

	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getX(), getY(), DIM_WIDTH, DIM_HEIGHT);
		// Drawing boundaries
		gc.strokeLine(getX(), getY(), getX() + getWidth(), getY()); // Top
		gc.strokeLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight()); // Bot
		gc.strokeLine(getX(), getY(), getX(), getY() + getHeight()); // Left
		gc.strokeLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight()); // Right
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
