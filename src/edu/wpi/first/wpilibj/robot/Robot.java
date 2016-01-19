/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.robot;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.subsystems.Drive;
import org.team3309.lib.controllers.generic.PIDPositionController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.subsystems.Drive;

/**
 * The VM is configured to automatically run this class, and to call the+
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    XboxController controller = new XboxController(1);
    private Counter shooter = new Counter(1);
    private double curVel = 0.0;
    private double lastVel = 0.0;
    private PIDPositionController pidController = new PIDPositionController(0.009, 0.0, 0.0);

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        shooter.start();
        SmartDashboard.putNumber("kp", .009);
        SmartDashboard.putNumber("ki", 0);
        SmartDashboard.putNumber("kd", 0);
        SmartDashboard.putNumber("ka", 0);
        SmartDashboard.putNumber("kv", .006);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    private double aimAcc = 0;
    private double aimVel = 0;
    private double kA = 0;
    private double kV = 0.006;
    private double offset = 0.0;
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        System.out.println("Reading: " + 1/shooter.getPeriod());
        //Drive.getInstance().runDrive(controllergetLeftY(), controller.getRightX());
        /*if (Controls.driverController.getAButton()) {
            Drive.getInstance().leftMiddle.set(0.63);
            Drive.getInstance().leftFront.set(0.63);
        }else if(Controls.driverController.getBButton()) {
            Drive.getInstance().leftMiddle.set(0.6);
            Drive.getInstance().leftFront.set(0.6);
        }else if(Controls.driverController.getXButton()) {
            Drive.getInstance().leftMiddle.set(0.7);
            Drive.getInstance().leftFront.set(0.7);
        }else if(Controls.driverController.getYButton()) {
            Drive.getInstance().leftMiddle.set(0.8);
            Drive.getInstance().leftFront.set(0.8);
        }else if(Controls.driverController.getRightBumper()) {
            Drive.getInstance().leftMiddle.set(0.9);
            Drive.getInstance().leftFront.set(0.9);
        }else if(Controls.driverController.getLeftBumper()) {
            Drive.getInstance().leftMiddle.set(1.0);
            Drive.getInstance().leftFront.set(1.0);
        }else {
            Drive.getInstance().leftMiddle.set(0.0);
            Drive.getInstance().leftFront.set(0.0);
        
        */
        curVel = 1/this.shooter.getPeriod();
        if (Controls.driverController.getAButton()) {
            aimVel = 75;
        }else if (Controls.driverController.getBButton()) {
            aimVel = 80;
        }else if (Controls.driverController.getXButton()) {
            aimVel = 85;
        }else if (Controls.driverController.getYButton()) {
            aimVel = 90;
        }else if (Controls.driverController.getRightBumper()) {
            aimVel = 100;
        }else {
            aimVel  = 0;
            offset =0;
        }
        
        if (Controls.driverController.getRightBumper()) {
            offset += 5;
        }else if (Controls.driverController.getLeftBumper()) {
            offset -= 5;
        }
        InputState state = new InputState();
        state.setError((aimVel + offset) - curVel );
        double value = kA * aimAcc + kV * aimVel + this.pidController.getOutputSignal(state).getMotor();
        
        this.pidController.kP = SmartDashboard.getNumber("kp");
        this.pidController.kI = SmartDashboard.getNumber("ki");
        this.pidController.kD = SmartDashboard.getNumber("kd");
        this.kA = SmartDashboard.getNumber("ka");
        this.kV = SmartDashboard.getNumber("kv");
        SmartDashboard.putNumber("value", value);
        SmartDashboard.putNumber("error",(aimVel + offset) - curVel);
        SmartDashboard.putNumber("vel", curVel);
        SmartDashboard.putNumber("aim", aimVel + offset);

        if(aimVel != 0) {
            Drive.getInstance().leftMiddle.set(value);
            Drive.getInstance().leftFront.set(value);
            System.out.println("setting");
        }else {
            Drive.getInstance().leftMiddle.set(0.0);
            Drive.getInstance().leftFront.set(0.0);
            System.out.println("not setting");
        }
        
        
        try {
            Thread.sleep(75);
        } catch (InterruptedException ex) { }
        Drive.getInstance().runDrive(controller.getLeftY(), controller.getRightX());
        System.out.println("HOWDY");
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
