package org.usfirst.frc.team5968.robot;

import com.kauailabs.navx.frc.AHRS;

public class NavXMXP implements IGyroscopeSensor {

    private AHRS navX;
    
    public NavXMXP(AHRS navX) {
        navX = this.navX;
    }
    
    @Override
    public double getPitch() {
        return navX.getPitch();
    }

    @Override
    public double getRoll() {
        return navX.getRoll();
    }

    @Override
    public double getYaw() {
        return navX.getYaw();
    }

    @Override
    public void resetYaw() {
        navX.reset();
    }

}
