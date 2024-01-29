package de.tuhh.diss.lab4;

public class Constants {

	public double turnConstant() {
		/*
		 * rotate(turnConstant) makes the robot turn 1 full round also
		 * setSpeed(turnConstant) makes the robot make a full rotation along its origin
		 * in 1 sec
		 */

		double pi = 3.1416;
		double outerAxleLength = 151; // [mm]
		double innerAxleLength = 95; // [mm]
		double wheelDiam = 55; // [mm]
		double axleLength = (outerAxleLength - innerAxleLength) / 2 + innerAxleLength; // [mm]
		double avgRotateCircum = pi * axleLength; // Average rotation circumference of the robot [mm]
		double wheelCircum = wheelDiam * pi; // Wheel circumference [mm]
		double oneRev = 360*3; // degree input for one full revolution of the wheel as calculated in Lab 2 (motor/wheel gear ratio = 1/3)
		double turnConstant = (avgRotateCircum / wheelCircum) * oneRev; 
		return turnConstant;

	}

}
