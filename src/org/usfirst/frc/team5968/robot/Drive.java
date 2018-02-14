package org.usfirst.frc.team5968.robot;

import java.util.function.Consumer;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

public class Drive implements IDrive {
    
    private AHRS navX;
    private TalonSRX leftMotorControllerLead;
    private TalonSRX rightMotorControllerFollow;
    

    private TalonSRX leftMotorControllerFollow;
    private TalonSRX rightMotorControllerLead;
    
    private double leftMotorSpeed;
    private double rightMotorSpeed;
    
    private final int PIDIDX = 0;
    private final int TIMEOUT = 0;
    private final int SENSORPOSITION = 0;
    
    private double distanceInches;
    private double targetRotations;
    private double angleToRotate;
    
    private ControlMode controlMode;
    
    private DriveMode driveMode;
    
    private final double ROTATION_TOLERANCE = 5;
    
    public Drive() {
        navX = new AHRS(SerialPort.Port.kMXP);
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
        distanceInches = 0;
        targetRotations = 0;
        angleToRotate = 0;
        driveMode = DriveMode.IDLEORMANUAL;
        
        // Reset encoders
        leftMotorControllerLead.setSelectedSensorPosition(SENSORPOSITION, PIDIDX, TIMEOUT);
        rightMotorControllerLead.setSelectedSensorPosition(SENSORPOSITION, PIDIDX, TIMEOUT);
        
        leftMotorSpeed = 0;
        rightMotorSpeed = 0;
        resetYaw();
    }
    

    public void initAutoPID() {
        leftMotorControllerLead.setSelectedSensorPosition(0, 0, 0);
        rightMotorControllerLead.setSelectedSensorPosition(0, 0, 0);
        

        /* set the peak, nominal outputs, and deadband */
        leftMotorControllerLead.configNominalOutputForward(0, 0);
        leftMotorControllerLead.configNominalOutputReverse(0, 0);
        leftMotorControllerLead.configPeakOutputForward(.5, 0);
        leftMotorControllerLead.configPeakOutputReverse(-.5, 0);
       
        /* set closed loop gains in slot0 */
        leftMotorControllerLead.config_kF(0, 0, 0);
        leftMotorControllerLead.config_kP(0, 0.58, 0);
        leftMotorControllerLead.config_kI(0, 0, 0);
        leftMotorControllerLead.config_kD(0, 0.08, 0);
        leftMotorControllerLead.config_IntegralZone(0, 100, 0);
        
        /* set the peak, nominal outputs, and deadband */
        rightMotorControllerLead.configNominalOutputForward(0, 0);
        rightMotorControllerLead.configNominalOutputReverse(0, 0);
        rightMotorControllerLead.configPeakOutputForward(.5, 0);
        rightMotorControllerLead.configPeakOutputReverse(-.5, 0);
        rightMotorControllerLead.config_IntegralZone(0, 100, 0);
       
        /* set closed loop gains in slot0 */
        rightMotorControllerLead.config_kF(0, 0, 0);
        rightMotorControllerLead.config_kP(0, 0.56, 0);
        rightMotorControllerLead.config_kI(0, 0, 0);
        rightMotorControllerLead.config_kD(0, 0.51, 0);
    }
    
    
    @Override
    public DriveMode getCurrentDriveMode() {
        return driveMode;
    }

    @Override
    public void driveDistance(double distanceInches, double speed) {
        driveMode = DriveMode.DRIVINGSTRAIGHT;
        driveStraight(ControlMode.Position);
        distanceInches = this.distanceInches;
        targetRotations = (distanceInches / (Math.PI * 6.0)) * 2048;
        
    }

    @Override
    public void rotateDegrees(double angle, double speed) {
        controlMode = ControlMode.PercentOutput;
        resetYaw();
        driveMode = DriveMode.ROTATING;
        leftMotorSpeed = 0.2;
        rightMotorSpeed = -0.2;
        angleToRotate = angle;
        
    }

    @Override
    public void driveDistance(double speed, double distanceInches, Consumer<IDrive> completionRoutine) {
        driveDistance(distanceInches, speed);
        completionRoutine.accept(this);
    }

    @Override
    public void rotateDegrees(double relativeAngle, double speed, Consumer<IDrive> completionRoutine) {
        rotateDegrees(relativeAngle, speed);
        completionRoutine.accept(this);
    }

    @Override
    public void driveManual(double leftSpeed, double rightSpeed) {
        driveMode = DriveMode.IDLEORMANUAL;
        leftMotorSpeed = leftSpeed;
        rightMotorSpeed = -rightSpeed;
    }

    private void driveStraight(ControlMode controlMode) {
        controlMode = ControlMode.Position;
    }
    
    private void resetYaw() {
        navX.reset();
    }
    
    private double getYaw() {
        return navX.getYaw();
    }
    
    private void setMotors(int leftMotorDirection, int rightMotorDirection, DriveMode driveMode) {
        if (driveMode == DriveMode.DRIVINGSTRAIGHT) {
            leftMotorControllerLead.set(controlMode, targetRotations * leftMotorDirection);
            leftMotorControllerLead.set(controlMode, targetRotations * rightMotorDirection);
        } else {
            leftMotorControllerLead.set(controlMode, leftMotorSpeed * leftMotorDirection);
            leftMotorControllerLead.set(controlMode, rightMotorSpeed * rightMotorDirection);
        }
    }

    @Override
    public void periodic() {
        if (getCurrentDriveMode() == DriveMode.IDLEORMANUAL) {
            setMotors(1, 1, DriveMode.IDLEORMANUAL);
        } else if (getCurrentDriveMode() == DriveMode.DRIVINGSTRAIGHT) {
            resetYaw();
            setMotors(-1, 1, DriveMode.DRIVINGSTRAIGHT);
        }
        else if (getCurrentDriveMode() == DriveMode.ROTATING) {
            if (Math.abs(getYaw() - angleToRotate) > ROTATION_TOLERANCE) {
                if ((getYaw() - angleToRotate) < 0) {
                    setMotors(1, -1, DriveMode.ROTATING);
                } else if ((getYaw() - angleToRotate) > 0) {
                    setMotors(-1, 1, DriveMode.ROTATING);
                }
            } else {
                setMotors(0, 0, DriveMode.IDLEORMANUAL);
            }
        }
    }
    
}
