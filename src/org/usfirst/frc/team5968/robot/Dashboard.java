package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard implements IDashboard{
    
    public Dashboard() {
        // Add SmartDashboard SendableChooser options
    }
    
	public AutoMode chooseModeforBOTH() {
		if (//GET DASHBOARD CHOICE ==AutoMode.SWITCH) {
			return AutoMode.SWITCH;
		} else if (//GET DASHBOARD CHOICE ==AutoMode.SCALE) {
			return AutoMode.SCALE;
		} else {
			return AutoMode.LINE;
		}
	}
}
