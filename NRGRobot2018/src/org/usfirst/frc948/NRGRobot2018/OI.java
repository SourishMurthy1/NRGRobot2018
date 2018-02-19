
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

import org.usfirst.frc948.NRGRobot2018.commandGroups.DriveSquare;
import org.usfirst.frc948.NRGRobot2018.commandGroups.DriveSquareWithTurning;
import org.usfirst.frc948.NRGRobot2018.commands.AutonomousCommand;
import org.usfirst.frc948.NRGRobot2018.commands.DriveStraightDistance;
import org.usfirst.frc948.NRGRobot2018.commands.DriveStraightTimed;
import org.usfirst.frc948.NRGRobot2018.commands.DriveToXYHeadingNoPIDTest;
import org.usfirst.frc948.NRGRobot2018.commands.ManualDrive;
import org.usfirst.frc948.NRGRobot2018.commands.ManualDriveStraight;
import org.usfirst.frc948.NRGRobot2018.commands.ManualStrafeStraight;
import org.usfirst.frc948.NRGRobot2018.commands.ResetSensors;
import org.usfirst.frc948.NRGRobot2018.commands.SetDriveScale;
import org.usfirst.frc948.NRGRobot2018.commands.StrafeStraightTimed;
import org.usfirst.frc948.NRGRobot2018.commands.TestPixyData;
import org.usfirst.frc948.NRGRobot2018.commands.TurnToHeading;
import org.usfirst.frc948.NRGRobot2018.subsystems.Drive;
import org.usfirst.frc948.NRGRobot2018.subsystems.Drive.Direction;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick leftJoystick;
	public static Joystick rightJoystick;
	public static Joystick xboxController;

	public JoystickButton leftShiftGears;
	public JoystickButton driveStraight;
	public JoystickButton strafeStraight;
	public JoystickButton rightShiftGears;

	public static SendableChooser<Command> chooser;

	public enum Side {
		LEFT, RIGHT;
	}

	public OI() {
		// Initializing Joysticks
		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		xboxController = new Joystick(2);

		// Initializing buttons AFTER the Joysticks
		leftShiftGears = new JoystickButton(leftJoystick, 1);
		driveStraight = new JoystickButton(leftJoystick, 2);
		strafeStraight = new JoystickButton(leftJoystick, 3);
		rightShiftGears = new JoystickButton(rightJoystick, 1); // drive team needed it for both joysticks

		// Initialize commands after initializing buttons
		leftShiftGears.whenPressed(new SetDriveScale(Drive.SCALE_HIGH));
		leftShiftGears.whenReleased(new SetDriveScale(Drive.SCALE_LOW));
		rightShiftGears.whenPressed(new SetDriveScale(Drive.SCALE_HIGH));
		rightShiftGears.whenReleased(new SetDriveScale(Drive.SCALE_LOW));
		driveStraight.whileHeld(new ManualDriveStraight());
		strafeStraight.whileHeld(new ManualStrafeStraight());

		// SmartDashboard Buttons
		chooser = new SendableChooser<>();
		chooser.addDefault("Autonomous Command", new AutonomousCommand());
		SmartDashboard.putData("Auto mode", chooser);

		SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
		SmartDashboard.putData("ManualDrive", new ManualDrive());
		SmartDashboard.putData("Reset Sensors", new ResetSensors());
		SmartDashboard.putData("Turn To 90 Degrees", new TurnToHeading(90));
		SmartDashboard.putData("Turn To -90 Degrees", new TurnToHeading(-90));
		SmartDashboard.putData("Drive Straight 2 seconds", new DriveStraightTimed(0.5, 2.0));
		SmartDashboard.putData("Strafe Straight 2 seconds", new StrafeStraightTimed(1, 2.0));
		SmartDashboard.putData("DriveSquareAuto", new DriveSquare());
		SmartDashboard.putData("DriveSquareWithTurning", new DriveSquareWithTurning());
		SmartDashboard.putData("TestPixyData", new TestPixyData());
		SmartDashboard.putData("driveStraightDistance 4 feet", new DriveStraightDistance(0.5, 48, Direction.FORWARD));
		SmartDashboard.putData("StrafeStraightDistance 4 feet", new DriveStraightDistance(1, 48, Direction.RIGHT));
		SmartDashboard.putData("driveStraightDistanceBackward 4 feet", new DriveStraightDistance(0.5, 48, Direction.BACKWARD));
		SmartDashboard.putData("drive to xy heading with p", new DriveToXYHeadingNoPIDTest());
	}

	public Joystick getRightJoystick() {
		return rightJoystick;
	}

	public Joystick getLeftJoystick() {
		return leftJoystick;
	}

	public Joystick getXboxController() {
		return xboxController;
	}

	public static double getRightJoystickX() {
		return rightJoystick.getX();
	}

	public static double getRightJoystickY() {
		return -rightJoystick.getY();
	}

	public static double getLeftJoystickX() {
		return leftJoystick.getX();
	}

	public static double getLeftJoystickY() {
		return -leftJoystick.getY();
	}

	// Only right JS is able to rotate
	public static double getRightJoystickRot() {
		return rightJoystick.getRawAxis(2);
	}

	public static double getXBoxTriggerL() {
		return xboxController.getRawAxis(2);
	}

	public static double getXBoxTriggerR() {
		return xboxController.getRawAxis(3);
	}

	public static boolean isXBoxDPadUp() {
		int pov = xboxController.getPOV();
		return pov >= 315 || pov <= 45;
	}

	public static boolean isXBoxDPadDown() {
		int pov = xboxController.getPOV();
		return pov >= 135 && pov <= 225;
	}

	public static Side getAllianceSwitchSide() {
		return DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'L' ? Side.LEFT : Side.RIGHT;
	}

	public static Side getScaleSide() {
		return DriverStation.getInstance().getGameSpecificMessage().charAt(1) == 'L' ? Side.LEFT : Side.RIGHT;
	}

	public static Side getOpposingSwitchSide() {
		return DriverStation.getInstance().getGameSpecificMessage().charAt(2) == 'L' ? Side.LEFT : Side.RIGHT;
	}

	public static DriverStation.Alliance getAllianceColor() {
		return DriverStation.getInstance().getAlliance();
	}
}
