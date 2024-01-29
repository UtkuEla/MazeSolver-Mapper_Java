package de.tuhh.diss.lab5;


import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.Color;
import lejos.hardware.lcd.LCD;

public class MazeMenu {

	private final int COLUMN = 2; // offset from x axis for the text to be displayed on the LCD screen
	private final int ROW = 1;
	private void startScreen() {
		LCD.clear();
		
		LCD.drawString(" MENU",COLUMN,ROW);
		LCD.drawString(" Select Color " ,COLUMN,ROW+1);
		LCD.drawString(" RED " ,COLUMN,ROW+2);
		LCD.drawString(" GREEN " ,COLUMN,ROW+3);
		LCD.drawString(" BLUE",COLUMN,ROW+4);
	}

	public int navigatorLCD() {
		int y = 3; // starting position of > indicator
		int upperLimit = y + 2; // upper limit for displaying the > indicator
		int lowerLimit = y; // upper limit for displaying the > indicator

		startScreen();
		LCD.drawString(">",COLUMN,y);

		while (true) {

			Button.waitForAnyPress();

			if (Button.UP.isDown()) {
				y--;				
			} 
			else if (Button.DOWN.isDown()) {
				y++;
			}
			if (y < lowerLimit) {
				y = upperLimit;
			}
			if (y > upperLimit) {
				y = lowerLimit;
			}
			if (Button.ENTER.isDown()) {
				LCD.clear();
				y = colorConverter(y);
				Sound.setVolume(500);
				Sound.playTone(500, 500);

				return y;
			} 

			startScreen();
			LCD.drawString(">" ,COLUMN , y);

		}

	}

	private int colorConverter (int y) {
		if (y == 3) {
			y = Color.RED;
			LCD.drawString("RED Selected" ,1,0);
		}
		else if (y == 4) {
			y = Color.GREEN;
			LCD.drawString("GREEN Selected" ,1,0);
		}
		else if (y == 5) {
			y = Color.BLUE;
			LCD.drawString("BLUE Selected" ,1,0);
		}
		return y;
	}
}





