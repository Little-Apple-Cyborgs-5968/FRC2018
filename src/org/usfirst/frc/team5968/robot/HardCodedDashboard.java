package org.usfirst.frc.team5968.robot;

public class HardCodedDashboard implements IDashboard {
    
    public AutoMode chooseModeforBoth() {
        return AutoMode.SWITCH;
    }
    
    public FieldPosition getMatchStartingPoint() {
        return FieldPosition.RIGHT;
    }
}
