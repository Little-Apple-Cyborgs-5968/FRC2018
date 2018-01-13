package org.usfirst.frc.team5968.interfaces;

public interface IHumanInterface {
	/*
	 * Called by teleopPeriodic to check for changes in joystick values
	 */
	public void checkJoystickValues();
	
	/*
	 * Checks if joysticks are moved past threshold, calls driving method in Drive
	 */
	public void teleopDrive();
	
	/*
	 * Checks if grabber button is pressed, calls toggle method in Grabber
	 */
	public void toggleGrabber();
	
	/*
	 * Checks if any scissor lift mode is selected, calls a method in ScissorLift
	 */
	public void scissorLiftMode();
}
