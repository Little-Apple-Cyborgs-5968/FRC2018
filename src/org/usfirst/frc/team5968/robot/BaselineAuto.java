package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class BaselineAuto {
	
	private StartingPoint startingPoint;
	private IDrive drive;
	private double driveSpeed = 0.5;

    public BaselineAuto(StartingPoint startingPoint, IDrive drive) {
        startingPoint = this.startingPoint;
        this.drive = drive;
        goStraight();
        System.out.println("Baseline Auto");
    }
    
    /*
	 * FIRST STEP: go straight 168 inches
	 */	
	public void goStraight() {
        System.out.println("Going Straight");
		drive.driveDistance(168.0, driveSpeed);
	}
	
}
