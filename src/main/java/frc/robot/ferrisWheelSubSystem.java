package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

// import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RMap.ferrisWheelState;

class ferrisWheelSubSystem {
    private VictorSPX ferrisWheelMotor;
    private ferrisWheelState sub_sys_state;
    // private DigitalInput screwSensor;
    private DoubleSolenoid hood;
    private double ferrisWheelSpeed = -0.25;

    public ferrisWheelSubSystem(ferrisWheelState initState) {
        sub_sys_state = initState;
        ferrisWheelMotor = new VictorSPX(RMap.ferrisWheelMotor);
        // screwSensor = new DigitalInput(RMap.screwSens);
        hood = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 5); //forward is shooter, reverse is intake
    }

    public ferrisWheelState poll(XboxController x) {
        switch (sub_sys_state) {
            case INTAKE_MODE:
                hood.set(Value.kReverse);
                if (x.getLeftBumper()) {
                    ferrisWheelMotor.set(ControlMode.PercentOutput, ferrisWheelSpeed);
                } else {
                    ferrisWheelMotor.set(ControlMode.PercentOutput, 0.0);
                }
                return sub_sys_state;
            
            case FIRE_MODE:
                hood.set(Value.kForward);
                if (x.getLeftBumper()) {
                    ferrisWheelMotor.set(ControlMode.PercentOutput, ferrisWheelSpeed);
                } else {
                    ferrisWheelMotor.set(ControlMode.PercentOutput, 0.0);
                }
            
            default:
                return sub_sys_state;
        }
    }

    public ferrisWheelState changeState(ferrisWheelState newState) {
        sub_sys_state = newState;
        return sub_sys_state;
    }
}
