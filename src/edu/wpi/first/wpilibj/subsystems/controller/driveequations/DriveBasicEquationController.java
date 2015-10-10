package edu.wpi.first.wpilibj.subsystems.controller.driveequations;

import edu.wpi.first.wpilibj.robot.Controls;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * Meant to be the "Oh Snap! The encoders and gyro don't work!" drive equation. This
 * drive equation simply directly sets the motor values to the throttle and
 * turn of the two joysticks. Arcade Drive.
 * 
 * @author TheMkrage
 *
 */
public class DriveBasicEquationController extends Controller {

	public void reset() {

	}

	public OutputSignal getOutputSignal(InputState inputState) {
		OutputSignal signal = new OutputSignal();
		double throttle = Controls.driverController.getLeftY(), turn = Controls.driverController
				.getRightX();
		double rightPower = throttle + turn;
		double leftPower = throttle - turn;
		signal.setLeftRightMotor(leftPower, rightPower);
		return signal;
	}

	public boolean isCompleted() {
		return false;
	}

}
