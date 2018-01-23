package org.usfirst.frc.team5968.robot;

public interface IDashboard {
	/*
	 * Uses NetworkTables to get the selected autonomous mode (in the case
	 * that both the switch and scale are on the same side as the robot
	 */
	public AutoMode getFallbackAutoMode();
}
