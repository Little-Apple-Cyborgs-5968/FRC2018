package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public interface IAutonomous {
    /*
     * Outline for autonomous options: 
     * 
     * Left & Right starting positions: 
     * - If only the switch is on the same side of the robot 
     *      - Switch 
     * - If only the scale is on the same side of the robot 
     *      - Scale
     *- If both are on the same side of the robot 
     *      - Switch 
     *      - Scale 
     *- If neither are on the same side of the robot 
     *      - Baseline 
     *
     *Center starting position: 
     *- Baseline
     */

    /*
     * Chooses the auto mode to execute.
     * Goes into specific mode class.
     * Sets states.
     */
    public void doAuto(StartingPoint startingPoint);
    
}