package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Grabber implements IGrabber {
    
    private TalonSRX rightMotor;
    private TalonSRX leftMotor;
    private DoubleSolenoid grabberPiston;
    private PistonState pistonState;

    
    private double grabberSpeed = 0.9;

    private double moveDirection = 0.0;
    private static final double moveIn = 1.0;
    private static final double moveOut = -1.0; 
    
    public Grabber (){
        rightMotor = new TalonSRX(PortMap.portOf(CAN.GRABBER_RIGHT_MOTOR_CONTROLLER));
        leftMotor = new TalonSRX(PortMap.portOf(CAN.GRABBER_LEFT_MOTOR_CONTROLLER));
        
        grabberPiston = new DoubleSolenoid(4, 5); // add channels later
        pistonState = PistonState.OPEN;

        init();
    }
    
    public void init() {
        // Abort the current action
        moveDirection = 0.0;
    }
    
    @Override
    public void setSpeed(double speed) {
        grabberSpeed = Math.abs(speed); 
        }

    @Override
    public void grab() {   
        moveDirection = moveIn;
    }

    @Override
    public void shoot() {
        moveDirection = moveOut;
    }
    
    @Override
    public void deploy(){
        grabberPiston.set(DoubleSolenoid.Value.kForward);
    }
    
    @Override
    public void periodic() {
        leftMotor.set(ControlMode.PercentOutput, -moveDirection * grabberSpeed);
        rightMotor.set(ControlMode.PercentOutput, moveDirection * grabberSpeed);
    }
    
}
