package org.usfirst.frc.team5968.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

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
