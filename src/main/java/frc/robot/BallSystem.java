/*
 * File Name: BallSystem.java
 * Author: Nathan Johnson
 * Date: 2023-11-04
 * Description: TO BE FILLED
 */

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.RMap.systemState;
import frc.robot.RMap.intakeState;
import frc.robot.RMap.shooterState;
import frc.robot.RMap.ferrisWheelState;

public class BallSystem {
    private systemState          SYSTEM;
    private feederArmSubSystem   fArmSys;
    private ferrisWheelSubSystem fWheelSys;
    private shooterSubSystem     sSystem;

    private XboxController controller;

    public BallSystem(XboxController x) {
        controller = x;
        SYSTEM = systemState.INTAKE_MODE;

        fArmSys = new feederArmSubSystem(intakeState.ARM_RETRACTED);
        fWheelSys = new ferrisWheelSubSystem(ferrisWheelState.INTAKE_MODE);
        sSystem = new shooterSubSystem(shooterState.IDLE);
    }

    public void pollSystem() {
        if (SYSTEM == systemState.INTAKE_MODE) {
            sSystem.changeState(shooterState.IDLE);
            fWheelSys.changeState(ferrisWheelState.INTAKE_MODE);
            fWheelSys.poll(controller);

            if (controller.getXButton()) {
                intakeState intakeCurrentState = fArmSys.poll();

                if (intakeCurrentState == intakeState.ARM_RETRACTED) {
                    fArmSys.requestState(intakeState.NO_BALL_DETECTED);
                }
            } else {
                fArmSys.requestState(intakeState.ARM_RETRACTED);
                fArmSys.poll();
            }

            shooterState shooterCurrentState = sSystem.poll();
            if (shooterCurrentState != shooterState.IDLE) {
                sSystem.changeState(shooterState.IDLE);
            }
        } else if (SYSTEM == systemState.FIRING_MODE) {
            fArmSys.requestState(intakeState.FIRING_MODE);
            sSystem.changeState(shooterState.SPINUP);
            fWheelSys.changeState(ferrisWheelState.FIRE_MODE);

            fArmSys.poll();
            fWheelSys.poll(controller);

            if (controller.getRightTriggerAxis() > 0.5) {
                if (sSystem.poll() == shooterState.READY) {
                    sSystem.changeState(shooterState.FIRING);
                }
            } else {
                if (sSystem.poll() == shooterState.FIRING) {
                    sSystem.changeState(shooterState.READY);
                }
            }
        }

        if (controller.getYButtonReleased()) {
            if (SYSTEM == systemState.INTAKE_MODE) {
                SYSTEM = systemState.FIRING_MODE;
            } else {
                SYSTEM = systemState.INTAKE_MODE;
            }
        }
    }

    public void changeState(systemState newRequestedState) {
        SYSTEM = newRequestedState;
    }
}
