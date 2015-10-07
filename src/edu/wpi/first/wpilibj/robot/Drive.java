/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.robot;

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
        setRightSide(throttle + turn);
        setLeftSide(throttle - turn);
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
        rightFront.set(v);
        rightMiddle.set(v);
        rightBack.set(v);
    }
    
     private void setLeftSide(double v) {
        leftFront.set(v);
        leftMiddle.set(v);
        leftBack.set(v);
    }
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
