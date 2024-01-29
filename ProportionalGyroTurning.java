package de.tuhh.diss.lab4;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class ProportionalGyroTurning implements Turner {

	public EV3LargeRegulatedMotor rightMotor;
	public EV3LargeRegulatedMotor leftMotor;
	public EV3GyroSensor gyroSens;
	public SampleProvider angle;
	private int sampleSize;
	private float[] sample;	
	private int degSign;
	private int degPerSec;
	private Constants Constants = new Constants(); // defining constants in another class in order to add more constant definitions in the future. These constants would be more accessible for the other projects.
	private double turnConstant = Constants.turnConstant();

	public void initializePorts (EV3LargeRegulatedMotor rightMotor, EV3LargeRegulatedMotor leftMotor, EV3GyroSensor gyroSens) {
		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;
		this.gyroSens = gyroSens;
		angle = gyroSens.getAngleMode();
		sampleSize = angle.sampleSize();
		sample = new float[sampleSize];	

	}
	
	public void setSpeed (int degreesPerSecond) {
		double dPS = (double)degreesPerSecond;
		int rotSpeed = (int)Math.abs(Math.round( (dPS/360) * turnConstant ));
		degSign = Integer.signum(degreesPerSecond); // for keeping the sign of the degreesPerSecond int value, this sign is used in order to rotate the motors CW if the speed input is negative
		degPerSec = rotSpeed; // used for controlling the speed with proportional controller
		setControlledSpeed(rotSpeed);
		
	}

	private void setControlledSpeed (int degreesPerSecond) {
		leftMotor.setSpeed(Math.abs(degreesPerSecond));
		rightMotor.setSpeed(Math.abs(degreesPerSecond));
		
	}

	public void turn (int degrees) {
		
		while (true) {
			rotateMotors(degrees);
			if (speedControl(degrees) == 0) { // implements speed control and stops the motors when speed is 0
				gyroSens.reset(); // reset the angle for future turn commands
				return;
			}
		}

	}

	private void rotateMotors(int degrees) {
		if (degrees*degSign > 0) { // turn the robot CCW 
			rightMotor.backward();
			leftMotor.forward();
		}
		else { // turn the robot CW
			rightMotor.forward();
			leftMotor.backward();
		}
	}

	private int speedControl(int degrees) {
		double kP = 10; // proportional control constant (set to 10 for real life calibration)
		angle.fetchSample(sample, 0);
		double delta = Math.abs(degrees) - Math.abs(sample[0]);
		int speed = (int)Math.round(delta * kP)*degSign;

		if (Math.abs(speed) > Math.abs(degPerSec)) { // limit the maximum wheel speed 
			speed = degPerSec;
		}

		setControlledSpeed(speed);		
		return speed;
	}

}