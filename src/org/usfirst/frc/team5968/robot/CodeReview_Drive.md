<!--
This is the Markdown source of this document. You probably want to view the PDF (which I should've sent to you) instead unless you intend to edit the document.
It is intended to be rendered using Visual Studio Code, you probably want the following extensions installed:
* AlanWalk.markdown-toc
* bierner.markdown-emoji
* yzane.markdown-pdf
-->

This document contains notes for things that you should either fix or carefully evaluate before competition as they relate to the `IDrive` interface and `Drive` implementation.

**It is advised that you read the notes about implementing the `completionRoutine` functionality in `CodeReview_General.md` before working on `Drive`.**

*Psst* I cleaned up `Drive.java` for my own benefit while I was writing this document. You could use my cleaned up version as a starting point. Double-check the diff though and make sure I didn't remove anything that mattered unintentionally. Also I got a little lazy on the notes here becuase of that. Sometimes it's easier to show than to describe.

Even if you don't want to "cheat" I recommend at least looking at the changes I made to `Drive.java`. I tried to make sure I marked things I *didn't* do with a :sleeping:, but you should double-check them just in case.

# Table of Contents

<!-- TOC -->

- [Table of Contents](#table-of-contents)
- [Gyroscope Functionality](#gyroscope-functionality)
    - [:sleeping: `navX`](#sleeping-navx)
    - [:sleeping: Dummy `IGyroscopeSensor` implementation](#sleeping-dummy-igyroscopesensor-implementation)
- [Remove PID Functionality](#remove-pid-functionality)
- [`targetRotations`](#targetrotations)
- [Constructor](#constructor)
- [`init`](#init)
- [`driveDistance`](#drivedistance)
- [`driveDistance(double, double, Consumer<IDrive>)`](#drivedistancedouble-double-consumeridrive)
- [:sleeping: `driveManual`](#sleeping-drivemanual)
- [The missing `stop` method](#the-missing-stop-method)
- [`setMotors`](#setmotors)
- [`periodic`](#periodic)
    - [`DRIVINGSTRAIGHT` handling](#drivingstraight-handling)
    - [`ROTATING` handling](#rotating-handling)
    - [<*Invalid State*> handling](#invalid-state-handling)
- [:sleeping: `completionRoutine` implementation](#sleeping-completionroutine-implementation)
- [:sleeping: Encoders](#sleeping-encoders)
    - [`IEncoder`](#iencoder)
    - [`RoboRioEncoder`](#roborioencoder)
    - [`TalonEncoder``](#talonencoder)

<!-- /TOC -->

# Gyroscope Functionality

## :sleeping: `navX`

* The NavXMXP reference should instead be `IGyroscopeSensor`.
* The reference should be obtained via the constructor.
* Ergo, `Robot` should initialize `NavXMXP`.
* The field should simply be called `gyro` or something similar since this class doesn't actually care that a NavX is used to implement it.

## :sleeping: Dummy `IGyroscopeSensor` implementation

Additionally, you should create a dummy `IGyroscopeSensor` implementation instead of selectively commenting out the `navX`-related code like that. It's not ideal, but it's less dangerous crashing-wise when you need to temporarily disable something. A dummy implementation would look something like this:

```java
public class NullGyroscopeSensor implements IGyroscopeSensor {
    @Override
    public double getPitch() {
        return 0.0;
    }

    // etc...

    @Override
    public void resetYaw() {
        // Nothing to do
    }
}
```

This isn't perfect since the robot will think it isn't turning and will spin infinitely if you ask it to rotate, but it's a bit better than a `NullReferenceException`

Alternatively, you should make it so anything that requires a gyro is disabled when the `navX` field is null. Although this is more work.

You could also add a special case that makes public rotation functions do nothing if the `navX` field is of type `NullGyroscopSensor`. For example:

```java
@Override
public void rotateDegrees(double angle, double speed) {
    if (navX instanceof NullGyroscopeSensor) {
        return;
    }
    controlMode = ControlMode.PercentOutput;
    navX.resetYaw();
    driveMode = DriveMode.ROTATING;
    leftMotorSpeed = 0.2;
    rightMotorSpeed = -0.2;
    angleToRotate = angle;
}
```

If you're worried about NavX failures at competition (although it'd be nice to figure out for sure why it failed that one time in the first place), you could do something like this:

```java
try {
    navX = new NavXMXP(/* ... */);
} catch {
    Debug.log("WARNING: NavXMXP initialization failed! Gyro-related features are disabled.");
    navX = new NullGyroscopeSensor();
}
```

# Remove PID Functionality

As per our discussion, I am going to recommend you remove everything relating to PID. It's too complicated of a feature with too little benefit to worry about without having proper time to test.

This involves removing:
* Calls to `configSelectedFeedbackSensor`
* Calls to `setSelectedSensorPosition`
* `initAutoPID` and all calls to it.
* Everything relating to `controlMode`. (This wasn't a great idea for various reasons anyway.)
* `driveStraight` and all calls to it.
* `PIDIDX`
* `TIMEOUT`
* `SENSORPOSITION`

If you decide to hail mary and try to get PID working, I have some concerns about how it is being used that I didn't look into enough to decide if they were valid concerns.

# `targetRotations`

Remove this, it is redundanty to `distanceInches`. (We also won't need it later.)

# Constructor

The constructor should call `init` at the very end in order to ensure the state of the peripheral is consistent.

(You can also remove the assignments to state like `controlMode` and `driveMode` too.)

Call `setInverted(true)` on the `leftMotorController` to invert it instead of inverting it manually everywhere. (Positive should be forward for both sides. For posterity: The right motor was manually inverted everywhere, but both motors were inverted again later in `setMotors`. Therefore the left motor should be the inverted one.)

NOTE: It is unclear whether you need to call `setInverted` on both or just one of the motors. I'm thinking it doesn't matter at all for the following controller, but keep this in mind as something that could go wrong.

# `init`

If you didn't remove them already, the calls to `setSelectedSensorPosition` are not appropriate here. The encoders should be reset in `driveDistance`

I removed a lot of unecessary initialization from here in general in my verson. It's not super important though.

# `driveDistance`

The encoders should be reset by this method.

# `driveDistance(double, double, Consumer<IDrive>)`

`driveMode` should not be set here, it is set by the subordinate method.

# :sleeping: `driveManual`

When you implement the proper `completionRoutine` pattern here. Add a call to `setCompletionRountime(null)` here even though `driveManual` doesn't (and shouldn't) have a `completionRoutine` overload.
This is done to ensure that this method does not interrupt an action that has a `completionRoutine` since `setCompletionRoutine` enforces that actions can't be interrupted.

Remove the manual negation of `rightMotorSpeed` since this is done in the constructor now.

# The missing `stop` method

I'd suggest adding a shortcut method for stopping:

```java
public void stop() {
    driveManual(0.0, 0.0);
}
```

# `setMotors`

Not sure what the original intent was, but the entire first branch of the if statement and the `driveMode` parameter should be removed as fields relating to it have been removed.

Ambiguous math should be avoided (mixing `double`s and `int`s) change `leftMotorDirection` and `rightMotorDirection` to `double. (99% sure it doesn't matter here, but this is a good habit.)

Remove the manual negation here.

# `periodic`

This method has some roughness in general.

## `DRIVINGSTRAIGHT` handling

Needs end condition to reset drive mode and dispatch completion routine.

Not sure what's going on with that `distance` bool. Fried brain?

## `ROTATING` handling

I think the motor directions were swapped.

It'd be cleaner if you computed the delta once.

Needs end condition to reset drive mode and dispatch completion routine.

## <*Invalid State*> handling

Instead of an empty `else` clause, you should throw an `InvalidStateException`.

# :sleeping: `completionRoutine` implementation

Don't forget to fix up the `completionRoutine` stuff based on the changes I made to `Lift`.

In particular, don't forget to invert it so that the non-`completionRoutine` variants call the `completionRoutine` variants. (As opposted to the way it is right now, which is the opposite.)

# :sleeping: Encoders

As I mentioned on Discord, I have some latency concerns about accessing the encoders through the Talons.

To mitigate this risk, you should abstract the encoders so swapping the implementation is easy. This shouldn't be too hard. Since the CTRE documentation leaves a lot to be desired, I'm basically just going to hand this one to you since the skill of navigating their API isn't particularly valuable.

**Make sure to check that `WHEEL_DIAMETER` is correct in `Drive`!**

## `IEncoder`

`Drive` should have two of these. One for the left encoder, one for the right encoder.

The same motor set that is inverted should also have an inverted encoder. (IE: Encoders should increase in value as the robot moves forward.)

(I've added a `Debug.logPeriodic` call to `periodic` to test the encoder directions. We should plan on confirming it before testing autonomous and then removing it.)

```java
public interface IEncoder {
    public void setInverted(boolean inverted);
    public void setDistancePerPulse(double distance);
    public double getDistance();
    public void reset();
}
```

## `RoboRioEncoder`

This is a super basic `IEncoder` implementation that works with the an encoder attached directly to the roboRIO:

```java
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
```

## `TalonEncoder``

This is a `IEncoder` implementation that uses the encoders through the Talons. This is the implementation you should use initially. If we test it and it doesn't perform as well as we'd like, we switch to `RoboRioEncoder` after making the applicable hardware modications.

```java
public class TalonEncoder implements IEncoder {
    private boolean isInverted = false;
    private TalonSRX talon;
    private double distancePerPulse = 1.0;

    public TalonEncoder(TalonSRX talon) {
        this.talon = talon;
    }

    @Override
    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }

    @Override
    public void setDistancePerPulse(double distance) {
        distancePerPulse = distance;
    }

    @Override
    public double getDistance() {
        int rawEncoderValue = talon.getSensorCollection().getQuadraturePosition();

        if (isInverted) {
            rawEncoderValue = -rawEncoderValue;
        }

        // Conver the raw reading to distance and return it
        return ((double)rawEncoderValue) * distancePerPulse;
    }

    @Override
    public void reset() {
        ErrorCode error = talon.getSensorCollection().setQuadraturePosition(0, 500);

        if (error != ErrorCode.OK) {
            // Bleh, overly generic exception.
            // If we were feeling fancy, we'd determine an ideal exception based on the error code.
            throw new RuntimeException("Failed to reset Talon encoder! Error code = " + error.toString());
        }
    }
}
```
