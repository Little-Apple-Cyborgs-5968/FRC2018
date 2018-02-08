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
		boolean isRobotDisabled = DriverStation.getInstance().isDisabled();
		while(isRobotDisabled) {			//poll for data while disabled
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
		Alliance alliance = DriverStation.getInstance().getAlliance();
		switch(automode) {
		case SWITCH:
			SwitchAuto switchAutoMode = new SwitchAuto(startingPoint, alliance);
			break;
		case SCALE:
			ScaleAuto scaleAutoMode = new ScaleAuto(startingPoint, alliance);
			break;
		case BOTH:
			AutoMode modeIfBothOnOurSide = chooseModeforBoth();
			if (modeIfBothOnOurSide==SWITCH) {
				SwitchAuto switchAutoMode = new SwitchAuto(startingPoint, alliance);
			} else if (modeIfBothOnOurSide==SCALE) {
				ScaleAuto scaleAutoMode = new ScaleAuto(startingPoint, alliance);
			} else {
				BaselineAuto baselineAutoMode = new BaselineAuto(startingPoint, alliance);
			}
			break;
		default:
			BaselineAuto baselineAutoMode = new BaselineAuto(startingPoint, alliance);
			break;
		}
	}
}
