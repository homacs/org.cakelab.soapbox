package org.cakelab.oge.app;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import java.io.IOException;

import org.cakelab.oge.diagnostics.Channel;
import org.cakelab.oge.diagnostics.Receiver;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

/** 
 * Swap control is responsible to control the speed in which the 
 * front and back buffer are exchanged. If the next frame is supposed
 * to be swapped in at a future time, this class will bleed off any 
 * remaining processing time before swapping the buffers based on the 
 * chosen type of swap control. For example {@link NoSpeedControlAtAll}
 * will return immediately while {@link VerticalSync} will return
 * after the current monitors vertical sync signal.
 * <p>
 * The method {@link #select(ApplicationContext)} will automatically 
 * select a suitable swap control based on the given settings in 
 * {@link Info} in regards to vsync and fps.
 * </p>
 * @author homac
 *
 */
public abstract class SwapControl {
	

	public static class NoSpeedControlAtAll extends SwapControl {

		public NoSpeedControlAtAll(long window) {
			super(window, 0);
			glfwSwapInterval(0);
		}

		@Override
		public double syncAndSwapBuffers() {
			lastTime = swapBuffers();
			return lastTime;
		}

	}

	public static class SoftwareFpsThrottle extends SwapControl {

		double shift = 0.0;
		double offset = -0.0015;
		double accuracy = 0.00001;

		private boolean firstCall;

		public SoftwareFpsThrottle(long window, int fps) {
			this(window, fps, 0);
		}

		public SoftwareFpsThrottle(long window, int fps, int interval) {
			super(window, fps);
			firstCall = true;
			glfwSwapInterval(interval);
		}

		@Override
		public double syncAndSwapBuffers() {
			double frameTime = GlobalClock.getLocalTime();

			double nextTime = lastTime + secondsPerFrame;
			
			double remaining = nextTime - frameTime;
			remaining = (remaining - shift) * 1000.0; // -> milliseconds
			// suspend thread for most of the remaining time
			if (remaining > 3) {
				// bleed off most of the milliseconds
				try {
					Thread.sleep((long) (remaining-2));
				} catch (InterruptedException e) {
					// probably shutting down
					// no need to get mental about it
					return frameTime;
				}
			}
			// bleed off remaining time
			frameTime = GlobalClock.getLocalTime();
			while ((nextTime+offset) -shift > frameTime) {
				frameTime = GlobalClock.getLocalTime();
			}
			
			frameTime = swapBuffers();
			
			if (!firstCall) {
				// adjust shift if necessary
				double jitter = frameTime - (nextTime+offset);
				if (Math.abs(jitter) > accuracy) {
					// too early or too late
					shift += jitter * 0.01;
				}
				logFps(frameTime, lastTime);
			}
			firstCall = false;
			lastTime = frameTime;
			return lastTime;
		}

	}

	public static class VerticalSync extends SwapControl {
		
		public VerticalSync(long window, int fps, int interval) {
			super(window, fps);
			glfwSwapInterval(interval);
		}

		@Override
		public double syncAndSwapBuffers() {
			glfwSwapBuffers(window);
			double frameTime = GlobalClock.getLocalTime();
			logFps(frameTime, lastTime);
			return lastTime = frameTime;
		}

		@Override
		public double nextFrameTime() {
			return lastTime + secondsPerFrame;
		}

	}

	public static class VerticalSyncSwapTear extends VerticalSync {
		public VerticalSyncSwapTear(long window, int fps, int interval) {
			super(window, fps, -interval);
		}
	}

	public static class SoftwareAssistedVerticalSync extends VerticalSync {
		public SoftwareAssistedVerticalSync(long window, int fps, int interval) {
			super(window, fps, interval);
		}
		@Override
		public double syncAndSwapBuffers() {
			double frameTime = GlobalClock.getLocalTime();
			double offset = 0.0005;
			double nextTime = lastTime + secondsPerFrame;

			// suspend thread for most of the remaining time
			double remaining = nextTime - frameTime;
			remaining = (remaining) * 1000.0; // -> milliseconds
			if (remaining > 3) {
				try {
					Thread.sleep((long) (remaining-2));
				} catch (InterruptedException e) {
					// system is shutting down --> just leave
					return frameTime;
				}
			}
			
			// bleed off still remaining time by busy waiting
			// to tell OS that we don't like to be interrupted now.
			frameTime = GlobalClock.getLocalTime();
			while ((nextTime-offset) > frameTime) {
				frameTime = GlobalClock.getLocalTime();
			}
			
			// now we have 'offset' seconds left to swap buffers
			// the closer we are, the smoother the transition will be
			frameTime = swapBuffers();
			logFps(frameTime, lastTime);
			lastTime = frameTime;
			return lastTime;
		}
	}

