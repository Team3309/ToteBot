package org.team3309.lib;

import edu.wpi.first.wpilibj.Timer;

/**
 * Timer that takes care of maintaining a certain state for a certain amount of
 * time
 * 
 * @author Krager
 *
 */
public class KragerTimer extends Timer {
	/**
	 * Time required of maintnance to end the timer
	 */
	private double timeToComplete = 0;
	/**
	 * The condition being tested
	 */
	private boolean isConditionMaintained = false;

	public KragerTimer(double timeToComplete) {
		this.timeToComplete = timeToComplete;
	}
	/**
	 * Run this in loop to check if condition has been true
	 * 
	 * @param isTrue
	 * @return
	 */
	public boolean isConditionMaintained(boolean isTrue) {
		if (isTrue) {
			if (isConditionMaintained && this.get() > timeToComplete) {
				return true;
			} else {
				this.start();
				isConditionMaintained = true;
			}
		} else {
			isConditionMaintained = false;
			this.stop();
			this.reset();
		}
		return false;
	}
}
