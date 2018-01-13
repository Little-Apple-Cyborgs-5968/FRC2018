package org.usfirst.frc.team5968.interfaces;

public interface IScissorLift {
	
	/*
	 * The following three methods call liftDistance to raise/lower to the correct value
	 */
	public void groundMode();
	public void switchMode();
	public void scaleMode();
	
	public void resetEncoders();
	
	/*
	 * Uses PID control to move the scissor lift to the correct height
	 */
	public void liftDistance(double height);
}
