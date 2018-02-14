package org.usfirst.frc.team5968.robot;

public interface IRobotMode {

    /*
     * Initializes the current robot mode
     */
    public void init();

    /*
     * Called periodically to execute actions for the current mode
     */
    public void periodic();
    
}
