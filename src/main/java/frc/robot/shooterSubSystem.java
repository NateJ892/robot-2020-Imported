package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.RMap.shooterState;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.ctre.phoenix.motorcontrol.ControlMode;

class shooterSubSystem {
    private CANSparkMax shooterMotorLeft, shooterMotorRight;
    private MotorControllerGroup uppperMotors;
    private shooterState sub_sys_state;
    private VictorSPX bottomMotor;

    public shooterSubSystem(shooterState initState) {
        sub_sys_state = initState;

        shooterMotorLeft    = new CANSparkMax(RMap.CANShooterMotorLeft, MotorType.kBrushless);
        shooterMotorRight   = new CANSparkMax(RMap.CANShooterMotorRight, MotorType.kBrushless);
        bottomMotor         = new VictorSPX(RMap.shooterFeedMotor);

        shooterMotorLeft .setIdleMode(IdleMode.kCoast);
        shooterMotorRight.setIdleMode(IdleMode.kCoast);

        uppperMotors = new MotorControllerGroup(shooterMotorLeft, shooterMotorRight);
    }

    public shooterState poll() {
        if (sub_sys_state == shooterState.IDLE) {
            uppperMotors.set(0.0);
            bottomMotor.set(ControlMode.PercentOutput, 0.0);
        }

        if (sub_sys_state == shooterState.SPINUP) {
            final double shooterRampRate = 0.04;
            
            if (uppperMotors.get() <= 0.9) {
                uppperMotors.set(uppperMotors.get() + shooterRampRate);
            } else {
                sub_sys_state = shooterState.READY;
            }
        }

        if (sub_sys_state == shooterState.READY) {
            uppperMotors.set(1.0);
            bottomMotor.set(ControlMode.PercentOutput, 0.0);
        }

        if (sub_sys_state == shooterState.FIRING) {
            uppperMotors.set(1.0);
            bottomMotor.set(ControlMode.PercentOutput, 1.0);
        }

        return sub_sys_state;
    }

    public shooterState changeState(shooterState newState) {
        switch (newState) {
            case IDLE:
                sub_sys_state = shooterState.IDLE;
                break;
            
            case SPINUP:
                if (sub_sys_state == shooterState.IDLE) {
                    sub_sys_state = shooterState.SPINUP;
                }
                break;

            case READY:
                if (sub_sys_state == shooterState.IDLE) {
                    sub_sys_state = shooterState.SPINUP;
                } else if (sub_sys_state == shooterState.FIRING) {
                    sub_sys_state = shooterState.READY;
                }
                break;
            
            case FIRING:
                if (sub_sys_state == shooterState.IDLE) {
                    sub_sys_state = shooterState.SPINUP;
                } else if (sub_sys_state == shooterState.READY) {
                    sub_sys_state = shooterState.FIRING;
                }
                break;
        }
        return sub_sys_state;
    }
}
