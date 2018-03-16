package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Grabber implements IGrabber {

    private DoubleSolenoid piston;
    private PistonState pistonState; 
    private Compressor compressor;
    
    
    public Grabber (){
        compressor = new Compressor(PortMap.portOf(CAN.PCM));
        compressor.setClosedLoopControl(true);
        piston = new DoubleSolenoid(3, 2); // add channels later
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
