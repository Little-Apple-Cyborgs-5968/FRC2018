package org.usfirst.frc.team5968.robot;

public class SwitchAuto {
	
	private StartingPoint startingPoint;
	private IDrive drive;
	private IGrabber grabber;
	private final double rotationSpeed = 0.3;
	private final double driveSpeed = 0.5;

    public SwitchAuto(StartingPoint startingPoint, IDrive drive, IGrabber grabber) {
        startingPoint = this.startingPoint;
        this.drive = drive;
        this.grabber = grabber;
        goStraightLong();
    }
    
    /*
	 * FIRST STEP: go straight 168 inches
	 */	
	public void goStraightLong() {
		
		if (startingPoint==StartingPoint.LEFT) {
		    drive.driveDistance(168.0, driveSpeed, drive -> turnRight());
        } else if (startingPoint==StartingPoint.RIGHT) {
            drive.driveDistance(168.0, driveSpeed, drive -> turnLeft());
        }
	}
	
	/*
	 * SECOND STEP: lift the grabber to the highest preset
	 * (Not necessary, as we can just set the lift to the correct height beforehand)
	 */	
	/*public void liftGrabber() {
		if (startingPoint==StartingPoint.LEFT) {
			//lift.goToSwitchHeight(drive -> turnRight());
		    turnRight();
		} else if (startingPoint==StartingPoint.RIGHT) {
			//lift.goToSwitchHeight(drive -> turnLeft());
		    turnLeft();
		}
	}*/
	
	/*
	 * THIRD STEP A: turn right towards scale
	 */	
	public void turnRight() {
		drive.rotateDegrees(90.0, rotationSpeed, drive -> goStraightShort());
	}
	
	/*
	 * THIRD STEP B: turn left towards scale
	 */	
	public void turnLeft() {
		drive.rotateDegrees(-90.0, rotationSpeed, drive -> goStraightShort());
	}
	
	/*
	 * FOURTH STEP: go straight 13 inches 
	 */	
	public void goStraightShort() {
		drive.driveDistance(13.0, driveSpeed, grabber -> openGrabber());
	}
	
	/*
	 * FIFTH STEP: open the grabber
	 */	
	public void openGrabber() {
		grabber.release();
	}

}
