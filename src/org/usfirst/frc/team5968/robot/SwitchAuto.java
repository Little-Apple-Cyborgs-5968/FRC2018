package org.usfirst.frc.team5968.robot;

public class SwitchAuto implements IRobotMode {
	
	private FieldSide startingPoint;
	private IDrive drive;
	private IGrabber grabber;
	private final double rotationSpeed = 0.3;
	private final double driveSpeed = 0.5;

    public SwitchAuto(FieldSide startingPoint, IDrive drive, IGrabber grabber) {
        startingPoint = this.startingPoint;
        this.drive = drive;
        this.grabber = grabber;
    }

    @Override
	public void init() {
    	// Start the first step of this autonomous mode:
		goStraightLong();
	}

	@Override
	public void periodic() {
    	// No periodic processing required, autonomous state is implicitly stored in drive and lift.
	}
    
    /*
	 * FIRST STEP: go straight 168 inches
	 */	
	public void goStraightLong() {
		
		if (startingPoint==FieldSide.LEFT) {
		    drive.driveDistance(168.0, driveSpeed, () -> turnRight());
        } else if (startingPoint==FieldSide.RIGHT) {
            drive.driveDistance(168.0, driveSpeed, () -> turnLeft());
        }
	}
	
	/*
	 * SECOND STEP: lift the grabber to the highest preset
	 * (Not necessary, as we can just set the lift to the correct height beforehand)
	 */	
	/*public void liftGrabber() {
		if (startingPoint==FieldSide.LEFT) {
			//lift.goToSwitchHeight(() -> turnRight());
		    turnRight();
		} else if (startingPoint==FieldSide.RIGHT) {
			//lift.goToSwitchHeight(() -> turnLeft());
		    turnLeft();
		}
	}*/
	
	/*
	 * THIRD STEP A: turn right towards scale
	 */	
	public void turnRight() {
		drive.rotateDegrees(90.0, rotationSpeed, () -> goStraightShort());
	}
	
	/*
	 * THIRD STEP B: turn left towards scale
	 */	
	public void turnLeft() {
		drive.rotateDegrees(-90.0, rotationSpeed, () -> goStraightShort());
	}
	
	/*
	 * FOURTH STEP: go straight 13 inches 
	 */	
	public void goStraightShort() {
		drive.driveDistance(13.0, driveSpeed, () -> openGrabber());
	}
	
	/*
	 * FIFTH STEP: open the grabber
	 */	
	public void openGrabber() {
		grabber.release();
	}

}
