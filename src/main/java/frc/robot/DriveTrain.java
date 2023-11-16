package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.XboxController;

public class DriveTrain {
  private final double joystickDeadzone = 0.06;
  private final double drivetrainMaxSpeed = 0.8;
  private final double maxSpeedAccel = 0.005;
  private final double maxSpeedDecel = maxSpeedAccel * 2.0;

  ADXRS450_Gyro ckGyro;
  CANSparkMax leftFrontMotor,
      rightFrontMotor,
      leftBackMotor,
      rightBackMotor;

  MecanumDrive ckDrive;
  XboxController ckController;

  double speedF = 0.0; // forward speed
  double speedR = 0.0; // rotation speed
  double speedS = 0.0; // strafe speed

  public DriveTrain() {

    // Init Motors w/ CAN BUS IDs & Motor Type
    leftFrontMotor = new CANSparkMax(RMap.CANLeftFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(RMap.CANLeftBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    rightFrontMotor = new CANSparkMax(RMap.CANRightFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(RMap.CANRightBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);

    // Engage motor brakes when robot is idle
    leftFrontMotor.setIdleMode(IdleMode.kBrake);
    leftBackMotor.setIdleMode(IdleMode.kBrake);
    rightFrontMotor.setIdleMode(IdleMode.kBrake);
    rightBackMotor.setIdleMode(IdleMode.kBrake);

    ckDrive = new MecanumDrive(
        leftFrontMotor,
        leftBackMotor,
        rightFrontMotor,
        rightBackMotor);
    ckDrive.setDeadband(0.05);

    // Creating the gyro
    ckGyro = new ADXRS450_Gyro();
  }

  public void teleDriveCartesian(double forward, double rotation, double strafe) {
    speedF = applyDampen(applyDeadBand(forward), speedF);
    speedS = applyDampen(applyDeadBand(strafe), speedS);
    speedR = applyDampen(applyDeadBand(rotation), speedR);

    ckDrive.driveCartesian(forward, strafe, speedR, Rotation2d.fromDegrees(0));
  }

  private double applyDeadBand(double value) {
    if (Math.abs(value) > joystickDeadzone) {
      if (value > 0.0) {
        if (value >= drivetrainMaxSpeed) {
          // Your at max speed, keep it there
          return drivetrainMaxSpeed;
        } else
          return (value - joystickDeadzone) / (drivetrainMaxSpeed - joystickDeadzone); // scale the speed up

      } else {
        if (value < -drivetrainMaxSpeed) {
          // Your at minimum speed, keep it there
          return -drivetrainMaxSpeed;
        } else {
          return (value + joystickDeadzone) / (drivetrainMaxSpeed - joystickDeadzone); // scale the speed down
        }
      }
    } else {
      return 0.0;
    }
  }

  public double applyDampen(double proposedValue, double currentSpeed) {
    // Apply Dapening
    if (currentSpeed >= 0.0) {
      // Going Forward
      if (proposedValue > currentSpeed) {
        // Accelerating Forward
        proposedValue = Math.min(proposedValue, currentSpeed + drivetrainMaxSpeed);
      } else {
        proposedValue = Math.max(proposedValue, currentSpeed - maxSpeedDecel); // Decelerating Forward
      }
    } else {
      // Going Backwards
      if (proposedValue < currentSpeed) {
        // Accelerating Backwards
        proposedValue = Math.max(proposedValue, currentSpeed - drivetrainMaxSpeed);
      } 
      else {
        proposedValue = Math.min(proposedValue, currentSpeed + maxSpeedDecel); // Decelerating Backward
      }
    }
    return proposedValue;
  }
}
