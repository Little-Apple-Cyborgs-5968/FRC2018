package org.usfirst.frc.team5968.robot;

import java.util.function.Consumer;

public interface IDrive {

    public DriveMode getCurrentDriveMode();
    
    /*
     * Drive straight forward a specified distance at a specified speed
     */
    public void driveDistance(double distance, double speed);

    /*
     * Turn in place a specified angle at a specified speed
     */
    public void rotateDegrees(double angle, double speed);

    /*
     * completionRoutine is called when the current action has been completed
     */
    public void driveDistance(double speed, double distanceInches, Runnable completionRoutine);

    public void rotateDegrees(double relativeAngle, double speed, Runnable completionRoutine);

    /*
     * This is the method used to drive manually during teleoperated mode
     */
    public void driveManual(double leftSpeed, double rightSpeed);
    

    public void init();
    
    /*
     * Called periodically to actually execute the driving and rotating set by
     * the driveDistance() and rotateDegrees() methods
     */
    public void periodic();

}
