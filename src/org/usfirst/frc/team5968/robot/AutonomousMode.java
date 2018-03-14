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
        if (!fieldInformation.isDataValid()) {
            return AutoMode.LINE;
        }

        // Determine the appropriate autonomous mode based on our position
        FieldSide ourSide = dashboard.getMatchStartingPoint();
        boolean switchSameSide = fieldInformation.getNearSwitchPosition() == ourSide;
        boolean scaleSameSide = fieldInformation.getScalePosition() == ourSide;

        // We always want to use the LINE strategy when we're in the center lane
        if (ourSide == FieldSide.CENTER) {
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
        FieldSide startingPoint = dashboard.getMatchStartingPoint();

        switch (automode) {
            case SWITCH:
                new SwitchAuto(startingPoint, drive, grabber);
                break;
            case SCALE:
                new ScaleAuto(startingPoint, drive, grabber, lift);
                break;
            case LINE:
                new BaselineAuto(startingPoint, drive);
                break;
            default:
                throw new IllegalArgumentException("Tried to instantiate invalid autonomous mode.");
        }
    }

    @Override
    public void init() {
        fieldInformation.update();
        autonomousSubMode = getAutoModeImplementation();
        autonomousSubMode.init();
    }

    @Override
    public void periodic() {
        autonomousSubMode.periodic();
    }
}
