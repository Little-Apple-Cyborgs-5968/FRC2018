package org.usfirst.frc.team5968.interfaces;

public interface IDrive {
	/*
	 * Initializes motors and encoders
	 */
	public void init();
	
	public void driveStraight(double initialSpeed);
	
	public void resetEncoders();
	
	public void getLeftDistance();
	public void getRightDistance();
	public void getLeftSpeed();
	public void getRightSpeed();
	
	/*
	 * Drive straight forward a specified distance
	 */
	public void driveDistance(double speed, double distance);
	
	/*
	 * Turn in place a specified angle
	 */
	public void rotateDegrees(double angle);
	
}
