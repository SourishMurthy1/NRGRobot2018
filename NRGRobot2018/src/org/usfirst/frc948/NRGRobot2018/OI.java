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

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import src.org.usfirst.frc948.NRGRobot2018.commandGroups.DriveSquare;
import src.org.usfirst.frc948.NRGRobot2018.commandGroups.DriveSquareWithTurning;
import src.org.usfirst.frc948.NRGRobot2018.commands.AutonomousCommand;
import src.org.usfirst.frc948.NRGRobot2018.commands.DriveStraightTimed;
import src.org.usfirst.frc948.NRGRobot2018.commands.ManualDrive;
import src.org.usfirst.frc948.NRGRobot2018.commands.ManualDriveStraight;
import src.org.usfirst.frc948.NRGRobot2018.commands.ManualStrafeStraight;
import src.org.usfirst.frc948.NRGRobot2018.commands.ResetSensors;
import src.org.usfirst.frc948.NRGRobot2018.commands.SetDriveScale;
import src.org.usfirst.frc948.NRGRobot2018.commands.TestPixyData;
import src.org.usfirst.frc948.NRGRobot2018.commands.TurnToHeading;
import src.org.usfirst.frc948.NRGRobot2018.subsystems.Drive;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public static Joystick leftJoystick;
	public static Joystick rightJoystick;
	public static Joystick xboxController;
	 
	public JoystickButton shiftGears;
	public JoystickButton driveStraight;
	public JoystickButton strafeStraight;

	public static SendableChooser<Command> chooser;
	
	public enum Side {
		LEFT, RIGHT;
	}

	public OI() {
		// Initializing Joysticks
		xboxController = new Joystick(2);

		rightJoystick = new Joystick(1);

		leftJoystick = new Joystick(0);
		
		// Initializing buttons AFTER the Joysticks
		shiftGears = new JoystickButton(leftJoystick, 1);
		driveStraight = new JoystickButton(leftJoystick, 2);
		strafeStraight = new JoystickButton(leftJoystick, 3);

		// Initialize commands after initializing buttons
		shiftGears.whenPressed(new SetDriveScale(Drive.SCALE_HIGH));
		shiftGears.whenReleased(new SetDriveScale(Drive.SCALE_LOW));
		driveStraight.whileHeld(new ManualDriveStraight());
		strafeStraight.whileHeld(new ManualStrafeStraight());

		// SmartDashboard Buttons
		chooser = new SendableChooser<>();
		chooser.addDefault("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("Auto mode", chooser);
        
		SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
		SmartDashboard.putData("ManualDrive", new ManualDrive());
		SmartDashboard.putData("Reset Sensor", new ResetSensors());
		SmartDashboard.putData("Turn To 90 Degrees", new TurnToHeading(90));
		SmartDashboard.putData("Turn To -90 Degrees", new TurnToHeading(-90));
		SmartDashboard.putData("Drive Straight 2 seconds", new DriveStraightTimed(0.5,2.0));
		SmartDashboard.putData("DriveSquareAuto", new DriveSquare());
		SmartDashboard.putData("DriveSquareWithTurning", new DriveSquareWithTurning());
		SmartDashboard.putData("TestPixyData", new TestPixyData());
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
	}

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

	public Joystick getLeftJoystick() {
		return leftJoystick;
	}

	public Joystick getRightJoystick() {
		return rightJoystick;
	}

	public Joystick getXboxController() {
		return xboxController;
	}

	public static double getX() {
		return rightJoystick.getX();
	}

	public static double getY() {
		return -leftJoystick.getY();
	}
	public static double getTriggerR() {
		return xboxController.getRawAxis(3);
	}
	public static double getTriggerL() {
		return xboxController.getRawAxis(2);
	}
	public static double getRot() {
		return leftJoystick.getRawAxis(2);
	}
	
	public static Side getAllianceSwitchSide(){
		return DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'L' ? Side.LEFT : Side.RIGHT;
	}
	
	public static Side getScaleSide(){
		return DriverStation.getInstance().getGameSpecificMessage().charAt(1) == 'L' ? Side.LEFT : Side.RIGHT;
	}
	
	public static Side getOppsingSwitchSide(){
		return DriverStation.getInstance().getGameSpecificMessage().charAt(2) == 'L' ? Side.LEFT : Side.RIGHT;
	}
	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}
