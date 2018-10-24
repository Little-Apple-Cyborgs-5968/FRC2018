package org.usfirst.frc.team5968.robot;

public class SwitchAuto implements IRobotMode {
    
    private FieldPosition startingPoint;
    private IDrive drive;
    private IGrabber grabber;
    private ILift lift;
    private final double rotationSpeed = 0.3;
    private final double driveSpeed = 0.5;

    public SwitchAuto(FieldPosition startingPoint, IDrive drive, IGrabber grabber, ILift lift) {
        startingPoint = this.startingPoint;
        this.drive = drive;
        this.grabber = grabber;
        this.lift = lift;
    }

    @Override
    public void init() {
        // Start the first step of this autonomous mode:
        goStraightFirst();
    }

    @Override
    public void periodic() {
        // No periodic processing required, autonomous state is implicitly stored in drive and lift.
    }
    
    /*
     * FIRST STEP: go straight 168 inches
     */ 
    public void goStraightFirst() {
        drive.driveDistance(80.0, driveSpeed, () -> goStraightSecond());
        Debug.log("Go straight first");
    }
    

    public void goStraightSecond() {
        drive.driveDistance(18.0, driveSpeed, () -> openGrabber());
        lift.goToScaleHeight();
        Debug.log("Go straight second");
    }

    /*
     * SECOND STEP: lift the grabber to the highest preset
     * TODO: Decide if we can preload the cube in a way that allows 
     * it to just be dropped on to the switch without lifting the scissor lift.
     * (Depends on how the grabber is mounted)
     */ 
    public void liftGrabber() {
        lift.goToScaleHeight(() -> goStraightSecond());
    }
    
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
     * FOURTH STEP: go straight 30 inches 
     */ 
    public void goStraightShort() {        
        if (startingPoint == FieldPosition.LEFT) {
            drive.driveDistance(30.0, driveSpeed, () -> liftGrabber());
        } else if (startingPoint == FieldPosition.RIGHT) {
            drive.driveDistance(30.0, driveSpeed, () -> liftGrabber());
        }
    }
    
    /*
     * FIFTH STEP: open the grabber
     */ 
    public void openGrabber() {
        Debug.log("Open grabber");
        grabber.release();
    }

}