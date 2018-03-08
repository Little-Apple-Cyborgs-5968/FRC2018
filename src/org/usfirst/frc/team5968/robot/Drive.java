package org.usfirst.frc.team5968.robot;

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
        //navX = new NavXMXP(new AHRS(SerialPort.Port.kMXP));
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
        driveMode = DriveMode.DRIVINGSTRAIGHT;
    }

    public void init() {
        distanceInches = 0;
        targetRotations = 0;
        angleToRotate = 0;
        //driveMode = DriveMode.IDLEORMANUAL;
        
        // Reset encoders
        leftMotorControllerLead.setSelectedSensorPosition(SENSORPOSITION, PIDIDX, TIMEOUT);
        rightMotorControllerLead.setSelectedSensorPosition(SENSORPOSITION, PIDIDX, TIMEOUT);
        
        leftMotorSpeed = 0;
        rightMotorSpeed = 0;
        //navX.resetYaw();
    }
    

    public void initAutoPID() {
        leftMotorControllerLead.setSelectedSensorPosition(0, 0, 0);
        rightMotorControllerLead.setSelectedSensorPosition(0, 0, 0);
        

        /* set the peak, nominal outputs, and deadband */
        /*leftMotorControllerLead.configNominalOutputForward(0, 0);
        leftMotorControllerLead.configNominalOutputReverse(0, 0);
        leftMotorControllerLead.configPeakOutputForward(.5, 0);
        leftMotorControllerLead.configPeakOutputReverse(-.5, 0);
       
        leftMotorControllerLead.config_kF(0, 0, 0);
        leftMotorControllerLead.config_kP(0, 0.58, 0);
        leftMotorControllerLead.config_kI(0, 0, 0);
        leftMotorControllerLead.config_kD(0, 0.08, 0);
        leftMotorControllerLead.config_IntegralZone(0, 100, 0);
        
        rightMotorControllerLead.configNominalOutputForward(0, 0);
        rightMotorControllerLead.configNominalOutputReverse(0, 0);
        rightMotorControllerLead.configPeakOutputForward(.5, 0);
        rightMotorControllerLead.configPeakOutputReverse(-.5, 0);
        rightMotorControllerLead.config_IntegralZone(0, 100, 0);
       
        rightMotorControllerLead.config_kF(0, 0, 0);
        rightMotorControllerLead.config_kP(0, 0.56, 0);
        rightMotorControllerLead.config_kI(0, 0, 0);
        rightMotorControllerLead.config_kD(0, 0.51, 0);*/
    }
    
    
    @Override
    public DriveMode getCurrentDriveMode() {
        return driveMode;
    }

    @Override
    public void driveDistance(double distanceInches, double speed) {
        driveMode = DriveMode.DRIVINGSTRAIGHT;
        //System.out.println("---Driving distance");
        System.out.println("Set drive mode to: " + driveMode);
        driveStraight(ControlMode.Position);
        distanceInches = this.distanceInches;
        targetRotations = (distanceInches / (Math.PI * 6.0)) * 2048;
        
    }

    @Override
    public void rotateDegrees(double angle, double speed) {
        controlMode = ControlMode.PercentOutput;
        navX.resetYaw();
        driveMode = DriveMode.ROTATING;
        leftMotorSpeed = 0.2;
        rightMotorSpeed = -0.2;
        angleToRotate = angle;
        
    }

    @Override
    public void driveDistance(double speed, double distanceInches, Consumer<IDrive> completionRoutine) {
        driveMode = DriveMode.DRIVINGSTRAIGHT;
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
        System.out.println("Driving straight in method");
    }
    
    private void setMotors(int leftMotorDirection, int rightMotorDirection, DriveMode driveMode) {
        if (driveMode == DriveMode.DRIVINGSTRAIGHT) {
            leftMotorControllerLead.set(controlMode, Math.pow(targetRotations * leftMotorDirection, 3));
            rightMotorControllerLead.set(controlMode, Math.pow(targetRotations * rightMotorDirection, 3));
        } else {
            leftMotorControllerLead.set(controlMode, -leftMotorSpeed * leftMotorDirection);
            rightMotorControllerLead.set(controlMode, -rightMotorSpeed * rightMotorDirection);
        }
    }
    int i = 0;
    boolean distance = false;
    @Override
    public void periodic() {
        /*if(i % 5000 == 0) {
            System.out.println("Nothing is working: " + driveMode);
        }*/
        if (driveMode == DriveMode.IDLEORMANUAL) {
            setMotors(1, 1, DriveMode.IDLEORMANUAL);
        } 
        else if (driveMode == DriveMode.DRIVINGSTRAIGHT) {
            //System.out.println("DRIVING STRAIGHT NOW");
            //navX.resetYaw();
            //setMotors(-1, 1, DriveMode.DRIVINGSTRAIGHT);
            if (!distance) {
                leftMotorControllerLead.set(ControlMode.Position, 100/*leftMotorSpeed * leftMotorDirection*/);
                rightMotorControllerLead.set(ControlMode.Position, -100/*rightMotorSpeed * rightMotorDirection*/);
                distance = true;
            }
        } 
        else if (driveMode == DriveMode.ROTATING) {
            if (Math.abs(navX.getYaw() - angleToRotate) > ROTATION_TOLERANCE) {
                if ((navX.getYaw() - angleToRotate) < 0) {
                    setMotors(1, -1, DriveMode.ROTATING);
                } else if ((navX.getYaw() - angleToRotate) > 0) {
                    setMotors(-1, 1, DriveMode.ROTATING);
                }
            } else {
                setMotors(0, 0, DriveMode.IDLEORMANUAL);
            }
        }
        else {
            
        }
        i++;
    }
    
}
