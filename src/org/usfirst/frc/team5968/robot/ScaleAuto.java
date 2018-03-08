package org.usfirst.frc.team5968.robot;

public class ScaleAuto {
	
	private StartingPoint startingPoint;
    private IDrive drive; 
    private IGrabber grabber;
    private ILift lift;
    private double rotationSpeed = 0.3;
    private double driveSpeed = 0.5;

    public ScaleAuto(StartingPoint s, IDrive drive, IGrabber grabber, ILift lift) {
        startingPoint = s;
        this.drive = drive;
        this.grabber = grabber;
        this.lift = lift;
        goStraightLong();
    }
    
    /*
	 * FIRST STEP: go straight 324 inches
	 */	
	public void goStraightLong() {
		
		if (startingPoint == StartingPoint.LEFT) {
	        drive.driveDistance(324.0, driveSpeed, drive -> turnRight());
        } else if (startingPoint==StartingPoint.RIGHT) {
            drive.driveDistance(324.0, driveSpeed, drive -> turnLeft());
        }
	}
	
    /*
     * SECOND STEP A: turn right towards scale
     */ 
    public void turnRight() {
        //drive.rotateDegrees(90.0, drive -> goStraightShort(drive));
        drive.rotateDegrees(90.0, rotationSpeed, lift -> liftGrabber());
    }
    
    /*
     * SECOND STEP B: turn left towards scale
     */ 
    public void turnLeft() {
        //drive.rotateDegrees(-90.0, drive -> goStraightShort(drive));
        drive.rotateDegrees(-90.0, rotationSpeed, lift -> liftGrabber());
    }
	
	/*
	 * THIRD STEP: lift the grabber to the highest preset
	 */	
	public void liftGrabber() {
		lift.goToScaleHeight(grabber -> openGrabber());

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
