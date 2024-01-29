package de.tuhh.diss.lab5;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import de.tuhh.diss.lab4.ProportionalGyroTurning;
import lejos.hardware.Sound;

public class MazeSolver {




	EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	EV3GyroSensor gyroSens = new EV3GyroSensor(SensorPort.S3);
	EV3ColorSensor colSens = new EV3ColorSensor(SensorPort.S1);
	SensorMode colorId = colSens.getColorIDMode();
	EV3UltrasonicSensor distSens = new EV3UltrasonicSensor(SensorPort.S4);
	SampleProvider dist = distSens.getDistanceMode();

	ProportionalGyroTurning turn = new ProportionalGyroTurning();
	Map Map = new Map();
	MazeMenu MazeMenu = new MazeMenu();
	private static final int TURN_SPEED = 360;
	private static final double CRITICAL_DISTANCE = 0.35;
	private static final double WALL_CRITICAL_DISTANCE = 0.04;
	private static final int FORWARD_SPEED=960;
	private static final int STEP_SIZE=37;
	private static final int MOVING_DISTANCE = -8;
	private static int selectedColor;
	private static int stepTurn;


	private void setSpeed(int degreesPerSecond) {
		rightMotor.setSpeed(degreesPerSecond);
		leftMotor.setSpeed(degreesPerSecond);
	}

	private boolean wallCheck() {
		// wallCheck method returns false if there exist a wall in front of the robot and otherwise it returns true.

		int distSampleSize = dist.sampleSize();
		float[] distance = new float[distSampleSize];
		int distOffset = distSampleSize -1;
		dist.fetchSample(distance, distOffset);

		if (distance[distOffset] < CRITICAL_DISTANCE) {

			while (distance[distOffset] > WALL_CRITICAL_DISTANCE ) {   // This while statement gets robot closer to the wall so that it can read the wall color. 
				dist.fetchSample(distance, distOffset);
				setSpeed(FORWARD_SPEED);
				rightMotor.backward();
				leftMotor.backward();

			}
			colorCheck();
			rightMotor.stop(true);
			leftMotor.stop();
			moveForward(MOVING_DISTANCE); // MOVING_DISTANCE is an empirical number for the robot to turn safely after approaching the wall.

			return false;
		}

		return true;
	}

	private void moveForward(int movingDistance) {
		// moveForward method contains a calculation that converts the cm input to rotate degrees. So that, the step size can be arranged.
		setSpeed(FORWARD_SPEED);
		int convertedDistance=(int)(Math.round((-1080*movingDistance)/(5.4*Math.PI)));

		rightMotor.rotate(convertedDistance,true);
		leftMotor.rotate(convertedDistance);

	}

	private void colorCheck() {
		// colorCheck method is checking the wall color and exists the program with a sound if desired color found.
		int colSampleSize = colorId.sampleSize();
		float[] color = new float[colSampleSize];
		int colOffset = colSampleSize -1;
		colorId.fetchSample(color, colOffset);

		if ((int)color[colOffset] == selectedColor) {

			setSpeed(0);
			Sound.setVolume(500);
			Sound.playTone(500, 500);
			LCD.clear();
			LCD.drawString("MAZE SOLVED",0,2);
			Delay.msDelay(3000);
			System.exit(0);
		}


	}

