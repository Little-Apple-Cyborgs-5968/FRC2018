package org.usfirst.frc.team5968.robot;

public interface IDashboard {
    /*
     * Get the selected autonomous mode (in the case that
     * both the switch and scale are on the same side as the robot.)
     */
    public AutoMode chooseModeforBoth();

    /*
     * Get the starting position for the robot.
     */
    public FieldPosition getMatchStartingPoint();
}