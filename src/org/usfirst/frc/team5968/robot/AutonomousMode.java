package org.usfirst.frc.team5968.robot;

public class AutonomousMode implements IRobotMode {

    private Drive drive;
    private Autonomous autonomous;
    private StartingPoint startingPoint;
    
    public AutonomousMode(IDrive drive) {
        drive = this.drive;
        autonomous = new Autonomous();
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