	private void directionDecider() {
		// directionDecider method is the main method that decides the behavior of the robot. It checks the forward, right and left walls.
		// Depending on the these three booleans and the previous mapped data from the Map Class, this method decides the next move in order to solve the maze.


		turn.setSpeed(TURN_SPEED);
		boolean f= true, r=true, l=true;
		stepTurn = 0;

		f = wallCheck();
		Delay.msDelay(100);

		turn.turn(-90);
		Delay.msDelay(100);

		r = wallCheck();

		turn.turn(180);
		Delay.msDelay(100);

		l = wallCheck();


		//		All possible situations for the maze walls (total number of possible situations for 3 different true-false options, 2^3=8) are added. In 2 of these cases, the robot doesn't need to take an extra action and can directly move forward. So for the simplicity of the code, these 2 cases are removed and only the remaining 6 cases are programmed.

		if (r == false && l== false && f == false ) {			
			stepTurn = 180;			
		}

		else if (r == true && l == true && f == false) { 
			if (Map.orientation == 0) {
				if (Map.mapCheck(Map.x, Map.y+1) < Map.mapCheck(Map.x, Map.y-1) ) {
					stepTurn = -90;
				}
				else {
					stepTurn = 90;
				}
			}
			else if (Map.orientation == -90) {
				if (Map.mapCheck(Map.x+1, Map.y) < Map.mapCheck(Map.x-1, Map.y) ) {
					stepTurn = -90;
				}
				else {
					stepTurn = 90;
				}
			}
			else if (Map.orientation == 180) {
				if (Map.mapCheck(Map.x, Map.y-1) < Map.mapCheck(Map.x, Map.y+1) ) {
					stepTurn = -90;
				}
				else {
					stepTurn = 90;
				}
			}
			else if (Map.orientation == 90) {

				if (Map.mapCheck(Map.x-1, Map.y) < Map.mapCheck(Map.x+1, Map.y) ) {
					stepTurn = -90;
				}
				else {
					stepTurn = 90;
				}
			}
		}

		else if (r == true && l == false && f == true) { 

			if (Map.orientation == 0) {
				if (Map.mapCheck(Map.x, Map.y+1) < Map.mapCheck(Map.x-1, Map.y) ) {
					stepTurn = -90;
				}
			}
			else if (Map.orientation == -90) {
				if (Map.mapCheck(Map.x+1, Map.y) < Map.mapCheck(Map.x, Map.y+1) ) {
					stepTurn = -90;
				}
			}
			else if (Map.orientation == 180) {
				if (Map.mapCheck(Map.x, Map.y-1) < Map.mapCheck(Map.x+1, Map.y) ) {
					stepTurn = -90;
				}
			}
			else if (Map.orientation == 90) {
				if (Map.mapCheck(Map.x-1, Map.y) < Map.mapCheck(Map.x, Map.y-1) ) {
					stepTurn = -90;
				}
			}
		}			

		else if (r == false && l == true && f == true) {  

			if (Map.orientation == 0) {
				if (Map.mapCheck(Map.x, Map.y-1) < Map.mapCheck(Map.x-1, Map.y) ) {
					stepTurn = 90;
				}
			}
			else if (Map.orientation == -90) {
				if (Map.mapCheck(Map.x-1, Map.y) < Map.mapCheck(Map.x, Map.y+1) ) {
					stepTurn = 90;
				}
			}
			else if (Map.orientation == 180) {
				if (Map.mapCheck(Map.x-1, Map.y) < Map.mapCheck(Map.x, Map.y-1) ) {
					stepTurn = 90;
				}
			}
			else if (Map.orientation == 90) {
				if (Map.mapCheck(Map.x+1, Map.y) < Map.mapCheck(Map.x, Map.y-1) ) {
					stepTurn = 90;
				}
			}
		}

		else if (r == false && l == true && f == false) {			
			stepTurn = 90;
		}
		else if (r == true && l == false && f == false) {			
			stepTurn = -90;
		}

		Delay.msDelay(300);
		turn.setSpeed(TURN_SPEED);

		if (stepTurn == 90) {
			stepTurn = 0;
			Map.mapGyro(90);
		}
		else if (stepTurn == -90)  {
			stepTurn = -180;
			Map.mapGyro(90);	
		}
		else  {
			turn.turn(-90);			
			Delay.msDelay(100);
		}
		turn.turn(stepTurn);
		Map.mapGyro(stepTurn);
		rightMotor.stop(true);
		leftMotor.stop();
	}

	private void mazeNavigator() {
		// mazeNavigator records the moves of the robot to the map.

		Map.map[3][3] = 1;

		while (true) {

			setSpeed(FORWARD_SPEED);
			directionDecider();
			Delay.msDelay(100);

			moveForward(STEP_SIZE);

			if (Map.orientation == 0 ) {
				Map.mapWalker("UP");

			}
			else if (Map.orientation == 90 ) {
				Map.mapWalker("LEFT");

			}
			else if (Map.orientation == 180 ) {
				Map.mapWalker("DOWN");

			}
			else if (Map.orientation == -90 ) {
				Map.mapWalker("RIGHT");

			}

			mapDrawer();

		}
	}

	private void mapDrawer() {
		// mapDrawer method shows the current map of the maze also the location and orientation of the robot.
		LCD.clear();
		LCD.drawString("Orientation: " + Map.orientation + "  ", 0 , 0 );
		LCD.drawString("Map:", 0 , 1 );
		for(int i = 0; i<7; i++)
		{
			for(int j = 0; j<7; j++)
			{
				LCD.drawInt(Map.map[j][i] , i+7, j+1);
			}
		}
	}

	public void initiator() {
		// initiator method starts the MENU and saves the selected color.
		turn.initializePorts (rightMotor, leftMotor, gyroSens);
		selectedColor = MazeMenu.navigatorLCD();
		mazeNavigator();
	}

}


