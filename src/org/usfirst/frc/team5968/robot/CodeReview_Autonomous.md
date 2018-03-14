<!--
This is the Markdown source of this document. You probably want to view the PDF (which I should've sent to you) instead unless you intend to edit the document.
It is intended to be rendered using Visual Studio Code, you probably want the following extensions installed:
* AlanWalk.markdown-toc
* bierner.markdown-emoji
* yzane.markdown-pdf
-->

This document contains notes for autonomous-related things that you should either fix or carefully evaluate before competition.

This document is different from the others in that it is ordered in the order which you should make the changes.

I recommend following the order here for less pain.

:purple_heart: means I did this for you.

Also I am extra doubleplus assuming you're going to do everything here. There's some bugs in `Autonomous` that are going to be eliminated by pure virture of it getting reworked so much.

# Table of Contents

<!-- TOC -->

- [Table of Contents](#table-of-contents)
- [Field information changes](#field-information-changes)
    - [Add `INVALID` to `FieldSide`](#add-invalid-to-fieldside)
    - [Replace `IFieldInformation`](#replace-ifieldinformation)
    - [Implement `IFieldInformation`](#implement-ifieldinformation)
    - [Instantiate `FieldInformation`](#instantiate-fieldinformation)
    - [Add updating the field information to `DisabledMode`](#add-updating-the-field-information-to-disabledmode)
- [Changes to autonomous submodes](#changes-to-autonomous-submodes)
    - [:purple_heart: Make autonomous submodes implement `IRobotMode`](#purple_heart-make-autonomous-submodes-implement-irobotmode)
    - [:purple_heart: Fix `Consumer<IDrive>` -> `Runnable`-related changes](#purple_heart-fix-consumeridrive---runnable-related-changes)
- [Merge `Autonomous` into `AutonomousMode`](#merge-autonomous-into-autonomousmode)
    - [Remove `BOTH` from `AutoMode`](#remove-both-from-automode)
    - [:purple_heart: Add dashboard reference to `AutonomousMode`](#purple_heart-add-dashboard-reference-to-autonomousmode)
    - [Pass `IFieldInformation` to `AutonomousMode` in `Robot`](#pass-ifieldinformation-to-autonomousmode-in-robot)
    - [:purple_heart: Update `determineAutoMode` (formerly `autoModeControl`)](#purple_heart-update-determineautomode-formerly-automodecontrol)
    - [:purple_heart: Rework `getAutoModeImplementation` to return the appropriate `IRobotMode` implementation](#purple_heart-rework-getautomodeimplementation-to-return-the-appropriate-irobotmode-implementation)
    - [:purple_heart: Implement `init` to determine and initialize mode](#purple_heart-implement-init-to-determine-and-initialize-mode)
    - [:purple_heart: Implement `periodic` to periodically update mode](#purple_heart-implement-periodic-to-periodically-update-mode)
    - [Finally, delete `Autonomous` and `IAutonomous`](#finally-delete-autonomous-and-iautonomous)
- [Dashboard fixes](#dashboard-fixes)
    - [`StartingPoint` is redundant to `FieldSide`, remove it.](#startingpoint-is-redundant-to-fieldside-remove-it)
    - [:purple_heart: Add `getMatchStartingPoint` to the `IDashboard` interface.](#purple_heart-add-getmatchstartingpoint-to-the-idashboard-interface)
    - [Add sendables to Smart Dashboard](#add-sendables-to-smart-dashboard)
    - [Prepare for Smart Dashboard issues](#prepare-for-smart-dashboard-issues)
- [`ScaleAuto.goStraightSort`](#scaleautogostraightsort)
- [`SwitchAuto.liftGrabber`](#switchautoliftgrabber)
- [Low priority: Driver autonomous mode override](#low-priority-driver-autonomous-mode-override)

<!-- /TOC -->

# Field information changes

The way field information is gotten right now is not entirely correct, is not robust, and does not reflect [the guidelines provided by WPIlib](http://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details).

Additional benefits of these changes are that you can shuttle off all of the validation code into its own little box and not worry about it when you actually need this data. It will also improve the readability of the code that processes the field information.

## Add `INVALID` to `FieldSide`

This will be needed later.

Also, consider renaming it to `FieldPosition` or `FieldLane` since "center" isn't a side.

## Replace `IFieldInformation`

This is similar to what is already there. It changes the following:
* The unecessary `getAlliance` is removed. (You should also remove `AllianceColor`)
* Added `isDataValid` and `refresh`
* Reworded `getXyzSide` to reflrect the terminology used by FIRST.

```java
interface IFieldInformation {
    // Returns the position of the side owned by us for a field element, or INVALID if the data is unavailable.
    public FieldSide getNearSwitchPosition();
    public FieldSide getScalePosition();
    public FieldSide getFarSwitchPosition();

    // Returns true if field information is valid and available. False otherwise.
    // This is necessary because the value of the game-specific data is indetermininate at the start. 
    // See http://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details
    public boolean isDataValid();

    // Polls new data from the field management system, updating the positions of the field elements.
    public void refresh();
}
```

## Implement `IFieldInformation`

Here is a partial implementation of `IFieldInformation` (with all the important stuff done.)

```java
class FieldInformation implements IFieldInformation {
    private FieldSide initialRobotPosition;
    private FieldSide nearSwitchPosition;
    private FieldSide scalePosition;
    private FieldSide farSwitchPosition;
    private boolean isDataValid;

    public FieldInformation() {
        refresh();
    }

    // ... Omitting getters and isDataValid ...

    public void refresh() {
        // Invalidate the field information
        isDataValid = false;
        initialRobotPosition = FieldSide.INVALID;
        nearSwitchPosition = FieldSide.INVALID;
        scalePosition = FieldSide.INVALID;
        farSwitchPosition = FieldSide.INVALID;

        // Try to get new field information from the field management system
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        // Don't process the message if it is invalid
        if (gameData == null || gameData.length != 3) {
            return;
        }

        nearSwitchPosition = messageCharacterToFieldSide(gameData.charAt(0));
        scalePosition = messageCharacterToFieldSide(gameData.charAt(1));
        farSwitchPosition = messageCharacterToFieldSide(gameData.charAt(2));

        // Data is only valid if all positions contained valid characters
        isDataValid = isValidFieldElementSide(nearSwitchPosition)
            && isValidFieldElementSide(scalePosition)
            && isValidFieldElementSide(farSwitchPosition)
        ;

        // You might consider forcing all field element positions to be FieldSide.INVALID if isDataValid == false.
        // That way if in the weird edge case that only one is invalid, they all read as invalid to anyone who failed to check isDataValid.
    }

    private static FieldSide messageCharacterToFieldSide(char messageCharacter) {
        switch (messageCharacter) {
            case 'L': return FieldSide.LEFT;
            case 'R': return FieldSide.RIGHT;
            default: return FieldSide.INVALID;
        }
    }

    private static boolean isValidFieldElementSide(FieldSide side) {
        switch (side) {
            case FieldSide.LEFT:
            case FieldSide.RIGHT:
                return true;
            default:
                return false;
        }                        
    }
}
```

## Instantiate `FieldInformation`

Instantiate an instance of `FieldInformation` in `Robot` as you do with other peripherals. However, **do not** call `update()` in `doPeripheralPeriodicProcessing`. It probably won't hurt, but...

## Add updating the field information to `DisabledMode`

* Modify `DisabledMode` to take an `IFieldInformation`
* Add a call to `fieldInformation.update()` to `periodic`

# Changes to autonomous submodes

## :purple_heart: Make autonomous submodes implement `IRobotMode`

The fact that the autonomous submodes start their processing in their constructor is a bit odd. Of course they can totally do that because of the functional implementation our autonomous has thanks to the completion routine pattern. That doesn't make it less odd.

We also might feasibly have an autonomous submode that can't take advantage of that pattern for whatever reason.

Therefore, we should treat them as they are - submodes of the autonomous robot mode.

Change `BaselineAuto`, `SwitchAuto`, and `ScaleAuto` to implement `IRobotMode:
* `init` should call the first action in the chain.
* `periodic` should do nothing.

## :purple_heart: Fix `Consumer<IDrive>` -> `Runnable`-related changes

When we changed all of the `completionRoutine`s to be `Runnable` instead of `Consumer<IDrive>`, we broke all of the completion routines in the autonomous submodes.

Fix them by replacing the parameters for the lambdas with `()` (an empty parameter list.)

# Merge `Autonomous` into `AutonomousMode`

Not sure how these ended up as two halves, but they shouldn't be.

Let's merge them together.

| `Autonomous` method | Fate |
|---|---|
| `pollGameData` | Replaced by `FieldInformation`, delete. |
| `autoModeControl` | :purple_heart: Replaced by `determineAutoMode` |
| `doAuto` | :purple_heart: Replaced by `getAutoModeImplementation` |

## Remove `BOTH` from `AutoMode`

It won't be necessary soon.

## :purple_heart: Add dashboard reference to `AutonomousMode`

Before, `Autonomous` was initializing its own `Dashboard` instance which would've made things not work as expected.

## Pass `IFieldInformation` to `AutonomousMode` in `Robot`

We added it to `Robot` earlier, but `AutonomousMode` needs a reference too.

## :purple_heart: Update `determineAutoMode` (formerly `autoModeControl`)

* Take in `dashboard` for starting position.
* Fall back on `LINE` when field infromation is unavailable.
* Adapt logic to use field information.
* Use `dashboard` to determine mode for what used to be the `BOTH` scenario.

## :purple_heart: Rework `getAutoModeImplementation` to return the appropriate `IRobotMode` implementation

* Remove `BOTH` logic since that part is now handled in `determineAutoMode`
* Remove instantiation of `dashboard` (it doesn't belong here.)
* Use `dashboard` to determine starting location.
* Throw an exception when the `autoMode` is invalid.

## :purple_heart: Implement `init` to determine and initialize mode

* Update the field information. (WPIlib recommended practice.)
* Use `getAutoModeImplementation` to get the appropriate autonomous mode implementation.
* Call `init` on the autonomous mode implementation.

## :purple_heart: Implement `periodic` to periodically update mode

* Call `periodic` on the autonomous mode implementation.

## Finally, delete `Autonomous` and `IAutonomous`

These are not unecessary. Delete them to avoid confusion for those reading the code base later on.

Additionally, remove any references to them from `AutonomousMode` if they've somehow survived.

# Dashboard fixes

## `StartingPoint` is redundant to `FieldSide`, remove it.

:purple_heart: I did some of this, but you need to delete the file.

(Having this enum separate is weird because it's convienent to be able to compare our position with values from `IFieldInformation`.)

## :purple_heart: Add `getMatchStartingPoint` to the `IDashboard` interface.

(Done.)

## Add sendables to Smart Dashboard

Your sendables need to be registered with the smart dashboard. Add calls to `SmartDashbaord.putData` to the `Dashboard` constructor. For example:

```java
SmartDashboard.putData("Switch/Scale Autonomous Mode", autoChoices);
```

## Prepare for Smart Dashboard issues

You expressed some concern about smart dashboard not working properly since you never had a chance to test it.

Definitely a valid concern. Thankfully our interface-oriented design makes it really easy to switch to using hard-coded values instead.

Create a new class called `HardCodedDashboard` which extends `IDashboard` but reports hard-coded values:

```java
public class Dashboard implements IDashboard {
	public AutoMode chooseModeforBoth() {	    
		return AutoMode.LINE;
	}
	
	public FieldSide getMatchStartingPoint() {
	    return FieldSide.LEFT;
	}	
}
```

# `ScaleAuto.goStraightSort`

This method is disabled. The comment above it states "`PROBABLY UNNECESSARY AS OF NOW`" but it seems kinda important to me. Are you sure about that one?

(Remember if you uncomment this method, you also need to update `liftGrabber` to call it instead.)

# `SwitchAuto.liftGrabber`

This method is disabled. The comment above it starts that you can just pre-raise the lift to the correct hight.

I would not rely on being able to do this unless you think you really need to. It's very hard to do stuff like this when you're queuing during quarter-finals.

Additionally, I imagine the robot will be very hard to move with the scissor lift partially raised. It might actually even be unsafe, and the team could get in trouble for doing it at all.

(Remember if you uncomment this method, you also need to update `liftGrabber` to call it instead.)

# Low priority: Driver autonomous mode override

I'd suggest adding a way for the driver to override the autonomous mode entirely.

One of these overrides should be "Disable autonomous". Better to have it now than to need to panic create it when it becomes necessary.
