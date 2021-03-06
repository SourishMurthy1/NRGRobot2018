package org.usfirst.frc948.NRGRobot2018.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DelaySeconds extends Command {
	private Timer timer;
	private double desiredSeconds;

	public DelaySeconds(double seconds) {
		this.desiredSeconds = seconds;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("DelaySeconds(" + desiredSeconds + ")");
		timer = new Timer();
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return timer.get() >= desiredSeconds;
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("DelaySeconds has ended");
		timer.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
