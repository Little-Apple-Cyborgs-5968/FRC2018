package org.usfirst.frc.team5968.robot;

public interface IFieldInformation {
	public FieldSide getAllianceSwitchSide();
    public FieldSide getScaleSide();
    public FieldSide getEnemySwitchSide();

    public AllianceColor getAlliance();
}
