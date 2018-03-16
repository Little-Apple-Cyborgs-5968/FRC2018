package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard implements IDashboard {
    
    private SendableChooser<AutoMode> autoChoices;
    private SendableChooser<FieldPosition> fieldPositions;
    
    public Dashboard() {
        autoChoices = new SendableChooser<>();
        autoChoices.addDefault("Baseline Mode", AutoMode.LINE);
        autoChoices.addObject("Switch Mode", AutoMode.SWITCH);
        autoChoices.addObject("Scale Mode", AutoMode.SCALE);
        SmartDashboard.putData("Autonomous modes", autoChoices);
        
        fieldPositions = new SendableChooser<>();
        fieldPositions.addDefault("Center", FieldPosition.CENTER);
        fieldPositions.addObject("Left", FieldPosition.LEFT);
        fieldPositions.addObject("Right", FieldPosition.RIGHT);
        SmartDashboard.putData("Field Position", fieldPositions);
        
        
    }
    
	public AutoMode chooseModeforBoth() {	    
		return autoChoices.getSelected();
	}
	
	public FieldPosition getMatchStartingPoint() {
	    return fieldPositions.getSelected();
	}
	
}
