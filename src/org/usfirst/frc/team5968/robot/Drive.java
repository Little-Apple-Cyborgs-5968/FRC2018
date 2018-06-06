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
    private double driveStraightSpeed;
    
    private double distanceInches;
    private double angleToRotate;
    
    private DriveMode driveMode;
    
    private static final double ROTATION_TOLERANCE = 5;

    private static final double ENCODER_RESOLUTION = 2048.0;
    private static final double WHEEL_DIAMETER = 6.0; // inches

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
        rightMotorControllerFollow.setInverted(true);
        rightMotorControllerLead.setInverted(true);

        leftMotorControllerFollow.follow(leftMotorControllerLead);
        rightMotorControllerFollow.follow(rightMotorControllerLead);
        
        init();
    }

    public void init() {
        currentCompletionRoutine = null;
        distanceInches = 0;
        angleToRotate = 0;
        stop();
        leftEncoder.reset();
        rightEncoder.reset();
    }
    
    @Override
    public DriveMode getCurrentDriveMode() {
        return driveMode;
    }

    @Override
    public void driveDistance(double distanceInches, double speed) {
        driveDistance(distanceInches, speed, null);
    }

    @Override
    public void rotateDegrees(double angle, double speed) {
        rotateDegrees(angle, speed, null);
    }

    @Override
    public void driveDistance(double distanceInches, double speed, Runnable completionRoutine) {
        setCompletionRoutine(completionRoutine);
        leftMotorSpeed = speed;
        rightMotorSpeed = speed;
        driveStraightSpeed = speed;
        driveMode = DriveMode.DRIVINGSTRAIGHT;
        this.distanceInches = distanceInches;
        leftEncoder.reset();
        rightEncoder.reset();
    }

    @Override
    public void rotateDegrees(double relativeAngle, double speed, Runnable completionRoutine) {
        setCompletionRoutine(completionRoutine);
        driveMode = DriveMode.ROTATING;
        navX.resetYaw();
        leftMotorSpeed = 0.2;
        rightMotorSpeed = 0.2;
        angleToRotate = relativeAngle;
    }


    private void driveManualImplementation(double leftSpeed, double rightSpeed) {
        driveMode = DriveMode.IDLEORMANUAL;
        leftMotorSpeed = leftSpeed;
        rightMotorSpeed = rightSpeed;
    }
    
    @Override
    public void driveManual(double leftSpeed, double rightSpeed) {
        setCompletionRoutine(null);
        driveManualImplementation(leftSpeed, rightSpeed);
    }

    private void stop() {
        driveManualImplementation(0.0, 0.0);
        driveStraightSpeed = 0.0;
    }
    
    private void setMotors(double leftMotorDirection, double rightMotorDirection) {
        // Debug.logPeriodic("Left motor speed: " + leftMotorSpeed + ", direction: " + leftMotorDirection);
        leftMotorControllerLead.set(ControlMode.PercentOutput, leftMotorSpeed * leftMotorDirection);
        rightMotorControllerLead.set(ControlMode.PercentOutput, rightMotorSpeed * rightMotorDirection);
    }

    private void handleActionEnd() {
        stop();
        
        if (currentCompletionRoutine != null) {
            Runnable oldCompletionRoutine = currentCompletionRoutine;
            currentCompletionRoutine = null;
            oldCompletionRoutine.run();
        }
    }

    private void setCompletionRoutine(Runnable completionRountime) {
        // If there's already a completion routine, fail because that means an action was interrupted, and we don't allow that.
        // Note that because we check this before seeing if a completion routine is being configured at all, we are saying that
        // even goTo*Height() without a completion routine must not be called until an action is completed.
        // The exception to this is if the action is explicitly aborted with abortCurrentAction.
        /*
        Other sensible things we could do here:
        A) Replace the completion routine - we're essentially canceling the action. (This might be confusing though - maybe print a warning.)
        B) Combine the completion routines:
        Runnable oldCompletionRoutine = currentCompletionRoutine; // Note: This may seem pointless, but it's actually very important! (Ask me to explain some other time.)
        Runnable combinedCompletionRoutine = () ->
        {
            oldCompletionRoutine.run();
            completionRoutine.run();
        };
        currentCompletionRoutine = combinedCompletionRoutine;
        */
        if (currentCompletionRoutine != null) {
            throw new IllegalStateException("Tried to perform a lift action while one was already in progress!");
        }

        currentCompletionRoutine = completionRountime;
    }

    private static final boolean enableSmartDriveStraight = false;
    private static final double driveStraightBackPedalMultiplier = 0.5;
    private static final double driveStraightTolerance = 0.25; // inches

    // "Smart" drive straight logic
    // Automatically adjusts motor speeds to try and keep encoders balanced within a tolerance.
    // Only relatively smart, a nicer implementation would try to dynamically choose a value for driveStraightBackPedalMultiplier.
    private void smartDriveStraightPeriodic() {
        // negative when left is behind, positive when left is ahead
        double encoderDifference = leftEncoder.getDistance() - rightEncoder.getDistance();

        if (Math.abs(encoderDifference) < driveStraightTolerance) {
            leftMotorSpeed = driveStraightSpeed;
            rightMotorSpeed = driveStraightSpeed;
        } else if (encoderDifference < 0.0) {
            leftMotorSpeed = driveStraightSpeed;
            rightMotorSpeed = driveStraightSpeed * driveStraightBackPedalMultiplier;
        } else {
            leftMotorSpeed = driveStraightSpeed * driveStraightBackPedalMultiplier;
            rightMotorSpeed = driveStraightSpeed;
        }

        // Debug.logPeriodic("Smart drive: " + leftMotorSpeed + ", " + rightMotorSpeed + " (encoder difference: " + encoderDifference + ")");
    }
    
    @Override
    public void periodic() {
        //TODO: Remove me once we confirm encoders are correct.
        // Debug.logPeriodic("Encoders: " + leftEncoder.getDistance() + ", " + rightEncoder.getDistance() + " inches");
        // Debug.logPeriodic("Distance inches: " + distanceInches + ", drive mode: " + driveMode);
        if (driveMode == DriveMode.IDLEORMANUAL) {
            setMotors(1, 1);
        } 
        else if (driveMode == DriveMode.DRIVINGSTRAIGHT) {
            // Process smart drive speed logic if enabled
            if (enableSmartDriveStraight) {
                smartDriveStraightPeriodic();
            }
            
            // Set the speed of the motors
            setMotors(1, 1);

            // Check if we've completed our travel
            double averageDistanceTraveled = (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;
            // Debug.logPeriodic("Avg distance: " + averageDistanceTraveled);
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
            throw new IllegalStateException("Drive controller is in an invalid drive mode.");
        }
    }
}