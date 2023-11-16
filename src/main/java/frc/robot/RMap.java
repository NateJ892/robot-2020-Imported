/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public class RMap 
{
    // Drive Train
    static final int CANLeftFrontMotor = 15; //USED TO BE 12
    static final int CANRightFrontMotor = 13;
    static final int CANLeftBackMotor = 14;
    static final int CANRightBackMotor = 10;

    static final int CANShooterMotorLeft = 2;
    static final int CANShooterMotorRight = 1;
    
    static final int CANLiftMotorBack = 11;
    static final int CANLiftMotorFront = 9;
    
    // DIO
    static final int screwSens = 1;
    static final int ballSens = 0;

    static final int intakeMotor = 12;
    static final int ferrisWheelMotor = 26;
    static final int shooterFeedMotor = 31;

    public static enum systemState {
        INTAKE_MODE,
        FIRING_MODE
    }

    public static enum intakeState {
        ARM_RETRACTED,
        BALL_DETECTED,
        NO_BALL_DETECTED,
        FIRING_MODE
    }

    public static enum ferrisWheelState {
        INTAKE_MODE,
        FIRE_MODE
    }

    public static enum shooterState {
        IDLE,
        SPINUP,
        READY,
        FIRING
    }
}
