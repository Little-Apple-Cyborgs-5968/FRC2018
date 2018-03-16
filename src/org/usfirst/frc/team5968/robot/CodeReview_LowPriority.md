<!--
This is the Markdown source of this document. You probably want to view the PDF (which I should've sent to you) instead unless you intend to edit the document.
It is intended to be rendered using Visual Studio Code, you probably want the following extensions installed:
* AlanWalk.markdown-toc
* bierner.markdown-emoji
* yzane.markdown-pdf
-->

This document contains notes for things that aren't necessarily important for competition. Don't worry about fixing these before competition, but they are worth considering.

However, you don't necessarily have to put these off until after competition (they will likely make emergency debugging easier at competition), just do the `CodeReview` comments first.

# Table of Contents

<!-- TOC -->

- [Table of Contents](#table-of-contents)
- [AutoMode.**class** / StartingPoint.**class**](#automodeclass--startingpointclass)
- [`DriveMode`](#drivemode)
- [`FieldSide`](#fieldside)
- [`Lift`](#lift)
    - [`motorSpeed`](#motorspeed)
    - [Constructor](#constructor)
    - [`goTo`](#goto)
    - [`setCurrentHeight`](#setcurrentheight)
- [`NavXMXP`](#navxmxp)
- [`StartingPoint`](#startingpoint)
- [`TeleoperatedMode`](#teleoperatedmode)

<!-- /TOC -->

# AutoMode.**class** / StartingPoint.**class**

**TL;DR:** These files should be deleted.

These files are build products and should not be comitted to Git. I'm not actually sure how they ended up here in the first place since Eclipse should've generated them into one of the build directories (which are ignored by the `.gitignore`.)

Luann comitted these.  I'm not familiar enough with Eclipse to know how these got created here in the first placer, but she should check her IDE configuration to try and find out why. Additionally, you should be more careful about what you commit, don't just blindly commit everything. (Obviously knowing what actually needs to be comitted comes with experience. Generally if Git complains that a file is binary, it probably shouldn't be comitted without better understanding of what it is.)

# `DriveMode`

These constants need some underscores! IE: `IDLE_OR_MANUAL`

# `FieldSide`

Pedantic: This enumeration should probably be named `FieldPosition` or `FieldLane` since `CENTER` is not a side.

Documentation that states these values are from the perspective of the driver station would be good for removing ambiguity.

# `Lift`

## `motorSpeed`

This field should be `static final` and assigned inline instead of in the constructor.

## Constructor

`desiredHeight` and `currentHeight` are not initialized.

They will be GROUND by default, but you should initialize them explicitly:

```
goTo(GROUND)
setCurrentHeight()
```

## `goTo`

I suspect the author her did not know about `this`. Rather than using a slightly different name for the argument, this method should use that:

```java
private void goTo(LiftHeight desiredHeight) {
    this.desiredHeight = desiredHeight;
}
```

## `setCurrentHeight`

I'd suggest naming this `updateCurrentHeight` since that better maps to what it does. (Normally the nomenclature `setXXX` is reserved for setter methods.)

# `NavXMXP`

This class should initialize `AHRS` directly since it is an abstraction of `AHRS`. IE: The constrctor should be parameterless and `navX` should get a new instance of `AHRS`.

# `StartingPoint`

This enumeration is redundant to `FieldSide` and probably doesn't need to exist.

# `TeleoperatedMode`

`TOLERANCE` - This is usually referred to as the "deadzone". I'd rename this to `JOYSTICK_DEADZONE` for the sake of clarity.
