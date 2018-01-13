package org.usfirst.frc.team5968.interfaces;

import org.usfirst.frc.team5968.robot.AutoMode;
import org.usfirst.frc.team5968.robot.AutoProgress;
import org.usfirst.frc.team5968.robot.StartingPoint;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

public interface IAutonomous {
	/*
	 * Outline for autonomous options:
	 * Left & Right starting positions:
	 *     - If only the switch is on the same side of the robot
	 *         - Switch
	 *     - If only the scale is on the same side of the robot
	 *         - Scale
	 *     - If both are on the same side of the robot
	 *         - Switch
	 *         - Scale
	 *     - If neither are on the same side of the robot
	 *         - Baseline
	 * Center starting position:
	 *     - Baseline
	 */
	
	/*
	 * Should be called in a fast loop (~20ms) to get data about 
	 * switch/scale sides. Returns a string of length 3 detailing
	 * the alliance sides, starting from the closest (own) switch.
	 * Ex. "LRL" "RRL" "RRR"
	 */
	public String pollGameData();
	
	/*
	 * Decides auto mode based on FMS game data, and makes the auto
	 * mode decision based on the flowchart above. The backup mode
	 * is executed if both the switch and scale are on the same side
	 */
	public AutoMode autoModeControl(String gameData, StartingPoint startingPoint, AutoMode backup);
	
	/*
	 * Execute autonomous choice returned from autoModeControl
	 */
	public void doAuto(StartingPoint startingPoint, AutoMode automode, Alliance alliance);
	
}
