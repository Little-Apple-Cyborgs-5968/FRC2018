package org.usfirst.frc.team5968.robot;

public interface IGrabber {
    /*
     * Returns state of the piston (in -> true, out -> false)
     */
    

   

   

    public void toggleGrabbing();
    
    /*
     * Called periodically to execute the actions stated above
     */
    public void periodic();
    
    public void grab();
    public void release();

}
