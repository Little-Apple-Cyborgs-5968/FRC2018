package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Dashboard implements IDashboard {
    
    private SendableChooser<AutoMode> autoChoices;
    
    public Dashboard() {
        autoChoices = new SendableChooser<>();
        autoChoices.addDefault("Baseline Mode", AutoMode.LINE);
        autoChoices.addObject("Switch Mode", AutoMode.SWITCH);
        autoChoices.addObject("Scale Mode", AutoMode.SCALE);
    }
    
	public AutoMode chooseModeforBoth() {	    
		return autoChoices.getSelected();
	}
	
}
