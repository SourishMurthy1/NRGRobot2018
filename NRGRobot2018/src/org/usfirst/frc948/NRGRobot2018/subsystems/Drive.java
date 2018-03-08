// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc948.NRGRobot2018.subsystems;

import org.usfirst.frc948.NRGRobot2018.Robot;
import org.usfirst.frc948.NRGRobot2018.RobotMap;
import org.usfirst.frc948.NRGRobot2018.commands.ManualDrive;
import org.usfirst.frc948.NRGRobot2018.utilities.PreferenceKeys;
import org.usfirst.frc948.NRGRobot2018.utilities.SimplePIDController;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements PIDOutput {
	public enum Direction {
		FORWARD, BACKWARD, LEFT, RIGHT
	}

	// private PIDController drivePIDController;
	private SimplePIDController xPIDController;
	private SimplePIDController yPIDController;
	private SimplePIDController turnPIDController;
	private SimplePIDController drivePIDController;
	private int cyclesOnTarget;
	private volatile double drivePIDOutput = 0;
	private static final int REQUIRED_CYCLES_ON_TARGET = 3;

	public final static double DEFAULT_TURN_P = 0.18;
	public final static double DEFAULT_TURN_I = 0.6;
	public final static double DEFAULT_TURN_D = 0.0135;

	public final static double DEFAULT_DRIVE_Y_P = 0.24;
	public final static double DEFAULT_DRIVE_Y_I = 0.6;
	public final static double DEFAULT_DRIVE_Y_D = 0.055;

	public final static double DEFAULT_DRIVE_X_P = 0.5;
	public final static double DEFAULT_DRIVE_X_I = 0.5;
	public final static double DEFAULT_DRIVE_X_D = 0;

	public final static double DEFAULT_DRIVE_X_POWER = 1.0;
	public final static double DEFAULT_DRIVE_Y_POWER = 0.8;
	public final static double DEFAULT_DRIVE_TURN_POWER = 1.0;

	public final static double SCALE_HIGH = 1.0;
	public final static double SCALE_LOW = 0.5;
	public double scale = SCALE_LOW;
	public static final double DEF_MAX_VEL_CHANGE = 0.1;

	private double lastVelX = 0.0;
	private double lastVelY = 0.0;

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new ManualDrive());
	}

	private SimplePIDController createPIDController(double setpoint, double tolerance, double xP, double xI, double xD,
			double xMaxPower) {
		return new SimplePIDController(xP, xI, xD).setOutputRange(-xMaxPower, xMaxPower).setAbsoluteTolerance(tolerance)
				.setSetpoint(setpoint).start();
	}

	public void drivePIDControllerInit(double p, double i, double d, double setpoint, double tolerance) {
		drivePIDController = new SimplePIDController(p, i, d, false, RobotMap.gyro, this);

		drivePIDController.setOutputRange(-1, 1);
		drivePIDController.setAbsoluteTolerance(tolerance);
		drivePIDController.setSetpoint(setpoint);

		drivePIDController.start();
		cyclesOnTarget = 0;
	}

	public void xPIDControllerInit(double setpoint, double tolerance) {
		double xP = Robot.preferences.getDouble(PreferenceKeys.DRIVE_X_P, DEFAULT_DRIVE_X_P);
		double xI = Robot.preferences.getDouble(PreferenceKeys.DRIVE_X_I, DEFAULT_DRIVE_X_I);
		double xD = Robot.preferences.getDouble(PreferenceKeys.DRIVE_X_D, DEFAULT_DRIVE_X_D);
		double xMaxPower = Robot.preferences.getDouble(PreferenceKeys.DRIVE_X_MAX_POWER, DEFAULT_DRIVE_X_POWER);

		xPIDController = createPIDController(setpoint, tolerance, xP, xI, xD, xMaxPower);

		cyclesOnTarget = 0;
	}

	public void yPIDControllerInit(double setpoint, double tolerance) {
		double yP = Robot.preferences.getDouble(PreferenceKeys.DRIVE_Y_P, DEFAULT_DRIVE_Y_P);
		double yI = Robot.preferences.getDouble(PreferenceKeys.DRIVE_Y_I, DEFAULT_DRIVE_Y_I);
		double yD = Robot.preferences.getDouble(PreferenceKeys.DRIVE_Y_D, DEFAULT_DRIVE_Y_D);
		double yMaxPower = Robot.preferences.getDouble(PreferenceKeys.DRIVE_Y_MAX_POWER, DEFAULT_DRIVE_Y_POWER);
		yPIDController = createPIDController(setpoint, tolerance, yP, yI, yD, yMaxPower);
	}

	public void turnPIDControllerInit(double setpoint, double tolerance) {
		double turnP = Robot.preferences.getDouble(PreferenceKeys.TURN_P_TERM, DEFAULT_TURN_P);
		double turnI = Robot.preferences.getDouble(PreferenceKeys.TURN_I_TERM, DEFAULT_TURN_I);
		double turnD = Robot.preferences.getDouble(PreferenceKeys.TURN_D_TERM, DEFAULT_TURN_D);
		double turnMaxPower = Robot.preferences.getDouble(PreferenceKeys.DRIVE_TURN_MAX_POWER,
				DEFAULT_DRIVE_TURN_POWER);

		turnPIDController = createPIDController(setpoint, tolerance, turnP, turnI, turnD, turnMaxPower);
	}

	public void driveHeadingPIDInit(double desiredHeading, double tolerance) {
		drivePIDControllerInit(Robot.preferences.getDouble(PreferenceKeys.TURN_P_TERM, DEFAULT_TURN_P),
				Robot.preferences.getDouble(PreferenceKeys.TURN_I_TERM, DEFAULT_TURN_I),
				Robot.preferences.getDouble(PreferenceKeys.TURN_D_TERM, DEFAULT_TURN_D), desiredHeading, tolerance);
	}

	public double turnPIDControllerExecute(double dTurn) {
		return turnPIDController.update(dTurn);
	}

	public double xPIDControllerExecute(double dX) {
		return xPIDController.update(dX);
	}

	public double yPIDControllerExecute(double dY) {
		return yPIDController.update(dY);
	}

	public void driveHeadingPIDExecute(double velX, double velY) {
		drivePIDController.update();
		double currentPIDOutput = drivePIDOutput;

		SmartDashboard.putNumber("Turn To Heading PID Error", drivePIDController.getError());
		SmartDashboard.putNumber("Turn To Heading PID Output", currentPIDOutput);

		rawDriveCartesian(velX, velY, currentPIDOutput);
	}

	public void driveHeadingPIDEnd() {
		drivePIDController = null;
		stop();
		SmartDashboard.putNumber("DriveOnHeadingEnd gyro", RobotMap.gyro.getAngle());
	}

	public boolean drivePIDControllerOnTarget() {
		if (drivePIDController.onTarget()) {
			cyclesOnTarget++;
		} else {
			cyclesOnTarget = 0;
		}
		return cyclesOnTarget >= REQUIRED_CYCLES_ON_TARGET;
	}

	public boolean xPIDControllerOnTarget() {
		return xPIDController.onTarget();
	}

	public boolean yPIDControllerOnTarget() {
		return yPIDController.onTarget();
	}

	public boolean turnPIDControllerOnTarget() {
		return turnPIDController.onTarget();
	}

	public boolean allControllersOnTarget(boolean isFinalWaypoint) {
		if (xPIDControllerOnTarget() && yPIDControllerOnTarget() && turnPIDControllerOnTarget()) {
			cyclesOnTarget++;
		} else {
			cyclesOnTarget = 0;
		}
		return cyclesOnTarget >= (isFinalWaypoint ? REQUIRED_CYCLES_ON_TARGET : 1);
	}

	public void driveCartesian(double currVelX, double currVelY, double currRot) {
		double maxVelDifference = Robot.preferences.getDouble(PreferenceKeys.MAX_VEL_CHANGE, DEF_MAX_VEL_CHANGE);
		double velXChange = currVelX - lastVelX;
		double velYChange = currVelY - lastVelY;

		if (Math.abs(velXChange) > maxVelDifference) {
			currVelX = lastVelX + Math.copySign(maxVelDifference, velXChange);
		}

		if (Math.abs(velYChange) > maxVelDifference) {
			currVelY = lastVelY + Math.copySign(maxVelDifference, velYChange);
		}

		rawDriveCartesian(currVelX, currVelY, currRot);
	}

	public void rawDriveCartesian(double velX, double velY, double rot) {
		lastVelX = velX;
		lastVelY = velY;

		velX *= scale;
		velY *= scale;
		rot *= scale;
		RobotMap.driveMecanumDrive.driveCartesian(velX, velY, rot);
		SmartDashboard.putNumber("velY", velY);
		SmartDashboard.putNumber("velX", velX);
	}

	public void stop() {
		lastVelX = 0;
		lastVelY = 0;

		RobotMap.driveMecanumDrive.stopMotor();
	}

	public void setScale(double s) {
		scale = s;
	}

	@Override
	public void pidWrite(double output) {
		drivePIDOutput = output;
	}

	public double getXError() {
		return xPIDController.getError();
	}

	public double getYError() {
		return yPIDController.getError();
	}

	public double getTurnError() {
		return turnPIDController.getError();
	}
}
