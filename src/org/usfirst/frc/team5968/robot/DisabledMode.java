package org.usfirst.frc.team5968.robot;

public class DisabledMode implements IRobotMode {
    
    private IGrabber grabber;

    public DisabledMode(IGrabber grabber) {
        this.grabber = grabber;
    }

    @Override
    public void init() {
        grabber.grab();
    }

    @Override
    public void periodic() {
    }

}
