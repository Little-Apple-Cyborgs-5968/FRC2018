<!--
This is the Markdown source of this document. You probably want to view the PDF (which I should've sent to you) instead unless you intend to edit the document.
It is intended to be rendered using Visual Studio Code, you probably want the following extensions installed:
* AlanWalk.markdown-toc
* bierner.markdown-emoji
* yzane.markdown-pdf
-->

This document contains notes for things that you should either fix or carefully evaluate before competition.

**The first section you should read is `Lift`. I want you to read through the changes I suggested and make sure you feel like you understand them well enough to fix the `completionRoutine` stuff in the `IDrive` implementation.**

# Table of Contents

<!-- TOC -->

- [Table of Contents](#table-of-contents)
- [`Dashboard` (and `IDashboard`)](#dashboard-and-idashboard)
- [`IFieldInformation`](#ifieldinformation)
- [`Grabber`](#grabber)
- [`Lift` (and `ILift` / `LiftHeight`)](#lift-and-ilift--liftheight)
- [`Robot`](#robot)
    - [`LiveWindow.setEnabled(isTest());`](#livewindowsetenabledistest)
- [The completion routine pattern is not being implemented correctly](#the-completion-routine-pattern-is-not-being-implemented-correctly)
    - [Peripherals requiring initialization](#peripherals-requiring-initialization)
- [The missing `Debug` helper class](#the-missing-debug-helper-class)
- [`TeleoperatedMode`](#teleoperatedmode)
    - [`periodic`](#periodic)

<!-- /TOC -->

# `Dashboard` (and `IDashboard`)

I consider this part of autonomous, see autonomous notes.

# `IFieldInformation`

This interface is never implemented. I consider this part of autonomous, see autonomous notes.

# `Grabber`

The compressor needs to be put into closed loop control mode somewhere. Since nothing else uses pnematics and the simplest solution would be to put it in `Grabber`.

1. Add a `private Compressor compressor;` field to `Grabber.
2. Instantiate it in the constructor. (Ideally, confirm the CAN bus ID of the PCM. `PortMap` has an entry for it, but no ID value. Default is 0.)
3. Enable closed-loop control: `compressor.setClosedLoopControl(true);`

# `Lift` (and `ILift` / `LiftHeight`)

This poor class has been through a lot as the capabilities of the robot's lift has changed. Unfortunately this has led to a lot of dead code and some unecessary complexity.

Additionally, the implementation is bugged right now, the lift can't move down. (The logic is there, but `setCurrentHeight` defaults to reporting that the current height is `GROUND` so when no switches are pressed, it thinks its at the ground. Also the implementation never stops powering the motor, so it's going to spazz around a lot.

I've modified `Lift.java` and `ILift.java` with the design I'd propose. (Sorry for the cop-out "Here, do this", but time is a bit short, yeah? Make sure you understand how and why I did things though.)

I've also fixed the completion routine stuff in my version since it wasn't done correctly.

`LiftPosition` should be removed as it is no longer relevant in this design.

(Also side reminder that I don't have my development environment set up to compile robotics code. So my apologies if there's some compilation errors. It's actually almost certainly not going to compile since some stuff that uses `Lift` is gonna be mad.)

**IMPORTANT**: Note the presence of `ILift.init` this **must** be called by `Robot.startCompetition` (in the `IRobotMode.init` logic).

This method cancels the current action be clearing the current completion routine. This is very important to avoid an fatal edge case where the robot is switched into a different mode while an action is awaiting completion. At best, it causes invalid actions to start firing (for instance: it could cause autonomous code to run during periodic mode) and at worst it causes a crash (due to the `IllegalStateException` intentionally thrown in `Lift.setCompletionRoutine.)

# `Robot`

## `LiveWindow.setEnabled(isTest());`

This line should instead be performed prior to initalizing a new mode in the main loop. IE:

```java
if (desiredMode != currentMode) {
    LiveWindow.setEnabled(isTest());
	desiredMode.init();
	currentMode = desiredMode;
}
```

I don't think this will cause issues at competition, but it is not what WPIlib expects.

# The completion routine pattern is not being implemented correctly

The completion routine pattern is not being implemented correctly. (Although at a glance it is being used correctly, so you should be good there.)

Right now the completion rountines are being dispatched immediately. This is incorrect behavior. Their execution is meant to be deferred until the requested operations completes.

See my modifications to `Lift.java` for guidance on proper implementation of this pattern.

Note that I switched from `Consumer<IDrive>` to `Runnable`. My original proposal for the completion rountimes assumed that only `IDrive` would feature them. Since you're extending them to `Lift`, it's better to use something more generic and use captured variables. Captured variables are slightly magic, and you might not notice them, so for the sake of bevity I'm not going to try to explaion them here.

**Let me know if you need help porting these changes to the `IDrive` implementation!**

**Make sure you add the peripheral initialization as noted below!**

## Peripherals requiring initialization

`Drive` and `Lift` have `init` methods, but they aren't always called when they should be.

Both should be called by by `Robot` prior to calling `desiredMode.init();`

Consider adding a `doPeripheralReinitialization()` method for conistency with `doPeripheralPeriodicProcessing`.

# The missing `Debug` helper class

A static class `Debug` should be added to make it easier to print statements in periodic methods.

Right now, this pattern is being used to prevent flooding the driver station with log packets:
```java
int i = 0;
public void periodic() {
    if (i % 5000 == 0) {
        System.out.println("Periodic!");
    }
    i++;
}
```

This works, but it has a few issues:
* It's a little verbose to implement every time.
* The print rate is tied to the speed the code is executing at. (IE: If there's a lot going on, log messages will come out slower.)

We should have a utility class for fixing both of these issues. Something like this:

```java
public final class Debug {
    // Don't allow instantiation
    private Debug() {
    }

    private static boolean isPeriodicLogTick = true;

    private static final long logPeriod = 500; // ms
    private static long lastLogTime = 0;

    // Call this method during the update loop in Robot.startCompetition to update the periodic log timer
    public static void periodic() {
        long currentTime = System.currentTimeMillis();

        // If the current time is before the last log tick time, we set the last log tick time to the current time
        // (This prevents issues with the system clock changing while we run.)
        if (currentTime < lastLogTime) {
            lastLogTime = currentTime;
        }

        // Compute the time that has elapsed since the last log tick
        long deltaTime = currentTime - lastLogTime;

        // If less than logPeriod ms have passed since the last log tick, this isn't a log tick
        if (deltaTime < logPeriod) {
            isPeriodicLogTick = false;
        } else { // Otherwise, this is a log tick so we record as such        
            isPeriodicLogTick = true;
            lastLogTime = currentTime;
        }
    }

    public static void log(String message) {
        System.out.println(message);
    }

    public static void logPeriodic(String message) {
        if (isPeriodicLogTick) {
            Log(message);
        }
    }
}
```

Then, in places like `init` methods, you can use `Debug.log(...)` and in places like `periodic` you use `Debug.logPeriodic(...)`

Make sure you add a call to `Debug.periodic` in the `Robot.startCompetition` main loop!

# `TeleoperatedMode`

In general, I did a tiny bit of cleanup to this file as I reviewed it. I also re-enabled the lift-related code.

## `periodic`

`liftButtonPressed` this field and the way it was being used is unecessary. I removed it.

There is a conflict between the button for toggling the grabber and setting the lift speed. (Left button 5 is used for both.)

The lift controls should probably be changed now since the "switch" level is gone now. I'd suggest changing button 6 to button 5.
