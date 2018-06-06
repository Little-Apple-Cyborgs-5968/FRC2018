package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class BaselineAuto implements IRobotMode {
    
    private FieldPosition startingPoint;
    private IDrive drive;
    private double driveSpeed = 0.5;

    public BaselineAuto(FieldPosition startingPoint, IDrive drive) {
        startingPoint = this.startingPoint;
        this.drive = drive;
    }

    @Override
    public void init() {
        // Start the first step of this autonomous mode:
        goStraight();
    }

    @Override
    public void periodic() {
        // No periodic processing required, autonomous state is implicitly stored in drive and lift.
    }

    /*
     * FIRST STEP: go straight 148 inches
     */ 
    public void goStraight() {
        drive.driveDistance(140.0, driveSpeed);
    }
    
}