package org.usfirst.frc.team5968.robot;

public class AutonomousMode implements IRobotMode {

    private IFieldInformation fieldInformation;
    private IDashboard dashboard;
    private IDrive drive;
    private IGrabber grabber;
    private ILift lift;

    private IRobotMode autonomousSubMode;
    
    public AutonomousMode(IDrive drive, IGrabber grabber, ILift lift, IFieldInformation fieldInformation, IDashboard dashboard) {
        this.drive = drive;
        this.grabber = grabber;
        this.lift = lift;
        this.fieldInformation = fieldInformation;
        this.dashboard = dashboard;
    }

    private AutoMode determineAutoMode() {
        // If no field information is available, default to the LINE strategy
        if (!fieldInformation.getIsDataValid()) {
            return AutoMode.LINE;
        }

        // Determine the appropriate autonomous mode based on our position
        FieldPosition ourSide = dashboard.getMatchStartingPoint();
        boolean switchSameSide = fieldInformation.getNearSwitchPosition() == ourSide;
        boolean scaleSameSide = fieldInformation.getScalePosition() == ourSide;

        // We always want to use the LINE strategy when we're in the center lane
        if (ourSide == FieldPosition.CENTER) {
            return AutoMode.LINE;
        }

        // If the switch and scale are both on our side, we use the strategy chosen on the dashboard
        if (switchSameSide && scaleSameSide) {
            return dashboard.chooseModeforBoth();
        } else if (switchSameSide) { // If the switch is on our side, we use the SWITCH strategy
            return AutoMode.SWITCH;
        } else if (scaleSameSide) { // If the scale is on our side, we use the SCALE strategy
            return AutoMode.SCALE;
        } else { // Otherwise, we fall back to the LINE strategy
            return AutoMode.LINE;
        }
    }

    /*
     * Creates instance of correct autonomous mode
     */
    private IRobotMode getAutoModeImplementation() {
        AutoMode autoMode = determineAutoMode();
        FieldPosition startingPoint = dashboard.getMatchStartingPoint();
        switch (autoMode) {
            case SWITCH:
                return new SwitchAuto(startingPoint, drive, grabber, lift);
            case SCALE:
                return new ScaleAuto(startingPoint, drive, grabber, lift);
            case LINE:
                return new BaselineAuto(startingPoint, drive);
            default:
                throw new IllegalArgumentException("Tried to instantiate invalid autonomous mode.");
        }
    }

    @Override
    public void init() {
        fieldInformation.refresh();
        autonomousSubMode = getAutoModeImplementation();
        Debug.log(autonomousSubMode.getClass().getName() + " mode is starting");
        autonomousSubMode.init();
    }

    @Override
    public void periodic() {
        autonomousSubMode.periodic();
    }
}