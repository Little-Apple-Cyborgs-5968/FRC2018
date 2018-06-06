package org.usfirst.frc.team5968.robot;

import java.lang.Runnable;

public interface ILift {
    
    /*
     * The following three methods call liftDistance to raise/lower to the
     * correct value
     */
    public void goToGroundHeight();
    public void goToScaleHeight();
    public void goToGroundHeight(Runnable completionRoutine);
    public void goToScaleHeight(Runnable completionRoutine);

    /*
     * (Re)-initializes this lift.
     * Aborts any curently-in-progress actions. This should be done when switching modes so that
     * actions from a previous mode do not affect the next one.
     */
    public void init();

    /*
     * Called periodically to execute the actions set by the above methods
     */
    public void periodic();
    public void setLiftSpeed(double speed);
    
}