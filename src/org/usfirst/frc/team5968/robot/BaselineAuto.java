package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class BaselineAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;

    public BaselineAuto(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
    }
    
    /*
	 * Execute the movements for auto mode: BASELINE only
	 */
	public void autoBaseline() {
		
	}

}
