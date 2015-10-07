/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author Friarbots
 */
public class Drive extends Subsystem {
    
    private Victor rightFront = new Victor(4); 
    private Victor rightMiddle = new Victor(5); ;
    private Victor rightBack = new Victor(6); ;
    private Victor leftFront = new Victor(1); ; 
    private Victor leftMiddle = new Victor(2); ;
    private Victor leftBack = new Victor(3); ;
    private static Drive instance; 
    public static Drive getInstance() {
        if (instance == null) 
            instance = new Drive();
        return instance;
    }
    
    private Drive() {
        
    }
    
    public void runDrive(double throttle, double turn) {
        setRightSide(throttle + turn);
        setLeftSide(throttle - turn);
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
