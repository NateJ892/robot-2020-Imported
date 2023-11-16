package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RMap.intakeState;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

class feederArmSubSystem {
    private DoubleSolenoid feederArm;
    private DigitalInput fiberOpticBallSensor;
    private intakeState sub_sys_state;
    private VictorSPX feederMotor;

    feederArmSubSystem(intakeState initValue) {
        sub_sys_state = initValue;
        fiberOpticBallSensor = new DigitalInput(RMap.ballSens);

        feederArm = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 4, 1);
        feederMotor = new VictorSPX(RMap.intakeMotor);
    }

    public intakeState poll() {
        switch(sub_sys_state) {
            case ARM_RETRACTED:
                if (feederArm.get() != Value.kReverse) {
                    feederArm.set(Value.kReverse);
                }
                feederMotor.set(ControlMode.PercentOutput, 0.0);
                return sub_sys_state;

            case BALL_DETECTED:
                if (!fiberOpticBallSensor.get()) {
                    sub_sys_state = intakeState.NO_BALL_DETECTED;
                }
                if (feederArm.get() != Value.kForward) {
                    feederArm.set(Value.kForward);
                }
                feederMotor.set(ControlMode.PercentOutput, 1.0);
                return sub_sys_state;

            case NO_BALL_DETECTED:
                if (fiberOpticBallSensor.get()) {
                    sub_sys_state = intakeState.BALL_DETECTED;
                }
                if (feederArm.get() != Value.kForward) {
                    feederArm.set(Value.kForward);
                }
                feederMotor.set(ControlMode.PercentOutput, 1.0);
                return sub_sys_state;

            case FIRING_MODE:
                feederMotor.set(ControlMode.PercentOutput, 0.0);
                feederArm.set(Value.kForward);
                return sub_sys_state;
            default:
                return sub_sys_state;
        }
    }

    public intakeState requestState(intakeState newState) {
        sub_sys_state = newState;
        return sub_sys_state;
    }
}