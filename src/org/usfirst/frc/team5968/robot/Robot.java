package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends RobotBase {
	
	private IRobotMode disabledMode;
	private IRobotMode autonomousMode;
	private IRobotMode teleoperatedMode;
	
	public Robot() {
		
	}

	@Override
	public void startCompetition() {
    	HAL.observeUserProgramStarting();
    	
    	while(true) {
    		
    		
    		SmartDashboard.updateValues();
            LiveWindow.updateValues();
    	}
	}
	
	private IRobotMode getDesiredMode() {
		if (isDisabled()) {
            HAL.observeUserProgramDisabled();
            return disabledMode;
        } else if (isAutonomous()) {
            HAL.observeUserProgramAutonomous();
            return autonomousMode;
        } else if (isOperatorControl()) {
            HAL.observeUserProgramTeleop();
            return teleoperatedMode;
        } else if (isTest()) {
            HAL.observeUserProgramTest();
            return teleoperatedMode;
        } else {
            throw new IllegalStateException("Robot is in an invalid mode.");
        }
	}
	
	
}

