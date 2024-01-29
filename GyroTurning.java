package de.tuhh.diss.lab4;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

class GyroTurning implements Turner {

	EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	EV3GyroSensor gyroSens = new EV3GyroSensor(SensorPort.S3);
	SampleProvider angle = gyroSens.getAngleMode();
	private int sampleSize = angle.sampleSize();
	private float[] sample = new float[sampleSize];	
	private int degSign;
	private Constants Constants = new Constants(); // defining constants in another class in order to add more constant definitions in the future. These constants would be more accessible for the other projects.
	private double turnConstant = Constants.turnConstant();

	public void setSpeed (int degreesPerSecond) {
		double dPS = (double)degreesPerSecond;
		double rotSpeed = (dPS/360) * turnConstant;
		leftMotor.setSpeed((int)Math.round(rotSpeed));
		rightMotor.setSpeed((int)Math.round(rotSpeed));
		degSign = Integer.signum(degreesPerSecond); // for keeping the sign of the degreesPerSecond int value
	}

	public void turn (int degrees) {

		while (true) {
			rotateMotors(degrees);
			if (sampledAngle() > Math.abs(degrees)) {
				return;
			}
		}

	}

	private void rotateMotors(int degrees) {
		if (degrees*degSign > 0) {
			rightMotor.backward();
			leftMotor.forward();
		}
		else {
			rightMotor.forward();
			leftMotor.backward();
		}
	}

	private int sampledAngle() {
		angle.fetchSample(sample, 0);
		int sampledAngle = (int) Math.abs(sample[0]);
		return sampledAngle;
	}

}