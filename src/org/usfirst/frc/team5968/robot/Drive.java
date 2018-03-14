package org.usfirst.frc.team5968.robot;

import java.lang.Runnable;
import java.lang.IllegalStateException;
import java.util.function.Consumer;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

public class Drive implements IDrive {
    
    private NavXMXP navX;
    private TalonSRX leftMotorControllerLead;
    private TalonSRX rightMotorControllerFollow;
    

    private TalonSRX leftMotorControllerFollow;
    private TalonSRX rightMotorControllerLead;
    
    private double leftMotorSpeed;
    private double rightMotorSpeed;
    
    private double distanceInches;
    private double angleToRotate;
    
    private DriveMode driveMode;
    
    private static final double ROTATION_TOLERANCE = 5;

    private static final double ENCODER_RESOLUTION = 2048.0;
    private static final double WHEEL_DIAMETER = 6.6; // inches

    private IEncoder leftEncoder;
    private IEncoder rightEncoder;

    private Runnable currentCompletionRoutine;
    
    public Drive() {
        //navX = new NavXMXP(new AHRS(SerialPort.Port.kMXP));
        rightMotorControllerFollow = new TalonSRX(PortMap.portOf(CAN.RIGHT_MOTOR_CONTROLLER_FOLLOWER));
        rightMotorControllerLead = new TalonSRX(PortMap.portOf(CAN.RIGHT_MOTOR_CONTROLLER_LEAD));
        leftMotorControllerFollow = new TalonSRX(PortMap.portOf(CAN.LEFT_MOTOR_CONTROLLER_FOLLOWER));
        leftMotorControllerLead = new TalonSRX(PortMap.portOf(CAN.LEFT_MOTOR_CONTROLLER_LEAD));

        leftEncoder = new TalonEncoder(leftMotorControllerLead);
        rightEncoder = new TalonEncoder(rightMotorControllerLead);

        double distancePerPulse = (WHEEL_DIAMETER * Math.PI) / ENCODER_RESOLUTION;
        leftEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setDistancePerPulse(distancePerPulse);

        leftEncoder.setInverted(true);
        leftMotorControllerFollow.setInverted(true);
        leftMotorControllerLead.setInverted(true);

        leftMotorControllerFollow.follow(leftMotorControllerLead);
        rightMotorControllerFollow.follow(rightMotorControllerLead);
        
        init();
    }

    public void init() {
        distanceInches = 0;
        angleToRotate = 0;
        stop();
    }
    
    @Override
    public DriveMode getCurrentDriveMode() {
        return driveMode;
    }

    @Override
    public void driveDistance(double distanceInches, double speed) {
        driveMode = DriveMode.DRIVINGSTRAIGHT;
        distanceInches = this.distanceInches;
        leftEncoder.reset();
        rightEncoder.reset();
    }

    @Override
    public void rotateDegrees(double angle, double speed) {
        driveMode = DriveMode.ROTATING;
        navX.resetYaw();
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
        rightMotorSpeed = rightSpeed;
    }

    public void stop() {
        driveManual(0.0, 0.0);
    }
    
    private void setMotors(double leftMotorDirection, double rightMotorDirection) {
        leftMotorControllerLead.set(ControlMode.PercentOutput, leftMotorSpeed * leftMotorDirection);
        rightMotorControllerLead.set(ControlMode.PercentOutput, rightMotorSpeed * rightMotorDirection);
    }

    private void handleActionEnd() {
        if (currentCompletionRoutine != null) {
            currentCompletionRoutine.run();
            currentCompletionRoutine = null;
        }

        stop();
    }

    @Override
    public void periodic() {
        //TODO: Remove me once we confirm encoders are correct.
        Debug.logPeriodic("Encoders: " + leftEncoder.getDistance().toString() + ", " + rightEncoder.getDistance().toString() + " inches");

        if (driveMode == DriveMode.IDLEORMANUAL) {
            setMotors(1, 1);
        } 
        else if (driveMode == DriveMode.DRIVINGSTRAIGHT) {
            setMotors(1, 1);

            // Check if we've completed our travel
            double averageDistanceTraveled = (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;

            if (averageDistanceTraveled > distanceInches) {
                handleActionEnd();
            }
        } 
        else if (driveMode == DriveMode.ROTATING) {
            double deltaAngle = navX.getYaw() - angleToRotate;

            if (deltaAngle > 0.0) {
                setMotors(1, -1);
            } else {
                setMotors(-1, 1);
            }

            // Check if we've finished rotating
            if (Math.abs(deltaAngle) <= ROTATION_TOLERANCE) {
                handleActionEnd();
            }
        }
        else {
            throw new InvalidStateException("Drive controller is in an invalid drive mode.");
        }
    }
}
