/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Friarbots
 */
public class Drive extends Subsystem {
    
    private Victor rightFront = new Victor(4); 
    private Victor rightMiddle = new Victor(5) ;
    private Victor rightBack = new Victor(6); 
    private Victor leftFront = new Victor(1);  
    private Victor leftMiddle = new Victor(2); 
    private Victor leftBack = new Victor(3); 
    private Gyro gyro = new Gyro(1);
    
    
    private static Drive instance; 
    public static Drive getInstance() {
        if (instance == null) 
            instance = new Drive();
        return instance;
    }
    
    private Drive() {
        
    }
    
    public double getAngularVelocity() {
        return gyro.getRate();
    }
    
    public void runDrive(double throttle, double turn) {
        System.out.println("THROTTLE : " + throttle);
        cheesyDrive(throttle, turn, true, false); 
        // 2setRightSide(throttle - turn);
        // setLeftSide(throttle + turn);
    }
    
    private double skimGain = .25;
    private double skim(double v) {
        // gain determines how much to skim off the top
        if (v > 1.0) {
            return -((v - 1.0) * skimGain);
        } else if (v < -1.0) {
            return -((v + 1.0) * skimGain);
        }
        return 0;
    }
    
    private double maxAngularVelocity = 720;
    private double gyroKpTele = .02;
    private double gyroKpAuto = .01;
    public void vinnieDrive(double throttle, double turn) {
        double originalTurn = turn;

        double desiredAngularVelocity = turn * maxAngularVelocity;
        double angularVelocity = getAngularVelocity();

        //proportional correction
        if (Math.abs(throttle) > .1) {
            if (DriverStation.getInstance().isAutonomous())
                turn = (desiredAngularVelocity - angularVelocity) * gyroKpAuto;
            else
                turn = (desiredAngularVelocity - angularVelocity) * gyroKpTele;
        } else {
            if (DriverStation.getInstance().isAutonomous())
                turn = (desiredAngularVelocity - angularVelocity) * (gyroKpAuto / 2);
            else
                turn = (desiredAngularVelocity - angularVelocity) * (gyroKpTele / 2);
        }

        double t_left = throttle + turn;
        double t_right = throttle - turn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        if (left > 1)
            left = 1;
        else if (left < -1)
            left = -1;
        if (right > 1)
            right = 1;
        else if (right < -1)
            right = -1;

        setLeftSide(left);
        setRightSide(right);
    }

    private void setRightSide(double v) {
        rightFront.set(-v);
        rightMiddle.set(-v);
        rightBack.set(-v);
    }
    
     private void setLeftSide(double v) {
        leftFront.set(v);
        leftMiddle.set(v);
        leftBack.set(v);
    }
     
     public double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
     
    double oldWheel, quickStopAccumulator;
    private double throttleDeadband = 0.02;
    private double wheelDeadband = 0.02;
    
    public static double limit(double v, double limit) {
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }
    
     public void cheesyDrive(double throttle, double wheel, boolean isQuickTurn,
                            boolean isHighGear) {
        if (DriverStation.getInstance().isAutonomous()) {
            return;
        }

        double wheelNonLinearity;

        wheel = handleDeadband(wheel, wheelDeadband);
        throttle = handleDeadband(throttle, throttleDeadband);

        double negInertia = wheel - oldWheel;
        oldWheel = wheel;

        if (isHighGear) {
            wheelNonLinearity = 0.6;
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        } else {
            wheelNonLinearity = 0.5;
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel)
                    / Math.sin(Math.PI / 2.0 * wheelNonLinearity);
        }

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaAccumulator = 0.0;
        double negInertiaScalar;
        if (isHighGear) {
            negInertiaScalar = 4.0;
            sensitivity = .75;
        } else {
            if (wheel * negInertia > 0) {
                negInertiaScalar = 2.5;
            } else {
                if (Math.abs(wheel) > 0.65) {
                    negInertiaScalar = 5.0;
                } else {
                    negInertiaScalar = 3.0;
                }
            }
            sensitivity = .85; // Constants.sensitivityLow.getDouble();
        }
        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;

        wheel = wheel + negInertiaAccumulator;
        if (negInertiaAccumulator > 1) {
            negInertiaAccumulator -= 1;
        } else if (negInertiaAccumulator < -1) {
            negInertiaAccumulator += 1;
        } else {
            negInertiaAccumulator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator
                        + alpha * limit(wheel, 1.0) * 5;
            }
            overPower = 1.0;
            if (isHighGear) {
                sensitivity = .8;
            } else {
                sensitivity = .8;
            }
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity
                    - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }

        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }
        this.setLeftSide(leftPwm );
        this.setRightSide(rightPwm);
        
    }
     
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
