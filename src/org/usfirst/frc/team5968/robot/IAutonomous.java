package org.usfirst.frc.team5968.robot;

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
     * Center starting position: 
     * - Baseline
     */


    /*
    * Creates instance of correct autonomous mode
    */
    public void doAuto(StartingPoint startingPoint);
    
}