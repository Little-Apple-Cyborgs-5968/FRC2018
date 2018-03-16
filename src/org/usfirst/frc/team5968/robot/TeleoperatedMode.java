package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.robot.PortMap.USB;

import edu.wpi.first.wpilibj.Joystick;

public class TeleoperatedMode implements IRobotMode {
    
    private ILift lift;
    private IGrabber grabber;
    private IDrive drive;
    private Joystick leftJoystick;
    private Joystick rightJoystick;
    
    private final double TOLERANCE = 0.1;
    
    public TeleoperatedMode(IDrive drive, IGrabber grabber, ILift lift) {
        leftJoystick = new Joystick(PortMap.portOf(USB.LEFT));
        rightJoystick = new Joystick(PortMap.portOf(USB.RIGHT));
        
        this.lift = lift;
        this.grabber = grabber;
        this.drive = drive;
    }

    @Override
    public void init() {
        drive.init();
    }

    @Override
    public void periodic() {
        if (!getLeftButtonPressed(5)) {
            drive.driveManual(getLeftStick(), getRightStick());
        } else {
            lift.setMotorSpeed(getLeftStick());
        }
        
        if (getLeftButtonPressed(5)){
            grabber.toggleGrabbing();
        }
        
        if (getRightButtonPressed(3)){
            lift.goToGroundHeight();
        }
        
        if (getRightButtonPressed(6)){
            lift.goToScaleHeight();
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
    
    private boolean getLeftButtonPressed(int buttonNumber) {
        return leftJoystick.getRawButton(buttonNumber);
    }
    
    private boolean getRightButtonPressed(int buttonNumber) {
        return rightJoystick.getRawButton(buttonNumber);
    }
    
}
