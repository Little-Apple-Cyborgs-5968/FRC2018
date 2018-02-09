package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class ScaleAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;
    IDrive drive; 
    IGrabber grabber;
    ILift lift;

    public ScaleAuto(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
        //put the first step here
        drive = new Drive();
        grabber = new Grabber();
        grabber.grab();
        lift = new Lift();
        goStraightLong();
    }
    
    /*
	 * FIRST STEP: go straight 324 inches
	 */	
	public void goStraightLong() {
		drive.driveDistance(324.0, 0.4, lift -> liftGrabber());
	}
	
	/*
	 * SECOND STEP: lift the grabber to the highest preset
	 */	
	public void liftGrabber() {
		if (startingPoint==LEFT) {
			lift.goToScaleHeight(drive -> turnRight());
		} else if (startingPoint==RIGHT) {
			lift.goToScaleHeight(drive -> turnLeft());
		}
	}
	
	/*
	 * THIRD STEP A: turn right towards scale
	 */	
	public void turnRight() {
		//drive.rotateDegrees(90.0, drive -> goStraightShort(drive));
		drive.rotateDegrees(90.0, drive -> openGrabber());
	}
	
	/*
	 * THIRD STEP B: turn left towards scale
	 */	
	public void turnLeft() {
		//drive.rotateDegrees(-90.0, drive -> goStraightShort(drive));
		drive.rotateDegrees(-90.0, grabber -> openGrabber());
	}

	/*
	 * FOURTH STEP: go straight a small amount of inches 
	 * PROBABLY UNNECESSARY AS OF NOW
	public void goStraightShort(IDrive drive) {
		drive.driveDistance(0.5, 30.0, drive -> openGrabber(drive));
	}
	*/	
	
	/*
	 * FIFTH STEP: open the grabber
	 */	
	public void openGrabber() {
		grabber.release();
	}
}
