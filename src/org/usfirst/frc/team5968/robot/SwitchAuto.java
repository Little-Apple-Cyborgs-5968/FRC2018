package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class SwitchAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;

    public SwitchAuto(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
    }
    
    /*
	 * Execute the movements for auto mode: SWITCH only from the left side
	 */
	public void switchFromLeft() {
		
	}
	
	/*
	 * Execute the movements for auto mode: SWITCH only from the left side
	 */
	public void switchFromRight() {
		
	}
	
	/*
	 * Chooses which side movements to execute.
	 */
	public void doSwitchMovements() {
		if (startingPoint==LEFT) {
			switchFromLeft();
		} else if (startingPoint==RIGHT) {
			switchFromRight();
		}
	}

}
