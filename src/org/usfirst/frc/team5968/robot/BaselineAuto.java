package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class BaselineAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;
	IDrive drive;

    public BaselineAuto(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
        drive = new Drive();
        goStraight();
    }
    
    /*
	 * FIRST STEP: go straight 168 inches
	 */	
	public void goStraight() {
		drive.driveDistance(168.0, 0.4);
	}

}
