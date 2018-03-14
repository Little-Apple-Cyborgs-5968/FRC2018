<!--
This is the Markdown source of this document. You probably want to view the PDF (which I should've sent to you) instead unless you intend to edit the document.
It is intended to be rendered using Visual Studio Code, you probably want the following extensions installed:
* AlanWalk.markdown-toc
* bierner.markdown-emoji
* yzane.markdown-pdf
-->

This code review consists of a couple different documents:

| Document | Description |
|---|---|
| `CodeReview_General` | Comments about the code base as a whole and non-autonomous stuff. |
| `CodeReview_LowPriority` | General comments that are of lesser concern. |
| `CodeReview_Drive` | Comments about the `Drive` class and related functionality. |
| `CodeReview_Autonomous` | Comments relating to autonomous functionality. |

These documents were meant to be rendered using Visual Studio Code. However, they should render fine on GitHub. Additionally, you can get them in PDF form here: https://pgn.moe/frc2018/code-reviews/

Additionally, this document provides some overall notes about the reviews. It also includes an index of types along with their review verdict.

I made some of my suggested changes as I went along in order to help me read things and to give you a bit less to worry about. I usually try to point this out when I do it.

# Table of Contents

<!-- TOC -->

- [Table of Contents](#table-of-contents)
- [Some general notes](#some-general-notes)
    - [Code Removal](#code-removal)
    - [Suggestions, not demands](#suggestions-not-demands)
    - ["Nice to have"](#nice-to-have)
- [Index of types](#index-of-types)
    - [Status descriptors](#status-descriptors)
    - [Index](#index)

<!-- /TOC -->

# Some general notes

## Code Removal

When I say to remove something: I literally mean straight-up throw it into the `void`. You can always git it back using version control.

Keeping the code around as comments just adds noise. It's OK to comment stuff out haphazardly in the heat of trying to debug something, but keeping it around now just makes it hardder to debug in the heat of things at competition.

After you are done fixing and changing things, I highly recommend doing a final sweep for commented out code and removing stuff or considering if it shouldn't be commented out. (You could also do this as you go along.)

## Suggestions, not demands

As always, all of the comments in all of these documents are suggestions, not demands. If you're confident something I said is wrong or unecessary, feel free to dismiss it.

However, I put extra effort this time into making sure I focused on the essentials for the sake of brevity since I know time is of the essence. As such, there are some instances where I did not mention or investigate certain things because I felt another issue I mentioned caused that problem to go away automatically. (EG: I suggested you remove PID-related code. I also think it might not be implemented right, but I did not investigate far enough to decide if I'm right.)

As such, if you decide to skip anything outside of the `LowPriority` documents, I'd suggest mentioning it on Discord so I know to see if there's something I glossed over.

## "Nice to have"

I tried to keep stuff that was strictly "nice to have" in the `LowPriority` documents. Some of the stuff in the other documents might seem like it's only nice to have, but generally speaking I'm probably suggesting it because I think doing so now diminishes the chance of issues at competition drastically or makes it easier for you to fix issues should they arise.

# Index of types

## Status descriptors

| Status | Description |
|---|---|
| :collision: | This file contains bugs. |
| :exclamation: | This file contains a fragile design or it needs changes to help something else. |
| :triumph: <!-- The name of this emoji makes no sense to me --> | This file isn't the best, but its issues are fairly minor as far as competition is concerned. |
| :thumbsup: | This file looks fine by me (alhough I might still have some minor, pedantic comments.) |
| :skull: | This file is no longer (or never was) necessary and should be deleted. |

## Index

| | |
|---|---|
| :skull: | AllianceColor |
| :exclamation: | AutoMode |
| :skull: | Autonomous |
| :collision: | AutonomousMode |
| :exclamation: | BaselineAuto |
| :collision: | Dashboard |
| :exclamation: | DisabledMode |
| :collision: | Drive |
| :thumbsup: | DriveMode |
| :thumbsup: | FieldSide |
| :thumbsup: | Grabber |
| :skull: | IAutonomous |
| :exclamation: | IDashboard |
| :thumbsup: | IDrive |
| :exclamation: | IFieldInformation |
| :thumbsup: | IGrabber |
| :thumbsup: | IGyroscopeSensor |
| :thumbsup: | ILift |
| :thumbsup: | IRobotMode |
| :collision: | Lift |
| :exclamation: | LiftHeight |
| :triumph: | NavXMXP |
| :thumbsup: | PistonState |
| :thumbsup: | PortMap |
| :exclamation: | Robot |
| :exclamation: | ScaleAuto |
| :skull: | StartingPoint |
| :exclamation: | SwitchAuto |
| :collision: | TeleoperatedMode |
