package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class SwitchAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;

    public doSwitch(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
    }
    
    /*
	 * Execute the movements for auto mode: SWITCH only
	 */
	public void autoSwitch() {
		
	}

}
