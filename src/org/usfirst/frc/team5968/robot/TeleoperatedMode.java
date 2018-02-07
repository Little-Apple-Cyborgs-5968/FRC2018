package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.robot.PortMap.USB;

import edu.wpi.first.wpilibj.Joystick;

public class TeleoperatedMode implements IRobotMode {
    
    // private Lift lift;
    private IGrabber grabber;
    private IDrive drive;
    private Joystick leftJoystick;
    private Joystick rightJoystick;
    
    private final double TOLERANCE = 0.1;
    
    public TeleoperatedMode(IDrive drive) {
        leftJoystick = new Joystick(PortMap.portOf(USB.LEFT));
        rightJoystick = new Joystick(PortMap.portOf(USB.RIGHT));
        
        // lift = new Lift();
        grabber = new Grabber();
        this.drive = drive;
    }

    @Override
    public void init() {
        // lift.goToCurrentHeight();
        drive.init();
    }

    @Override
    public void periodic() {
        drive.driveManual(getLeftStick(), getRightStick());
        if (getButtonPressed(1)){
            grabber.toggleGrabbing();
        }
    }
    
    private double getLeftStick() {
        double leftY = leftJoystick.getY();
        return (Math.abs(leftY) < TOLERANCE) ? 0 : leftY;
        
    }
    
    private double getRightStick() {
        double rightY = rightJoystick.getY();
        return (Math.abs(rightY) < TOLERANCE) ? 0 : rightY;
        
    }
    private boolean getButtonPressed(int buttonNumber ){
        switch (buttonNumber){
        case 1: 
            return leftJoystick.getRawButton(buttonNumber);
           
        default: 
            return false;
                
        }
    }

    
}
