package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends TimedRobot {
    XboxController ckController;
    DriveTrain ckDrive;
    BallSystem ckBall;

    @Override
    public void robotInit() {
        ckController = new XboxController(0);
        ckDrive = new DriveTrain();
        ckBall = new BallSystem(ckController);

        CameraServer.startAutomaticCapture().setFPS(15);
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        ckDrive.teleDriveCartesian(-ckController.getLeftY(), ckController.getRightX(), ckController.getLeftX());
        ckBall.pollSystem();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }
}
