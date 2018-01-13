package org.usfirst.frc.team5968.interfaces;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;

public interface IPneumatics {
	/*
	 * Initializes compressor, other pneumatics stuff
	 */
	public void init();
	
	/*
	 * Returns state of the piston (in -> true, out -> false)
	 */
	public boolean getPistonState(DoubleSolenoid piston);
	
	public void togglePiston(DoubleSolenoid piston);
	
	public void stopCompressor();
}
