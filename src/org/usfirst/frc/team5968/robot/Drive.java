package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Consumer;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
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
        // Inverts encoder output
        leftMotorControllerLead.setSensorPhase(true);
        // Set relevant frame periods to be at least as fast as periodic rate
        leftMotorControllerLead.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, TIMEOUT);
        leftMotorControllerLead.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, TIMEOUT);
        // Set peak and nominal outputs
        leftMotorControllerLead.configNominalOutputForward(0, TIMEOUT);
        leftMotorControllerLead.configNominalOutputReverse(0, TIMEOUT);
        leftMotorControllerLead.configPeakOutputForward(1, TIMEOUT);
        leftMotorControllerLead.configPeakOutputReverse(-1, TIMEOUT);
        rightMotorControllerLead.configNominalOutputForward(0, TIMEOUT);
        rightMotorControllerLead.configNominalOutputReverse(0, TIMEOUT);
        rightMotorControllerLead.configPeakOutputForward(1, TIMEOUT);
        rightMotorControllerLead.configPeakOutputReverse(-1, TIMEOUT);
        // Set PIDF values
        leftMotorControllerLead.selectProfileSlot(0, PIDIDX);
        leftMotorControllerLead.config_kF(0, 0.0, TIMEOUT);
        leftMotorControllerLead.config_kP(0, 0.2, TIMEOUT);
        leftMotorControllerLead.config_kI(0, 0.0, TIMEOUT); 
        leftMotorControllerLead.config_kD(0, 0.0, TIMEOUT);
        rightMotorControllerLead.selectProfileSlot(0, 0);
        rightMotorControllerLead.config_kF(0, 0.0, TIMEOUT);
        rightMotorControllerLead.config_kP(0, 0.2, TIMEOUT);
        rightMotorControllerLead.config_kI(0, 0.0, TIMEOUT); 
        rightMotorControllerLead.config_kD(0, 0.0, TIMEOUT);
        
        leftMotorControllerLead.configMotionCruiseVelocity(15000, TIMEOUT);
        leftMotorControllerLead.configMotionAcceleration(6000, TIMEOUT);
        
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
        leftMotorSpeed = leftSpeed * 512 * 500 / 600;
        rightMotorSpeed = rightSpeed * 512 * 500 / 600;
    }

    @Override
    public void periodic() {
        if (getCurrentDriveMode() == DriveMode.IdleOrManual) {
            leftMotorControllerLead.set(ControlMode.Velocity, leftMotorSpeed);
            rightMotorControllerLead.set(ControlMode.Velocity, rightMotorSpeed);
            
            SmartDashboard.putNumber("Left Error", leftMotorControllerLead.getClosedLoopError(PIDIDX));
            SmartDashboard.putNumber("Right Error", rightMotorControllerLead.getClosedLoopError(PIDIDX));
            SmartDashboard.putNumber("Left Target Velocity", leftMotorSpeed);
            SmartDashboard.putNumber("Right Target Velocity", rightMotorSpeed);
            SmartDashboard.putNumber("Left Encoder Velocity", leftMotorControllerLead.getSelectedSensorVelocity(0));
            SmartDashboard.putNumber("Right Encoder Velocity", rightMotorControllerLead.getSelectedSensorVelocity(0));
        }
        else if (getCurrentDriveMode() == DriveMode.DrivingStraight) {
            
        }
        else if (getCurrentDriveMode() == DriveMode.Rotating) {
            
        }
    }

}
