package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DigitalInput;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift implements ILift{

    private LiftHeight desiredHeight;
    
    private LiftHeight currentHeight;
    
    DigitalInput groundLimit;
    
    DigitalInput switchLimit;
    
    DigitalInput scaleLimit;
    
    DigitalInput topLimit;
    
    TalonSRX liftMotor;
    
    double motorSpeed;
    
    public Lift() {
        liftMotor = new TalonSRX(PortMap.portOf(CAN.LIFT_MOTOR_CONTROLLER));
        motorSpeed = 0.2;
    }
    
    private void goTo (LiftHeight desiredHt) {
        desiredHeight = desiredHt;
    }
    
    public void goToGroundHeight() {
        goTo(LiftHeight.GROUND);
    }
    
    public void goToSwitchHeight() {
        goTo(LiftHeight.SWITCH);
    }
    
    public void goToScaleHeight() {
        goTo(LiftHeight.SCALE);
    }
    
   
    public void goToCurrentHeight() {
        
        // if x limit switch is triggered, go to that switch's height
        // else go to ground height
        
        if (groundLimit.get()) {
            goTo(LiftHeight.GROUND);
        } else {
            goTo(LiftHeight.GROUND);
        }
        
        if (switchLimit.get()) {
            goTo(LiftHeight.SWITCH);
        } else {
            goTo(LiftHeight.GROUND);
        }
        
        if (scaleLimit.get()) {
            goTo(LiftHeight.SCALE);
        } else {
            goTo(LiftHeight.GROUND);
        }
        
        if (topLimit.get()) {
            goTo(LiftHeight.TOP);
        } else {
            goTo(LiftHeight.GROUND);
        }
    }
    
    public void periodic() {
        
        if (desiredHeight == LiftHeight.SCALE) {
            if (currentHeight == LiftHeight.GROUND || currentHeight ==  LiftHeight.SWITCH) {
                liftMotor.set(ControlMode.PercentOutput,motorSpeed);
            } else if (currentHeight == LiftHeight.TOP) {
                liftMotor.set(ControlMode.PercentOutput, -motorSpeed);
            }
        }
        
        else if (desiredHeight == LiftHeight.SWITCH) {
            if (currentHeight == LiftHeight.GROUND || currentHeight == LiftHeight.SCALE) {
                liftMotor.set(ControlMode.PercentOutput, motorSpeed);
            } else if (currentHeight == LiftHeight.TOP) {
                liftMotor.set(ControlMode.PercentOutput, -motorSpeed);
            }
        
    }
        
        else if (desiredHeight == LiftHeight.GROUND) {
            if (currentHeight == LiftHeight.SCALE ||currentHeight == LiftHeight.SWITCH) {
                liftMotor.set(ControlMode.PercentOutput, motorSpeed);
            } else if (currentHeight == LiftHeight.TOP) {
                liftMotor.set(ControlMode.PercentOutput, -motorSpeed);
            }
            
        }
    }
}
        
 
    
        
  