package org.team3309.lib.controllers.generic;

import org.team3309.lib.controllers.Controller;
import org.team3309.lib.controllers.statesandsignals.InputState;
import org.team3309.lib.controllers.statesandsignals.OutputSignal;

/**
 * Controller that returns only zero values. Used when a subsystem is disabled,
 * but still requires a controller.
 * 
 * @author TheMkrage
 * 
 */
public class BlankController extends Controller {

	
	public void reset() {
		
	}

	public OutputSignal getOutputSignal(InputState inputState) {
		return new OutputSignal(); // Returns only zeros for everything
	}

	
	public boolean isCompleted() {
		return true; // Is always complete
	}
}
