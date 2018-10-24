package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

<<<<<<< HEAD
public class Grabber implements IGrabber {
=======
public class Grabber implements IGrabber{
>>>>>>> dbf063a37b8d18e22be1d013e8945e43c570c46f

    private DoubleSolenoid piston;
    private PistonState pistonState; 
    
    
    public Grabber (){
<<<<<<< HEAD
        piston = new DoubleSolenoid(0,0); // add ports later
        pistonState = PistonState.OPEN;
    }
    
    @Override
    public void grab() {      
        pistonState = PistonState.OPEN;
    }

    @Override
    public void release() {
        pistonState = PistonState.CLOSED;
=======
        DoubleSolenoid piston = new DoubleSolenoid(0,0); //add ports later
        pistonState = PistonState.OPEN;
    }
    
    private void grab() {      
        pistonState = PistonState.OPEN;
        
    }

    private void release() {
        pistonState = PistonState.CLOSED;
        
>>>>>>> dbf063a37b8d18e22be1d013e8945e43c570c46f
    }

    @Override
    public void toggleGrabbing() {
<<<<<<< HEAD
        if (pistonState == PistonState.OPEN) {
            release();
        } else {
            grab();
        }
=======
        if (pistonState == PistonState.OPEN){
            release();
        }else{
            grab();
        }
        
>>>>>>> dbf063a37b8d18e22be1d013e8945e43c570c46f
    }
    
    
    @Override
    public void periodic() {
<<<<<<< HEAD
        if (pistonState == PistonState.OPEN) {
            piston.set(DoubleSolenoid.Value.kReverse);
        } else{
            piston.set(DoubleSolenoid.Value.kForward);
        }
    }
    
=======
        if (pistonState == PistonState.OPEN){
            piston.set(DoubleSolenoid.Value.kReverse);
        }else{
            piston.set(DoubleSolenoid.Value.kForward);
        }
        
    }

>>>>>>> dbf063a37b8d18e22be1d013e8945e43c570c46f
}
