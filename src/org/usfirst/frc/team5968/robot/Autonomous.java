package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation;

public class Autonomous implements IAutonomous {
    
    private Dashboard dashboard;
    
    private IGrabber grabber;
    private ILift lift;
    
    public Autonomous(IGrabber grabber, ILift lift) {
        this.grabber = grabber;
        this.lift = lift;
    }
    
	 /*
     * Returns a string of length 3 detailing the alliance sides, 
     * starting from the closest (own) switch.
     * Ex. "LRL" "RRL" "RRR"
     */
	private String pollGameData() {
		String gameData;
		boolean isRobotDisabled = DriverStation.getInstance().isDisabled();
		while (isRobotDisabled) {
			gameData = DriverStation.getInstance().getGameSpecificMessage();
		}
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		return gameData;
	}

	/*
     * Returns autonomous mode, given polled game data and robot starting point
     */
	private AutoMode autoModeControl(StartingPoint startingPoint) {
		String gameData = pollGameData();
		gameData = "RRR";
		char robotPosition = 'N';
		switch (startingPoint) {
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
		if (robotPosition == gameData.charAt(0)) {
			if (robotPosition == gameData.charAt(1)) {
				return AutoMode.BOTH;
			} else {
				return AutoMode.SWITCH;
			}
		} else if (robotPosition == gameData.charAt(1)) {
			return AutoMode.SCALE;
		} else {
			return AutoMode.LINE;
		}
	}
	
	 /*
     * Creates instance of correct autonomous mode
     */
	public void doAuto(StartingPoint startingPoint) {
	    dashboard = new Dashboard();
		AutoMode automode = autoModeControl(startingPoint);
		switch(automode) {
		case SWITCH:
			new SwitchAuto(startingPoint, grabber);
			break;
		case SCALE:
			new ScaleAuto(startingPoint, grabber, lift);
			break;
		case BOTH:
			AutoMode modeIfBothOnOurSide = dashboard.chooseModeforBoth();
			if (modeIfBothOnOurSide == AutoMode.SWITCH) {
				new SwitchAuto(startingPoint, grabber);
			} else if (modeIfBothOnOurSide == AutoMode.SCALE) {
				new ScaleAuto(startingPoint, grabber, lift);
			} else {
				new BaselineAuto(startingPoint);
			}
			break;
		default:
			new BaselineAuto(startingPoint);
			break;
		}
	}
	
}
