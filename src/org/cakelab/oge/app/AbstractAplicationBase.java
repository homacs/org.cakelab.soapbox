package org.cakelab.oge.app;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_STEREO;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_API_ERROR_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_DEPRECATION_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD;
import static org.lwjgl.opengl.AMDDebugOutput.GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_HIGH;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_LOW;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_MEDIUM;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_NOTIFICATION;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_ERROR;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_PORTABILITY;
import static org.lwjgl.opengl.GL43.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR;

import java.nio.DoubleBuffer;

import org.cakelab.appbase.log.Log;
import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.shader.GLException;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public abstract class AbstractAplicationBase {
	public static final boolean DEBUG = true;

	protected Info info = new Info();
	protected DebugMessageHandler debugMessageHandler;
	protected int exitStatus;
	protected GLFWWindowSizeCallback windowSizeCB;
	protected GLFWKeyCallback keyCB;
	protected GLFWMouseButtonCallback mouseButtonCB;
	protected GLFWCursorPosCallback cursorPosCB;
	protected GLFWScrollCallback scrollCB;

	protected GraphicContext context;
	protected static long window;

	public abstract void createWindow();
	
	
	
	
	protected void init() throws GLException {
		if (!glfwInit()) {
			throw new GLException("Failed to initialize GLFW\n");
		}
		createWindow();

		windowSizeCB = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				try {
					onResize(width, height);
				} catch (Throwable e) {
					e.printStackTrace();
					requestExit(-1);
				}
			}
		};
		glfwSetWindowSizeCallback(window, windowSizeCB);
		
		keyCB = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				try {
					onKey(key, scancode, action, mods);
				} catch (Throwable e) {
					e.printStackTrace();
					requestExit(-1);
				}
			}
		};
		glfwSetKeyCallback(window, keyCB);
		mouseButtonCB = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				onMouseButton(button, action);
			}

		};
		glfwSetMouseButtonCallback(window, mouseButtonCB);
		
		cursorPosCB = new GLFWCursorPosCallback() {
			double xpos_last;
			double ypos_last;
			boolean init = true;
			@Override
			public void invoke(long window, double xpos, double ypos) {
				// TODO: consider large values in xpos and ypos and reset mouse cursor
				if(init) {
					xpos_last = xpos;
					ypos_last = ypos;
					init = false;
				}
				onMouseMove(xpos, ypos, xpos-xpos_last, ypos-ypos_last);
				xpos_last = xpos;
				ypos_last = ypos;
			}
		};
		glfwSetCursorPosCallback(window, cursorPosCB);

		scrollCB = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				onMouseWheel(yoffset);
			}

		};
		glfwSetScrollCallback(window, scrollCB);
		if (info.flags.cursor)
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		else
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

		info.flags.stereo = (glfwGetWindowAttrib(window, GLFW_STEREO) == 1);

		glfwMakeContextCurrent(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
		
        GLCapabilities capabilities = GL.createCapabilities();
        context = new GraphicContext(capabilities);
		
		// loading of extensions is done automatically by the lwjgl library,
		// thus, we dont need w3g.

		if (DEBUG) {
			info("VENDOR: " + glGetString(GL_VENDOR));
			info("VERSION: " + glGetString(GL_VERSION));
			info("RENDERER: " + glGetString(GL_RENDERER));
		}
		// Creates a debug message callback for the context
		// which supports OpenGL43, GL_KHR_debug, GL_ARB_debug_output
		// and GL_AMD_debug_output if supported by the drivers.
		debugMessageHandler = new DebugMessageHandler(GL.getCapabilities(), this);
	}
	

	protected void onResize(int w, int h) throws Throwable {
		info.setWindowWidth(w);
		info.setWindowHeight(h);
	}

	/**
	 * Will be called when a key is pressed, repeated or released.
	 *
	 * @param window   the window that received the event
	 * @param key      the keyboard key that was pressed or released
	 * @param scancode the system-specific scancode of the key
	 * @param action   the key action. One of:<br>{@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_RELEASE}, {@link GLFW#GLFW_REPEAT}
	 * @param mods     bitfield describing which modifiers keys were held down. One of: 
	 * <br>{@link GLFW#GLFW_MOD_SHIFT}, {@link GLFW#GLFW_MOD_CONTROL}, {@link GLFW#GLFW_MOD_ALT}, {@link GLFW#GLFW_MOD_SUPER}
	 */
	protected void onKey(int key, int scancode, int action, int mods) throws Throwable {

	}

	protected void onMouseButton(int button, int action) {

	}

	protected void onMouseMove(double xpos, double ypos, double xmov, double ymov) {

	}

	protected void onMouseWheel(double yoffset) {

	}

	protected void requestExit(int status) {
		exitStatus = status;
		glfwSetWindowShouldClose(window, true);
	}
	
	public void setVirtualCursor(boolean enabled) {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}


	
	/** Immediate program exit */
	protected void exit (int status) {
		debugMessageHandler.release();
		glfwTerminate();
		System.exit(status);
	}


	void onDebugMessage(int source, int typeOrCategory, int id, int severity,
			int length, String message) {
		switch (typeOrCategory) {
		case GL_DEBUG_TYPE_ERROR:
		case GL_DEBUG_CATEGORY_API_ERROR_AMD:
			fatal(message);
			break;
		case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
		case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
		case GL_DEBUG_TYPE_PORTABILITY:
		case GL_DEBUG_CATEGORY_WINDOW_SYSTEM_AMD:
		case GL_DEBUG_CATEGORY_DEPRECATION_AMD:
		case GL_DEBUG_CATEGORY_UNDEFINED_BEHAVIOR_AMD:
			error(message);
			break;
		default:
			switch(severity) {
			case GL_DEBUG_SEVERITY_HIGH:
				// same as:
				/* GL_DEBUG_SEVERITY_HIGH_ARB */
				/* GL_DEBUG_SEVERITY_HIGH_AMD */
				fatal(message);
				break;
			case GL_DEBUG_SEVERITY_MEDIUM:
				// same as:
				/* GL_DEBUG_SEVERITY_MEDIUM_ARB */
				/* GL_DEBUG_SEVERITY_MEDIUM_AMD */
				error(message);
				break;
			case GL_DEBUG_SEVERITY_LOW:
				// same as:
				/* GL_DEBUG_SEVERITY_LOW_ARB */
				warn(message);
				break;
			case GL_DEBUG_SEVERITY_NOTIFICATION:
				// same as:
				/* GL_DEBUG_SEVERITY_MEDIUM_AMD */
				/* GL_DEBUG_SEVERITY_LOW_AMD */
				if(DEBUG) info(message);
				break;
			default:
				warn(message);
			}
		}
	}

	static Cursor getMousePosition() {
		// TODO keep cursor object!
		final DoubleBuffer x = DoubleBuffer.allocate(1);
		final DoubleBuffer y = DoubleBuffer.allocate(1);
		glfwGetCursorPos(window, x, y);
		return new Cursor(x.get(), y.get());

	}

	void setVsync(boolean enable) {
		info.flags.vsync = enable;
		glfwSwapInterval(info.flags.vsync ? 1 : 0);
	}

	/**
	 * Print error message and exit abnormal
	 * @param errmsg
	 */
	protected void fatal(String errmsg) {
		Log.fatal(errmsg);
		throw new Error("FATAL: " + errmsg);
	}

	/** 
	 * Logs an error message on stderr.
	 * @param errmsg
	 */
	protected void error(String errmsg) {
		Log.error(errmsg);
	}

	/**
	 * Logs warnmsg on stdout
	 * @param warnmsg
	 */
	protected void warn(String warnmsg) {
		Log.warn(warnmsg);
	}

	/**
	 * Logs infomsg on stdout.
	 * @param infomsg
	 */
	protected void info(String infomsg) {
		Log.info(infomsg);
	}
	

	public static class Info {
		public String title;
		public int windowWidth;
		public int windowHeight;
		public int majorVersion;
		public int minorVersion;
		public int samples;

		public static class Flags {
			public boolean fullscreen;
			public boolean vsync;
			public boolean cursor;
			public boolean stereo;
			public boolean debug;
		}

		public Flags flags = new Flags();
		private Vector2f center = new Vector2f();

		public int getWindowWidth() {
			return windowWidth;
		}

		public void setWindowWidth(int windowWidth) {
			this.windowWidth = windowWidth;
			this.center.x = (float) ((((float)windowWidth) +0.5)/2);
		}

		public int getWindowHeight() {
			return windowHeight;
		}

		public void setWindowHeight(int windowHeight) {
			this.windowHeight = windowHeight;
			this.center.y = (float) ((((float)windowHeight) +0.5)/2);
		}

		public Vector2f getCenter() {
			return center;
		}

	}

	public static class Cursor {

		private double x;
		private double y;

		public Cursor(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

	}


	public void setVSync(boolean enable) {
        info.flags.vsync = enable;
        glfwSwapInterval(info.flags.vsync?1:0);
	}
	
}