package org.usfirst.frc.team5968.robot;

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
    public void goToHeight(double heightInches);

    /*
     * Called periodically to execute the actions set by the above methods
     */
    public void periodic();
}
