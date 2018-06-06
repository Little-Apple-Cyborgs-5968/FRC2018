package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.CameraServer;
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
    private IFieldInformation fieldInformation;
    
    private IDashboard dashboard;
    
    public Robot() {
        drive = new Drive();
        grabber = new Grabber();
        lift = new Lift();
        dashboard = new HardCodedDashboard();
        fieldInformation = new FieldInformation();
        
        disabledMode = new DisabledMode(grabber, fieldInformation);
        autonomousMode = new AutonomousMode(drive, grabber, lift, fieldInformation, dashboard);
        teleoperatedMode = new TeleoperatedMode(drive, grabber, lift);
        //TODO: CameraServer.getInstance().startAutomaticCapture();
    }
    
    @Override
    public void startCompetition() {
        HAL.observeUserProgramStarting();
        
        IRobotMode currentMode = null;
        IRobotMode desiredMode = null;
        
        while (true) {
            desiredMode = getDesiredMode();
        
            if (desiredMode != currentMode) {
                LiveWindow.setEnabled(isTest());
                doPeripheralReinitialization();
            	desiredMode.init();
            	currentMode = desiredMode;
            }
            currentMode.periodic();
            doPeripheralPeriodicProcessing();
            SmartDashboard.updateValues();
            LiveWindow.updateValues();
        }
    }
    
    private void doPeripheralReinitialization() {
        drive.init();
        lift.init();
    }
    private void doPeripheralPeriodicProcessing() {
        drive.periodic();
        grabber.periodic();
        lift.periodic();
        Debug.periodic();
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
