package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotBase {
    
    private IRobotMode disabledMode;
    private IRobotMode autonomousMode;
    private IRobotMode teleoperatedMode;
    
    private IDrive drive;
    private IGrabber grabber;
    private ILift lift;
    
    public Robot() {
        drive = new Drive();
        grabber = new Grabber();
        lift = new Lift();
        
        disabledMode = new disabledMode();
        autonomousMode = new autonomousMode(drive, grabber, lift);
        teleoperatedMode = new teleoperatedMode(drive, grabber, lift);
    }
    
    @Override
    public void startCompetition() {
        HAL.observeUserProgramStarting();
        LiveWindow.setEnabled(isTest());
        
        IRobotMode currentMode = null;
        IRobotMode desiredMode = null;
        
        while (true) {
            desiredMode = getDesiredMode();
        
            if (desiredMode != currentMode) {
            	desiredMode.init();
            	currentMode = desiredMode;
            }
        
            currentMode.periodic();
            doPeripheralPeriodicProcessing();
            SmartDashboard.updateValues();
            LiveWindow.updateValues();
        }
    }
    
    private void doPeripheralPeriodicProcessing() {
        drive.periodic();
        grabber.periodic();
        lift.periodic();
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
            throw new IllegalStateException("Robot is in an invalid mode");
        }
    }

}
