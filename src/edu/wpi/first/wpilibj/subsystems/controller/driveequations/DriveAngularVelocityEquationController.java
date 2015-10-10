package edu.wpi.first.wpilibj.subsystems.controller.driveequations;

import edu.wpi.first.wpilibj.robot.Controls;
import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.generic.PIDVelocityController;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

public class DriveAngularVelocityEquationController extends Controller {
	private double aimAngularVelocity = 0.0;
	private final double MAX_ANGULAR_VELOCITY = 720;
	private PIDVelocityController angController = new PIDVelocityController(.4, 0, .01);

	public void reset() {

	}

	public OutputSignal getOutputSignal(InputState inputState) {
		aimAngularVelocity = MAX_ANGULAR_VELOCITY * Controls.driverController.getLeftX();
		inputState.setError(aimAngularVelocity - inputState.getAngularVel());
		OutputSignal angularOutput = angController.getOutputSignal(inputState);
		OutputSignal signal = new OutputSignal();
		signal.setLeftMotor(Controls.driverController.getLeftY() - angularOutput.getMotor());
		signal.setRightMotor(Controls.driverController.getLeftY() + angularOutput.getMotor());
		return signal;
	}

	public boolean isCompleted() {
		return false;
	}

}
