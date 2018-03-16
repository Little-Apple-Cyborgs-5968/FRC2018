package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation;

class FieldInformation implements IFieldInformation {
    private FieldPosition initialRobotPosition;
    private FieldPosition nearSwitchPosition;
    private FieldPosition scalePosition;
    private FieldPosition farSwitchPosition;
    private boolean isDataValid;
    
    public FieldInformation() {
        refresh();
    }
    
    public void refresh() {
        // Invalidate the field information
        isDataValid = false;
        initialRobotPosition = FieldPosition.INVALID;
        nearSwitchPosition = FieldPosition.INVALID;
        scalePosition = FieldPosition.INVALID;
        farSwitchPosition = FieldPosition.INVALID;
        // Try to get new field information from the field management system
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        // Don't process the message if it is invalid
        if (gameData == null || gameData.length() != 3) {
            return;
        }
        
        nearSwitchPosition = messageCharacterToFieldSide(gameData.charAt(0));
        scalePosition = messageCharacterToFieldSide(gameData.charAt(1));
        farSwitchPosition = messageCharacterToFieldSide(gameData.charAt(2));
        // Data is only valid if all positions contained valid characters
        isDataValid = isValidFieldElementSide(nearSwitchPosition) &&
                      isValidFieldElementSide(scalePosition) &&
                      isValidFieldElementSide(farSwitchPosition);
        
        // You might consider forcing all field element positions to be
        // FieldSide.INVALID if isDataValid == false.
        // That way if in the weird edge case that only one is invalid, they
        // all read as invalid to anyone who failed to check isDataValid.
    }
    
    private static FieldPosition messageCharacterToFieldSide(char messageCharacter) {
        switch (messageCharacter) {
            case 'L': 
                return FieldPosition.LEFT;
            case 'R': 
                return FieldPosition.RIGHT;
            default: 
                return FieldPosition.INVALID;
        }
    }
    
    private static boolean isValidFieldElementSide(FieldPosition side) {
        switch (side) {
            case LEFT:
            case RIGHT:
                return true;
            default:
                return false;
        }
    }
        
    @Override
    public FieldPosition getNearSwitchPosition() {
        return nearSwitchPosition;
    }
    
    @Override
    public FieldPosition getScalePosition() {
        return scalePosition;
    }
    
    @Override
    public FieldPosition getFarSwitchPosition() {
        return farSwitchPosition;
    }
    
    @Override
    public boolean getIsDataValid() {
        return isDataValid;
    }
}