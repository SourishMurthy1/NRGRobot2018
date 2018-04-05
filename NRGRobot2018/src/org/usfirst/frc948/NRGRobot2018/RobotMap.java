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

import org.usfirst.frc948.NRGRobot2018.utilities.Arduino;
import org.usfirst.frc948.NRGRobot2018.utilities.ContinuousGyro;
import org.usfirst.frc948.NRGRobot2018.utilities.PreferenceKeys;
import org.usfirst.frc948.NRGRobot2018.vision.I2Cwrapper;
import org.usfirst.frc948.NRGRobot2018.vision.IPixyLink;
import org.usfirst.frc948.NRGRobot2018.vision.PixyCam;
import org.usfirst.frc948.NRGRobot2018.vision.SPIwrapper;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name.
 * This provides flexibility changing wiring, makes checking the wiring easier and significantly
 * reduces the number of magic numbers floating around.
 */

public class RobotMap {
	public static Victor driveLeftFrontMotor;
	public static Victor driveLeftRearMotor;
	public static Victor driveRightFrontMotor;
	public static Victor driveRightRearMotor;
	public static MecanumDrive driveMecanumDrive;

	public static Victor cubeLifterMotor;
	public static Victor cubeTilterMotor;
	
	public static Victor acquirerRightMotor;
	public static Victor acquirerLeftMotor;

	public static Victor climberMotor;

	public static Encoder xEncoder;
	public static Encoder yEncoder;
	public static Encoder leftFrontEncoder;
	public static Encoder leftRearEncoder;
	public static Encoder rightFrontEncoder;
	public static Encoder rightRearEncoder;
	
	private static double mecEncoderLFRatio;
	private static double mecEncoderLRRatio;
	private static double mecEncoderRFRatio;
	private static double mecEncoderRRRatio;
	
	public static final double MEC_ENCODER_INCHES_PER_TICK_SHARED = 0.0227;
	public static final double DEF_MEC_ENCODER_LF_RATIO_PRACTICE = 0.0227;
	public static final double DEF_MEC_ENCODER_LR_RATIO_PRACTICE = 0.0227;
	public static final double DEF_MEC_ENCODER_RF_RATIO_PRACTICE = 0.0227;
	public static final double DEF_MEC_ENCODER_RR_RATIO_PRACTICE = 0.0227;
	public static final double DEF_MEC_ENCODER_LF_RATIO_COMP = 0.0227;
	public static final double DEF_MEC_ENCODER_LR_RATIO_COMP = 0.0227;
	public static final double DEF_MEC_ENCODER_RF_RATIO_COMP = 0.0227;
	public static final double DEF_MEC_ENCODER_RR_RATIO_COMP = 0.0227;

	public static Encoder cubeLiftEncoder;
	public static Encoder cubeTiltEncoder;
	
	public static DigitalInput lifterLowerLimitSwitch;
	public static DigitalInput lifterUpperLimitSwitch;
	public static DigitalInput cubeDetectSwitch;

	public static AHRS navx;
	public static ContinuousGyro gyro;
	
	public static IPixyLink pixyLink;
	public static PixyCam pixy;
	
	public static I2Cwrapper arduinoLink;
	public static Arduino arduino;
	
	public static UsbCamera USBCamera;
	
	public static void init() {
		if (Robot.preferences.getBoolean(PreferenceKeys.USING_PRACTICE_BOT, true)) {
			mecEncoderLFRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_LF_RATIO_PRACTICE, DEF_MEC_ENCODER_LF_RATIO_PRACTICE);
			mecEncoderLRRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_LR_RATIO_PRACTICE, DEF_MEC_ENCODER_LR_RATIO_PRACTICE);
			mecEncoderRFRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_RF_RATIO_PRACTICE, DEF_MEC_ENCODER_RF_RATIO_PRACTICE);
			mecEncoderRRRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_RR_RATIO_PRACTICE, DEF_MEC_ENCODER_RR_RATIO_PRACTICE);
		} else {
			mecEncoderLFRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_LF_RATIO_COMP, DEF_MEC_ENCODER_LF_RATIO_COMP);
			mecEncoderLRRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_LR_RATIO_COMP, DEF_MEC_ENCODER_LR_RATIO_COMP);
			mecEncoderRFRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_RF_RATIO_COMP, DEF_MEC_ENCODER_RF_RATIO_COMP);
			mecEncoderRRRatio = Robot.preferences.getDouble(PreferenceKeys.MEC_ENCODER_RR_RATIO_COMP, DEF_MEC_ENCODER_RR_RATIO_COMP);
		}
		
		driveLeftFrontMotor = new Victor(1);
		driveLeftRearMotor = new Victor(3);
		driveRightFrontMotor = new Victor(0);
		driveRightRearMotor = new Victor(2);
		
		driveMecanumDrive = new MecanumDrive(driveLeftFrontMotor, driveLeftRearMotor, driveRightFrontMotor, driveRightRearMotor);
		driveMecanumDrive.setSafetyEnabled(false);
		driveMecanumDrive.setExpiration(0.1);
		driveMecanumDrive.setMaxOutput(1.0);

		cubeLifterMotor = new Victor(4);
		
		cubeTilterMotor = new Victor(7);
		cubeTilterMotor.setInverted(false);
		
		acquirerLeftMotor = new Victor(5);
		acquirerRightMotor = new Victor(6);
		acquirerLeftMotor.setInverted(true);
		
		climberMotor = new Victor(8);

		xEncoder = new Encoder(2, 3, true); // positive is right
		yEncoder = new Encoder(0, 1, true); // positive is forward
		leftFrontEncoder = new Encoder(11, 12, false);
		rightFrontEncoder = new Encoder(13, 18, true);
		leftRearEncoder = new Encoder(19, 20, false);
		rightRearEncoder = new Encoder(21, 22, true);
		
		cubeLiftEncoder = new Encoder(6, 7, false);
		cubeTiltEncoder = new Encoder(8, 9, false);
		
		xEncoder.setDistancePerPulse(0.0457); // inches per pulse, encoder is slipping, 0.0519 -> previous value
		yEncoder.setDistancePerPulse(0.0501); // .0544 -> prev value
		
		leftFrontEncoder.setDistancePerPulse(0.02534833333333333333333333333333);
		rightFrontEncoder.setDistancePerPulse(0.02534833333333333333333333333333);
		leftRearEncoder.setDistancePerPulse(0.02534833333333333333333333333333);
		rightRearEncoder.setDistancePerPulse(0.02534833333333333333333333333333);
		
		cubeLiftEncoder.setDistancePerPulse(1.0);
		cubeTiltEncoder.setDistancePerPulse(1.0);
		
		navx = new AHRS(SPI.Port.kMXP);
		gyro = new ContinuousGyro(navx);
		
		lifterLowerLimitSwitch = new DigitalInput(4);
		lifterUpperLimitSwitch = new DigitalInput(5);
		cubeDetectSwitch = new DigitalInput(10);
		
		pixyLink = new SPIwrapper(SPI.Port.kOnboardCS0);
		pixy = new PixyCam(pixyLink);
		pixy.startVisionThread();
		
		arduinoLink = new I2Cwrapper(I2C.Port.kOnboard, 84);
		arduino = new Arduino(arduinoLink);
		
		// Will show up on ShuffleBoard/SmartDashboard after calling
//		USBCamera = CameraServer.getInstance().startAutomaticCapture();
	}
}
