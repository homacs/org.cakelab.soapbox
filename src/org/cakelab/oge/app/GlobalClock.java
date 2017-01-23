package org.cakelab.oge.app;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/** 
 * 
 * GlobalClock maintains time stamps of important process states.
 * For example, many methods just needs to execute once for each 
 * frame. Those methods can use {@link #getFrameTime()} to check
 * whether they need to run or not.
 * 
 * At start, all time stamps will be {@link #TIME_ZERO}. Use {@link #TIME_INVALID}
 * to initialise time stamps in your application. 
 */
public class GlobalClock {

	/** 
	 * This is the value every time stamp in this class will be initialised with.
	 */
	public static final double TIME_ZERO = 0;
	
	/**
	 * This is indicates an invalid time stamp.
	 * {@link #TIME_INVALID} is always less than {@link #TIME_ZERO} and less than any 
	 * other time stamp received from global clock.
	 * </br>
	 * The expression:
	 * </br>
	 * <code>TIME_INVALID &lt; TIME_ZERO</code>
	 * </br>
	 * is always true.
	 */
	public static final double TIME_INVALID = -1;
	
	/** Time stamp of the current frame */
	static volatile double frameTime = TIME_ZERO;

	/**
	 * This is a time stamp indicating the time since program start [sec.msec], 
	 * when the drawing of the current frame was started. By default this is
	 * the time at which {@link ApplicationBase#process(double, ApplicationContext)}
	 * was entered.
	 * 
	 * It contains seconds in the absolute part and microseconds 
	 * in the positions after decimal point.
	 * 
	 * It is updated by {@link ApplicationBase} before 
	 * {@link ApplicationBase#process(double, ApplicationContext)} is called.
	 * 
	 * This time stamp will be {@link #TIME_ZERO} when the application was just started.
	 * 
	 * @return time stamp of the current frame to be drawn.
	 */
	public static double getFrameTime() {
		return frameTime;
	}

	/**
	 * Returns the current state of the local clock.
	 * @return
	 */
	public static double getLocalTime() {
		return glfwGetTime();
//		return ((double)System.nanoTime()-startTime)/1000000000.0;
	}
	
	
}
