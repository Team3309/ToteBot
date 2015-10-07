package org.team3309.lib.controllers.statesandsignals;

/**
 * An Object used to let a Controller know the current state of a subsystem.
 * Contains methods for default values so keys do not have to be memorized.
 * Additional keys can be added if the InputState is treated as a HashMap
 *
 * @author TheMkrage
 *
 */
public class InputState {
    
    private double error = 0;
    private double x = 0;
    private double y = 0;
    private double w = 0;
    private double rightPos = 0;
    private double leftPos = 0;
    private double leftVel = 0;
    private double rightVel = 0;
    private double angPos = 0;

    public InputState() {
        super();
    }

    // Default Key Methods
    public void setError(double error) {
        this.error = error;
    }

    public double getError() {
        return error;
    }

    // For Drives (and whatever else may use them)
    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setAngularVel(double w) {
        this.w = w;
    }

    // Vel for Drive
    public double getAngularVel() {
        return w;
    }

    public void setLeftVel(double leftVel) {
        this.leftVel = leftVel;
    }

    public double getLeftVel() {
        return leftVel;
    }

    public void setRightVel(double rightVel) {
        this.rightVel = rightVel;
    }

    public double getRightVel() {
        return this.rightVel;
    }

    // Pos for Drive
    public void setAngularPos(double heading) {
        this.angPos = heading;
    }

    public double getAngularPos() {
        return angPos;
    }

    public void setLeftPos(double leftPos) {
        this.leftPos = leftPos;
    }

    public double getLeftPos() {
        return this.leftPos;
    }

    public void setRightPos(double rightPos) {
        this.rightPos = rightPos;
    }

    public double getRightPos() {
        return rightPos;
    }
}
