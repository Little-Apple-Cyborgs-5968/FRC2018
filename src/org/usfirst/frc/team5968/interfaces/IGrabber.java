package org.usfirst.frc.team5968.interfaces;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public interface IGrabber {
	/*
	 * Returns state of the piston (in -> true, out -> false)
	 */
	public boolean getPistonState(DoubleSolenoid piston);
	
	public void togglePiston(DoubleSolenoid piston);
	
	public void stopCompressor();
	
	/*
	 * Only toggle piston if pressure is not too low
	 */
	public boolean checkLowPressure();
}
