package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class BaselineAuto {
	
	private StartingPoint startingPoint;
	private Alliance alliance;
	private IDrive drive;
	private double driveSpeed = 0.2;

    public BaselineAuto(StartingPoint startingPoint, Alliance alliance) {
        startingPoint = this.startingPoint;
        alliance = this.alliance;
        drive = new Drive();
        goStraight();
    }
    
    /*
	 * FIRST STEP: go straight 168 inches
	 */	
	public void goStraight() {
		drive.driveDistance(168.0, driveSpeed);
	}
	
}
