package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DigitalInput;

import java.lang.Runnable;
import java.lang.IllegalStateException;

import org.usfirst.frc.team5968.robot.PortMap.CAN;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift implements ILift {

    private LiftHeight desiredHeight;
    private LiftHeight currentHeight;
    
    private DigitalInput groundLimit;
    private DigitalInput topLimit;
    
    private TalonSRX liftMotor;
    
    private double liftSpeed = 0.9;

    private double moveDirection = 0.0;
    private static final double moveUp = 1.0;
    private static final double moveDown = -1.0 / 9.0; // When moving down, we want to go slow to avoid crashing into the ground.

    private Runnable currentCompletionRoutine;

    public Lift() {
        groundLimit = new DigitalInput(8);
        topLimit = new DigitalInput(9);
        liftMotor = new TalonSRX(PortMap.portOf(CAN.LIFT_MOTOR_CONTROLLER));

        init();
    }

    @Override
    public void init() {
        // Abort the current action
        currentCompletionRoutine = null;
        moveDirection = 0.0;
    }
    
    @Override
    public void setLiftSpeed(double speed) {
        liftSpeed = Math.abs(speed); // Don't allow negative speeds since this kills the lift.
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
        moveDirection = moveDown;
    }

    @Override
    public void goToScaleHeight(Runnable completionRoutine) {
        setCompletionRoutine(completionRoutine);
        moveDirection = moveUp;
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
    private boolean shouldStopMoving() {
        boolean topHit = !topLimit.get();
        boolean groundHit = !groundLimit.get();
        
        // Debug.logPeriodic("Top hit: " + topHit + ", Ground hit: " + groundHit + ", Move direction " + moveDirection);
        // If we are moving up, but we've hit the top:
        if (moveDirection > 0.0 && topHit) {
            return true;
        }

        // If we are moving down, but we've hit the bottom:
        if (moveDirection < 0.0 && groundHit) {
            return true;
        }

        // Otherwise, we are somewhere in the middle and can continue moving
        return false;
    }

    public void periodic() {
        // Stop moving if we've hit the limit switch in the direction we want to go
        if (shouldStopMoving()) {
            liftMotor.set(ControlMode.PercentOutput, 0.0);

            // Since we just completed and action, dispatch the current completion routine if we have one
            if (currentCompletionRoutine != null) {
                Runnable oldCompletionRoutine = currentCompletionRoutine;
                currentCompletionRoutine = null;
                oldCompletionRoutine.run();
            }
        } else { // Otherwise, keep moving in the appropriate direction
            liftMotor.set(ControlMode.PercentOutput, moveDirection * liftSpeed);
        }
    }
}