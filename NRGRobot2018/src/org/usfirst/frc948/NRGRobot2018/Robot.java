// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package src.org.usfirst.frc948.NRGRobot2018;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.usfirst.frc948.NRGRobot2018.subsystems.Climber;
import src.org.usfirst.frc948.NRGRobot2018.subsystems.CubeAcquirer;
import src.org.usfirst.frc948.NRGRobot2018.subsystems.CubeLifter;
import src.org.usfirst.frc948.NRGRobot2018.subsystems.Drive;
import src.org.usfirst.frc948.NRGRobot2018.utilities.PreferenceKeys;
import src.org.usfirst.frc948.NRGRobot2018.vision.PixyCam.Block;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	Command autonomousCommand;
	public static Preferences preferences;

	public static OI oi;
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public static Drive drive;
	public static final CubeAcquirer acquirer = new CubeAcquirer();
	public static final CubeLifter cubeLifter = new CubeLifter();
	public static final Climber climber=new Climber();
	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {
		preferences = Preferences.getInstance();
		RobotMap.init();
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

		drive = new Drive();

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();

		// Add commands to Autonomous Sendable Chooser
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

		initPreferences();
		RobotMap.pixy.startVisionThread();
	}

	/**
	 * This function is called when the disabled button is hit. You can use it to reset subsystems
	 * before shutting down.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();

	}

	@Override
	public void autonomousInit() {
		autonomousCommand = OI.chooser.getSelected();
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void robotPeriodic() {
		// TODO Auto-generated method stub
		SmartDashboard.putNumber("navx gyro yaw", RobotMap.navx.getYaw());
		// System.out.println("spi data: "+
		// Integer.toHexString(RobotMap.pixyLink.getWord()));
		System.out.println(RobotMap.pixy.getPixyFrameData().size());
//		SmartDashboard.putString("Alliance Scale Side", OI.getScaleSide().toString());
//		SmartDashboard.putString("Alliance Switch Side", OI.getAllianceSwitchSide().toString());
//		SmartDashboard.putString("Opposing Switch Side", OI.getOppsingSwitchSide().toString());
		
		ArrayList<Block> currFrame = RobotMap.pixy.getPixyFrameData();

		if (currFrame.size() > 0) {
			SmartDashboard.putString("Cube", currFrame.get(0).toString());
		}
	}

	public void initPreferences() {
		if (preferences.getBoolean(PreferenceKeys.WRITE_DEFAULT, true)) {
			preferences.putDouble(PreferenceKeys.TURN_P_TERM, Drive.DEFAULT_TURN_P);
			preferences.putDouble(PreferenceKeys.TURN_I_TERM, Drive.DEFAULT_TURN_I);
			preferences.putDouble(PreferenceKeys.TURN_D_TERM, Drive.DEFAULT_TURN_D);
			preferences.putDouble(PreferenceKeys.MAX_VEL_CHANGE, Drive.DEF_MAX_VEL_CHANGE);
			preferences.putBoolean(PreferenceKeys.WRITE_DEFAULT, false);
		}
	}
}
