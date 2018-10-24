package org.usfirst.frc.team5968.robot;

public interface IFieldInformation {
    
    // Returns the position of the side owned by us for a field element, or
    // INVALID if the data is unavailable.
    public FieldPosition getNearSwitchPosition();
    public FieldPosition getScalePosition();
    public FieldPosition getFarSwitchPosition();
    
    // Returns true if field information is valid and available. False
    // otherwise.
    // This is necessary because the value of the game-specific data is
    // indetermininate at the start.
    public boolean getIsDataValid();
    
    // Polls new data from the field management system, updating the positions
    // of the field elements.
    public void refresh();
}
