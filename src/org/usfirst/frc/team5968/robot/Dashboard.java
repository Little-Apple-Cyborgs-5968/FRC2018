package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Dashboard implements IDashboard {
    
    private SendableChooser<AutoMode> autoChoices;
    private SendableChooser<FieldSide> startingPoints;
    
    public Dashboard() {
        autoChoices = new SendableChooser<>();
        autoChoices.addDefault("Baseline Mode", AutoMode.LINE);
        autoChoices.addObject("Switch Mode", AutoMode.SWITCH);
        autoChoices.addObject("Scale Mode", AutoMode.SCALE);

        startingPoints = new SendableChooser<>();
        startingPoints.addDefault("Center", FieldSide.CENTER);
        startingPoints.addObject("Left", FieldSide.LEFT);
        startingPoints.addObject("Right", FieldSide.RIGHT);
    }
    
	public AutoMode chooseModeforBoth() {	    
		return autoChoices.getSelected();
	}
	
	public FieldSide getMatchStartingPoint() {
	    return startingPoints.getSelected();
	}
	
}
