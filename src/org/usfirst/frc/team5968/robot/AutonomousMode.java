package org.usfirst.frc.team5968.robot;

public class AutonomousMode implements IRobotMode {

    private IDrive drive;
    private Autonomous autonomous;
    private StartingPoint startingPoint;
    private IGrabber grabber;
    private ILift lift;
    
    public AutonomousMode(IDrive drive, IGrabber grabber, ILift lift) {
        this.drive = drive;
        this.grabber = grabber;
        this.lift = lift;
        autonomous = new Autonomous(this.drive, this.grabber, this.lift);
        startingPoint = StartingPoint.CENTER;
    }

    @Override
    public void init() {
        //drive.initAutoPID();
        autonomous.doAuto(startingPoint);
    }

    @Override
    public void periodic() {
    }
    
}
