package org.usfirst.frc.team5968.robot;

<<<<<<< HEAD
import java.util.function.Consumer;

public interface ILift {
    
=======
public interface ILift {
    
   
>>>>>>> dbf063a37b8d18e22be1d013e8945e43c570c46f
    /*
     * The following three methods call liftDistance to raise/lower to the
     * correct value
     */
    public void goToGroundHeight();
<<<<<<< HEAD
=======
    
>>>>>>> dbf063a37b8d18e22be1d013e8945e43c570c46f
    public void goToSwitchHeight();
    public void goToScaleHeight();
    public void goToSwitchHeight(Consumer<IDrive> completionRoutine);
    public void goToScaleHeight(Consumer<IDrive> completionRoutine);
    public void goToGroundHeight(Consumer<IDrive> completionRoutine);
    
    /*
     * Moves the scissor lift to the correct height
     */
    public void goToCurrentHeight();

       
    /*
     * Called periodically to execute the actions set by the above methods
     */
    public void periodic();
    
}
