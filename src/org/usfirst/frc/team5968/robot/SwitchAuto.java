package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class SwitchAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;
	IDrive drive;
	IGrabber grabber;
	ILift lift;

    public SwitchAuto(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
        //put the first step here
        drive = new Drive();
        grabber = new Grabber();
        lift = new Lift();
        goStraightLong();
    }
    
    /*
	 * FIRST STEP: go straight 168 inches
	 */	
	public void goStraightLong() {
		drive.driveDistance(168.0, 0.4, lift -> liftGrabber());
	}
	
	/*
	 * SECOND STEP: lift the grabber to the highest preset
	 */	
	public void liftGrabber() {
		if (startingPoint==LEFT) {
			lift.goToSwitchHeight(drive -> turnRight());
		} else if (startingPoint==RIGHT) {
			lift.goToSwitchHeight(drive -> turnLeft());
		}
	}
	
	/*
	 * THIRD STEP A: turn right towards scale
	 */	
	public void turnRight() {
		drive.rotateDegrees(90.0, drive -> goStraightShort());
	}
	
	/*
	 * THIRD STEP B: turn left towards scale
	 */	
	public void turnLeft() {
		drive.rotateDegrees(-90.0, drive -> goStraightShort());
	}
	
	/*
	 * FOURTH STEP: go straight 13 inches 
	 */	
	public void goStraightShort() {
		drive.driveDistance(13.0, 0.4, grabber -> openGrabber());
	}
	
	/*
	 * FIFTH STEP: open the grabber
	 */	
	public void openGrabber() {
		grabber.release();
	}

}
