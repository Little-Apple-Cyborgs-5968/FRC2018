package org.usfirst.frc.team5968.robot;

import java.util.function.Consumer;

public interface ILift {
    
   
    /*
     * The following three methods call liftDistance to raise/lower to the
     * correct value
     */
    public void goToGroundHeight();
    
    public void goToSwitchHeight();

    public void goToScaleHeight();

    /*
     * Moves the scissor lift to the correct height
     */
    public void goToCurrentHeight();

       
    /*
     * Called periodically to execute the actions set by the above methods
     */
    public void periodic();

    public void goToSwitchHeight(Consumer<IDrive> completionRoutine);
    public void goToScaleHeight(Consumer<IDrive> completionRoutine);
    public void goToGroundHeight(Consumer<IDrive> completionRoutine);
}
