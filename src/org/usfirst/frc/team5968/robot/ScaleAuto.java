package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class ScaleAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;

    public doScale(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
    }
    
    /*
	 * Execute the movements for auto mode: SCALE only
	 */
	public void autoScale() {
		
	}

}
