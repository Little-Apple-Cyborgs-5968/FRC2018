package org.usfirst.frc.team5968.robot;

public class DisabledMode implements IRobotMode {
    
    private IGrabber grabber;
    private IFieldInformation fieldInformation;

    public DisabledMode(IGrabber grabber, IFieldInformation fieldInformation) {
        this.grabber = grabber;
        this.fieldInformation = fieldInformation;
    }

    @Override
    public void init() {
        grabber.grab();
    }

    @Override
    public void periodic() {
        fieldInformation.refresh();
    }

}
