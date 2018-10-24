package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.Encoder;

public class RoboRioEncoder implements IEncoder {
    private boolean isInverted = false;
    private Encoder encoder;
    public RoboRioEncoder(int portA, int portB) {
        encoder = new Encoder(portA, portB);
    }
    
    @Override
    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }
    
    @Override
    public void setDistancePerPulse(double distance) {
        encoder.setDistancePerPulse(distance);
    }
    
    @Override
    public double getDistance() {
        double ret = encoder.getDistance();
        if (isInverted) {
        ret = -ret;
    }
    return ret;
    }
    
    @Override
    public void reset() {
        encoder.reset();
    }
}
