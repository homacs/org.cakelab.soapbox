package org.cakelab.oge.app;

public class GlobalClock {

	/** currentTime (GLFW).
	 * It is initialised with 1 (1.1.1970 00:00:00.001)
	 */
	static double currentTime = 1;

	public static double getCurrentTime() {
		return currentTime;
	}

	// FIXME remove this, because only the core should be allowed to set this
	public static void setCurrentTime(double time) {
		currentTime = time;
	}

}
