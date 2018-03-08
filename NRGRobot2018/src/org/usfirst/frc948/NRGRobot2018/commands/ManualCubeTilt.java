package org.usfirst.frc948.NRGRobot2018.commands;

import org.usfirst.frc948.NRGRobot2018.OI;
import org.usfirst.frc948.NRGRobot2018.Robot;
import org.usfirst.frc948.NRGRobot2018.RobotMap;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeLifter;
import org.usfirst.frc948.NRGRobot2018.subsystems.CubeTilter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualCubeTilt extends Command {
	private double prevPower;

	public ManualCubeTilt() {
		requires(Robot.cubeTilter);
	}

	protected void initialize() {
		prevPower = Double.MAX_VALUE;
	}

	protected void execute() {
		double power = 0;

		if (OI.isXBoxDPadUp()) {
			power = CubeTilter.TILT_UP_POWER;
			Robot.cubeTilter.rawTilt(power);
		} else if (OI.isXBoxDPadDown()) {
			power = CubeTilter.TILT_DOWN_POWER;
			Robot.cubeTilter.rawTilt(power);
		} else {
//
//			if (prevPower != 0) {
//				Robot.cubeTilter.tiltToAnglePIDIntialize(RobotMap.cubeTiltEncoder.getDistance(), 1);
//			}
//			Robot.cubeTilter.tiltToAnglePIDExecute();
		}

		prevPower = power;
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.cubeTilter.stop();
	}

	protected void interrupted() {
		end();
	}
}
