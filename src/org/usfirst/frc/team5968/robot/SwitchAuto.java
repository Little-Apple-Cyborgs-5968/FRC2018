package org.usfirst.frc.team5968.robot;

public class SwitchAuto implements IRobotMode {
    
    private FieldPosition startingPoint;
    private IDrive drive;
    private IGrabber grabber;
    private final double rotationSpeed = 0.3;
    private final double driveSpeed = 0.5;

    public SwitchAuto(FieldPosition startingPoint, IDrive drive, IGrabber grabber) {
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
        
        if (startingPoint == FieldPosition.LEFT) {
            drive.driveDistance(168.0, driveSpeed, () -> turnRight());
        } else if (startingPoint == FieldPosition.RIGHT) {
            drive.driveDistance(168.0, driveSpeed, () -> turnLeft());
        }
    }
    
    /*
     * SECOND STEP: lift the grabber to the highest preset
     * TODO: Decide if we can preload the cube in a way that allows 
     * it to just be dropped on to the switch without lifting the scissor lift.
     * (Depends on how the grabber is mounted)
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