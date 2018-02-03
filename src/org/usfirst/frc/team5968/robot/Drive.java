package org.usfirst.frc.team5968.robot;

import java.util.function.Consumer;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Drive implements IDrive {
    
    private TalonSRX leftMotorControllerLead;
    private TalonSRX rightMotorControllerFollow;
    

    private TalonSRX leftMotorControllerFollow;
    private TalonSRX rightMotorControllerLead;
    
    private double leftMotorSpeed;
    private double rightMotorSpeed;
    
    private final int PIDIDX = 0;
    private final int TIMEOUT = 0;
    private final int SENSORPOSITION = 0;
    
    private ControlMode controlMode;
    
    private DriveMode driveMode;
    
    public Drive() {
        rightMotorControllerFollow = new TalonSRX(PortMap.portOf(CAN.RIGHT_MOTOR_CONTROLLER_FOLLOWER));
        rightMotorControllerLead = new TalonSRX(PortMap.portOf(CAN.RIGHT_MOTOR_CONTROLLER_LEAD));
        leftMotorControllerFollow = new TalonSRX(PortMap.portOf(CAN.LEFT_MOTOR_CONTROLLER_FOLLOWER));
        leftMotorControllerLead = new TalonSRX(PortMap.portOf(CAN.LEFT_MOTOR_CONTROLLER_LEAD));
        
        // Configure encoders on lead motors
        leftMotorControllerLead.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PIDIDX, TIMEOUT);
        rightMotorControllerLead.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PIDIDX, TIMEOUT);

        leftMotorControllerFollow.follow(leftMotorControllerLead);
        rightMotorControllerFollow.follow(rightMotorControllerLead);
        
        controlMode = ControlMode.PercentOutput;
    }

    public void init() {
        
        driveMode = DriveMode.IdleOrManual;
        // Resets encoders
        leftMotorControllerLead.setSelectedSensorPosition(SENSORPOSITION, PIDIDX, TIMEOUT);
        rightMotorControllerLead.setSelectedSensorPosition(SENSORPOSITION, PIDIDX, TIMEOUT);
        
        leftMotorSpeed = 0;
        rightMotorSpeed = 0;
    }
    
    @Override
    public DriveMode getCurrentDriveMode() {
        return driveMode;
    }

    @Override
    public void driveDistance(double distance, double speed) {
        driveMode = DriveMode.DrivingStraight;
        
    }

    @Override
    public void rotateDegrees(double angle, double speed) {
        driveMode = DriveMode.Rotating;
        
    }

    @Override
    public void driveDistance(double speed, double distanceInches, Consumer<IDrive> completionRoutine) {
        driveMode = DriveMode.DrivingStraight;
    }

    @Override
    public void rotateDegrees(double relativeAngle, Consumer<IDrive> completionRoutine) {
        driveMode = DriveMode.Rotating;
    }

    @Override
    public void driveManual(double leftSpeed, double rightSpeed) {
        driveMode = DriveMode.IdleOrManual;
        leftMotorSpeed = (leftSpeed + 1.0) / 2.0;
        rightMotorSpeed = (rightSpeed + 1.0) / 2.0;
    }

    @Override
    public void periodic() {
        if (getCurrentDriveMode() == DriveMode.IdleOrManual) {
            leftMotorControllerLead.set(controlMode, leftMotorSpeed);
            rightMotorControllerLead.set(controlMode, rightMotorSpeed);
        }
        else if (getCurrentDriveMode() == DriveMode.DrivingStraight) {
            
        }
        else if (getCurrentDriveMode() == DriveMode.Rotating) {
            
        }
    }

}
