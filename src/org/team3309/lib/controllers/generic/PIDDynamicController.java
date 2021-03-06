package org.team3309.lib.controllers.generic;

import org.team3309.lib.KragerTimer;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDDynamicController extends PIDController {
	/**
	 * Threshold for steady state error that must be maintained to switch over
	 * to transient constants.
	 */
	private double CONSTANT_CHANGING_THRESHOLD = 40;
	/**
	 * Time needed to switch constants from impulse to transient
	 */
	private final double TIME_TO_SWITCH_CONSTANST_MILLISECONDS = 250;
	/**
	 * Timer that counts time where error is below constant_changing_threshold
	 */
	private KragerTimer constantSwitchingTimer = new KragerTimer(TIME_TO_SWITCH_CONSTANST_MILLISECONDS);
	/**
	 * If the constants have changed to transient
	 */
	private boolean hasSwitchedToTransient = false;

	private double kPTrans, kITrans, kDTrans;
        private double kPImpulse, kIImpulse, kDImpulse;

	public PIDDynamicController(double kP, double kI, double kD, double kPTrans, double kITrans, double kDTrans) {
		super(kP, kI, kD);
                this.kPImpulse = kP;
                this.kIImpulse = kI;
                this.kDImpulse = kD;
		this.kPTrans = kPTrans;
		this.kITrans = kITrans;
		this.kDTrans = kDTrans;
		SmartDashboard.putNumber(" kP Trans", kPTrans);
		SmartDashboard.putNumber(" kI Trans", kITrans);
		SmartDashboard.putNumber(" kD Trans", kDTrans);
		SmartDashboard.putNumber(" CONSTANTCHANGING", CONSTANT_CHANGING_THRESHOLD);
	}

	private void switchToTransientConstants() {
		this.setConstants(kPTrans, kITrans, kDTrans);
	}

	public OutputSignal getOutputSignal(InputState inputState) {
		OutputSignal signal = new OutputSignal();
		if (!hasSwitchedToTransient) {
			boolean isWithinThreshold = Math.abs(inputState.getError()) < CONSTANT_CHANGING_THRESHOLD;
			if (constantSwitchingTimer.isConditionMaintained(isWithinThreshold)) {
				hasSwitchedToTransient = true;
				this.switchToTransientConstants();
			}
		}
		signal.setMotor(super.getOutputSignal(inputState).getMotor());
		return signal;
	}

	public void sendToSmartDash() {
		super.sendToSmartDash();
		kPTrans = SmartDashboard.getNumber(" kP Trans");
		kITrans = SmartDashboard.getNumber( " kI Trans");
		kDTrans = SmartDashboard.getNumber( " kD Trans");
		CONSTANT_CHANGING_THRESHOLD = SmartDashboard.getNumber( " CONSTANTCHANGING");
		SmartDashboard.putNumber(" kP Trans", kPTrans);
		SmartDashboard.putNumber(" kI Trans", kITrans);
		SmartDashboard.putNumber( " kD Trans", kDTrans);
		SmartDashboard.putNumber( " CONSTANTCHANGING", CONSTANT_CHANGING_THRESHOLD);
	}
}
