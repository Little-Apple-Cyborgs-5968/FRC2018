package org.usfirst.frc.team5968.robot;

public class AutonomousMode implements IRobotMode {

    private Drive drive;
    
    public AutonomousMode(IDrive drive) {
        drive = this.drive;
    }

    @Override
    public void init() {
        drive.initAutoPID();
    }

    @Override
    public void periodic() {
    }
    
}
