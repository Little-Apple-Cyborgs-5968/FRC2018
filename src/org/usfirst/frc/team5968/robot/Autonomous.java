package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Autonomous implements IAutonomous{
	 /*
     * Should be called in a fast loop (~20ms) while disabled to get data about 
     * switch/scale sides. Returns a string of length 3 detailing
     * the alliance sides, starting from the closest (own) switch.
     * Ex. "LRL" "RRL" "RRR"
     */
	private String pollGameData() {
		String gameData;
		boolean status = DriverStation.getInstance().isDisabled();
		while(status==true) {			//poll for data while disabled
			gameData = DriverStation.getInstance().getGameSpecificMessage();
		}
		gameData = DriverStation.getInstance().getGameSpecificMessage();  //possibly unnecessary: poll for data immediately once enabled
		return gameData;
	}

	/*
     * Decides auto mode based on FMS game data, and makes the auto
     * mode decision based on the flowchart above.
     */
	private AutoMode autoModeControl(StartingPoint startingPoint) {
		String gameData = pollGameData();
		char robotPosition = 'N';
		switch(startingPoint) {
		case RIGHT:
			robotPosition = 'R';
			break;
		case LEFT:
			robotPosition = 'L';
			break;
		case CENTER:
			robotPosition = 'C';
			break;
		default:
			break;
		}
		
		if (robotPosition==gameData.charAt(0)) {
			if (robotPosition==gameData.charAt(1)) {
				return AutoMode.BOTH;
			} else {
				return AutoMode.SWITCH;
			}
		} else if (robotPosition==gameData.charAt(1)) {
			return AutoMode.SCALE;
		} else {
			return AutoMode.LINE;
		}
	}
	
	 /*
     * Chooses the auto mode to execute.
     * Goes into specific mode class.
     * Sets states.
     */
	public void doAuto(StartingPoint startingPoint) {
		AutoMode automode = autoModeControl(startingPoint);
		switch(automode) {
		case SWITCH:
			doSwitch md = new doSwitch(startingPoint, DriverStation.getInstance().getAlliance());
			//need to do stuff here REGARDING SETTING STATES
			break;
		case SCALE:
			doScale md = new doScale(startingPoint, DriverStation.getInstance().getAlliance());
			break;
		case BOTH:
			AutoMode a2 = chooseModeforBoth();
			if (a2==SWITCH) {
				doSwitch md = new doSwitch(startingPoint, DriverStation.getInstance().getAlliance());
			} else if (a2==SCALE) {
				doScale md = new doScale(startingPoint, DriverStation.getInstance().getAlliance());
			} else {
				doBaseline md = new doBaseline(startingPoint, DriverStation.getInstance().getAlliance());
			}
			break;
		default:
			doBaseline md = new doBaseline(startingPoint, DriverStation.getInstance().getAlliance());
		}
	}
}
