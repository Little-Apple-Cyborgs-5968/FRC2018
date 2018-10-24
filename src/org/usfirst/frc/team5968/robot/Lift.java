package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import java.lang.Runnable;
import java.lang.IllegalStateException;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

public class Lift implements ILift {
    
    private DoubleSolenoid piston;
    private PistonState pistonState; 
    private Compressor compressor;
    
    private Runnable currentCompletionRoutine;

    public Lift() {
        compressor = new Compressor(PortMap.portOf(CAN.PCM));
        compressor.setClosedLoopControl(true);
        piston = new DoubleSolenoid(3, 2); // add channels later
        pistonState = PistonState.OPEN;
        
        init();
    }

    @Override
    public void init() {
        // Abort the current action
        currentCompletionRoutine = null;
    }
    

    private void setCompletionRoutine(Runnable completionRountime) {
        // If there's already a completion routine, fail because that means an action was interrupted, and we don't allow that.
        // Note that because we check this before seeing if a completion routine is being configured at all, we are saying that
        // even goTo*Height() without a completion routine must not be called until an action is completed.
        // The exception to this is if the action is explicitly aborted with abortCurrentAction.
        /*
        Other sensible things we could do here:
        A) Replace the completion routine - we're essentially canceling the action. (This might be confusing though - maybe print a warning.)
        B) Combine the completion routines:
        Runnable oldCompletionRoutine = currentCompletionRoutine; // Note: This may seem pointless, but it's actually very important! (Ask me to explain some other time.)
        Runnable combinedCompletionRoutine = () ->
        {
            oldCompletionRoutine.run();
            completionRoutine.run();
        };
        currentCompletionRoutine = combinedCompletionRoutine;
        */
        if (currentCompletionRoutine != null) {
            throw new IllegalStateException("Tried to perform a lift action while one was already in progress!");
        }

        currentCompletionRoutine = completionRountime;
    }

    @Override
    public void goToGroundHeight(Runnable completionRoutine) {
        setCompletionRoutine(completionRoutine);
        pistonState = PistonState.CLOSED;
    }

    @Override
    public void goToScaleHeight(Runnable completionRoutine) {
        setCompletionRoutine(completionRoutine);
        pistonState = PistonState.OPEN;
    }

    @Override
    public void goToGroundHeight() {
        goToGroundHeight(null);
    }

    @Override
    public void goToScaleHeight() {
        goToScaleHeight(null);
    }

    // Returns true if the lift should stop moving during this tick, false otherwise

    public void periodic() {
        // Stop moving if we've hit the limit switch in the direction we want to go
        if (pistonState == PistonState.OPEN) {
            piston.set(DoubleSolenoid.Value.kReverse);
        } else{
            piston.set(DoubleSolenoid.Value.kForward);
        }
    }
}
