// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc948.NRGRobot2018;

import java.sql.Driver;
import java.util.ArrayList;

import org.usfirst.frc948.NRGRobot2018.commandGroups.AutonomousRoutines;
import org.usfirst.frc948.NRGRobot2018.subsystems.Climber;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeAcquirer;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeLifter;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeTilter;
import org.usfirst.frc948.NRGRobot2018.subsystems.Drive;
import org.usfirst.frc948.NRGRobot2018.utilities.CubeCalculations;
import org.usfirst.frc948.NRGRobot2018.utilities.MathUtil;
import org.usfirst.frc948.NRGRobot2018.utilities.PositionTracker;
import org.usfirst.frc948.NRGRobot2018.utilities.PreferenceKeys;
import org.usfirst.frc948.NRGRobot2018.vision.PixyCam.Block;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	Command autonomousCommand;
	public static SendableChooser<AutoStartingPosition> autoPositionChooser;
	public static SendableChooser<AutoMovement> autoMovementChooser;

	public static Preferences preferences;
	public static Drive drive;
	public static CubeAcquirer cubeAcquirer;
	public static CubeLifter cubeLifter;
	public static CubeTilter cubeTilter;
	public static Climber climber;
	public static PositionTracker positionTracker;

	public enum AutoStartingPosition {
		LEFT, CENTER, RIGHT
	}

	public enum AutoMovement {
		SWITCH, SCALE, BOTH, FORWARD, NONE
	}

	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {
		System.out.println("robotInit() started");
		
		preferences = Preferences.getInstance();
		RobotMap.init();

		drive = new Drive();
		cubeAcquirer = new CubeAcquirer();
		cubeLifter = new CubeLifter();
		cubeTilter = new CubeTilter();
		climber = new Climber();
		positionTracker = new PositionTracker(0, 0);

		OI.init();
		OI.initTriggers();

		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.

		initPreferences();
		RobotMap.pixy.startVisionThread();
		RobotMap.arduino.startArduinoThread();

		autoPositionChooser = new SendableChooser<AutoStartingPosition>();
		autoPositionChooser.addDefault("Left", AutoStartingPosition.LEFT);
		autoPositionChooser.addObject("Center", AutoStartingPosition.CENTER);
		autoPositionChooser.addObject("Right", AutoStartingPosition.RIGHT);

		autoMovementChooser = new SendableChooser<AutoMovement>();
		autoMovementChooser.addObject("Switch", AutoMovement.SWITCH);
		autoMovementChooser.addObject("Scale", AutoMovement.SCALE);
		autoMovementChooser.addDefault("Both", AutoMovement.BOTH);
		autoMovementChooser.addDefault("Forward", AutoMovement.FORWARD);
		autoMovementChooser.addDefault("None", AutoMovement.NONE);

		SmartDashboard.putData("Choose autonomous position", autoPositionChooser);
		SmartDashboard.putData("Choose autonomous movement", autoMovementChooser);

		System.out.println("robotInit() done");
	}

	/**
	 * This function is called when the disabled button is hit. You can use it to reset subsystems
	 * before shutting down.
	 */
	@Override
	public void disabledInit() {
		drive.stop();
		cubeLifter.stop();
		cubeTilter.stop();
		cubeAcquirer.stop();
		climber.stop();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		System.out.println("autoInit()");
		
		OI.initTriggers();
		drive.setMaxAccel(preferences.getDouble(PreferenceKeys.AUTO_MAX_DRIVE_ACCEL, Drive.DEF_AUTO_MAX_DRIVE_ACCEL));

		autonomousCommand = new AutonomousRoutines();
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
		System.out.println("teleopInit()");
		
		OI.initTriggers();
		drive.setMaxAccel(
				preferences.getDouble(PreferenceKeys.TELEOP_DRIVE_ACCEL_MAX_LIFT_HEIGHT, Drive.DEF_TELEOP_DRIVE_ACCEL_MAX_LIFT_HEIGHT));

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
		
		double liftHeight = RobotMap.cubeLiftEncoder.getDistance();
		double liftPercentage = MathUtil.clamp(liftHeight / CubeLifter.DEFAULT_SCALE_HIGH_TICKS, 0, 1);
		double defaultAccel = preferences.getDouble(PreferenceKeys.TELEOP_DRIVE_ACCEL_MAX_LIFT_HEIGHT, 
				Drive.DEF_TELEOP_DRIVE_ACCEL_MAX_LIFT_HEIGHT);
		drive.setMaxAccel(MathUtil.clamp(Math.pow((1 - liftPercentage), 2.0), defaultAccel, 1));
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void robotPeriodic() {
		positionTracker.updatePosition();
		
		ArrayList<Block> currFrame = RobotMap.pixy.getPixyFrameData();
		if (currFrame.size() > 0) { 
			Block cube = currFrame.get(0);

			SmartDashboard.putString("Cube/Cube", cube.toString());
			SmartDashboard.putNumber("Cube/Angle to turn", CubeCalculations.getAngleToTurn(cube));
			SmartDashboard.putNumber("Cube/Inches to cube from width", CubeCalculations.getDistanceFromWidth(cube));
		}

		SmartDashboard.putNumber("navx gyro yaw", RobotMap.navx.getYaw());
		
		SmartDashboard.putNumber("PositionTracker/current x", positionTracker.getX());
		SmartDashboard.putNumber("PositionTracker/current y", positionTracker.getY());
		
		SmartDashboard.putNumber("Encoders/omni x", RobotMap.xEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/omni y", RobotMap.yEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/left front", RobotMap.leftFrontEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/left rear", RobotMap.leftRearEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/right front", RobotMap.rightFrontEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/right rear", RobotMap.rightRearEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/lifter", RobotMap.cubeLiftEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/tilter", RobotMap.cubeTiltEncoder.getDistance());
		SmartDashboard.putNumber("Encoders/MechY", Robot.positionTracker.getMechY());
		
		SmartDashboard.putData("LifterLimitSwitches/upper", RobotMap.lifterUpperLimitSwitch);
		SmartDashboard.putData("LifterLimitSwitches/lower", RobotMap.lifterLowerLimitSwitch);
		
		SmartDashboard.putNumber("LeftJoystick/x", OI.getLeftJoystickX());
		SmartDashboard.putNumber("LeftJoystick/y", OI.getLeftJoystickY());
		
		SmartDashboard.putNumber("RightJoystick/x", OI.getRightJoystickX());
		SmartDashboard.putNumber("RightJoystick/y", OI.getRightJoystickY());
		SmartDashboard.putNumber("RightJoystick/rot", OI.getRightJoystickRot());
		
		SmartDashboard.putNumber("XBox/left joystick y",  OI.getXBoxLeftY());
		SmartDashboard.putNumber("XBox/right joystick y", OI.getXBoxRightY());
		SmartDashboard.putNumber("XBox/left trigger", OI.getXBoxTriggerL());
		SmartDashboard.putNumber("XBox/right trigger", OI.getXBoxTriggerR());
		SmartDashboard.putNumber("XBox/d-pad", OI.xboxController.getPOV());
	}

	public void initPreferences() {
		if (preferences.getBoolean(PreferenceKeys.WRITE_DEFAULT, true)) {
			preferences.putDouble(PreferenceKeys.LIFT_P_TERM, CubeLifter.DEFAULT_LIFT_P);
			preferences.putDouble(PreferenceKeys.LIFT_I_TERM, CubeLifter.DEFAULT_LIFT_I);
			preferences.putDouble(PreferenceKeys.LIFT_D_TERM, CubeLifter.DEFAULT_LIFT_D);
			preferences.putDouble(PreferenceKeys.LIFT_UP_MAX_POWER, CubeLifter.LIFT_POWER_SCALE_UP);
			preferences.putDouble(PreferenceKeys.LIFT_DOWN_MAX_POWER, CubeLifter.LIFT_POWER_SCALE_DOWN);

			preferences.putDouble(PreferenceKeys.TURN_P_TERM, Drive.DEFAULT_TURN_P);
			preferences.putDouble(PreferenceKeys.TURN_I_TERM, Drive.DEFAULT_TURN_I);
			preferences.putDouble(PreferenceKeys.TURN_D_TERM, Drive.DEFAULT_TURN_D);
			preferences.putDouble(PreferenceKeys.DRIVE_TURN_MAX_POWER, Drive.DEFAULT_DRIVE_TURN_POWER);

			preferences.putDouble(PreferenceKeys.DRIVE_X_P, Drive.DEFAULT_DRIVE_X_P);
			preferences.putDouble(PreferenceKeys.DRIVE_X_I, Drive.DEFAULT_DRIVE_X_I);
			preferences.putDouble(PreferenceKeys.DRIVE_X_D, Drive.DEFAULT_DRIVE_X_D);
			preferences.putDouble(PreferenceKeys.DRIVE_X_MAX_POWER, Drive.DEFAULT_DRIVE_X_POWER);

			preferences.putDouble(PreferenceKeys.DRIVE_Y_P, Drive.DEFAULT_DRIVE_Y_P);
			preferences.putDouble(PreferenceKeys.DRIVE_Y_I, Drive.DEFAULT_DRIVE_Y_I);
			preferences.putDouble(PreferenceKeys.DRIVE_Y_D, Drive.DEFAULT_DRIVE_Y_D);
			preferences.putDouble(PreferenceKeys.DRIVE_Y_MAX_POWER, Drive.DEFAULT_DRIVE_Y_POWER);

			preferences.putDouble(PreferenceKeys.DRIVE_XYH_X, 48.0);
			preferences.putDouble(PreferenceKeys.DRIVE_XYH_Y, 48.0);
			preferences.putDouble(PreferenceKeys.DRIVE_XYH_H, 0);
			preferences.putDouble(PreferenceKeys.DRIVE_XYH_X_POWER, 0.9);
			preferences.putDouble(PreferenceKeys.DRIVE_XYH_Y_POWER, 0.5);
			preferences.putDouble(PreferenceKeys.DRIVE_XYH_TURN_POWER, 0.3);

			preferences.putInt(PreferenceKeys.SWITCH_TICKS, CubeLifter.DEFAULT_SWITCH_TICKS);
			preferences.putInt(PreferenceKeys.SCALE_HIGH_TICKS, CubeLifter.DEFAULT_SCALE_HIGH_TICKS);
			preferences.putInt(PreferenceKeys.SCALE_MEDIUM_TICKS, CubeLifter.DEFAULT_SCALE_MEDIUM_TICKS);
			preferences.putInt(PreferenceKeys.SCALE_LOW_TICKS, CubeLifter.DEFAULT_SCALE_LOW_TICKS);
			preferences.putInt(PreferenceKeys.STOWED_TICKS, CubeLifter.DEFAULT_STOWED_TICKS);

			preferences.putBoolean(PreferenceKeys.USE_PHYSICAL_AUTO_CHOOSER, true);
			preferences.putBoolean(PreferenceKeys.USE_FOUR_ENCODERS, false);
			preferences.putBoolean(PreferenceKeys.USING_PRACTICE_BOT, true);
			
			preferences.putBoolean(PreferenceKeys.WRITE_DEFAULT, false);
			
			preferences.putDouble(PreferenceKeys.AUTO_MAX_DRIVE_ACCEL, Drive.DEF_AUTO_MAX_DRIVE_ACCEL);
			preferences.putDouble(PreferenceKeys.TELEOP_DRIVE_ACCEL_MAX_LIFT_HEIGHT, Drive.DEF_TELEOP_DRIVE_ACCEL_MAX_LIFT_HEIGHT);

			preferences.putDouble(PreferenceKeys.MEC_ENCODER_LF_RATIO_PRACTICE, RobotMap.DEF_MEC_ENCODER_LF_RATIO_PRACTICE);
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_LR_RATIO_PRACTICE, RobotMap.DEF_MEC_ENCODER_LR_RATIO_PRACTICE);
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_RF_RATIO_PRACTICE, RobotMap.DEF_MEC_ENCODER_RF_RATIO_PRACTICE);
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_RR_RATIO_PRACTICE, RobotMap.DEF_MEC_ENCODER_RR_RATIO_PRACTICE);
			
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_LF_RATIO_COMP, RobotMap.DEF_MEC_ENCODER_LF_RATIO_COMP);
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_LR_RATIO_COMP, RobotMap.DEF_MEC_ENCODER_LR_RATIO_COMP);
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_RF_RATIO_COMP, RobotMap.DEF_MEC_ENCODER_RF_RATIO_COMP);
			preferences.putDouble(PreferenceKeys.MEC_ENCODER_RR_RATIO_COMP, RobotMap.DEF_MEC_ENCODER_RR_RATIO_COMP);
			
			preferences.putInt(PreferenceKeys.PIXY_CAM_CUBE_SIGNATURE, 1);
		}
	}
}
