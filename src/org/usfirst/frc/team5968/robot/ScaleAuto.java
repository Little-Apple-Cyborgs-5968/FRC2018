package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class ScaleAuto implements IRobotMode {
	
	StartingPoint startingPoint;
	Alliance alliance;

    public ScaleAuto(StartingPoint s, Alliance a) {
        startingPoint = s;
        alliance = a;
    }
    
    /*
	 * Execute the movements for auto mode: SCALE only from the left side
	 */
	public void scaleFromLeft() {
		
	}
	
	/*
	 * Execute the movements for auto mode: SCALE only from the right side
	 */
	public void scaleFromRight() {
		
	}
	
	/*
	 * Chooses which side movements to execute.
	 */
	public void doScaleMovements() {
		if (startingPoint==LEFT) {
			scaleFromLeft();
		} else if (startingPoint==RIGHT) {
			scaleFromRight();
		}
	}

}
