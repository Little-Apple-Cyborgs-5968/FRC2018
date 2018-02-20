package org.usfirst.frc.team5968.robot;

public class SwitchAuto {
	
	private StartingPoint startingPoint;
	private IDrive drive;
	private IGrabber grabber;
	private ILift lift;
	private final double rotationSpeed = 0.2;
	private final double driveSpeed = 0.2;

    public SwitchAuto(StartingPoint startingPoint) {
        startingPoint = this.startingPoint;
        drive = new Drive();
        //grabber = new Grabber();
        //grabber.grab();
        //lift = new Lift(drive);
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
		if (startingPoint==StartingPoint.LEFT) {
			//lift.goToSwitchHeight(drive -> turnRight());
		    turnRight();
		} else if (startingPoint==StartingPoint.RIGHT) {
			//lift.goToSwitchHeight(drive -> turnLeft());
		    turnLeft();
		}
	}
	
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
		drive.driveDistance(13.0, driveSpeed/*, grabber -> openGrabber()*/);
	}
	
	/*
	 * FIFTH STEP: open the grabber
	 */	
	public void openGrabber() {
		grabber.release();
	}

}
