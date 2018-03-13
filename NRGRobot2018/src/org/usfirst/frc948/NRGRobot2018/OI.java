
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

import org.usfirst.frc948.NRGRobot2018.commandGroups.TiltAcquirerAndEject;
import org.usfirst.frc948.NRGRobot2018.commands.DriveStraightDistance;
import org.usfirst.frc948.NRGRobot2018.commands.DriveToCubeNoPID;
import org.usfirst.frc948.NRGRobot2018.commands.DriveToXYHeadingPIDTest;
import org.usfirst.frc948.NRGRobot2018.commands.InterruptCommands;
import org.usfirst.frc948.NRGRobot2018.commands.LiftToHeight;
import org.usfirst.frc948.NRGRobot2018.commands.ManualClimb;
import org.usfirst.frc948.NRGRobot2018.commands.ManualDrive;
import org.usfirst.frc948.NRGRobot2018.commands.ManualDriveStraight;
import org.usfirst.frc948.NRGRobot2018.commands.ManualStrafeStraight;
import org.usfirst.frc948.NRGRobot2018.commands.ResetSensors;
import org.usfirst.frc948.NRGRobot2018.commands.SetDriveScale;
import org.usfirst.frc948.NRGRobot2018.commands.TiltAcquirerToAngle;
import org.usfirst.frc948.NRGRobot2018.commands.TurnToHeading;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeLifter;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeTilter;
import org.usfirst.frc948.NRGRobot2018.subsystems.Drive;
import org.usfirst.frc948.NRGRobot2018.subsystems.Drive.Direction;
import org.usfirst.frc948.NRGRobot2018.utilities.MathUtil;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick leftJoystick;
	public static Joystick rightJoystick;
	public static XboxController xboxController;
	public static Joystick arduinoJoystick;

	public JoystickButton leftShiftGears;
	public JoystickButton driveStraight;
	public JoystickButton strafeStraight;
	public JoystickButton rightShiftGears;
	public JoystickButton climberButton;
	public JoystickButton interruptButton;
	public JoystickButton driveToCube;
//	public JoystickButton tiltAcquirerAndEjectCube;
	
	public static SendableChooser<Command> chooser;

	public enum Side {
		LEFT, RIGHT;
	}

	public OI() {
		// Initializing Joysticks
		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		xboxController = new XboxController(2);
		arduinoJoystick = new Joystick(3);

		// Initializing buttons AFTER the Joysticks
		leftShiftGears = new JoystickButton(leftJoystick, 1);
		driveStraight = new JoystickButton(leftJoystick, 2);
		strafeStraight = new JoystickButton(leftJoystick, 3);
		interruptButton = new JoystickButton(leftJoystick, 6);
		rightShiftGears = new JoystickButton(rightJoystick, 1);// drive team needed it for both
		driveToCube = new JoystickButton(rightJoystick, 2);// joysticks

		// arduino buttons
		 climberButton = new JoystickButton(arduinoJoystick, 10);
		 
		// xbox buttons
		// climberButton = new JoystickButton(xboxController, 8);
//		 tiltAcquirerAndEjectCube = new JoystickButton(xboxController, 1);

		// Initialize commands after initializing buttons
		leftShiftGears.whenPressed(new SetDriveScale(Drive.SCALE_LOW));
		leftShiftGears.whenReleased(new SetDriveScale(Drive.SCALE_HIGH));
		rightShiftGears.whenPressed(new SetDriveScale(Drive.SCALE_LOW));
		rightShiftGears.whenReleased(new SetDriveScale(Drive.SCALE_HIGH));
		driveStraight.whileHeld(new ManualDriveStraight());
		strafeStraight.whileHeld(new ManualStrafeStraight());
		climberButton.whileHeld(new ManualClimb(0.7));
		interruptButton.whenPressed(new InterruptCommands());
		driveToCube.whenPressed(new DriveToCubeNoPID(false));
//		tiltAcquirerAndEjectCube.whenPressed(new TiltAcquirerAndEject(45, 1, 0.5));
		
		// SmartDashboard Buttons
		SmartDashboard.putData("Reset Sensors", new ResetSensors());

		SmartDashboard.putData("Lift to Scale?", new LiftToHeight(CubeLifter.SCALE_MEDIUM));
		SmartDashboard.putData("Lift to Switch?", new LiftToHeight(CubeLifter.SWITCH_LEVEL));
		SmartDashboard.putData("Set Lift height to zero?", new LiftToHeight(CubeLifter.STOWED));
		
		SmartDashboard.putData("Tilt acquirer and eject cube", new TiltAcquirerAndEject(45, 1, 0.5));

		SmartDashboard.putData("ManualDrive", new ManualDrive());
		SmartDashboard.putData("driveStraightDistance 20 feet", new DriveStraightDistance(1, 240, Direction.FORWARD));
		SmartDashboard.putData("Drive to XY Heading Test", new DriveToXYHeadingPIDTest());
		SmartDashboard.putData("Drive to Cube NoPID", new DriveToCubeNoPID(false));
//		SmartDashboard.putData("StrafeStraightDistance 4 feet", new DriveStraightDistance(1, 48, Direction.RIGHT));
//		SmartDashboard.putData("driveStraightDistanceBackward 4 feet",
//				new DriveStraightDistance(0.5, 48, Direction.BACKWARD));
		SmartDashboard.putData("CubeTiltDown", new TiltAcquirerToAngle(CubeTilter.TILTER_DOWN));
		SmartDashboard.putData("CubeTiltUp", new TiltAcquirerToAngle(CubeTilter.TILTER_UP));
		
		SmartDashboard.putData("Turn To 90 Degrees", new TurnToHeading(90));
		SmartDashboard.putData("Turn To -90 Degrees", new TurnToHeading(-90));

		SmartDashboard.putData("Set to high gear", new SetDriveScale(Drive.SCALE_HIGH));
		SmartDashboard.putData("Set to low gear", new SetDriveScale(Drive.SCALE_LOW));
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
		return MathUtil.deadband(xboxController.getRawAxis(2), 0.1);
	}

	public static double getXBoxTriggerR() {
		return MathUtil.deadband(xboxController.getRawAxis(3), 0.1);
	}

	public static boolean isXBoxDPadUp() {
		int pov = xboxController.getPOV();
		return pov == 0 || pov == 45 || pov == 315;
	}

	public static boolean isXBoxDPadDown() {
		int pov = xboxController.getPOV();
		return pov >= 135 && pov <= 225;
	}
	
	public static DriverStation.Alliance getAllianceColor() {
		return DriverStation.getInstance().getAlliance();
	}

	// These methods return the location of the alliance's switch/scale plates,
	// relative to the alliance facing the field from behind the alliance wall
	public static Side getAllianceSwitchSide() {
		return DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'L' ? Side.LEFT : Side.RIGHT;
	}

	public static Side getScaleSide() {
		return DriverStation.getInstance().getGameSpecificMessage().charAt(1) == 'L' ? Side.LEFT : Side.RIGHT;
	}

	public static Side getOpposingSwitchSide() {
		return DriverStation.getInstance().getGameSpecificMessage().charAt(2) == 'L' ? Side.LEFT : Side.RIGHT;
	}
}
