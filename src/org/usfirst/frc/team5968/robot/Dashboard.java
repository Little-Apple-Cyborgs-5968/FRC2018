import edu.wpi.first.wpilibj.DriverStation;

public class Dashboard implements IDashboard{
	//probably using SmartDashboard for now
	public AutoMode chooseModeforBOTH() {
		if (//GET DASHBOARD CHOICE ==AutoMode.SWITCH) {
			return SWITCH;
		} else if (//GET DASHBOARD CHOICE ==AutoMode.SCALE) {
			return SCALE;
		} else {
			return LINE;
		}
	}
}
