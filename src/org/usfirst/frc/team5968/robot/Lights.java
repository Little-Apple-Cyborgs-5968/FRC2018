package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Lights {

    private DigitalOutput white = new DigitalOutput(0);
    private DigitalOutput blue = new DigitalOutput(1);
    private DigitalOutput green = new DigitalOutput(2);
    private DigitalOutput red = new DigitalOutput(3);
    
    
    public void white() {
        white.set(true);
        blue.set(false);
        green.set(false);
        red.set(false);
        
    }
    
    public void blue() {
        white.set(false);
        blue.set(true);
        green.set(false);
        red.set(false);
        
    }
    
    public void green() {
        white.set(false);
        blue.set(false);
        green.set(true);
        red.set(false);
        
    }
    
    public void red() {
        white.set(false);
        blue.set(false);
        green.set(false);
        red.set(true);
        
    }
    
    public void turquoise() {
        white.set(false);
        blue.set(true);
        green.set(true);
        red.set(false);
        
    }
    
   public void purple() {
       white.set(false);
       blue.set(true);
       green.set(false);
       red.set(true);
       
   }
   
   public void yellow() {
       white.set(false);
       blue.set(false);
       green.set(true);
       red.set(true);
       
   }
   
   public void off() {
       white.set(false);
       blue.set(false);
       green.set(false);
       red.set(false);
       
   }
   
   public void allianceColor() {
       if (DriverStation.getInstance().getAlliance() == Alliance.Red) {
           red();
       } else if (DriverStation.getInstance().getAlliance() == Alliance.Blue) {
           blue();
       } else {
           white();
       }
       
   }
   
   public void scissorLiftFlashing() {
       if (Lift.moving() == true) { 
           turquoise(); 
       } else {
           white();
       }
   }
}
