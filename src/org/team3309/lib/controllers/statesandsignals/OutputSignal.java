package org.team3309.lib.controllers.statesandsignals;

/**
 * Signal that is sent to a subsystem from a controller. It is then parsed and
 * sent to motors from the subsystem.
 *
 * @author TheMkrage
 *
 */
public class OutputSignal {

    public OutputSignal() {
        super();
    }

    private double motor = 0;
    private double rightMotor = 0;
    private double leftMotor = 0;

    // Default Key Methods
    public void setMotor(double motor) {
        this.motor = motor;
    }

    public double getMotor() {
        return this.motor;
    }

    public void setRightMotor(double rightPower) {
        this.rightMotor = rightPower;
    }

    public double getRightMotor() {
        return this.rightMotor;
    }

    public void setLeftMotor(double leftPower) {
        this.leftMotor = leftPower;
    }

    public double getLeftMotor() {
        return leftMotor;
    }

    public void setLeftRightMotor(double leftPower, double rightPower) {
        setLeftMotor(leftPower);
        setRightMotor(rightPower);
    }

}