	public static class SoftwareAssistedVerticalSyncSwapTear extends SoftwareAssistedVerticalSync {
		public SoftwareAssistedVerticalSyncSwapTear(long window, int fps, int interval) {
			super(window, fps, -interval);
		}
	}

	/**
	 *  The method {@link #select(ApplicationContext)} will automatically 
	 * select a suitable swap control based on the given settings regards 
	 * to vsync, fps and requested software assistance.
	 * @param context Context of the running application.
	 * @param vsync Whether vertical sync is requested or not
	 * @param fps Requested fps. 0 or {@link Integer#MAX_VALUE} means unlimited and turns off speed control.
	 * @param softwareAssist Useful to smooth vertical sync on some systems
	 * @return selected swap control
	 */
	public static SwapControl select(ApplicationContext context, boolean vsync, int fps, boolean softwareAssist) {
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		int monitorFps = mode.refreshRate();

		
		if (vsync) {
	        int interval = 1;
	        boolean swap_control_tear = GLFW.glfwExtensionSupported("WGL_EXT_swap_control_tear");
	        swap_control_tear |= GLFW.glfwExtensionSupported("GLX_EXT_swap_control_tear");
	        if (swap_control_tear) {
	        	if (softwareAssist) return new SoftwareAssistedVerticalSyncSwapTear(context.getWindow(), monitorFps, interval);
	        	return new VerticalSyncSwapTear(context.getWindow(), monitorFps, interval);
	        } else {
	        	if (softwareAssist) return new SoftwareAssistedVerticalSync(context.getWindow(), monitorFps, interval);
	        	return new VerticalSync(context.getWindow(), monitorFps, interval);
	        }
		} else if (fps > 0) {
			return new SoftwareFpsThrottle(context.getWindow(), monitorFps);
		} else {
			return new NoSpeedControlAtAll(context.getWindow());
		}

	}
	

	
	
	
	/* *********************************************************************************** *
	 * *********************************************************************************** */
	
	/** active window */
	protected long window;
	/** seconds per frame */
	protected double secondsPerFrame;
	/** last time the {@link #syncAndSwapBuffers()} was called. */
	protected double lastTime = 0;
	/** channel for fps diagnostics */
	protected Channel fpsDiagnostics;

	protected SwapControl(long window, int fps) {
		this.window = window;
		this.secondsPerFrame = (fps>0&&fps<Integer.MAX_VALUE)?1.0/fps:0;
	}

	/**
	 * Method to be called at one point int the update loop.
	 * Will sync with given time constraints and swap the frame buffer back to front.
	 * @return frameTime (GlobalClock#getLocalTime()) taken right after the buffer swap
	 */
	public abstract double syncAndSwapBuffers();

	/**
	 * Estimates the next frame time (see {@link GlobalClock#frameTime}) based 
	 * on the selected target frames per second. You can determine the time left 
	 * for processing by querying {@link GlobalClock#getLocalTime()} and compare
	 * it to next frame time received here.
	 * 
	 * In case FPS was set to 0 or {@link Integer#MAX_VALUE}, this will return 
	 * the current frame time (i.e. the next frame is scheduled to be rendered ASAP).
	 * @return estimated local time of the next frame (i.e. next {@link GlobalClock#frameTime}).
	 */
	public double nextFrameTime() {
		return lastTime + secondsPerFrame;
	}
	
	protected double swapBuffers() {
		glfwSwapBuffers(window);
		return GlobalClock.getLocalTime();
	}

	/** 
	 * Method to subscribe to fps diagnostics messages. The swap 
	 * control will send the measured actual fps to the receiver.
	 * 
	 * @param diagnostics
	 */
	public void setDiagnostics(Receiver diagnostics) {
		this.fpsDiagnostics = diagnostics.openChannel("fps");
	}

	/**
	 * 
	 * @param frameTime
	 * @param lastTime
	 */
	protected void logFps(double frameTime, double lastTime) {
		if (fpsDiagnostics != null) fpsDiagnostics.log(frameTime, 1.0/(frameTime - lastTime));		
	}

	public void stop() {
		if (fpsDiagnostics != null) {
			try {
				fpsDiagnostics.close();
			} catch (IOException e) {
			}
		}
	}


}
