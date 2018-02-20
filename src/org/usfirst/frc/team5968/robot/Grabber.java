package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Grabber implements IGrabber {

    private DoubleSolenoid piston;
    private PistonState pistonState; 
    
    
    public Grabber (){
        piston = new DoubleSolenoid(0,1); // add channels later
        pistonState = PistonState.OPEN;
    }
    
    @Override
    public void grab() {      
        pistonState = PistonState.OPEN;
    }

    @Override
    public void release() {
        pistonState = PistonState.CLOSED;
    }

    @Override
    public void toggleGrabbing() {
        if (pistonState == PistonState.OPEN) {
            release();
        } else {
            grab();
        }
    }
    
    
    @Override
    public void periodic() {
        if (pistonState == PistonState.OPEN) {
            piston.set(DoubleSolenoid.Value.kReverse);
        } else{
            piston.set(DoubleSolenoid.Value.kForward);
        }
    }
    
}
